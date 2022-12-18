package com.github.marschall.jacksonjaxpbridge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.cfg.JsonNodeFeature;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

/**
 * Adapts JAX-P {@link JsonValue} to Jackson {@link JsonNode}.
 */
public final class JsonpNodeAdapter {

  private JsonpNodeAdapter() {
    throw new AssertionError("not instantiable");
  }

  public static JsonNode adapt(JsonValue value, JsonNodeFactory nc) {
    Objects.requireNonNull(value, "value");
    Objects.requireNonNull(nc, "nc");
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

  static List<JsonNode> findParents(JsonValue jsonValue, String fieldName, List<JsonNode> foundSoFar, JsonNodeFactory nc) {
    return switch (jsonValue.getValueType()) {
      case ARRAY  -> {
        List<JsonNode> result = foundSoFar;
        for (JsonValue child : jsonValue.asJsonArray()) {
          result = findParents(child, fieldName, result, nc);
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
            result.add(adapt(jsonValue, nc));
          } else { // only add children if parent not added
            result = findParents(entry.getValue(), fieldName, result, nc);
          }
        }
        yield result;
      }
      case STRING, NUMBER, TRUE, FALSE, NULL   -> foundSoFar;
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
  
  static List<String> findValuesAsText(JsonValue jsonValue, String fieldName, List<String> foundSoFar) {
    return switch (jsonValue.getValueType()) {
    case ARRAY  -> {
      List<String> result = foundSoFar;
      for (JsonValue child : jsonValue.asJsonArray()) {
        result = findValuesAsText(child, fieldName, result);
      }
      yield result;
    }
    case OBJECT  -> {
      List<String> result = foundSoFar;
      for (Entry<String, JsonValue> entry : jsonValue.asJsonObject().entrySet()) {
        if (fieldName.equals(entry.getKey())) {
          if (result == null) {
            result = new ArrayList<>();
          }
          result.add(asText(entry.getValue()));
        } else { // only add children if parent not added
          result = findValuesAsText(entry.getValue(), fieldName, result);
        }
      }
      yield result;
    }
    case STRING, NUMBER, TRUE, FALSE, NULL   -> foundSoFar;
    };
  }

  private static String asText(JsonValue value) {
    return switch (value.getValueType()) {
      case ARRAY, OBJECT  -> "";
      case STRING -> ((JsonString) value).getString();
      case NUMBER -> value.toString();
      case TRUE   -> "true";
      case FALSE  -> "false";
      case NULL   -> "null";
    };
  }

  static void serialize(JsonValue value, JsonGenerator g, SerializerProvider provider) throws IOException {
    switch (value.getValueType()) {
      case ARRAY  -> {
        List<JsonValue> children = (JsonArray) value;
        int size = children.size();
        g.writeStartArray(value, size);
        for (JsonValue child : children) {
          serialize(child, g, provider);
        }
        g.writeEndArray();
      }
      case OBJECT  -> {
        if (provider != null) {
          boolean trimEmptyArray = !provider.isEnabled(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS);
          boolean skipNulls = !provider.isEnabled(JsonNodeFeature.WRITE_NULL_PROPERTIES);
          if (trimEmptyArray || skipNulls) {
              g.writeStartObject(value);
              serializeFilteredContents(((JsonObject) value), g, provider, trimEmptyArray, skipNulls);
              g.writeEndObject();
              return;
          }
        }
        g.writeStartObject(value);
        for (Map.Entry<String, JsonValue> entry : ((JsonObject) value).entrySet()) {
            JsonValue v = entry.getValue();
            g.writeFieldName(entry.getKey());
            serialize(v, g, provider);
        }
        g.writeEndObject();
      }
      case STRING -> g.writeString(((JsonString) value).getString());
      case NUMBER -> g.writeNumber(((JsonNumber) value).bigDecimalValue());
      case TRUE   -> g.writeBoolean(true);
      case FALSE  -> g.writeBoolean(false);
      case NULL   -> g.writeNull();
    };
  }

  static void serializeFilteredContents(JsonObject jsonObject, JsonGenerator g, SerializerProvider provider, boolean trimEmptyArray, boolean skipNulls)
      throws IOException {
    for (Entry<String, JsonValue> entry : jsonObject.entrySet()) {
      JsonValue value = entry.getValue();

      if (trimEmptyArray && (value.getValueType() == ValueType.ARRAY) && ((JsonArray) value).isEmpty()) {
        continue;
      }
      if (skipNulls && (value.getValueType() == ValueType.NULL)) {
        continue;
      }

      g.writeFieldName(entry.getKey());
      serialize(value, g, provider);
    }
  }

}
