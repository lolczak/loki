/*
 * Copyright (C) Grape Software 2006
 *
 * All rights reserved. Any use, copying, modification, distribution and selling 
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package org.speech.asr.recognition.util;

import sun.misc.BASE64Decoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 13, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class SerializerUtils {
  /**
   * slf4j Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(SerializerUtils.class.getName());

  public static void save(Object obj, File file) {
    FileOutputStream fileOut = null;
    ObjectOutputStream objOut = null;
    try {
      fileOut = new FileOutputStream(file);
      objOut = new ObjectOutputStream(fileOut);
      objOut.writeObject(obj);
      objOut.flush();
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      if (fileOut != null) {
        try {
          fileOut.close();
        } catch (IOException e) {
          log.error("", e);
        }
      }
      if (objOut != null) {
        try {
          objOut.close();
        } catch (IOException e) {
          log.error("", e);
        }
      }
    }
  }

  public static byte[] serialize(Serializable obj) {
    try {
      ByteArrayOutputStream f = new ByteArrayOutputStream();
      ObjectOutput s = new ObjectOutputStream(f);
      s.writeObject(obj);
      s.flush();
      return f.toByteArray();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static Object deserialize(byte[] bytes) {
    try {
      ByteArrayInputStream fi = new ByteArrayInputStream(bytes);
      ObjectInput oi = new ObjectInputStream(fi);
      return oi.readObject();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static Object deepCopy(Serializable obj) {
    return deserialize(serialize(obj));
  }

  public static byte[] fromBase64(String str) {
    try {
      return new BASE64Decoder().decodeBuffer(str);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String toBase64(byte[] bytes) {
    String s = new sun.misc.BASE64Encoder().encode(bytes);
    return s;
  }

  public static Object load(File file) {
    FileInputStream fileIn = null;
    ObjectInputStream objIn = null;
    try {
      fileIn = new FileInputStream(file);
      objIn = new ObjectInputStream(fileIn);
      return objIn.readObject();
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      if (fileIn != null) {
        try {
          fileIn.close();
        } catch (IOException e) {
          log.error("", e);
        }
      }
      if (objIn != null) {
        try {
          objIn.close();
        } catch (IOException e) {
          log.error("", e);
        }
      }
    }
  }
}
