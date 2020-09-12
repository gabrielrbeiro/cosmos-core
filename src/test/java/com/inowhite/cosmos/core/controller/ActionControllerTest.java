package com.inowhite.cosmos.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inowhite.cosmos.core.dto.ActionDto;
import com.inowhite.cosmos.core.entity.Action;
import com.inowhite.cosmos.core.repository.ActionRepository;
import com.inowhite.cosmos.core.service.ActionService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ActionControllerTest {

  private static final Logger logger = LoggerFactory.getLogger(ActionControllerTest.class);

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ActionService actionService;

  @Autowired
  private ActionRepository actionRepository;

  @Test
  public void testCreateNewAction() throws Exception {

    var query = "code==random:test_action;description=='Just a test action'";
    var result = actionService.list(1, 15, query);

    if (!result.isEmpty()) {
      result.forEach(actionService::remove);
    }

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
  public void testListAction() throws Exception {

    mockMvc.perform(
      get("/api/action")
    ).andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(header().exists("X-Count"));

  }

  @Test
  public void testUpdateAction() throws Exception {
    createOnMissing();
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
  public void testDeleteAction() throws Exception {
    createOnMissing();

    mockMvc.perform(
      delete("/api/action/random:test_action")
    ).andExpect(status().isNoContent());
  }

  private void createOnMissing() {
    var list = actionService.list(1, 15, "code=='random:test_action'");
    logger.info("find {} record(s) on database", list.size());

    if (list.isEmpty()) {
      logger.info("creating a new record on database");
      actionRepository.saveAndFlush(
        new Action()
          .setCode("random:test_action")
          .setDescription("Just a test action")
      );
    }
  }

}
