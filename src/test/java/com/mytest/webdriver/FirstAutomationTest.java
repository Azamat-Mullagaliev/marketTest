package com.mytest.webdriver;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.NoSuchElementException;

public class FirstAutomationTest {

    public void selectPhone(WebDriver driver) throws InterruptedException { //выдача информации по найденному телефону
        driver.findElement(By.className("n-product-tabs__item_name_offers")).click(); //переход к ценам
        driver.findElement(By.linkText("по цене")).click(); //сортировка по цене
        Thread.sleep(2000); //время на прогрузку

        String phoneShop = driver.findElement(By.className("n-snippet-card__shop")).findElement(By.className("link")).getText(); //магазин
        String phonePrice = driver.findElement(By.className("snippet-card__price")).getText();//цена

        driver.findElement(By.className("snippet-card__image")).click();//переход в магазин

        System.out.println("магазин: " + phoneShop);
        System.out.println("цена: " + phonePrice);
    }

    public void searchFromList(WebDriver driver, ArrayList<WebElement> phones, String phoneName) throws InterruptedException { //поиск нужной модели в списке
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
        String phoneName = "LG Q6";
        driver.findElement(By.className("input__control")).sendKeys(phoneName); //ввод названия телефона
        driver.findElement(By.className("search2__button")).click();

        try {
            driver.findElement(By.className("n-noresult__desc")); //для случая, если ничего не найдено
        } catch (NoSuchElementException e1) {
            try {
                String foundPhone = driver.findElement(By.className("n-title__text")).getText(); //для сценария, когда маркет сразу предлагает определенную модель. Для данного случая это Q6+ вместо запрашиваемого Q6
                if (foundPhone.contains(" " + phoneName + " ")||foundPhone.contains(phoneName + " ")||foundPhone.endsWith(phoneName)) //проверка конкретной модификации
                    selectPhone(driver);
                else { // на случай, если маркет предложил неправильно
                    //System.out.println("ищем телефон");
                    driver.findElement(By.className("n-search-preciser__text")).findElement(By.className("link")).click();
                    Thread.sleep(2000);
                    //driver.findElement(By.linkText("Мобильные телефоны")).click();
                    //System.out.println("уточнили категорию");
                    ArrayList<WebElement> phones = new ArrayList<WebElement>(driver.findElements(By.className("n-snippet-card2__header")));
                    //System.out.println("поиск конкретной модели");
                    searchFromList(driver,phones,phoneName);
 
                }
            } catch (NoSuchElementException e2) { //на случай, если маркет выдал список телефонов
                ArrayList<WebElement> phones = new ArrayList<WebElement>(driver.findElements(By.className("n-snippet-cell2__header")));
                searchFromList(driver,phones,phoneName);
            }

    }
        Thread.sleep(20000);
        driver.quit();
    }
}