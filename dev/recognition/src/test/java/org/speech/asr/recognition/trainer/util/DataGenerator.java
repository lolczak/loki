package org.speech.asr.recognition.trainer.util;

import org.speech.asr.recognition.trainer.TrainSentence;
import org.speech.asr.recognition.trainer.baumwelch.vo.Symbol;
import org.speech.asr.recognition.trainer.cvt.SegmentedTrainingItem;
import org.speech.asr.recognition.acoustic.AcousticModel;
import org.speech.asr.recognition.acoustic.NeuralHybridAcousticModel;
import org.speech.asr.recognition.math.LogScale;

import java.util.List;
import java.util.Map;

/**
 * //@todo interface description
 * <p/>
 * Creation date: Aug 14, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public interface DataGenerator {
  void generateSymbols();

  List<TrainSentence> generateSentenceSet(int noSentences, int sentenceLength, int avgFeatureLength,
                                                 int featureLengthDeviation, AcousticModel model);

//  List<SegmentedTrainingItem> generateSegmentedSet(int noSentences, int sentenceLength, int avgFeatureLength,
//                                                                                                         int featureLengthDeviation, NeuralHybridAcousticModel model);

  void setAvgObservationDeviation(double avgObservationDeviation);

  void setAvgObservationDistance(double avgObservationDistance);

  void setLogScale(LogScale logScale);

  void setNoDimensions(int noDimensions);

  void setNoMixtures(int noMixtures);

  void setNoSymbols(int noSymbols);

  Map<String, Symbol> getSymbols();
}
