package org.speech.asr.gui.logic;

import org.speech.asr.common.entity.CorpusEntity;
import org.speech.asr.common.entity.TranscribedUtterance;

import java.util.List;

/**
 * //@todo interface description
 * <p/>
 * Creation date: Apr 22, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public interface CorpusBean {

  List<CorpusEntity> getAllCorpora();

  CorpusEntity getByUuid(String uuid);

  String create(CorpusEntity corpus);

  void delete(String uuid);

  CorpusEntity update(CorpusEntity corpus);

  void saveCorpusContent(String uuid, List<TranscribedUtterance> utternaces);

  List<TranscribedUtterance> getCorpusContent(String uuid);

  int getItemsCount(String uuid);

  TranscribedUtterance addItem(String corpusUuid, TranscribedUtterance tu);

  TranscribedUtterance updateItem(TranscribedUtterance tu);

  void deleteItem(String uuid);
}
