package com.github.marschall.jacksonjaxpbridge;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.json.JsonArray;
import javax.json.JsonObject;
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

  private final JsonArray jsonArray;

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
      return new AdaptingIterator(this.jsonArray.iterator(), _nodeFactory);
  }

  @Override
  public JsonNode get(int index) {
    if (index >= 0 && index < this.jsonArray.size()) {
      JsonValue child = this.jsonArray.get(index);
      return JsonpNodeFactory.adapt(child, _nodeFactory);
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
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int hashCode() {
    // TODO Auto-generated method stub
    return 0;
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
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public JsonNode path(String fieldName) {
    return MissingNode.getInstance();
  }

  @Override
  public JsonNode path(int index) {
    if (index >= 0 && index < this.jsonArray.size()) {
      JsonValue child = this.jsonArray.get(index);
      return JsonpNodeFactory.adapt(child, _nodeFactory);
    }
    return MissingNode.getInstance();
  }

  @Override
  protected JsonNode _at(JsonPointer ptr) {
    return get(ptr.getMatchingIndex());
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
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public JsonNode findParent(String fieldName) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<JsonNode> findValues(String fieldName, List<JsonNode> foundSoFar) {
    // TODO Auto-generated method stub
    return null;
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
    // TODO Auto-generated method stub
    return false;
  }

}
