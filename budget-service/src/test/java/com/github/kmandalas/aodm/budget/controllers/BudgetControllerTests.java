package com.github.kmandalas.aodm.budget.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kmandalas.aodm.budget.controller.BudgetController;
import com.github.kmandalas.aodm.budget.domain.Account;
import com.github.kmandalas.aodm.budget.repository.AccountRepository;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class BudgetControllerTests {

  private static final String SPRING_CLOUD_BUS_TOPIC = "springCloudBus-0";
  private static final ObjectMapper MAPPER = new ObjectMapper();

  @ClassRule
  public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, SPRING_CLOUD_BUS_TOPIC);

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private AccountRepository accountRepository;

  private MockMvc mockMvc;

  private Account account;

  @Before
  public void setupMockMvc() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

    account = Account.builder()
            .adGroupId(999)
            .adGroupName("testGroup")
            .dailyBudget(500.00)
            .actualSpend(499.98)
            .itemPrice(0.2)
            .inFlightSpend(0.0)
            .build();
    accountRepository.save(account);
  }

  @Test
  public void getAllEligible_shouldReturnSuccessfully_whenHappyPath() throws Exception {
    mockMvc.perform(get(BudgetController.CONTROLLER_PATH + "/eligible"))
            .andExpect(status().isOk());
  }

  @Test
  public void getAccount_shouldReturnSuccessfully_whenHappyPath() throws Exception {
    mockMvc.perform(get(BudgetController.CONTROLLER_PATH + "/" + account.getAdGroupId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.adGroupId").value(account.getAdGroupId()));
  }

  @Test
  public void updateActualSpend_shouldReturnSuccessfully_whenHappyPath() throws Exception {
    mockMvc.perform(put(BudgetController.CONTROLLER_PATH + "/actual/" + account.getAdGroupId()))
            .andExpect(status().isOk());
  }

  @Test
  public void updateInFlightSpend_shouldReturnSuccessfully_whenHappyPath() throws Exception {
    mockMvc.perform(put(BudgetController.CONTROLLER_PATH + "/inFlight/" + account.getAdGroupId())
            .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .content(MAPPER.writeValueAsString(0.20)))
            .andExpect(status().isOk());
  }

}
