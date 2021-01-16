/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.trainer.baumwelch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.recognition.acoustic.*;
import org.speech.asr.recognition.context.AsrContext;
import org.speech.asr.recognition.math.LogScale;
import org.speech.asr.recognition.math.MathUtils;
import org.speech.asr.recognition.trainer.TrainSentence;
import org.speech.asr.recognition.trainer.TrainingItem;
import org.speech.asr.recognition.util.GmmAmSerializer;
import org.speech.asr.recognition.util.NiceThreadFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Manager algorytmu Baum-Welch'a.
 * <p/>
 * <p/>
 * Creation date: Jul 13, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class BaumWelchTrainManager {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(BaumWelchTrainManager.class.getName());

  private static final int DEFAULT_MAX_CYCLES = 6;

  private static final double DEFAULT_MIN_RELATIVE_DIFF = 1.0e-4;

  private static final int MAX_ELEMENTS = 4;

  private GmmAcousticModel acousticModel;

  private ModelInitializer initializer;

  private MixtureSplitter splitter;

  private List<TrainSentence> trainingSet;

  private List<TrainingItem> trainSet;

  private int desiredNoMixtures;

  private LogScale logScale;

  private BlockingQueue<TrainingItem> trainQueue;

  private List<Worker> workers;

  private volatile CountDownLatch doneSignal;

  private ThreadPoolExecutor threadPoolExecutor;

  private int maxCycles;

  private double minRelativeErrorChange;

  private boolean dump;

  public BaumWelchTrainManager(GmmAcousticModel model, List<TrainSentence> trainingSet, int noMixtures) {
    this.acousticModel = model;
    this.trainingSet = trainingSet;
    this.desiredNoMixtures = noMixtures;
    initializer = new FlatInitializer();
    logScale = AsrContext.getContext().getLogScale();
//    splitter = new AdaptiveMixtureSplitter(acousticModel, noMixtures);
    maxCycles = DEFAULT_MAX_CYCLES;
    minRelativeErrorChange = DEFAULT_MIN_RELATIVE_DIFF;
    createTrainSet(trainingSet);
    createWorkers();
  }

  public BaumWelchTrainManager(GmmAcousticModel model, List<TrainSentence> trainingSet, int noMixtures, int maxCycles,
                               double minRelativeErrorChange, boolean dump) {
    this(model, trainingSet, noMixtures);
    this.maxCycles = maxCycles;
    this.minRelativeErrorChange = minRelativeErrorChange;
    this.dump = dump;
  }

  private void createWorkers() {
    trainQueue = new LinkedBlockingQueue();
    int availableProcessors = Runtime.getRuntime().availableProcessors();
    threadPoolExecutor = new ScheduledThreadPoolExecutor(availableProcessors, new NiceThreadFactory());
    log.info("Number of processors {}", availableProcessors);
    workers = new LinkedList();
    for (int i = 0; i < availableProcessors; i++) {
      Worker worker = new Worker(i + 1);
      workers.add(worker);
    }
  }

  private void createTrainSet(List<TrainSentence> trainingSet) {
    trainSet = new LinkedList();
    int index = 1;
    for (TrainSentence sentence : trainingSet) {
      TrainingItem item = new TrainingItem();
      item.setObservationSequence(sentence.getObservations().toArray(new Feature[0]));
      item.setLogObservationSequence(createLogFeature(sentence.getObservations()).toArray(new Feature[0]));
      item.setTranscription(sentence.getTranscription());
      item.setIndex(index++);
      trainSet.add(item);
    }
  }

  private List<Feature> createLogFeature(List<Feature> linearFeatures) {
    List<Feature> logFeatures = new ArrayList(linearFeatures.size());
    for (Feature linearFeature : linearFeatures) {
      double[] logData = new double[linearFeature.getData().length];
      for (int i = 0; i < logData.length; i++) {
        logData[i] = logScale.linearToLog(linearFeature.getData()[i]);
      }
      FeatureImpl newFeature = new FeatureImpl(logData);
      newFeature.setSequenceNumber(linearFeature.getSequenceNumber());
      logFeatures.add(newFeature);
    }
    return logFeatures;
  }

  /**
   * Przeprowadza pelny algorytm uczenia.
   * Wejscie: slownik, zbior wektorow uczacyc, opcjonalnie zbior wektorow testujacych
   * Wyjscie: model akustyczny
   */
  public void train() {
    log.info("Initializing acoustic model...");
    initializer.initializeStates(trainingSet, acousticModel);
    splitter = new AdaptiveMixtureSplitter(acousticModel, desiredNoMixtures);
    boolean end = false;
    double lastPosteriori;
    double posterioriSum;
    int iteration = 1;
    long time = System.currentTimeMillis();
    log.info("Starting learning for {} sentences", trainingSet.size());
    do {
      posterioriSum = logScale.getLogOne();
      int cycle = 0;
      do {
        log.info("Performing {} iteration of Baum-Welch training...", iteration++);
        lastPosteriori = posterioriSum;

        trainQueue.addAll(trainSet);
        startWorkers();
        try {
          log.info("Waiting for workers...");
          doneSignal.await();
        } catch (InterruptedException e) {
          log.error("", e);
        }
        posterioriSum = logScale.getLogOne();
        for (Worker worker : workers) {
          posterioriSum += worker.getPartialPosterioriSum();
        }
        assert MathUtils.isReal(posterioriSum) : "Posteriori sum " + posterioriSum;
        log.info("Posteriori sum after iteration {} is {}", iteration - 1, posterioriSum);
        acousticModel.normalizeEstimates();
        dump(iteration, time);
        cycle++;
      } while (!isConvergence(lastPosteriori, posterioriSum) && cycle < maxCycles);
      if (splitter.getNoMixtures() < desiredNoMixtures) {
        splitter.split();
      } else {
        end = true;
      }
    } while (!end);
    log.info("{} iterations taken {} ms", iteration - 1, System.currentTimeMillis() - time);
  }

  private void dump(int iteration, long startTime) {
    if (dump) {
      log.info("Dumping acoustic model...");
      try {
        File workDir = AsrContext.getContext().getWorkDir();
        File dumpDir = new File(workDir, String.valueOf(startTime));
        if (!dumpDir.exists()) {
          if (!dumpDir.mkdirs()) {
            log.error("Cannot create dir {}", dumpDir);
          }
        }
        GmmAmSerializer
            .save(acousticModel, new FileOutputStream(
                dumpDir.getAbsolutePath() + "/iteration" + (iteration < 11 ? "0" : "") + (iteration - 1) + ".xml"));
      } catch (IOException e) {
        log.error("", e);
      }
    }
  }

  protected void startWorkers() {
    doneSignal = new CountDownLatch(workers.size());
    new Thread(new Runnable() {
      public void run() {
        for (Worker worker : workers) {
          threadPoolExecutor.execute(worker);
        }
      }
    }).start();
  }

  protected boolean isConvergence(double lastPosteriori, double actual) {
    if (lastPosteriori == 0) {
      lastPosteriori = Double.MIN_VALUE;
    }
    double change = Math.abs((actual - lastPosteriori) / lastPosteriori);
    return change < minRelativeErrorChange;
  }

  protected void printItem(TrainingItem item) {
    StringBuilder sb = new StringBuilder();
    for (PhoneticUnit phonem : item.getTranscription()) {
      sb.append(phonem.getName()).append(" ");
    }
    String str = sb.toString();
    log.debug("Training sentence {}", str);
    if (log.isTraceEnabled()) {
      for (PhoneticUnit<StateDescriptor> phoneme : item.getTranscription()) {
        for (StateDescriptor state : phoneme.getStatesSequence()) {
          log.trace("PDF for {} is {}", state.getId(), state.getScorer());
        }
      }
    }
  }

  protected Hmm buildSentenceHmm(TrainSentence sentence) {
    LeftRightHmmBuilder builder = new LeftRightHmmBuilder();

    for (PhoneticUnit<StateDescriptor> phoneme : sentence.getTranscription()) {
      for (StateDescriptor state : phoneme.getStatesSequence()) {
        builder.addState(state);
      }
    }
    return builder.getHmm();
  }

  private class Worker implements Runnable {
    private volatile int id;

    private volatile double partialPosterioriSum;

    public Worker(int id) {
      this.id = id;
    }

    public void run() {
      try {
        log.info("Starting worker with id {}", id);
        List<TrainingItem> partialTrainingSet;
        partialPosterioriSum = logScale.getLogOne();
        partialTrainingSet = new LinkedList();
        while (true) {
          int taken = trainQueue.drainTo(partialTrainingSet, MAX_ELEMENTS);
          log.debug("Taken {} elements, for worker {}", taken, id);
          if (taken == 0) {
            log.info("Stopping worker with id {}", id);
            //notify work done in finally clause
            return;
          }
          for (TrainingItem item : partialTrainingSet) {
            log.debug("{} sentence in worker {}", item.getIndex(), id);
            printItem(item);
            BaseBaumWelchLearner learner = new BaseBaumWelchLearner();
            double posteriori = learner.trainLeftToRight(item, splitter.getNoMixtures());
            assert MathUtils.isReal(posteriori) : "Posteriori sum " + posteriori;
            partialPosterioriSum = MathUtils.sum(partialPosterioriSum, posteriori);
            assert MathUtils.isReal(partialPosterioriSum) : "Posteriori sum " + partialPosterioriSum;
            log.debug("Returned posteriori {}, and partial sum {} by worker {} after learn sentence {}",
                new Object[]{posteriori, partialPosterioriSum, id, item.getIndex()});
            Map<String, StateEstimates> estimatesMap = learner.getEstimates();
            acousticModel.collectEstimates(estimatesMap);
          }
          partialTrainingSet.clear();
        }
      } catch (Throwable th) {
        log.error("Exception occurred in worker " + id, th);
        System.exit(1);
      } finally {
        log.info("Count down in worker {}", id);
        doneSignal.countDown();
      }
    }

    public double getPartialPosterioriSum() {
      return partialPosterioriSum;
    }
  }

  /**
   * Setter for property 'initializer'.
   *
   * @param initializer Value to set for property 'initializer'.
   */
  public void setInitializer(ModelInitializer initializer) {
    this.initializer = initializer;
  }
}
