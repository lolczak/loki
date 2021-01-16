package org.speech.asr.gui.dao.jcr.mapping;

import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.gui.dao.jcr.resource.JcrResourceImpl;
import org.speech.asr.gui.dao.jcr.resource.LazyLoadedJcrResource;
import org.speech.asr.common.entity.*;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Creation date: Apr 24, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0.0
 */
public final class MapperUtils {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(MapperUtils.class.getName());

  private static Map<Class, JcrMapper> mappers;

  static {
    initialize();
  }

  private static void initialize() {
    mappers = new HashedMap();
    mappers.put(DictionaryEntity.class, new DictionaryMapper());
    mappers.put(Word.class, new WordMapper());
    mappers.put(CorpusEntity.class, new CorpusMapper());
    mappers.put(LazyLoadedJcrResource.class, new JcrResourceMapper());
    mappers.put(TranscribedUtterance.class, new TranscribedUtteranceMapper());
    mappers.put(JcrAudioFormat.class, new AudioFormatMapper());
    mappers.put(JcrResourceImpl.class, new JcrResourceMapper());
  }

  public static <T> T mapNode(Node node, Class<T> type) throws RepositoryException {
    JcrMapper<T> mapper = getMapper(type);

    return mapper.mapNode(node);
  }

  public static <T> void mapEntity(T fromEntity, Node toNode) throws RepositoryException {
    JcrMapper<T> mapper = getMapper(fromEntity.getClass());

    mapper.mapEntity(fromEntity, toNode);
  }

  private static JcrMapper getMapper(Class type) {
    JcrMapper mapper = mappers.get(type);
    if (mapper == null) {
      throw new IllegalArgumentException("There is no mapper for " + type.getName());
    }
    return mapper;
  }
}
