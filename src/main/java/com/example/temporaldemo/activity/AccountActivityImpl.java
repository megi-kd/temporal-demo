package com.example.temporaldemo.activity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccountActivityImpl implements AccountActivity {


  @Override
  public void deposit(String accountId, String referenceId, double amount) {
    log.info("Depositing {} into account {}, referenceId {}" , amount, accountId, referenceId);

    //uncommenting this line will simulate an ACTIVITY error
    //throw new RuntimeException("simulated");
  }

  @Override
  public void withdraw(String accountId, String referenceId, double amount) {
    log.info("Withdrawing {} from account {}, referenceId {}", amount, accountId, referenceId);
  }
}
