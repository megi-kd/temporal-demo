package com.example.temporaldemo.controller;

import com.example.temporaldemo.util.Constants;
import com.example.temporaldemo.wokrflow.MoneyTransferWorkflow;
import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InitiateMoneyTransfer {

  public static void main(String[] args) {

    // WorkflowServiceStubs is 'a gRPC stubs wrapper' that talks to the local instance of the Temporal server.
    WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();

    WorkflowOptions options = WorkflowOptions.newBuilder()
        .setTaskQueue(Constants.MONEY_TRANSFER_TASK_QUEUE)
        .setWorkflowId("money-transfer-workflow")        // A WorkflowId prevents this it from having duplicate instances, remove it to duplicate.
        .build();

    // WorkflowClient can be used to start, signal, query, cancel, and terminate Workflows.
    WorkflowClient client = WorkflowClient.newInstance(service);

    // WorkflowStubs enable calls to methods as if the Workflow object is local, but actually perform an RPC.
    MoneyTransferWorkflow mtWorkflow = client.newWorkflowStub(MoneyTransferWorkflow.class, options);
    String referenceId = UUID.randomUUID().toString();
    String fromAccount = "001-001";
    String toAccount = "002-002";
    double amount = 200.00;

    // Asynchronous execution. This process will exit after making this call.
    WorkflowExecution execution = WorkflowClient.start(mtWorkflow::transfer, fromAccount,toAccount,referenceId,amount);

    log.info("Transfer of amount [{}] is processing from account [{}] to account [{}]", amount, fromAccount,toAccount);
    log.info("WorkflowID [{}], runID [{}]", execution.getWorkflowId(), execution.getRunId() );

    System.exit(0);



  }

}
