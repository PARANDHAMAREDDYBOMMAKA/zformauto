package com.zoho.automation;

public class TestVisible {
    public static void main(String[] args) {
        System.out.println("Starting Zoho Form Automation (Visible Browser)...");
        FormAutomation automation = new FormAutomation(true);
        automation.fillAndSubmitForm();
        System.out.println("Test completed!");
    }
}
