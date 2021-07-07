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
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Michael Jost (joscht@gmail.com)
 */
@SuppressWarnings("static-method")
public class VPackNewLineTest {

  private static final String RESULT_V2_5_2 = "\"{\\n"
      + "  \\\"description\\\": \\\"String with new line\\\"\\n"
      + "}\"";

  private static final String RESULT_V2_5_3 = "\"{\n"
      + "  \\\"description\\\": \\\"String with new line\\\"\n"
      + "}\"";

  private static final ObjectMapper MAPPER = new ObjectMapper();

  @Test
  public void newLine() throws VPackException {
    String json = "{\n"
        + "  \"description\": \"String with new line\"\n"
        + "}";
    VPackSlice vpack = new VPack.Builder().build().serialize(json);
    String parsed = new VPackParser.Builder().build().toJson(vpack);

    assertEquals(RESULT_V2_5_3, parsed);
    assertNotEquals(RESULT_V2_5_2, parsed);

    // all '\n' in arangodb have to be replaced with '\\n' for version 2.5.3
    json = "{\\n"
        + "  \"description\": \"String with new line\"\\n"
        + "}";
    vpack = new VPack.Builder().build().serialize(json);
    parsed = new VPackParser.Builder().build().toJson(vpack);
    assertEquals(RESULT_V2_5_2, parsed);
  }

  @Test
  public void readV2_5_2() throws JsonMappingException, JsonProcessingException {
    assertNotNull(MAPPER.readValue(RESULT_V2_5_2, Object.class));
  }

  @Test(expected = JsonParseException.class)
  public void readV2_5_3() throws JsonMappingException, JsonProcessingException {
    MAPPER.readValue(RESULT_V2_5_3, Object.class);
  }
}
