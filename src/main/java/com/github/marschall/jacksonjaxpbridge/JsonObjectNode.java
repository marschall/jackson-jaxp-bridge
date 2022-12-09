package com.github.marschall.jacksonjaxpbridge;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Map.Entry;

import javax.json.JsonObject;
import javax.json.JsonValue;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

final class JsonObjectNode extends ContainerNode<JsonObjectNode> {

  private final JsonObject jsonObject;

  public JsonObjectNode(JsonObject jsonObject, JsonNodeFactory nc) {
    super(nc);
    Objects.requireNonNull(jsonObject, "jsonObject");
    this.jsonObject = jsonObject;
  }

  @Override
  public JsonToken asToken() {
    return JsonToken.START_OBJECT;
  }

  @Override
  public int size() {
    return this.jsonObject.size();
  }
  
  @Override
  public boolean isEmpty() {
    return this.jsonObject.isEmpty();
  }
  
  @Override
  public Iterator<String> fieldNames() {
    return this.jsonObject.keySet().iterator();
  }
  
  @Override
  public Iterator<JsonNode> elements() {
    return new AdaptingIterator(this.jsonObject.values().iterator(), _nodeFactory);
  }
  
  @Override
  public Iterator<Entry<String, JsonNode>> fields() {
    return new AdaptingIterator(this.jsonObject.entrySet().iterator(), _nodeFactory);
  }

  @Override
  public JsonNode get(int index) {
    return null;
  }

  @Override
  public JsonNode get(String fieldName) {
    JsonValue child = this.jsonObject.get(fieldName);
    if (child != null) {
      return JsonpNodeFactory.adapt(child, _nodeFactory);
    } else {
      return null;
    }
  }

  @Override
  protected ObjectNode _withObject(JsonPointer origPtr, JsonPointer currentPtr, OverwriteMode overwriteMode,
      boolean preferIndex) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public JsonObjectNode removeAll() {
    // FIXME will fail
    this.jsonObject.clear();
    return this;
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
    JsonValue child = this.jsonObject.get(fieldName);
    if (child != null) {
        return JsonpNodeFactory.adapt(child, _nodeFactory);
    }
    return MissingNode.getInstance();
  }

  @Override
  public JsonNode path(int index) {
    return MissingNode.getInstance();
  }

  @Override
  protected JsonNode _at(JsonPointer ptr) {
    return get(ptr.getMatchingProperty());
  }

  @Override
  public JsonNodeType getNodeType() {
    return JsonNodeType.OBJECT;
  }
  
  @Override
  public boolean isObject() {
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
