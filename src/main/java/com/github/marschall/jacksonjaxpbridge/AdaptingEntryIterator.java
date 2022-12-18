package com.github.marschall.jacksonjaxpbridge;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Consumer;

import javax.json.JsonValue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

final class AdaptingEntryIterator implements Iterator<Entry<String, JsonNode>> {

  private final Iterator<Entry<String, JsonValue>> delegate;
  private final JsonNodeFactory nc;

  AdaptingEntryIterator(Iterator<Entry<String, JsonValue>> delegate, JsonNodeFactory nc) {
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
  public Entry<String, JsonNode> next() {
    Entry<String, JsonValue> unAdapted = this.delegate.next();
    JsonNode jsonNode = JsonpNodeAdapter.adapt(unAdapted.getValue(), this.nc);
    return Map.entry(unAdapted.getKey(), jsonNode);
  }

  @Override
  public void remove() {
    this.delegate.remove();
  }

  @Override
  public void forEachRemaining(Consumer<? super Entry<String, JsonNode>> action) {
    this.delegate.forEachRemaining(unAdapted -> {
      String key = unAdapted.getKey();
      Entry<String, JsonNode> adapted = Map.entry(key, JsonpNodeAdapter.adapt(unAdapted.getValue(), this.nc));
      action.accept(adapted);
    });
  }

}
