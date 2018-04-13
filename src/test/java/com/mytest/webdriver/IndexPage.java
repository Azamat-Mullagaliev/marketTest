package com.mytest.webdriver;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.NoSuchElementException;

public class IndexPage {

    public String phoneName;
    public WebDriver driver;

    IndexPage(WebDriver your_driver){
        driver = your_driver;
    }

    public IndexPage sendKeys(){
        driver.get("https://market.yandex.ru/");

        driver.findElement(By.className("input__control")).sendKeys(phoneName);
        driver.findElement(By.className("search2__button")).click();

        return this;
    }

    public IndexPage sortByPrice() throws InterruptedException {
        driver.findElement(By.className("n-product-tabs__item_name_offers")).click();
        driver.findElement(By.linkText("по цене")).click();
        Thread.sleep(2000);

        return this;
    }

    public Item getInfo(){
        Item item = new Item();

        item.phoneName = phoneName;
        item.shop = driver.findElement(By.className("n-snippet-card__shop")).findElement(By.className("link")).getText();
        item.price = driver.findElement(By.className("snippet-card__price")).getText();

        driver.findElement(By.className("snippet-card__image")).click();

        item.getInfo();

        return item;
    }

    public Item selectPhone() throws InterruptedException {
        sortByPrice();
        return getInfo();
    }

    public Item searchFromList(ArrayList<WebElement> phones) throws InterruptedException {
        for (WebElement phone : phones)
        {
            String anotherPhone = phone.findElement(By.className("link")).getText();
            if (anotherPhone.contains(" " + phoneName + " ")||anotherPhone.contains(phoneName + " ")||anotherPhone.endsWith(phoneName)) {
                phone.findElement(By.className("link")).click();
                return selectPhone();
            }
        }
        return null;
    }

    private Boolean noResult(){
        try {
            driver.findElement(By.className("n-noresult__desc"));
            return true;
        } catch (NoSuchElementException e1) {
            return false;
        }
    }

    public Item search(String your_phoneName) throws InterruptedException {

        phoneName = your_phoneName;
        sendKeys();

        if (noResult()) return null;

        try {
            String foundPhone = driver.findElement(By.className("n-title__text")).getText();
            if (foundPhone.contains(" " + phoneName + " ") || foundPhone.contains(phoneName + " ") || foundPhone.endsWith(phoneName))
                return selectPhone();
            else {
                driver.findElement(By.className("n-search-preciser__text")).findElement(By.className("link")).click();
                Thread.sleep(2000);

                ArrayList<WebElement> phones = new ArrayList<WebElement>(driver.findElements(By.className("n-snippet-card2__header")));

                return searchFromList(phones);
            }
        } catch (NoSuchElementException e2) {
            ArrayList<WebElement> phones = new ArrayList<WebElement>(driver.findElements(By.className("n-snippet-cell2__header")));
            return searchFromList(phones);
        }
    }
}
