package com.github.kmandalas.aodm.inventory.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kmandalas.aodm.inventory.transport.AccountDTO;
import com.github.kmandalas.aodm.inventory.transport.InsertionDTO;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.netflix.ribbon.StaticServerList;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class InventoryControllerTests {

  private static final String DEFAULT_TOPIC = "ad-insertion-input-test";
  private static final String SPRING_CLOUD_BUS_TOPIC = "springCloudBus-0";
  private static final ObjectMapper MAPPER = new ObjectMapper();

  @ClassRule
  public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, DEFAULT_TOPIC, SPRING_CLOUD_BUS_TOPIC);

  @ClassRule
  public static WireMockClassRule wiremock = new WireMockClassRule(wireMockConfig().dynamicPort());

  private static final String URI = "/inventory";

  private static final int ELIGIBLE_AD_GROUP_ID = 999;
  private static final int INELIGIBLE_AD_GROUP_ID = 1000;

  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Before
  public void setupMockMvc() throws Exception {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

    MockitoAnnotations.initMocks(this);

    AccountDTO eligibleAccount = AccountDTO.builder()
            .id(999)
            .adGroupId(999)
            .actualSpend(400.00)
            .inFlightSpend(99.98)
            .dailyBudget(500.00)
            .itemPrice(0.2)
            .build();

    AccountDTO inelegibleAccount = AccountDTO.builder()
            .id(1000)
            .adGroupId(1000)
            .actualSpend(400.00)
            .inFlightSpend(100.02)
            .dailyBudget(500.00)
            .itemPrice(0.2)
            .build();

    stubFor(get(urlEqualTo("/budget/999"))
            .withHeader("Content-Type", equalTo("application/json;charset=UTF-8"))
            .willReturn(aResponse()
                    .withHeader("Content-Type", "application/json;charset=UTF-8")
                    .withStatus(HttpStatus.OK.value())
                    .withBody(MAPPER.writeValueAsString(eligibleAccount))));

    stubFor(get(urlEqualTo("/budget/1000"))
            .withHeader("Content-Type", equalTo("application/json;charset=UTF-8"))
            .willReturn(aResponse()
                    .withHeader("Content-Type", "application/json;charset=UTF-8")
                    .withStatus(HttpStatus.OK.value())
                    .withBody(MAPPER.writeValueAsString(inelegibleAccount))));
  }

  @Test
  public void insertAd_shouldReturnSuccessfully_whenUnderBudget() throws Exception {
    InsertionDTO insertionDTO = InsertionDTO.builder().adGroupId(ELIGIBLE_AD_GROUP_ID).domain("whatever.com").build();

    mockMvc.perform(post(URI)
            .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .content(MAPPER.writeValueAsString(insertionDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(true));
  }

  @Test
  public void insertAd_shouldReturnSuccessfully_whenOverBudget() throws Exception {
    InsertionDTO insertionDTO = InsertionDTO.builder().adGroupId(INELIGIBLE_AD_GROUP_ID).domain("whatever.com").build();

    mockMvc.perform(post(URI)
            .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .content(MAPPER.writeValueAsString(insertionDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(false));
  }

  @TestConfiguration
  public static class LocalRibbonClientConfiguration {
    @Bean
    public ServerList<Server> ribbonServerList() {
      return new StaticServerList<>(new Server("localhost", wiremock.port()));
    }
  }

}
