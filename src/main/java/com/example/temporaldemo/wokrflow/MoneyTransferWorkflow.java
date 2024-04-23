package com.example.temporaldemo.wokrflow;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface MoneyTransferWorkflow {

  //TODO: check both ways : The Workflow method is called by the initiator either via code or CLI.
  @WorkflowMethod
  void transfer( String fromAccountId, String toAccountId, String referenceId, double amount);

}
