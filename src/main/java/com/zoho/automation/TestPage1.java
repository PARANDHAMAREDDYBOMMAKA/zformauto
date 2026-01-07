package com.zoho.automation;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

public class TestPage1 {
    public static void main(String[] args) {
        WebDriver driver = null;
        try {
            Properties config = new Properties();
            FileInputStream fis = new FileInputStream("config.properties");
            config.load(fis);
            fis.close();

            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--remote-allow-origins=*");

            driver = new ChromeDriver(options);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

            String formUrl = "https://forms.zohopublic.in/Kalvium/form/Signup/formperma/GeJFMLBDfoWlIJfhI46Qyx0Dlf3kHhMSRsvMItq_Riw";
            driver.get(formUrl);
            System.out.println("Form loaded");
            Thread.sleep(3000);

            System.out.println("Filling Page 1...");

            WebElement emailField = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='Email-li']/div/div[2]/div[1]/span/input")));
            emailField.clear();
            emailField.sendKeys(config.getProperty("email"));
            System.out.println("Email filled: " + config.getProperty("email"));

            WebElement dateField = driver.findElement(By.xpath("//*[@id='Date-date']"));
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            String currentDate = sdf.format(new Date());
            dateField.sendKeys(currentDate);
            System.out.println("Date filled: " + currentDate);

            Thread.sleep(1000);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("document.getElementById('ui-datepicker-div').style.display='none';");

            WebElement checkbox = driver.findElement(By.xpath("//*[@id='TermsConditions-input']"));
            js.executeScript("arguments[0].checked = true; arguments[0].dispatchEvent(new Event('change'));", checkbox);
            System.out.println("Checkbox checked");

            System.out.println("\nPage 1 filled successfully!");
            System.out.println("Please verify the form in the browser.");
            System.out.println("Browser will remain open. Press Ctrl+C to stop when done verifying.");

            while (true) {
                Thread.sleep(60000);
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.out.println("Browser will remain open. Press Ctrl+C to stop.");
            try {
                while (true) {
                    Thread.sleep(60000);
                }
            } catch (Exception ignored) {
            }
        }
    }
}
