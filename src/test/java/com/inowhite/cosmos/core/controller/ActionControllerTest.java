package com.inowhite.cosmos.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inowhite.cosmos.core.dto.ActionDto;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ActionControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @Order(1)
  public void testCreateNewAction() throws Exception {

    var body = new ActionDto()
      .setCode("random:test_action")
      .setDescription("Just a test action");

    mockMvc.perform(
      post("/api/action")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(body))
    ).andExpect(status().isCreated());

  }

  @Test
  @Order(2)
  public void testListAction() throws Exception {

    var body = new ActionDto()
      .setCode("random:test_action")
      .setDescription("Just a test action");

    mockMvc.perform(
      get("/api/action")
    ).andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(header().exists("X-Count"));

  }

  @Test
  @Order(3)
  public void testUpdateAction() throws Exception {

    var body = new ActionDto()
      .setCode("random:test_action")
      .setDescription("Just a test action, changed");

    mockMvc.perform(
      patch("/api/action")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(body))
    ).andExpect(status().isOk());

  }

  @Test
  @Order(4)
  public void testDeleteAction() throws Exception {

    mockMvc.perform(
      delete("/api/action/random:test_action")
    ).andExpect(status().isNoContent());

  }

}
