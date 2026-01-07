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

public class TestPage3 {
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
            System.out.println("Page 1 completed");

            WebElement nextButton1 = driver.findElement(By.xpath("//*[@id='formBodyDiv']/div[7]/div[1]/div[1]/div/button"));
            nextButton1.click();
            System.out.println("Clicked Next button from Page 1");
            Thread.sleep(3000);

            System.out.println("\nFilling Page 2...");

            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='Dropdown-li']/div/div[2]/div[1]/span/span[1]/span")));
            dropdown.click();
            System.out.println("Dropdown opened");
            Thread.sleep(2000);

            WebElement dropdownOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[contains(text(), 'Work Integrated - Simulated')]")));
            dropdownOption.click();
            System.out.println("Selected 'Work Integrated - Simulated'");
            Thread.sleep(2000);

            WebElement keyTasksTextarea = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='MultiLine-arialabel']")));
            keyTasksTextarea.clear();
            keyTasksTextarea.sendKeys("need to complete the tasks scheduled today");
            System.out.println("Key tasks filled");
            System.out.println("Page 2 completed");

            WebElement nextButton2 = driver.findElement(By.xpath("//*[@id='formBodyDiv']/div[7]/div[2]/div[1]/div[2]/button"));
            nextButton2.click();
            System.out.println("Clicked Next button from Page 2");
            Thread.sleep(3000);

            System.out.println("\nFilling Page 3...");

            WebElement slider5 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='sld-Slider']/a[6]")));
            slider5.click();
            System.out.println("Slider set to 5 (high)");

            System.out.println("\nPage 3 filled successfully!");
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
