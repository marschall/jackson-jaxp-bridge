package com.github.marschall.jacksonjaxpbridge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.json.JsonNumber;
import javax.json.JsonString;
import javax.json.JsonValue;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
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
        if ((longValue <= Integer.MAX_VALUE) && (longValue >= Integer.MIN_VALUE)) {
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

  static JsonNode findParent(JsonValue jsonValue, String fieldName, JsonNodeFactory nc) {
    return switch (jsonValue.getValueType()) {
      case ARRAY  -> {
        for (JsonValue child : jsonValue.asJsonArray()) {
          JsonNode parent = findParent(child, fieldName, nc);
          if (parent != null) {
            yield parent;
          }
        }
        yield null;
      }
      case OBJECT  -> {
        for (Entry<String, JsonValue> entry : jsonValue.asJsonObject().entrySet()) {
          if (fieldName.equals(entry.getKey())) {
            yield adapt(jsonValue, nc);
          }
          JsonNode value = findValue(entry.getValue(), fieldName, nc);
          if (value != null) {
            yield value;
          }
        }
        yield null;
      }
      case STRING, NUMBER, TRUE, FALSE, NULL   -> null;
    };
  }

  static JsonNode findValue(JsonValue jsonValue, String fieldName, JsonNodeFactory nc) {
    return switch (jsonValue.getValueType()) {
      case ARRAY  -> {
        for (JsonValue child : jsonValue.asJsonArray()) {
          JsonNode value = findValue(child, fieldName, nc);
          if (value != null) {
            yield value;
          }
        }
        yield null;
      }
      case OBJECT  -> {
        for (Entry<String, JsonValue> entry : jsonValue.asJsonObject().entrySet()) {
          if (fieldName.equals(entry.getKey())) {
            yield adapt(entry.getValue(), nc);
          }
          JsonNode value = findValue(entry.getValue(), fieldName, nc);
          if (value != null) {
            yield value;
          }
        }
        yield null;
      }
      case STRING, NUMBER, TRUE, FALSE, NULL   -> null;
    };
  }

  static List<JsonNode> findValues(JsonValue jsonValue, String fieldName, List<JsonNode> foundSoFar, JsonNodeFactory nc) {
    return switch (jsonValue.getValueType()) {
    case ARRAY  -> {
      List<JsonNode> result = foundSoFar;
      for (JsonValue child : jsonValue.asJsonArray()) {
        result = findValues(child, fieldName, result, nc);
      }
      yield result;
    }
    case OBJECT  -> {
      List<JsonNode> result = foundSoFar;
      for (Entry<String, JsonValue> entry : jsonValue.asJsonObject().entrySet()) {
        if (fieldName.equals(entry.getKey())) {
          if (result == null) {
            result = new ArrayList<>();
          }
          result.add(adapt(entry.getValue(), nc));
        } else { // only add children if parent not added
          result = findValues(entry.getValue(), fieldName, result, nc);
        }
      }
      yield result;
    }
    case STRING, NUMBER, TRUE, FALSE, NULL   -> foundSoFar;
    };
  }

  static void serialize(JsonValue value, JsonGenerator g, SerializerProvider provider) {
    switch (value.getValueType()) {
    case ARRAY  -> new JsonArrayNode(value.asJsonArray(), nc);
    case OBJECT  -> new JsonObjectNode(value.asJsonObject(), nc);
    case STRING -> nc.textNode(((JsonString) value).getString());
    case NUMBER -> adaptNumberNode((JsonNumber) value, nc);
    case TRUE   -> g.writeBoolean(true);
    case FALSE  -> g.writeBoolean(false);
    case NULL   -> g.writeString(((JsonString) value).getString());
  };
  }

}
