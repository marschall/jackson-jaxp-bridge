package com.github.marschall.jacksonjaxpbridge;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;

import javax.json.JsonValue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

final class AdaptingIterator implements Iterator<JsonNode> {

  private final Iterator<JsonValue> delegate;
  private final JsonNodeFactory nc;

  AdaptingIterator(Iterator<JsonValue> delegate, JsonNodeFactory nc) {
    this.nc = nc;
    Objects.requireNonNull(delegate, "delegate");
    Objects.requireNonNull(nc, "nc");
    this.delegate = delegate;
  }

  @Override
  public boolean hasNext() {
    return this.delegate.hasNext();
  }

  @Override
  public JsonNode next() {
    return JsonpNodeFactory.adapt(this.delegate.next(), this.nc);
  }

  @Override
  public void remove() {
    this.delegate.remove();
  }

  @Override
  public void forEachRemaining(Consumer<? super JsonNode> action) {
    this.delegate.forEachRemaining(value -> {
      JsonNode jsonNode = JsonpNodeFactory.adapt(value, this.nc);
      action.accept(jsonNode);
    });
  }

}
