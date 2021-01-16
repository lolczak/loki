/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.util;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.recognition.acoustic.GmmAcousticModel;
import org.speech.asr.recognition.acoustic.Phoneme;
import org.speech.asr.recognition.acoustic.PhoneticUnit;
import org.speech.asr.recognition.acoustic.StateDescriptor;
import static org.speech.asr.recognition.constant.GmmAcousticModelConstants.*;
import org.speech.asr.recognition.context.AsrContext;
import org.speech.asr.recognition.math.GaussianMixture;
import org.speech.asr.recognition.math.LogScale;
import org.speech.asr.recognition.math.MultivariateGaussian;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Creation date: Jul 28, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class GmmAmSerializer {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(GmmAmSerializer.class.getName());

  private static final String ENCODING = "UTF-8";

  public static void save(GmmAcousticModel model, OutputStream out) throws IOException {
    OutputFormat outformat = OutputFormat.createPrettyPrint();
    outformat.setEncoding(ENCODING);
    try {
      XMLWriter writer = new XMLWriter(out, outformat);
      writer.write(createModelDocument(model));
      writer.flush();
    } catch (Exception e) {
      throw new IOException("Error occurred during serialization of model", e);
    }
  }

  protected static Document createModelDocument(GmmAcousticModel model) {
    Document document = DocumentHelper.createDocument();
    Element root = document.addElement(ACOUSTIC_MODEL_TAG);
    root.addAttribute(ACOUSTIC_MODEL_TYPE_ATTR, GMM_AM_TYPE);
    Element noMixtures = root.addElement(NO_MIXTURES_TAG);
    noMixtures.setText(String.valueOf(model.getNoMixtures()));
    Element noDimensions = root.addElement(NO_DIMENSIONS_TAG);
    noDimensions.setText(String.valueOf(model.getNoDimensions()));
    for (PhoneticUnit unit : model.getPhoneSet()) {
      createPhonemeElement(unit, root);
    }
    return document;
  }

  protected static Element createPhonemeElement(PhoneticUnit<StateDescriptor> unit, Element parent) {
    Element unitElement = parent.addElement(PHONETIC_UNIT_TAG);
    unitElement.addAttribute(PHONETIC_UNIT_NAME_ATTR, unit.getName());
    int index = 0;
    for (StateDescriptor state : unit.getStatesSequence()) {
      createStateElement(state, index++, unitElement);
    }
    return unitElement;
  }

  protected static Element createStateElement(StateDescriptor state, int index, Element parent) {
    Element stateElement = parent.addElement(STATE_TAG);
    stateElement.addAttribute(STATE_ID_ATTR, state.getId());
    stateElement.addAttribute(STATE_INDEX_ATTR, String.valueOf(index));
    Element selfLoopElement = stateElement.addElement(SELF_LOOP_TAG);
    selfLoopElement.addText(String.valueOf(state.getSelfLoopProbability()));
    createGmmElement((GaussianMixture) state.getScorer(), stateElement);
    return stateElement;
  }

  protected static Element createGmmElement(GaussianMixture gmm, Element parent) {
    Element gmmElement = parent.addElement(GMM_TAG);
    double[] weights = gmm.getWeights();
    MultivariateGaussian[] gaussians = gmm.getMixtureComponents();
    assert weights.length == gaussians.length : "Values " + weights.length + "," + gaussians.length;
    for (int i = 0; i < weights.length; i++) {
      Element mixtureComponent = gmmElement.addElement(MIXTURE_COMPONENT_TAG);
      Element weight = mixtureComponent.addElement(WEIGHT_TAG);
      weight.addText(String.valueOf(weights[i]));
      double[] meanVector = gaussians[i].getMean();
      double[] deviationVector = gaussians[i].getDeviation();
      assert meanVector.length == deviationVector.length : "Values " + meanVector.length + "," + deviationVector.length;
      for (int d = 0; d < meanVector.length; d++) {
        Element dimElement = mixtureComponent.addElement(DIMENSION_TAG);
        dimElement.addAttribute(DIMENSION_INDEX_ATTR, String.valueOf(d));
        dimElement.addAttribute(MEAN_ATTR, String.valueOf(meanVector[d]));
        dimElement.addAttribute(DEVIATION_ATTR, String.valueOf(deviationVector[d]));
      }
    }

    return gmmElement;
  }

  public static GmmAcousticModel load(InputStream in) throws IOException {
    try {
      SAXReader reader = new SAXReader();
      Document document = reader.read(in);
      return loadModel(document);
    } catch (Exception e) {
      throw new IOException("Error occurred during serialization of model", e);
    }
  }

  protected static GmmAcousticModel loadModel(Document document) {
    Element rootElement = document.getRootElement();
    Element noDimensionsElement = rootElement.element(NO_DIMENSIONS_TAG);
    Element noMixturesElement = rootElement.element(NO_MIXTURES_TAG);
    int noMixtures = Integer.valueOf(noMixturesElement.getText());
    int noDimensions = Integer.valueOf(noDimensionsElement.getText());

    GmmAcousticModel model = new GmmAcousticModel(noDimensions, noMixtures);
    for (Element phonemeElement : (List<Element>) rootElement.elements(PHONETIC_UNIT_TAG)) {
      model.addPhoneticUnit(loadPhoneme(phonemeElement));
    }
    return model;
  }

  protected static Phoneme loadPhoneme(Element phonemeElement) {
    String name = phonemeElement.attribute(PHONETIC_UNIT_NAME_ATTR).getText();
    List<Element> stateElements = phonemeElement.elements(STATE_TAG);
    Collections.sort(stateElements, new IndexComparator());
    List<StateDescriptor> states = new LinkedList();
    for (Element stateElement : stateElements) {
      states.add(loadState(stateElement));
    }
    Phoneme phoneme = new Phoneme(name, states);
    log.info("Phoneme {} loaded", phoneme.getName());
    return phoneme;
  }

  protected static StateDescriptor loadState(Element stateElement) {
    String id = stateElement.attribute(STATE_ID_ATTR).getText();
    double selfScore = Double.valueOf(stateElement.element(SELF_LOOP_TAG).getText());
    double logSelfScore = AsrContext.getContext().getLogScale().linearToLog(selfScore);
    GaussianMixture gmm = loadGmm(stateElement.element(GMM_TAG));
    StateDescriptor state = new StateDescriptor(id, gmm, logSelfScore);
    log.debug("HmmState {} loaded", state.getId());
    return state;
  }

  protected static GaussianMixture loadGmm(Element gmmElement) {
    LogScale logScale = AsrContext.getContext().getLogScale();
    List<Element> mixtureElements = gmmElement.elements(MIXTURE_COMPONENT_TAG);
    double[] weights = new double[mixtureElements.size()];
    MultivariateGaussian[] components = new MultivariateGaussian[mixtureElements.size()];
    int index = 0;
    for (Element mixtureElement : mixtureElements) {
      MultivariateGaussian component = loadMixtureComponent(mixtureElement);
      double weight = Double.valueOf(mixtureElement.element(WEIGHT_TAG).getText());
      components[index] = component;
      weights[index] = logScale.linearToLog(weight);
      index++;
    }

    return new GaussianMixture(logScale, components, weights);
  }

  protected static MultivariateGaussian loadMixtureComponent(Element mixtureElement) {
    List<Element> dimensionElements = mixtureElement.elements(DIMENSION_TAG);
    Collections.sort(dimensionElements, new IndexComparator());
    double[] means = new double[dimensionElements.size()];
    double[] deviations = new double[dimensionElements.size()];
    int index = 0;
    for (Element dimensionElement : dimensionElements) {
      means[index] = Double.valueOf(dimensionElement.attribute(MEAN_ATTR).getText());
      deviations[index] = Double.valueOf(dimensionElement.attribute(DEVIATION_ATTR).getText());
      index++;
    }

    return new MultivariateGaussian(AsrContext.getContext().getLogScale(), means, deviations);
  }

}
