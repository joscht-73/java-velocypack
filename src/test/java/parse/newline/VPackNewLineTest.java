/*
 *******************************************************************************
 *  Copyright (C) 2021 by Misurio AG.
 *  All Rights Reserved.
 *******************************************************************************
 */
package parse.newline;

import org.junit.Test;

import com.arangodb.velocypack.VPack;
import com.arangodb.velocypack.VPackParser;
import com.arangodb.velocypack.VPackSlice;
import com.arangodb.velocypack.exception.VPackException;

import static org.junit.Assert.assertEquals;

/**
 * @author Michael Jost (joscht@gmail.com)
 */
@SuppressWarnings("static-method")
public class VPackNewLineTest {

  @Test
  public void newLine() throws VPackException {
    String json = "{\n"
        + "  \"description\": \"String with new line\"\n"
        + "}";
    VPackSlice vpack = new VPack.Builder().build().serialize(json);
    String expected = "\"{\n"
        + "  \\\"description\\\": \\\"String with new line\\\"\n"
        + "}\"";
    assertEquals(expected,
        new VPackParser.Builder().build().toJson(vpack));
  }
}
