package com.zoho.automation;

public class TestFullAutomation {
    public static void main(String[] args) {
        System.out.println("Starting Zoho Form Automation Test...");
        FormAutomation automation = new FormAutomation(false);
        automation.fillAndSubmitForm();
        System.out.println("Test completed!");
    }
}
