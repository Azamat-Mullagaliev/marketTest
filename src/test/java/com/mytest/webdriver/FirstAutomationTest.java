package com.mytest.webdriver;

import ru.yandex.qatools.allure.annotations.*;

import org.testng.annotations.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class FirstAutomationTest {

    @BeforeTest
    public void set(){
        System.setProperty("webdriver.chrome.driver", "C:/Program Files/chromedriver/chromedriver.exe");
    }

    @Step("test initialization")
    @Test
    public void chromeTest() throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        IndexPage indexPage = new IndexPage(driver);
        Item results = indexPage.search("LG Q6");

        Thread.sleep(20000);
        driver.quit();
    }
}