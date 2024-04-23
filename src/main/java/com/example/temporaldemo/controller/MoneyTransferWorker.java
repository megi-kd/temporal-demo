package com.example.temporaldemo.controller;

import com.example.temporaldemo.activity.AccountActivityImpl;
import com.example.temporaldemo.util.Constants;
import com.example.temporaldemo.wokrflow.MoneyTransferWorkflowImpl;
import io.temporal.api.workflowservice.v1.WorkflowServiceGrpc.WorkflowServiceStub;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;

public class MoneyTransferWorker {

  public static void main(String[] args) {

    //1 create service stubs
    WorkflowServiceStubs service  = WorkflowServiceStubs.newLocalServiceStubs();

    //2. create client using service
    WorkflowClient client = WorkflowClient.newInstance(service);

    //3. // Worker factory is used to create Workers that poll specific Task Queues.
    WorkerFactory factory = WorkerFactory.newInstance(client);

    //4 create worker
    Worker worker =factory.newWorker(Constants.MONEY_TRANSFER_TASK_QUEUE);
    // This Worker hosts both Workflow and Activity implementations.
    // Workflows are stateful so a type is needed to create instances.
    worker.registerWorkflowImplementationTypes(MoneyTransferWorkflowImpl.class);
    // Activities are stateless and thread safe so a shared instance is used.
    worker.registerActivitiesImplementations(new AccountActivityImpl());

    factory.start();
  }

}
