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
import org.joone.net.NeuralNet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.recognition.acoustic.NeuralHybridAcousticModel;
import org.speech.asr.recognition.acoustic.NeuralStateDescriptor;
import org.speech.asr.recognition.acoustic.Phoneme;
import org.speech.asr.recognition.acoustic.PhoneticUnit;
import org.speech.asr.recognition.ann.JooneNeuralNetAdapter;
import static org.speech.asr.recognition.constant.NeuralHybridAmConstants.*;
import org.speech.asr.recognition.context.AsrContext;
import org.speech.asr.recognition.frontend.ContextMfccMapper;
import org.speech.asr.recognition.frontend.MfccMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 28, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class NeuralHybridAmSerializer {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(NeuralHybridAmSerializer.class.getName());

  private static final String ENCODING = "UTF-8";

  public static void save(NeuralHybridAcousticModel model, OutputStream out) throws IOException {
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

  protected static Document createModelDocument(NeuralHybridAcousticModel model) {
    Document document = DocumentHelper.createDocument();
    Element root = document.addElement(ACOUSTIC_MODEL_TAG);
    root.addAttribute(ACOUSTIC_MODEL_TYPE_ATTR, HYBRID_AM_TYPE);
    for (PhoneticUnit unit : model.getPhoneSet()) {
      createPhonemeElement(unit, root);
    }
    Serializable nnetMemento = model.getNeuralNetwork().getMemento();
    byte[] byteArray = SerializerUtils.serialize(nnetMemento);
    String base64Memento = SerializerUtils.toBase64(byteArray);
    Element neuralElement = root.addElement(NEURAL_NET_TAG);
    neuralElement.addCDATA(base64Memento);
    return document;
  }

  protected static Element createPhonemeElement(PhoneticUnit<NeuralStateDescriptor> unit, Element parent) {
    Element unitElement = parent.addElement(PHONETIC_UNIT_TAG);
    unitElement.addAttribute(PHONETIC_UNIT_NAME_ATTR, unit.getName());
    int index = 0;
    for (NeuralStateDescriptor state : unit.getStatesSequence()) {
      createStateElement(state, index++, unitElement);
    }
    return unitElement;
  }

  protected static Element createStateElement(NeuralStateDescriptor state, int index, Element parent) {
    Element stateElement = parent.addElement(STATE_TAG);
    stateElement.addAttribute(STATE_ID_ATTR, state.getId());
    stateElement.addAttribute(STATE_INDEX_ATTR, String.valueOf(index));
    stateElement.addAttribute(STATE_OUTPUT_NUMBER_ATTR, String.valueOf(state.getOutputNumber()));
    double prior = AsrContext.getContext().getLogScale().logToLinear(state.getLogPriorProbability());
    stateElement.addAttribute(STATE_PRIOR_ATTR, String.valueOf(prior));
    return stateElement;
  }

  public static NeuralHybridAcousticModel load(InputStream in) throws IOException {
    try {
      SAXReader reader = new SAXReader();
      Document document = reader.read(in);
      return loadModel(document);
    } catch (Exception e) {
      throw new IOException("Error occurred during serialization of model", e);
    }
  }

  protected static NeuralHybridAcousticModel loadModel(Document document) {
    Element rootElement = document.getRootElement();
    Element neuralNetElement = rootElement.element(NEURAL_NET_TAG);
    String base64Net = neuralNetElement.getText();
    byte[] bytes = SerializerUtils.fromBase64(base64Net);
    NeuralNet neuralNet = (NeuralNet) SerializerUtils.deserialize(bytes);
    JooneNeuralNetAdapter adapter = new JooneNeuralNetAdapter(neuralNet, true);
    //todo fix mapper
    NeuralHybridAcousticModel model = new NeuralHybridAcousticModel(adapter, new MfccMapper());
    for (Element phonemeElement : (List<Element>) rootElement.elements(PHONETIC_UNIT_TAG)) {
      model.addPhoneticUnit(loadPhoneme(phonemeElement));
    }
    return model;
  }

  protected static Phoneme loadPhoneme(Element phonemeElement) {
    String name = phonemeElement.attribute(PHONETIC_UNIT_NAME_ATTR).getText();
    List<Element> stateElements = phonemeElement.elements(STATE_TAG);
    Collections.sort(stateElements, new IndexComparator());
    List<NeuralStateDescriptor> states = new LinkedList();
    for (Element stateElement : stateElements) {
      states.add(loadState(stateElement));
    }
    Phoneme<NeuralStateDescriptor> phoneme = new Phoneme(name, states);
    log.info("Phoneme {} loaded", phoneme.getName());
    return phoneme;
  }

  protected static NeuralStateDescriptor loadState(Element stateElement) {
    String id = stateElement.attribute(STATE_ID_ATTR).getText();
    int outputNumber = Integer.valueOf(stateElement.attribute(STATE_OUTPUT_NUMBER_ATTR).getText());
    double selfScore = 0.5;
    double logSelfScore = AsrContext.getContext().getLogScale().linearToLog(selfScore);
    double logPrior = AsrContext.getContext().getLogScale()
        .linearToLog(Double.valueOf(stateElement.attribute(STATE_PRIOR_ATTR).getText()));
    NeuralStateDescriptor state = new NeuralStateDescriptor(id, logSelfScore, outputNumber);
    state.setLogPriorProbability(logPrior);
    log.debug("NeuralHmmState {} loaded", state.getId());
    return state;
  }
}
