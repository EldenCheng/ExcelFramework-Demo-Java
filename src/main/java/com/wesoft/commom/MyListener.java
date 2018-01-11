package com.wesoft.commom;

import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.engine.TestExecutionResult;

public class MyListener extends Object implements TestExecutionListener {
    public void testPlanExecutionStarted(TestPlan testPlan){
        //System.out.println("The Test Plan execution started");
    }

    public void testPlanExecutionFinished(TestPlan testPlan){
        //System.out.println("The Test Plan execution finished");
    }

    public void dynamicTestRegistered(TestIdentifier testIdentifier){
        //System.out.println("The Test dynamic test registered.");
        //System.out.println("The dynamic test to be added now is: " + testIdentifier);
    }

    public void executionSkipped(TestIdentifier testIdentifier,String reason){
        //System.out.println("The Test skipped");
    }

    public void executionStarted(TestIdentifier testIdentifier){
        if(testIdentifier.getType().toString() != "CONTAINER") {
            //System.out.println("The Test execution started.");
        	//System.out.println("The test is now started: " + testIdentifier.getUniqueId());
            System.out.println("The test is now started: " + testIdentifier.getDisplayName());
        }
    }

    public void executionFinished(TestIdentifier testIdentifier,TestExecutionResult testExecutionResult){
        
    	if(testIdentifier.getType().toString() != "CONTAINER") {
            //System.out.println("The Test execution finished.");
            System.out.println("The test is now finished : " + testIdentifier.getDisplayName());
            String result = testExecutionResult.getStatus().toString();
            System.out.println("The result of this test case is: " + result);
            if (result == "FAILED") {
                System.out.println("The exception of the test is: " + testExecutionResult.getThrowable().get().getMessage());
            }
        }
        
    }
}
