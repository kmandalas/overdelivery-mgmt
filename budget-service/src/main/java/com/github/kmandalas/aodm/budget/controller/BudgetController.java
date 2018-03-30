package com.github.kmandalas.aodm.budget.controller;

import com.github.kmandalas.aodm.budget.domain.Account;
import com.github.kmandalas.aodm.budget.domain.AmountsOnly;
import com.github.kmandalas.aodm.budget.service.AccountService;
import com.github.kmandalas.aodm.budget.transport.AccountDTO;
import com.github.kmandalas.aodm.budget.transport.ChartDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@Slf4j
public class BudgetController {

  public static final String CONTROLLER_PATH = "/budget";

  private final AccountService accountService;

  @Autowired
  public BudgetController(AccountService accountService) {
    this.accountService = accountService;
  }

  @GetMapping(CONTROLLER_PATH + "/eligible")
  @ResponseStatus(HttpStatus.OK)
  public List<Account> getEligible() {
    log.info("Getting the eligible as groups...");

    return accountService.findEligible();
  }

  @GetMapping(CONTROLLER_PATH + "/{adGroupId}")
  @ResponseStatus(HttpStatus.OK)
  public AccountDTO getAccount(@PathVariable int adGroupId) {
    log.info("Getting account info for ad-group: " + adGroupId);

    final Account account = accountService.findByAdGroupId(adGroupId);
    final AccountDTO dto = new AccountDTO();
    BeanUtils.copyProperties(account, dto);

    return dto;
  }

  @GetMapping(CONTROLLER_PATH + "/poll/{adGroupId}")
  @ResponseStatus(HttpStatus.OK)
  public ChartDTO poll(@PathVariable int adGroupId) {
    AmountsOnly amountsOnly = accountService.poll(adGroupId);
    ChartDTO dto = ChartDTO.builder()
            .adGroupId(adGroupId)
            .actual(amountsOnly.getActualSpend())
            .predicted(amountsOnly.getInFlightSpend())
            .build();

    return dto;
  }

  // @PutMapping("/actual/{adGroupId}/") | TODO: check if this works and if yes apply it everywhere
  @RequestMapping(value = CONTROLLER_PATH + "/actual/{adGroupId}", method = RequestMethod.PUT)
  @ResponseStatus(HttpStatus.OK)
  public void updateActualSpend(@PathVariable int adGroupId) {
    log.info("updating actual spend for ad-group-id: " + adGroupId);

    accountService.updateActualSpend(adGroupId);
  }

  @RequestMapping(value = CONTROLLER_PATH + "/inFlight/{adGroupId}", method = RequestMethod.PUT)
  @ResponseStatus(HttpStatus.OK)
  public void updateInflightSpend(@PathVariable int adGroupId, @RequestBody double amount) {
    log.info("updating in-flight spend for ad-group-id:: " + adGroupId);

    accountService.updateInFlightSpend(adGroupId, amount);
  }
}
