package com.example.temporaldemo.wokrflow;

import com.example.temporaldemo.activity.AccountActivity;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

//The Workflow function is the application entry point.
public class MoneyTransferWorkflowImpl implements MoneyTransferWorkflow {

  public static final String WITHDRAW = "Withdraw";

  // RetryOptions specify how to automatically handle retries
  // when Activities fail.
  private final RetryOptions retryOptions = RetryOptions.newBuilder()
      .setInitialInterval(Duration.ofSeconds(1))
      .setMaximumInterval(Duration.ofSeconds(100))
      .setBackoffCoefficient(2)
      .setMaximumAttempts(500)
      .build();

  private final ActivityOptions defaultActivityOptions = ActivityOptions.newBuilder()
      //this timeout specify: when to automatically timeout Activities
      // if the process it taking too long
      .setStartToCloseTimeout(Duration.ofSeconds(5))
      //this is optional if we want to provide customer retryOptions
      // temporal retries failures by default
      .setRetryOptions(retryOptions)
      .build();

  // TODO: (check what is this)
  //  ActivityStubs enable calls to methods as if the Activity object is local,
  //  but actually perform an RPC.
  private final Map<String, ActivityOptions> perActivityMethodOptions =
      new HashMap<String, ActivityOptions>() {{
        put(WITHDRAW, ActivityOptions
            .newBuilder()
            .setHeartbeatTimeout(Duration.ofSeconds(5))
            .build());
  }};

  private final AccountActivity accountActivity =
      Workflow.newActivityStub(
          AccountActivity.class,
          defaultActivityOptions,
          perActivityMethodOptions);

  // The transfer method is the entry point of the Workflow
  // Activity method execution can be orchestrated here or from within other Activity methods
  @Override
  public void transfer(String fromAccountId, String toAccountId, String referenceId, double amount) {
    accountActivity.withdraw(fromAccountId, referenceId, amount);
    accountActivity.deposit(toAccountId, referenceId, amount);
  }
}
