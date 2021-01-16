package org.speech.asr.gui.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.common.entity.CorpusEntity;
import org.speech.asr.common.entity.DictionaryEntity;
import org.springframework.core.closure.Constraint;
import org.springframework.rules.Rules;
import org.springframework.rules.support.DefaultRulesSource;

/**
 * //@todo class description
 * <p/>
 * Creation date: Apr 18, 2009 <br/>
 *
 * @author Lukasz Olczak
 * @since 1.0
 */
public class SimpleValidationRulesSource extends DefaultRulesSource {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(SimpleValidationRulesSource.class.getName());

  private final Constraint NAME_CONSTRAINT = all(new Constraint[]{required(), minLength(2),
      regexp("[-'.a-zA-Z0-9 ]*", "alphabeticConstraint")
  });

  private final Constraint ZIPCODE_CONSTRAINT = all(new Constraint[]{required(), minLength(5), maxLength(10),
      regexp("[0-9]{5}(-[0-9]{4})?", "zipcodeConstraint")
  });

  private final Constraint FREQUENCY_CONSTRAINT = all(new Constraint[]{required(), minLength(4), maxLength(5),
      regexp("[1-9][0-9]*", "frequencyConstraint")
  });

  private final Constraint LANGUAGE_CONSTRAINT =
      all(new Constraint[]{required(), regexp("[a-z][a-z]-[a-z][a-z]", "languageConstraint")
      });

  public SimpleValidationRulesSource() {
    super();
    addRules(createDictionaryRules());
    addRules(createCorpusRules());
  }

  private Rules createDictionaryRules() {
    return new Rules(DictionaryEntity.class) {
      @Override
      protected void initRules() {
        add("name", NAME_CONSTRAINT);
        add("description", NAME_CONSTRAINT);
        add("phoneticAlphabet", NAME_CONSTRAINT);
        add(not(eqProperty("name", "description")));
        add("language", LANGUAGE_CONSTRAINT);
      }
    };
  }

  private Rules createCorpusRules() {
    return new Rules(CorpusEntity.class) {
      @Override
      protected void initRules() {
        add("name", NAME_CONSTRAINT);
        add("description", NAME_CONSTRAINT);
        add(not(eqProperty("name", "description")));
        add("language", required());
        add("frequency", FREQUENCY_CONSTRAINT);
      }
    };
  }

}
