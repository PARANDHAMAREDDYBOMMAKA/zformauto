package com.zoho.automation;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class FormSubmissionJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Starting scheduled form submission at: " + new java.util.Date());
        try {
            FormAutomation automation = new FormAutomation(false);
            automation.fillAndSubmitForm();
        } catch (Exception e) {
            System.err.println("Failed to submit form: " + e.getMessage());
            e.printStackTrace();
            throw new JobExecutionException(e);
        }
    }
}
