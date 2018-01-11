package com.wesoft;

import java.io.PrintWriter;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

import com.wesoft.commom.RequestGenerater;
import com.wesoft.commom.MyListener;
import com.wesoft.commom.Report;

public class JavaDemo {

	public static void main(String[] args) {


        //Create a Junit5 launcher
        Launcher launcher = LauncherFactory.create();

        // Register a result listener of your choice
        //In fact, it can register more than one listener if needed
        //Listener provided by Junit, it can show some Summary after all the tests are done
        SummaryGeneratingListener listener = new SummaryGeneratingListener(); 
        //Listener I extends the TestExecutionListerner interface, to send out some info during the testing
        TestExecutionListener mylistener = new MyListener();
        launcher.registerTestExecutionListeners(listener,mylistener);

        
        //Define a finder to find which packages/classes/methods should be launch
        LauncherDiscoveryRequest request=null;        
        try {
			request = RequestGenerater.generateRequest();
		} catch (Throwable e) {
			e.printStackTrace();
		}
               
        
        //To create a test plan by using the found test cases, not a must step in the runner
        TestPlan testPlan = launcher.discover(request);

        //Create folder and copy test data excel to place test cases results
        try {
        	Report.createReportFolderAndFile();
        }catch (Throwable e) {
        	e.printStackTrace();
        }
        
        //Execute the found Junit 5 test cases
        launcher.execute(request);

        //Get the summary report
        listener.getSummary().printTo(new PrintWriter(System.out));

	}

}
