package com.mytest.webdriver;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.NoSuchElementException;

public class FirstAutomationTest {

    public void selectPhone(WebDriver driver) throws InterruptedException {
        driver.findElement(By.className("n-product-tabs__item_name_offers")).click();
        driver.findElement(By.linkText("по цене")).click();
        Thread.sleep(2000);

        String phoneShop = driver.findElement(By.className("n-snippet-card__shop")).findElement(By.className("link")).getText();
        String phonePrice = driver.findElement(By.className("snippet-card__price")).getText();

        driver.findElement(By.className("snippet-card__image")).click();

        System.out.println("магазин: " + phoneShop);
        System.out.println("цена: " + phonePrice);
    }

    public void searchFromList(WebDriver driver, ArrayList<WebElement> phones, String phoneName) throws InterruptedException {
        for (WebElement phone : phones)
        {
            //System.out.println("ищем...");
            String anotherPhone = phone.findElement(By.className("link")).getText();
            if (anotherPhone.contains(" " + phoneName + " ")||anotherPhone.contains(phoneName + " ")||anotherPhone.endsWith(phoneName)) { //проверка конкретной модификации
                //System.out.println("нашли!");
                phone.findElement(By.className("link")).click();
                selectPhone(driver);
                break;
            }
        }
    }

    @Test
    public void chromeTest() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:/Users/azama/Downloads/chromedriver/chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("https://market.yandex.ru/");
        String phoneName = "LG Q6+";
        driver.findElement(By.className("input__control")).sendKeys(phoneName);
        driver.findElement(By.className("search2__button")).click();

        try {
            driver.findElement(By.className("n-noresult__desc"));
        } catch (NoSuchElementException e1) {
            try {
                String foundPhone = driver.findElement(By.className("n-title__text")).getText();
                if (foundPhone.contains(" " + phoneName + " ")||foundPhone.contains(phoneName + " ")||foundPhone.endsWith(phoneName)) //проверка конкретной модификации
                    selectPhone(driver);
                else {
                    //System.out.println("ищем телефон");
                    driver.findElement(By.className("n-search-preciser__text")).findElement(By.className("link")).click();
                    Thread.sleep(2000);
                    //driver.findElement(By.linkText("Мобильные телефоны")).click();
                    //System.out.println("уточнили категорию");
                    ArrayList<WebElement> phones = new ArrayList<WebElement>(driver.findElements(By.className("n-snippet-card2__header")));
                    //System.out.println("поиск конкретной модели");
                    searchFromList(driver,phones,phoneName);
 
                }
            } catch (NoSuchElementException e2) {
                ArrayList<WebElement> phones = new ArrayList<WebElement>(driver.findElements(By.className("n-snippet-cell2__header")));
                searchFromList(driver,phones,phoneName);
            }

    }

        Thread.sleep(20000);
        driver.quit();
    }
}