package org.speech.asr.recognition.math;

/**
 * //@todo interface description
 * <p/>
 * Creation date: Jun 28, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public interface LogScale {

  double getBase();

  double getLogOne();

  double getLogZero();

  double getLogE();

  double getLogHalf();

  double linearToLog(double value);

  double logToLinear(double value);

  void linearToLog(double[] vector);

  void logToLinear(double[] vector);

  double addAsLinear(double logVal1, double... logValues);

  double subtractAsLinear(double logMinuend, double logSubtrahend);

  double logToLn(double value);

  double lnToLog(double value);

}
