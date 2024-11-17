package com.example.mapaCife.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.example.mapaCife.config.SecurityConfiguration;

@RunWith(SpringRunner.class)
@WebMvcTest(HealthcheckController.class)
@Import(SecurityConfiguration.class)
public class HealthcheckControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void testHealthcheck() throws Exception {
    mockMvc.perform(get("/healthcheck"))
        .andExpectAll(
            status().isOk(),
            content().string("WORKING"));
  }

}
