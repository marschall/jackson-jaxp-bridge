package com.github.marschall.jacksonjaxpbridge;

import javax.json.JsonNumber;
import javax.json.JsonValue;
import javax.json.JsonString;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

final class JsonpNodeFactory {

  private JsonpNodeFactory() {
    throw new AssertionError("not instantiable");
  }

  static JsonNode adapt(JsonValue value, JsonNodeFactory nc) {
    return switch (value.getValueType()) {
      case ARRAY  -> new JsonArrayNode(value.asJsonArray(), nc);
      case OBJECT  -> new JsonObjectNode(value.asJsonObject(), nc);
      case STRING -> nc.textNode(((JsonString) value).getString());
      case NUMBER -> adaptNumberNode((JsonNumber) value, nc);
      case TRUE   -> nc.booleanNode(true);
      case FALSE  -> nc.booleanNode(false);
      case NULL   -> nc.nullNode();
    };
  }

  private static JsonNode adaptNumberNode(JsonNumber value, JsonNodeFactory nc) {
    if (value.isIntegral()) {
      return nc.numberNode(value.bigDecimalValue());
    } else {
      try {
        long longValue = value.longValueExact();
        if (longValue <= Integer.MAX_VALUE && longValue >= Integer.MIN_VALUE) {
          int intValue = Math.toIntExact(longValue);
          return nc.numberNode(intValue);
        } else {
          return nc.numberNode(longValue);
        }
      } catch (ArithmeticException e) {
        return nc.numberNode(value.bigIntegerValueExact());
      }
    }
  }

}
