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
import org.speech.asr.recognition.acoustic.AcousticModel;
import org.speech.asr.recognition.acoustic.Feature;
import org.speech.asr.recognition.acoustic.StateDescriptor;
import org.speech.asr.recognition.decoder.*;
import org.speech.asr.recognition.linguist.SearchGraphBuilder;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 22, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class ForcedAlignment {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(ForcedAlignment.class.getName());

  private SentenceSearchGraphBuilder builder;

  private ViterbiSearchManager searchManager;

  private Decoder decoder;

  private AcousticModel model;

  private List<? extends StateDescriptor> transcription;

  public ForcedAlignment(AcousticModel model, List<? extends StateDescriptor> transcription) {
    this.transcription = transcription;
    this.model = model;
    builder = new SentenceSearchGraphBuilder(transcription);
    initDecoder(transcription.size() + 1);
  }

  public List<StateSegment> findAlignment(List<Feature> features) {
    List<Result> results = getResults(features);
//    log.debug("Found {} results", results.size());
    if (results.size() == 0) {
      throw new RuntimeException("There is no result");
    }
    Result result = results.get(0);
    List<FrameAlignment> frameAlignments = result.getFrameAlignments();
    assert frameAlignments.size() == features.size() : "Values " + frameAlignments.size() + "," + features.size();
    List<StateSegment> segments = new LinkedList();
    StateSegment lastSegment = new StateSegment();
    Iterator<FrameAlignment> iter = frameAlignments.iterator();
    FrameAlignment fa = iter.next();
    StateDescriptor state = model.getState(fa.getStateId());
    lastSegment.setState(state);
    lastSegment.setFirstFrame((int) fa.getSequenceNumber());
    while (iter.hasNext()) {
      fa = iter.next();
      if (fa.getStateId() != lastSegment.getState().getId()) {
        lastSegment.setLastFrame((int) fa.getSequenceNumber() - 1);
        segments.add(lastSegment);
        lastSegment = new StateSegment();
        state = model.getState(fa.getStateId());
        lastSegment.setState(state);
        lastSegment.setFirstFrame((int) fa.getSequenceNumber());
      }
    }
    lastSegment.setLastFrame((int) frameAlignments.get(frameAlignments.size() - 1).getSequenceNumber());
    segments.add(lastSegment);
    assert segments.size() == transcription.size();
    return segments;
  }


  protected void initDecoder(int beam) {
    searchManager = new ViterbiSearchManager();
    searchManager.setBeamWidth(beam);
    searchManager.setSearchGraphBuilder(builder);
    searchManager.init();

    DecoderImpl decoder = new DecoderImpl();
    decoder.setSearchManager(searchManager);
    this.decoder = decoder;
  }

  protected List<Result> getResults(List<Feature> features) {
    for (Feature feature : features) {
      decoder.decode(feature);
    }
    List<Result> results = decoder.getAllHypotheses();

    return results;
  }

  private class SentenceSearchGraphBuilder implements SearchGraphBuilder {

    private static final String NODE_SUFFIX = "_node";

    private static final String START_NODE = "start_node";

    private static final String END_NODE = "end_node";

    List<? extends StateDescriptor> transcription;

    public SentenceSearchGraphBuilder(List<? extends StateDescriptor> transcription) {
      this.transcription = transcription;
    }

    public SearchGraphSegment createSearchGraph() {
      log.debug("Creating search graph...");
      SearchNode startNode = new ConnectorSearchNode(START_NODE);
      SearchNode endNode = new ConnectorSearchNode(END_NODE);

      SearchNode lastNode;
      log.debug("Creating branch for {}", transcription);
      lastNode = startNode;
      for (StateDescriptor state : transcription) {
        StateDescriptor renewState = model.getState(state.getId());
        HmmSearchNode node = new HmmSearchNode(renewState);
        lastNode.addSuccessor(node, 0.0);
        lastNode = node;
      }
      WordSearchNode wordNode = new WordSearchNode("unknown" + NODE_SUFFIX, "unknown");
      lastNode.addSuccessor(wordNode, 0.0);
      wordNode.addSuccessor(endNode, 0.0);
      return new SearchGraphSegment(startNode, endNode);
    }
  }
}
