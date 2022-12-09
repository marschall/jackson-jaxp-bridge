package com.github.marschall.jacksonjaxpbridge;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;

import javax.json.JsonValue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

final class AdaptingIterator implements Iterator<JsonNode> {
  
  private Iterator<JsonValue> delegate;
  private JsonNodeFactory nc;

  AdaptingIterator(Iterator<JsonValue> delegate, JsonNodeFactory nc) {
    this.nc = nc;
    Objects.requireNonNull(delegate, "delegate");
    Objects.requireNonNull(nc, "nc");
    this.delegate = delegate;
  }

  @Override
  public boolean hasNext() {
    return delegate.hasNext();
  }

  @Override
  public JsonNode next() {
    return JsonpNodeFactory.adapt(delegate.next(), this.nc);
  }

  @Override
  public void remove() {
    delegate.remove();
  }

  @Override
  public void forEachRemaining(Consumer<? super JsonNode> action) {
    delegate.forEachRemaining(value -> action.accept(JsonpNodeFactory.adapt(value, this.nc)));
  }

}
