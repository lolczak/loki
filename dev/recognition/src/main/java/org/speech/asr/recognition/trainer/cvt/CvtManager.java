/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.trainer.cvt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.common.exception.AsrRuntimeException;
import org.speech.asr.recognition.acoustic.*;
import org.speech.asr.recognition.ann.NeuralNetwork;
import org.speech.asr.recognition.ann.TrainParameters;
import org.speech.asr.recognition.ann.TrainPattern;
import org.speech.asr.recognition.ann.TrainResult;
import org.speech.asr.recognition.context.AsrContext;
import org.speech.asr.recognition.frontend.InputMapper;
import org.speech.asr.recognition.frontend.SimpleMapper;
import org.speech.asr.recognition.trainer.TrainSentence;
import org.speech.asr.recognition.util.NeuralHybridAmSerializer;
import org.speech.asr.recognition.util.NiceThreadFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jul 26, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class CvtManager {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(CvtManager.class.getName());

  private NeuralNetwork neuralNetwork;

  private List<SegmentedTrainingItem> trainingSet;

  private NeuralHybridAcousticModel model;

  private List<Double> errorHistory;

  private double lastError;

  private int epoch;

  private volatile double alpha;

  private double alphaDelta;

  private double randomWeightAmpl;

  private double learningRate;

  private double momentum;

  private int firstEpochNoCycles;

  private int noCycles;

  private double stopError;

  private int minEpochs;

  private int maxEpochs;

  private double errorStabilizationFactor;

  private int window;

  private int batchSize;

  private InputMapper inputMapper;

  private Map<SegmentedTrainingItem, ForcedAlignment> forcedAlignmentMap;

  private AcousticModel bootstrapModel;

  private ThreadPoolExecutor threadPoolExecutor;

  private List<SegmentationWorker> workers;

  private volatile CountDownLatch doneSignal;

  private PriorAccumulator priorAccumulator;

  private enum SegmentationType {
    BOOTSTRAP, RE_SEGMENTATION
  }

  private volatile SegmentationType segmentationType;

  private BlockingQueue<SegmentedTrainingItem> resegmentQueue;

  public CvtManager(NeuralHybridAcousticModel model, List<TrainSentence> trainSentences) {
    this.model = model;
    this.neuralNetwork = model.getNeuralNetwork();
    errorHistory = new LinkedList();
    priorAccumulator = new PriorAccumulator();
    setDefaultValues();
    buildTrainingSet(trainSentences);
    buildForcedAlignments();
    initWorkers();
  }

  private void setDefaultValues() {
    firstEpochNoCycles = 2;
    noCycles = 2;
    alpha = 0.8;
    alphaDelta = 0.02;
    learningRate = 0.005;
    momentum = 0.0;
    randomWeightAmpl = 0.5;
    stopError = 0.1;
    minEpochs = 200;
    maxEpochs = 200;
    batchSize = 17;
    errorStabilizationFactor = 1E-3;
    window = 5;
    bootstrapModel = null;
    inputMapper = new SimpleMapper();
  }

  protected void initWorkers() {
    int availableProcessors = Runtime.getRuntime().availableProcessors();
    threadPoolExecutor = new ScheduledThreadPoolExecutor(availableProcessors, new NiceThreadFactory());
    log.info("Number of processors {}", availableProcessors);
    workers = new ArrayList(availableProcessors);
    for (int i = 0; i < availableProcessors; i++) {
      SegmentationWorker worker = new SegmentationWorker(i + 1);
      workers.add(worker);
    }
    resegmentQueue = new LinkedBlockingQueue();
  }

  protected void buildTrainingSet(List<TrainSentence> trainSentences) {
    Set<TrainSentence> hashedTrainSentences = new HashSet(trainSentences);
    trainingSet = new LinkedList();
    for (TrainSentence sentence : hashedTrainSentences) {
      SegmentedTrainingItem item = new SegmentedTrainingItem();
      item.setObservationSequence(new ArrayList(sentence.getObservations()));
      List<NeuralStateDescriptor> trans = new LinkedList();
      for (PhoneticUnit unit : sentence.getTranscription()) {
        PhoneticUnit<NeuralStateDescriptor> phoneticUnit = model.getPhoneticUnit(unit.getName());
        trans.addAll(phoneticUnit.getStatesSequence());
      }
      item.setTranscription(new ArrayList(trans));
      trainingSet.add(item);
    }
  }

  protected void buildForcedAlignments() {
    log.debug("Building forced alignments graphs for training set...");
    forcedAlignmentMap = new ConcurrentHashMap();
    for (SegmentedTrainingItem item : trainingSet) {
      ForcedAlignment forcedAlignment = new ForcedAlignment(model, item.getTranscription());
      forcedAlignmentMap.put(item, forcedAlignment);
    }
  }

  protected void bootstrapSegment() {
    log.info("Segmenting using bootstrap model");
    segmentationType = SegmentationType.BOOTSTRAP;
    doneSignal = new CountDownLatch(workers.size());
    resegmentQueue.addAll(trainingSet);
    for (SegmentationWorker worker : workers) {
      threadPoolExecutor.execute(worker);
    }
    try {
      doneSignal.await();
    } catch (InterruptedException e) {
      log.error("", e);
    }
    assert resegmentQueue.isEmpty();
    log.info("Bootstrap segmentation finished");
  }

  protected void segmentUniformly() {
    log.info("Segmenting uniformly...");
    for (SegmentedTrainingItem item : trainingSet) {
      List<StateSegment> segmentation = new LinkedList();
      int avgIntervalSize = item.getObservationSequence().size() / item.getTranscription().size();
      assert avgIntervalSize > 0;
      int offset = 0;
      for (NeuralStateDescriptor stateDescriptor : item.getTranscription()) {
        StateSegment interval = new StateSegment();
        interval.setState(stateDescriptor);
        interval.setFirstFrame(offset);
        interval.setLastFrame(offset + avgIntervalSize - 1);
        offset = offset + avgIntervalSize;
        segmentation.add(interval);
      }
      int lastOffset = item.getTranscription().size() - 1;
      segmentation.get(segmentation.size() - 1).setLastFrame(lastOffset);
      item.setSegmentation(segmentation);
    }
  }

  public void train() {
    long time = System.currentTimeMillis();
    if (bootstrapModel == null) {
      segmentUniformly();
    } else {
      bootstrapSegment();
    }
    epoch = 0;
    do {
      epoch++;
      log.info("Performing {} epoch with alpha {}...", new Object[]{epoch, alpha});
      retrain();
      createSnapshot(time);
      resegment();
      updateParameters();
    } while (!isConvergence());
  }

  private void createSnapshot(long startTime) {
    log.debug("Creating snapshot of model");
    try {
      File workDir = AsrContext.getContext().getWorkDir();
      File dumpDir = new File(workDir, String.valueOf(startTime));
      if (!dumpDir.exists()) {
        if (!dumpDir.mkdirs()) {
          log.error("Cannot create dir {}", dumpDir);
        }
      }
      File file = new File(dumpDir, "nn_epoch" + (epoch < 10 ? "0" : "") + epoch + ".xml");
      NeuralHybridAmSerializer.save(model, new FileOutputStream(file));
    } catch (IOException e) {
      log.error("", e);
    }
  }

  public void updateParameters() {
    alpha -= alphaDelta;
    if (alpha < 0.0) {
      alpha = 0.0;
    }
  }

  protected void retrain() {
    log.debug("{} epoch - retraining neural net...", epoch);
    TrainParameters parameters = new TrainParameters();
    parameters.setLearningRate(learningRate);
    parameters.setMomentum(momentum);
    parameters.setRandomizeAmplitude(randomWeightAmpl);
    parameters.setNoCycles(epoch == 1 ? firstEpochNoCycles : noCycles);
    parameters.setMaxRmse(0.01);
    parameters.setRandomize(epoch == 1);
    parameters.setBatchSize(batchSize);

    List<TrainPattern> trainPatterns = createTrainPatterns();
    updatePriors();
    TrainResult tr = neuralNetwork.train(trainPatterns, parameters);
    lastError = tr.getError();
    errorHistory.add(tr.getError());
    log.info("{} epoch - error {}", epoch, tr.getError());
  }

  protected void updatePriors() {
    File workDir = AsrContext.getContext().getWorkDir();
    try {
      FileWriter statsWriter = new FileWriter(new File(workDir, "stats" + System.currentTimeMillis() + ".csv"));
      log.info("Updating priors for collected patterns {}...", priorAccumulator.getTotalNoSamples());
      List<NeuralStateDescriptor> states = model.getAllStates();
      for (NeuralStateDescriptor state : states) {
        double prior = priorAccumulator.getPrior(state.getId());
        statsWriter.write(state.getId() + "," + priorAccumulator.getCount(state.getId())+"\n");
        log.debug("New prior probability for state {} is {}", state.getId(), prior);
        state.setLogPriorProbability(AsrContext.getContext().getLogScale().linearToLog(prior));
      }
      priorAccumulator.clear();
      statsWriter.close();
    } catch (Exception e) {
      log.error("", e);
    }
  }

  protected void resegment() {
    log.info("{} epoch - performing re-segmentation...", epoch);
    segmentationType = SegmentationType.RE_SEGMENTATION;
    doneSignal = new CountDownLatch(workers.size());
    resegmentQueue.addAll(trainingSet);
    for (SegmentationWorker worker : workers) {
      threadPoolExecutor.execute(worker);
    }
    try {
      doneSignal.await();
    } catch (InterruptedException e) {
      log.error("", e);
    }
    assert resegmentQueue.isEmpty();
    log.info("Segmentation finished successfully");
  }

  protected boolean isConvergence() {
    if (lastError < stopError) {
      return true;
    }
    if (epoch > minEpochs) {
      double avg = getAvgError(window);
      double diff = Math.abs(lastError - avg);
      diff = diff / avg;
      if (diff < errorStabilizationFactor) {
        log.info("Error stabilized");
        return true;
      }
    }
    if (epoch >= maxEpochs) {
      return true;
    }
    return false;
  }

  private double getAvgError(int window) {
    int w = window;
    if (errorHistory.size() < w) {
      w = errorHistory.size();
    }

    double sum = 0.0;
    for (int i = 0; i < w; i++) {
      sum += errorHistory.get(errorHistory.size() - i - 1);
    }

    return sum / w;
  }

  protected List<TrainPattern> createTrainPatterns() {
    int outputLength = model.getAllStates().size();
    List<TrainPattern> trainPatterns = new LinkedList();
    for (SegmentedTrainingItem item : trainingSet) {
      assert item.getSegmentation().size() == item.getTranscription().size() :
          "Values " + item.getSegmentation().size() + "," + item.getTranscription().size();
      List<StateSegment> segmentation = item.getSegmentation();
      for (int s = 0; s < segmentation.size(); s++) {
        StateSegment segment = segmentation.get(s);
        int length = segment.getLastFrame() - segment.getFirstFrame() + 1;
        priorAccumulator.collect(segment.getState().getId(), length);
        for (int i = segment.getFirstFrame(); i <= segment.getLastFrame(); i++) {
          Feature feature = item.getObservationSequence().get(i);
          NeuralStateDescriptor state = (NeuralStateDescriptor) segment.getState();
          double[] desiredOut = createDesiredOutput(outputLength, state.getOutputNumber());
          TrainPattern pattern =
              new TrainPattern(inputMapper.mapInput(feature.getData()), desiredOut);
          trainPatterns.add(pattern);
        }
      }
    }
    log.info("Train patterns size {}", trainPatterns.size());
    return trainPatterns;
  }

  protected double[] createDesiredOutput(int length, int classIndex) {
    double[] output = new double[length];
    output[classIndex] = 1.0;
    return output;
  }

  protected int getMaxIndex(double[] output) {
    int maxIndex = 0;
    for (int i = 0; i < output.length; i++) {
      if (output[i] > output[maxIndex]) {
        maxIndex = i;
      }
    }
    return maxIndex;
  }

  public void setWindow(int window) {
    this.window = window;
  }

  public void setStopError(double stopError) {
    this.stopError = stopError;
  }

  public void setRandomWeightAmpl(double randomWeightAmpl) {
    this.randomWeightAmpl = randomWeightAmpl;
  }

  public void setNoCycles(int noCycles) {
    this.noCycles = noCycles;
  }

  public void setMomentum(double momentum) {
    this.momentum = momentum;
  }

  public void setMinEpochs(int minEpochs) {
    this.minEpochs = minEpochs;
  }

  public void setMaxEpochs(int maxEpochs) {
    this.maxEpochs = maxEpochs;
  }

  public void setLearningRate(double learningRate) {
    this.learningRate = learningRate;
  }

  public void setFirstEpochNoCycles(int firstEpochNoCycles) {
    this.firstEpochNoCycles = firstEpochNoCycles;
  }

  public void setErrorStabilizationFactor(double errorStabilizationFactor) {
    this.errorStabilizationFactor = errorStabilizationFactor;
  }

  public void setEpoch(int epoch) {
    this.epoch = epoch;
  }

  public void setAlphaDelta(double alphaDelta) {
    this.alphaDelta = alphaDelta;
  }

  public void setAlpha(double alpha) {
    this.alpha = alpha;
  }

  public void setBatchSize(int batchSize) {
    this.batchSize = batchSize;
  }

  public void setBootstrapModel(AcousticModel bootstrapModel) {
    this.bootstrapModel = bootstrapModel;
  }

  public void setInputMapper(InputMapper inputMapper) {
    this.inputMapper = inputMapper;
  }

  private class SegmentationWorker implements Runnable {
    private int id;

    double avg;
    int min;
    int max;
    int n;
    String minId;
    String maxId;

    int noChanges;
    int sumChanges;

    public SegmentationWorker(int id) {
      this.id = id;
    }

    public void run() {
      avg = 0;
      min = 1000;
      max = 0;
      n = 0;
      minId = "";
      maxId = "";
      noChanges = 0;
      sumChanges = 0;
      try {
        while (true) {
          SegmentedTrainingItem item = resegmentQueue.poll(100, TimeUnit.MILLISECONDS);
          if (item == null) {
            log.info("Stopping worker with id {}", id);
            return;
          }
          if (segmentationType == SegmentationType.BOOTSTRAP) {
            bootstrapSegment(item);
          } else {
            resegment(item);
          }
        }
      } catch (Exception e) {
        log.error("", e);
        throw new AsrRuntimeException(e);
      } finally {
        if (segmentationType == SegmentationType.BOOTSTRAP) {
          log.info("Segm. stats: avg={}, min={}, max={}, minId={} maxId={}",
              new Object[]{avg / n, min, max, minId, maxId});
        } else {
          log.info("No changed segments {} avg frame shift {}", noChanges, ((double) sumChanges / (double) noChanges));
        }
        doneSignal.countDown();
      }
    }

    protected void resegment(SegmentedTrainingItem item) {
      ForcedAlignment forcedAlignment = forcedAlignmentMap.get(item);
      List<StateSegment> newSegmentation = forcedAlignment.findAlignment(item.getObservationSequence());
      List<Integer> boundaries = new ArrayList(newSegmentation.size() + 1);
      assert newSegmentation.size() == item.getSegmentation().size() :
          "Values " + newSegmentation.size() + "," + item.getSegmentation().size();
      Iterator<StateSegment> oldSegmentIter = item.getSegmentation().iterator();
      Iterator<StateSegment> newSegmentIter = newSegmentation.iterator();
      int lastBoundary = -1;
      while (newSegmentIter.hasNext()) {
        StateSegment newSegment = newSegmentIter.next();
        StateSegment oldSegment = oldSegmentIter.next();
        int newBoundary =
            (int) Math.floor(
                alpha * (double) oldSegment.getFirstFrame() + (1 - alpha) * (double) newSegment.getFirstFrame() + 0.1);
        if (newBoundary == lastBoundary) {
          newBoundary++;
        }
        assert newBoundary > lastBoundary :
            "New boundary=" + newBoundary + ", lastBoundary=" + lastBoundary + " old=" +
                oldSegment.getFirstFrame() + " new=" + newSegment.getFirstFrame();
        assert newBoundary < item.getObservationSequence().size();
        if (newBoundary != oldSegment.getFirstFrame()) {
          noChanges++;
          sumChanges += Math.abs(newBoundary - oldSegment.getFirstFrame());
        }
        boundaries.add(newBoundary);
        lastBoundary = newBoundary;
      }
      boundaries.add(item.getObservationSequence().size());
      List<StateSegment> segmentation = new LinkedList();
      for (int i = 0; i < newSegmentation.size(); i++) {
        StateSegment stateSegment = new StateSegment();
        stateSegment.setState(newSegmentation.get(i).getState());
        stateSegment.setFirstFrame(boundaries.get(i));
        stateSegment.setLastFrame(boundaries.get(i + 1) - 1);
        segmentation.add(stateSegment);
      }
      item.setSegmentation(segmentation);
    }

    public void bootstrapSegment(SegmentedTrainingItem item) {
      log.debug("Segmenting using bootstrap model in worker {}", id);
      ForcedAlignment forcedAlignment = new ForcedAlignment(bootstrapModel, item.getTranscription());
      List<StateSegment> segmentation = forcedAlignment.findAlignment(item.getObservationSequence());
      for (StateSegment stateSegment : segmentation) {
        n++;
        NeuralStateDescriptor neuralStateDescriptor = model.getState(stateSegment.getState().getId());
        assert neuralStateDescriptor != null;
        stateSegment.setState(neuralStateDescriptor);
        int length = stateSegment.getLastFrame() - stateSegment.getFirstFrame() + 1;
        avg += length;
        if (length < min) {
          min = length;
          minId = stateSegment.getState().getId();
        }
        if (length > max) {
          max = length;
          maxId = stateSegment.getState().getId();
        }
      }
      item.setSegmentation(segmentation);
    }
  }
}
