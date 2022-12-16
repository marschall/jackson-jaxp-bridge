package com.github.marschall.jacksonjaxpbridge;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonValue;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

final class JsonArrayNode extends ContainerNode<JsonArrayNode> {

  private JsonArray jsonArray;

  public JsonArrayNode(JsonArray jsonArray, JsonNodeFactory nc) {
    super(nc);
    Objects.requireNonNull(jsonArray, "jsonArray");
    this.jsonArray = jsonArray;
  }

  @Override
  public JsonToken asToken() {
    return JsonToken.START_ARRAY;
  }

  @Override
  public int size() {
    return this.jsonArray.size();
  }

  @Override
  public boolean isEmpty() {
    return this.jsonArray.isEmpty();
  }

  @Override
  public Iterator<JsonNode> elements() {
      return new AdaptingIterator(this.jsonArray.iterator(), this._nodeFactory);
  }

  @Override
  public JsonNode get(int index) {
    if ((index >= 0) && (index < this.jsonArray.size())) {
      JsonValue child = this.jsonArray.get(index);
      return JsonpNodeFactory.adapt(child, this._nodeFactory);
    }
    return null;
  }

  @Override
  public JsonNode get(String fieldName) {
    return null;
  }

  @Override
  protected ObjectNode _withObject(JsonPointer origPtr, JsonPointer currentPtr, OverwriteMode overwriteMode,
      boolean preferIndex) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public JsonArrayNode removeAll() {
    // JsonValue are immutable, have to create a new one
    this.jsonArray = Json.createArrayBuilder().build();
    return this;
  }

  @Override
  public int hashCode() {
    return this.jsonArray.hashCode();
  }

  @Override
  public void serialize(JsonGenerator g, SerializerProvider ctxt) throws IOException {
    // TODO Auto-generated method stub

  }

  @Override
  public void serializeWithType(JsonGenerator g, SerializerProvider ctxt, TypeSerializer typeSer) throws IOException {
    // TODO Auto-generated method stub

  }

  @Override
  public <T extends JsonNode> T deepCopy() {
    JsonArray copy = Json.createArrayBuilder(this.jsonArray).build();
    return (T) new JsonArrayNode(copy, this._nodeFactory);
  }

  @Override
  public JsonNode path(String fieldName) {
    return MissingNode.getInstance();
  }

  @Override
  public JsonNode path(int index) {
    if ((index >= 0) && (index < this.jsonArray.size())) {
      JsonValue child = this.jsonArray.get(index);
      return JsonpNodeFactory.adapt(child, this._nodeFactory);
    }
    return MissingNode.getInstance();
  }

  @Override
  protected JsonNode _at(JsonPointer ptr) {
    return this.get(ptr.getMatchingIndex());
  }

  @Override
  public JsonNodeType getNodeType() {
    return JsonNodeType.ARRAY;
  }

  @Override
  public boolean isArray() {
    return true;
  }

  @Override
  public JsonNode findValue(String fieldName) {
    return JsonpNodeFactory.findValue(this.jsonArray, fieldName, this._nodeFactory);
  }

  @Override
  public JsonNode findParent(String fieldName) {
    return JsonpNodeFactory.findParent(this.jsonArray, fieldName, this._nodeFactory);
  }

  @Override
  public List<JsonNode> findValues(String fieldName, List<JsonNode> foundSoFar) {
    return JsonpNodeFactory.findValues(this.jsonArray, fieldName, foundSoFar, this._nodeFactory);
  }

  @Override
  public List<String> findValuesAsText(String fieldName, List<String> foundSoFar) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<JsonNode> findParents(String fieldName, List<JsonNode> foundSoFar) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    return (o instanceof JsonArrayNode other)
        && this.jsonArray.equals(other.jsonArray);
  }

}
