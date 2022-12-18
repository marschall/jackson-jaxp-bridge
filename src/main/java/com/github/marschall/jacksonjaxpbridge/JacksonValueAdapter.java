package com.github.marschall.jacksonjaxpbridge;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;

public final class JacksonValueAdapter {

  private JacksonValueAdapter() {
    throw new AssertionError("not instantiable");
  }

  public static JsonValue adapt(JsonNode node) {
    return switch (node.getNodeType()) {
      case ARRAY ->
      case BINARY -> throw new UnsupportedOperationException("binary type not supported");
      case BOOLEAN -> node.booleanValue() ? JsonValue.TRUE : JsonValue.FALSE;
      case MISSING ->
      case NULL -> JsonValue.NULL;
      case NUMBER -> new NumberNodeAdapter(node);
      case POJO -> throw new UnsupportedOperationException("POJO type not supported");
      case STRING -> new TextNodeAdapter(node);
    }
  }

  static final class TextNodeAdapter implements JsonString {

    private final JsonNode textNode;

    TextNodeAdapter(JsonNode textNode) {
      this.textNode = textNode;
    }

    @Override
    public ValueType getValueType() {
      return ValueType.STRING;
    }

    @Override
    public JsonObject asJsonObject() {
      throw new ClassCastException();
    }

    @Override
    public JsonArray asJsonArray() {
      throw new ClassCastException();
    }

    @Override
    public String getString() {
      return this.textNode.textValue();
    }

    @Override
    public CharSequence getChars() {
      return this.getString();
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == this) {
        return true;
      }
      return (obj instanceof JsonString other)
          && this.getString().equals(other.getString());
    }

    @Override
    public int hashCode() {
      return this.getString().hashCode();
    }

    @Override
    public String toString() {
      return this.getString();
    }

  }

  static final class NumberNodeAdapter implements JsonNumber {

    private final JsonNode numberNode;

    NumberNodeAdapter(JsonNode numberNode) {
      this.numberNode = numberNode;
    }

    @Override
    public ValueType getValueType() {
      return ValueType.STRING;
    }

    @Override
    public JsonObject asJsonObject() {
      throw new ClassCastException();
    }

    @Override
    public JsonArray asJsonArray() {
      throw new ClassCastException();
    }

    @Override
    public boolean isIntegral() {
      return this.numberNode.isIntegralNumber();
    }

    @Override
    public int intValue() {
      return this.numberNode.intValue();
    }

    @Override
    public int intValueExact() {
      if (!this.numberNode.canConvertToInt()) {
        throw new ArithmeticException("Overflow");
      }
      return this.numberNode.intValue();
    }

    @Override
    public long longValue() {
      return this.numberNode.longValue();
    }

    @Override
    public long longValueExact() {
      if (!this.numberNode.canConvertToLong()) {
        throw new ArithmeticException("Overflow");
      }
      return this.numberNode.longValue();
    }

    @Override
    public BigInteger bigIntegerValue() {
      return this.numberNode.bigIntegerValue();
    }

    @Override
    public BigInteger bigIntegerValueExact() {
      if (!this.numberNode.canConvertToExactIntegral()) {
        throw new ArithmeticException("Overflow");
      }
      return this.numberNode.bigIntegerValue();
    }

    @Override
    public double doubleValue() {
      return this.numberNode.doubleValue();
    }

    @Override
    public BigDecimal bigDecimalValue() {
      return this.numberNode.decimalValue();
    }

    @Override
    public Number numberValue() {
      return this.numberNode.numberValue();
    }

  }

}
