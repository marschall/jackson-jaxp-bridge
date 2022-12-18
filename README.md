Jackson JAX-P Bridge
====================

Adapts JAX-P `JsonValue` objects to Jackson `JsonNode` so that they can be passed to `ObjectReader`.

Usage
-----

```java
ObjectMapper objectMapper = ...;
JsonObject jaxpNode = ...;

JsonNode jacksonNode = JsonpNodeAdapter.adapt(jaxpNode, objectMapper.getNodeFactory());

ObjectReader objectReader = objectMapper.readerFor(Project.class);
Project project = objectReader.readValue(jacksonNode);
```

Limitations
-----------

- Only the bare minimum to support `ObjectReader` is implemented, many mutation methods are missing.
- Only `JsonObject` and `JsonArray` are adapted, all value nodes are lazily copied.

