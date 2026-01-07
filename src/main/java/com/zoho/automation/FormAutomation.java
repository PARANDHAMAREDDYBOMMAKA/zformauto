package com.zoho.automation;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.commons.io.FileUtils;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class FormAutomation {
    private WebDriver driver;
    private WebDriverWait wait;
    private Properties config;
    private boolean testMode;
    private List<File> screenshots;
    private static final String FORM_URL = "https://forms.zohopublic.in/Kalvium/form/Signup/formperma/GeJFMLBDfoWlIJfhI46Qyx0Dlf3kHhMSRsvMItq_Riw";

    public FormAutomation(boolean testMode) {
        this.testMode = testMode;
        this.screenshots = new ArrayList<>();
        loadConfig();
        setupDriver();
    }

    private void loadConfig() {
        config = new Properties();
        try {
            FileInputStream fis = new FileInputStream("config.properties");
            config.load(fis);
            fis.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    private void setupDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        if (!testMode) {
            options.addArguments("--headless");
        }
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public void fillAndSubmitForm() {
        try {
            driver.get(FORM_URL);
            Thread.sleep(3000);

            System.out.println("Filling Page 1...");
            fillPage1();
            screenshots.add(takeScreenshot("page1_filled"));
            clickNextButton1();
            Thread.sleep(2000);

            System.out.println("Filling Page 2...");
            fillPage2();
            screenshots.add(takeScreenshot("page2_filled"));
            clickNextButton2();
            Thread.sleep(2000);

            System.out.println("Filling Page 3...");
            fillPage3();
            screenshots.add(takeScreenshot("page3_filled"));
            clickNextButton3();
            Thread.sleep(2000);

            System.out.println("Filling Page 4...");
            fillPage4();
            screenshots.add(takeScreenshot("page4_filled"));
            clickNextButton4();
            Thread.sleep(2000);

            System.out.println("Reached submit page");
            screenshots.add(takeScreenshot("submit_page"));

            System.out.println("Form filled successfully for: " + config.getProperty("email"));

            sendEmailNotification();

        } catch (Exception e) {
            screenshots.add(takeScreenshot("error"));
            System.err.println("Form submission failed: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Form submission failed", e);
        } finally {
            if (!testMode) {
                driver.quit();
            }
        }
    }

    private void fillPage1() {
        WebElement emailField = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='Email-li']/div/div[2]/div[1]/span/input")));
        emailField.clear();
        emailField.sendKeys(config.getProperty("email"));
        System.out.println("Email filled: " + config.getProperty("email"));

        WebElement dateField = driver.findElement(By.xpath("//*[@id='Date-date']"));
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        String currentDate = sdf.format(new Date());
        dateField.sendKeys(currentDate);
        System.out.println("Date filled: " + currentDate);

        try {
            Thread.sleep(1000);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("document.getElementById('ui-datepicker-div').style.display='none';");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        WebElement checkbox = driver.findElement(By.xpath("//*[@id='TermsConditions-input']"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].checked = true; arguments[0].dispatchEvent(new Event('change'));", checkbox);
        System.out.println("Page 1 completed");
    }

    private void fillPage2() {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='Dropdown-li']/div/div[2]/div[1]/span/span[1]/span")));
        dropdown.click();
        System.out.println("Dropdown opened");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        WebElement dropdownOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[contains(text(), 'Work Integrated - Simulated')]")));
        dropdownOption.click();
        System.out.println("Selected 'Work Integrated - Simulated'");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        WebElement keyTasksTextarea = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='MultiLine-arialabel']")));
        keyTasksTextarea.clear();
        keyTasksTextarea.sendKeys(config.getProperty("key.tasks"));
        System.out.println("Page 2 completed");
    }

    private void fillPage3() {
        WebElement slider5 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='sld-Slider']/a[6]")));
        slider5.click();
        System.out.println("Page 3 completed - Engagement slider set to 5");
    }

    private void fillPage4() {
        WebElement slider2_5 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='sld-Slider2']/a[6]")));
        slider2_5.click();
        System.out.println("Page 4 completed - Productivity slider set to 5");
    }

    private void clickNextButton1() {
        WebElement nextButton = driver.findElement(By.xpath("//*[@id='formBodyDiv']/div[7]/div[1]/div[1]/div/button"));
        nextButton.click();
        System.out.println("Clicked Next from Page 1");
    }

    private void clickNextButton2() {
        WebElement nextButton = driver.findElement(By.xpath("//*[@id='formBodyDiv']/div[7]/div[2]/div[1]/div[2]/button"));
        nextButton.click();
        System.out.println("Clicked Next from Page 2");
    }

    private void clickNextButton3() {
        WebElement nextButton = driver.findElement(By.xpath("//*[@id='formBodyDiv']/div[7]/div[3]/div[1]/div[2]/button"));
        nextButton.click();
        System.out.println("Clicked Next from Page 3");
    }

    private void clickNextButton4() {
        WebElement nextButton = driver.findElement(By.xpath("//*[@id='formBodyDiv']/div[7]/div[4]/div[1]/div[2]/button"));
        nextButton.click();
        System.out.println("Clicked Next from Page 4");
    }

    private void submitForm() {
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit'], button.submitButton")));
        submitButton.click();
        System.out.println("Clicked Submit button");
    }

    private File takeScreenshot(String fileNamePrefix) {
        try {
            File screenshotDir = new File("screenshots");
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String timestamp = sdf.format(new Date());
            String fileName = String.format("screenshots/%s_%s_%s.png",
                fileNamePrefix,
                config.getProperty("email").replace("@", "_at_"),
                timestamp);

            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destination = new File(fileName);
            FileUtils.copyFile(screenshot, destination);

            System.out.println("Screenshot saved: " + fileName);
            return destination;
        } catch (IOException e) {
            System.err.println("Failed to take screenshot: " + e.getMessage());
            return null;
        }
    }

    private void sendEmailNotification() {
        try {
            EmailService emailService = new EmailService(config);
            emailService.sendScreenshots(screenshots, config.getProperty("email"));
        } catch (Exception e) {
            System.err.println("Failed to send email notification: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
