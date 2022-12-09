package com.github.marschall.jacksonjaxpbridge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonStructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

class ObjectMapperTests {

  private static final String JSON = """
      {
        "projectId": "ABC-123-4",
        "members": ["Jack", "Joe"]
      }
      """;

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    this.objectMapper = new ObjectMapper();
  }

  private static void assertProject(Project project) {
    assertNotNull(project);
    assertEquals("ABC-123-4", project.getProjectId());
    assertEquals(List.of("Jack", "Joe"), project.getMembers());
  }

  @Test
  void defaultParser() throws JacksonException {
    ObjectReader reader = this.objectMapper.readerFor(Project.class);
    Project project = reader.readValue(JSON);
    assertProject(project);
  }

  @Test
  void customParser() throws JacksonException, IOException {
    // Setup JSON-P
    JsonReader jsonReader = Json.createReader(new StringReader(JSON));
    JsonStructure structure = jsonReader.read();

    // Adapt to Jackson
    JsonNode jsonNode = JsonpNodeFactory.adapt(structure, this.objectMapper.getNodeFactory());

    ObjectReader objectReader = this.objectMapper.readerFor(Project.class);
    Project project = objectReader.readValue(jsonNode);
    assertProject(project);
  }

  static final class Project {

    private String projectId;

    private List<String> members;

    String getProjectId() {
      return projectId;
    }

    void setProjectId(String projectId) {
      this.projectId = projectId;
    }

    List<String> getMembers() {
      return members;
    }

    void setMembers(List<String> members) {
      this.members = members;
    }

  }

}
