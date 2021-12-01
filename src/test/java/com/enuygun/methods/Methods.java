package com.enuygun.methods;

import com.enuygun.base.Driver;
import com.enuygun.helper.ElementHelper;
import com.enuygun.helper.StoreHelper;
import com.enuygun.model.ElementInfo;
import groovy.util.logging.Slf4j;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.Log4jLoggerAdapter;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

@Slf4j
public class Methods extends Driver {

    private static Log4jLoggerAdapter logger = (Log4jLoggerAdapter) LoggerFactory
            .getLogger(Methods.class);

    private static String SAVED_ATTRIBUTE;
    WebDriver driver;
    FluentWait<WebDriver> wait;
    JsMethods jsMethods;
    MethodsUtil methodsUtil;
    ActionMethods actionMethods;
    long waitElementTimeout;
    long pollingEveryValue;

    public Methods(){

        this.driver = Driver.driver;
        setWaitElementTimeout();
        setPollingEveryValue();
        wait = setFluentWait(waitElementTimeout);
        jsMethods = new JsMethods(driver);
        actionMethods = new ActionMethods(driver);
        methodsUtil = new MethodsUtil();
    }

    private void setWaitElementTimeout(){

        waitElementTimeout = Driver.isTestinium ? Long.parseLong(Driver.ConfigurationProp
                .getString("testiniumWaitElementTimeout")) : Long.parseLong(Driver.ConfigurationProp
                .getString("localWaitElementTimeout"));
    }

    private void setPollingEveryValue(){

        pollingEveryValue = Driver.isTestinium ? Long.parseLong(Driver.ConfigurationProp
                .getString("testiniumPollingEveryMilliSecond")) : Long.parseLong(Driver.ConfigurationProp
                .getString("localPollingEveryMilliSecond"));
    }

    public FluentWait<WebDriver> setFluentWait(long timeout){

        FluentWait<WebDriver> fluentWait = new FluentWait<WebDriver>(driver);
        fluentWait.withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofMillis(pollingEveryValue))
                .ignoring(NoSuchElementException.class);
        return fluentWait;
    }

    public ElementInfo getElementInfo(String key) {

        return StoreHelper.INSTANCE.findElementInfoByKey(key);
    }

    public By getBy(String key) {

        logger.info("Element " + key + " değerinde tutuluyor");
        return ElementHelper.getElementInfoToBy(getElementInfo(key));
    }

    public List<String> getByValueAndSelectorType(By by){

        List<String> list = new ArrayList<String>();
        String[] values = by.toString().split(": ",2);
        list.add(values[1].trim());
        list.add(getSelectorTypeName(values[0].replace("By.","").trim()));
        return list;
    }

    public WebElement findElement(String key){
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        By infoParam = ElementHelper.getElementInfoToBy(elementInfo);
        WebDriverWait webDriverWait = new WebDriverWait(driver, 5);
        WebElement webElement = webDriverWait
                .until(ExpectedConditions.presenceOfElementLocated(infoParam));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
                webElement);
        return webElement;
    }

    public List<WebElement> findElements(String key){
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        By infoParam = ElementHelper.getElementInfoToBy(elementInfo);
        return driver.findElements(infoParam);
    }


    public void clickElement(String key) {

        findElement(key).click();
        logger.info("Elemente tıklandı.");
    }

    public void clickElement2(String key) {

        boolean advantage=false;
        findElement(key).click();
        advantage=true;

        if(advantage){
            //logger.info("Avantaj pakete yükseltildi.");
        }
    }

    public void acceptAlertMessage() {
        WebDriverWait wait = new WebDriverWait(driver,30);

      wait.until(ExpectedConditions.alertIsPresent());
      driver.switchTo().alert().accept();
    }

    public void waitClickElement(String key) {

        WebDriverWait wait = new WebDriverWait(driver,30);

        wait.until(ExpectedConditions.elementToBeClickable(getBy(key)));
        logger.info("Element etkin");
    }

    public void waitElementIsVisible(String key) {
        WebDriverWait wait = new WebDriverWait(driver,30);

        wait.until(ExpectedConditions.visibilityOfElementLocated(getBy(key)));
        logger.info("Element görünür");
    }

    public void getPageSources() {
        String pageSource = driver.getPageSource();
        logger.info("PAGE SOURCE : " + pageSource);
    }

    public void clearElement(String key) {

        findElement(key).clear();
    }

    public void sendKeys(String key, String text) {

        findElement(key).sendKeys(text);
        logger.info("Elemente " + text + " texti yazıldı.");
    }

    public String getText(String key) {

        return findElement(key).getText();
    }

    public String getAttribute(String key, String attribute) {

        return findElement(key).getAttribute(attribute);
    }

    public String getPageSource() {

        return driver.getPageSource();
    }

    public String getCurrentUrl() {

        return driver.getCurrentUrl();
    }

    public List<String> listTabs() {
        List<String> list = new ArrayList<String>();
        for (String window : driver.getWindowHandles()) {
            list.add(window);
        }
        return list;
    }

    public void switchTab(int tabNumber) {

        logger.info(listTabs().get(tabNumber));
        driver.switchTo().window(listTabs().get(tabNumber));
        logger.info(driver.getCurrentUrl());
    }

    public void switchFrame(int frameNumber) {

        driver.switchTo().frame(frameNumber - 1);
    }

    public void switchFrame(String frameName) {

        driver.switchTo().frame(frameName);

    }

    public void switchFrameWithKey(String key) {

        WebElement webElement = findElement(key);
        driver.switchTo().frame(webElement);
    }

    public void switchDefaultContent() {

        driver.switchTo().defaultContent();
    }

    public void navigateTo(String url) {

        driver.navigate().to(url);
    }

    public void scrollToElement(String key) {

        Actions actions = new Actions(driver);
        WebElement webElement = findElement(key);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", webElement);
        actions.moveToElement(webElement).build().perform();
    }

    public void scrollElementCenterWithJs(String key) {

        WebElement webElement = findElement(key);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
                webElement);
    }

    public void focusToElement(String key) {

        WebElement webElement = findElement(key);
        JavascriptExecutor jse = ((JavascriptExecutor) driver);
        jse.executeScript("arguments[0].scrollIntoView();", webElement);
        jse.executeScript("arguments[0].focus();", webElement);
    }

    public void focusToElementAndClick(String key) {

        WebElement webElement = findElement(key);
        JavascriptExecutor jse = ((JavascriptExecutor) driver);
        jse.executeScript("arguments[0].scrollIntoView();", webElement);
        jse.executeScript("arguments[0].focus();", webElement);
        jse.executeScript("var evt = document.createEvent('MouseEvents');"
                + "evt.initMouseEvent('click',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);"
                + "arguments[0].dispatchEvent(evt);", webElement);
    }

    public void jsExecutor(String script, Object... args) {

        JavascriptExecutor jse = ((JavascriptExecutor) driver);
        jse.executeScript(script, args);
    }

    public void jsExecutorWithKey(String script, String key) {

        jsExecutor(script, findElement(key));
    }

    public void waitByMilliSeconds(long milliSeconds) {

        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void waitBySeconds(long seconds) {

        logger.info(seconds + " saniye bekleniyor...");
        waitByMilliSeconds(seconds * 1000);
    }

    public boolean isElementVisible(String key) {
        WebDriverWait wait = new WebDriverWait(driver,20);

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(getBy(key)));
            logger.info(key+" elementi görünür durumdadır.");
            return true;
        } catch (Exception e) {
            logger.info("Element görünür değil");
            return false;
        }
    }

    public boolean isElementVisibleCustomTime(String key, int time){

        WebDriverWait wait = new WebDriverWait(driver,time);

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(getBy(key)));
            logger.info(key+" elementi görünür durumdadır.");
            return true;
        } catch (Exception e) {
            logger.info("Element görünür değil");
            return false;
        }
    }

    public boolean isElementInVisible(String key) {
        WebDriverWait wait = new WebDriverWait(driver,30);

        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(getBy(key)));
            return true;
        } catch (Exception e) {
            logger.info("Element görünüyor");
            return false;
        }
    }

    public boolean isElementPresent(String key){
        WebDriverWait wait = new WebDriverWait(driver,30);

        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(getBy(key)));
            return true;
        } catch (Exception e) {
            logger.info("Element sayfada mevcut");
            return false;
        }
    }

    public boolean isElementClickable(String key) {
        WebDriverWait wait = new WebDriverWait(driver,20);

        try {
            wait.until(ExpectedConditions.elementToBeClickable(getBy(key)));
            return true;
        } catch (Exception e) {
            logger.info("Element görünür değil");
            return false;
        }

    }
    public boolean isElementClickable(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver,30);

        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            return true;
        } catch (Exception e) {
            logger.info("Element görünür değil");
            return false;
        }

    }

    public WebElement findElement(By by){

        logger.info("Element " + by.toString() + " by değerine sahip");
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public WebElement findElementWithoutWait(By by){

        logger.info("Element " + by.toString() + " by değerine sahip");
        return driver.findElement(by);
    }

    public WebElement findElementForJs(By by, String type){

        WebElement webElement = null;
        switch (type){
            case "1":
                webElement = findElement(by);
                break;
            case "2":
                webElement = findElementWithoutWait(by);
                break;
            case "3":
                List<String> byValueList = getByValueAndSelectorType(by);
                webElement = jsMethods.findElement(byValueList.get(0),byValueList.get(1));
                break;
            default:
                Assert.fail("type hatalı");
        }
        return webElement;
    }

    public List<WebElement> findElements(By by){

        logger.info("Element " + by.toString() + " by değerine sahip");
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
    }

    public List<WebElement> findElementsWithOutError(By by){

        logger.info("Element " + by.toString() + " by değerine sahip");
        List<WebElement> list = new ArrayList<>();
        try {
            list.addAll(wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by)));
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public List<WebElement> findElementsWithoutWait(By by){

        logger.info("Element " + by.toString() + " by değerine sahip");
        return driver.findElements(by);
    }

    public List<WebElement> findElementsForJs(By by, String type){

        List<WebElement> webElementList = null;
        switch (type){
            case "1":
                webElementList = findElements(by);
                break;
            case "2":
                webElementList = findElementsWithoutWait(by);
                break;
            case "3":
                List<String> byValueList = getByValueAndSelectorType(by);
                webElementList = jsMethods.findElements(byValueList.get(0),byValueList.get(1));
                break;
            default:
                Assert.fail("type hatalı");
        }
        return webElementList;
    }

    private String getSelectorTypeName(String type){

        String selectorType = "";
        switch (type) {

            case "id":
                selectorType = "id";
                break;

            case "name":
                selectorType = "name";
                break;

            case "className":
                selectorType = "class";
                break;

            case "cssSelector":
                selectorType = "css";
                break;

            case "xpath":
                selectorType = "xpath";
                break;

            default:
                Assert.fail("HATA");
                break;
        }
        return selectorType;
    }

    public void hoverElementAction(By by) {

        WebElement webElement = findElementForJs(by,"1");
        jsMethods.scrollElement(webElement);
        actionMethods.hoverElement(webElement);
    }

    public void moveAndClickElement(By by) {

        WebElement webElement = findElementForJs(by,"1");
        jsMethods.scrollElement(webElement);
        actionMethods.moveAndClickElement(webElement);
    }

    public void clickElementWithAction(By by){

        WebElement webElement = findElementForJs(by,"1");
        jsMethods.scrollElement(webElement);
        actionMethods.clickElement(webElement);
    }

    public void doubleClickElementWithAction(By by){

        WebElement webElement = findElementForJs(by,"1");
        jsMethods.scrollElement(webElement);
        actionMethods.doubleClickElement(webElement);
    }

    public void selectAction(By by, int optionIndex){

        WebElement webElement = findElementForJs(by,"1");
        jsMethods.scrollElement(webElement);
        actionMethods.select(webElement, optionIndex);
    }

    // 1 loop 400 ms
    public boolean isImageLoadingJs(By by, int loopCount){

        boolean isImageLoading = false;
        try {
            isImageLoading = jsMethods.jsImageLoading(findElementForJs(by,"1"), loopCount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return isImageLoading;
    }

    public void waitPageLoadCompleteJs() {

        jsMethods.waitPageLoadComplete();
    }

    public void waitForAngularLoadJs() {

        jsMethods.waitForAngularLoad();
    }

    public void waitJQueryCompleteJs() {

        jsMethods.waitJQueryComplete();
    }

    public void waitPageScrollingCompleteJs() {

        jsMethods.waitPageScrollingComplete();
    }

    public void stopPageLoadJs() {

        jsMethods.stopPageLoad();
    }

    public void focusToElementJs(By by){

        WebElement webElement = findElementForJs(by,"3");
        waitByMilliSeconds(1000);
        jsMethods.scrollElement(webElement);
        waitByMilliSeconds(100);
        jsMethods.scrollElement(webElement);
        waitByMilliSeconds(1000);
    }

    public void sendKeysJs(By by, String text, String type){

        jsMethods.sendKeys(findElementForJs(by,type), text);
        logger.info("Elemente " + text + " texti yazıldı.");
    }

    public String getText(By by){

        return findElement(by).getText();
    }

    public String getTextContentJs(By by, String type){

        return jsMethods.getText(findElementForJs(by,type),"textContent");
    }

    public String getInnerTextJs(By by, String type){

        return jsMethods.getText(findElementForJs(by,type),"innerText");
    }

    public String getOuterTextJs(By by, String type){

        return jsMethods.getText(findElementForJs(by,type),"outerText");
    }

    public void mouseOverJs(By by, String type){

        jsMethods.mouseOver(findElementForJs(by,type));
        logger.info("mouseover " + by);
    }

    public void mouseOutJs(By by, String type){

        jsMethods.mouseOut(findElementForJs(by,type));
        logger.info("mouseout " + by);
    }

    public String getAttribute(By by, String attribute){

        return findElement(by).getAttribute(attribute);
    }

    public String getAttributeJs(By by, String attribute, String type){

        return jsMethods.getAttribute(findElementForJs(by,type), attribute);
    }

    public String getValueJs(By by, String type){

        return jsMethods.getValue(findElementForJs(by,type));
    }

    public String getCssValue(By by, String attribute){

        return findElement(by).getCssValue(attribute);
    }

    public String getHexCssValue(By by, String attribute){

        return Color.fromString(getCssValue(by, attribute)).asHex();
    }

    public String getCssValueJs(By by, String attribute, String type){

        return jsMethods.getCssValue(findElementForJs(by,type), attribute);
    }

    public String getHexCssValueJs(By by, String attribute, String type){

        return Color.fromString(getCssValueJs(by, attribute, type)).asHex();
    }

    public void clickElementJs(By by){

        jsMethods.clickByElement(findElementForJs(by,"3"));
    }

    public void clickElementJs(By by, boolean notClickByCoordinate){

        jsMethods.clickByElement(findElementForJs(by,"3"), notClickByCoordinate);
    }

    public void clickByCoordinateJs(int x, int y){

        jsMethods.clickByCoordinate(x, y);
    }

    public void clickByWebElementCoordinate(By by){

        jsMethods.clickByWebElementCoordinate(findElementForJs(by,"3"));
    }

    public void clickByWebElementCoordinate(By by, int x, int y){

        jsMethods.clickByWebElementCoordinate(findElementForJs(by,"3"), x, y);
    }

    public void focusAndClickElementJs(By by){

        WebElement webElement = findElement(by);
        jsMethods.scrollElement(webElement);
        waitByMilliSeconds(100);
        jsMethods.focusElement(webElement);
        waitByMilliSeconds(100);
        jsMethods.scrollElementCenter(webElement);
        waitByMilliSeconds(1000);
        jsMethods.clickMouseEvent(webElement);
        waitByMilliSeconds(100);
    }

    public void closeAddsTab(int tabNumber, String urls, String bannedUrls) {

        List<String> windowList = new ArrayList<String>();
        for (String winHandle : driver.getWindowHandles()) {
            windowList.add(winHandle.trim());
        }
        logger.info(windowList.toString());
        for (int i = 0; i < windowList.size(); i++) {
            // Switch to new window opened
            driver.switchTo().window(windowList.get(i));
            waitBySeconds(2);
            String newUrl = driver.getCurrentUrl();
            logger.info(newUrl);
            if(!containsControlUrl(newUrl,urls) || containsControlUrl(newUrl,bannedUrls)) {
                waitBySeconds(1);
                // Perform the actions on new window
                //this will close new opened window
                driver.close();
                waitBySeconds(1);
            }
        }
        windowList = new ArrayList<String>();
        for (String winHandle : driver.getWindowHandles()) {
            windowList.add(winHandle.trim());
        }

        driver.switchTo().window(windowList.get(tabNumber));
    }

    public boolean containsControlUrl(String currentUrl, String urls){

        String[] urlArray = urls.split(",");
        boolean result=false;
        for(int i = 0; i < urlArray.length; i++){
            if(!urlArray[i].equals("")) {
                result = currentUrl.contains(urlArray[i]);
            }
            if(result){
                break;
            }
        }
        return result;
    }
    private void hoverElement(WebElement element){
        Actions actions= new Actions(driver);
        actions.moveToElement(element).build().perform();
    }

    public void hoverElement(String key) {
        WebElement element = findElement(key);
        Actions hoverAction = new Actions(driver);
        hoverAction.moveToElement(element).perform();
    }

    Double price1 = 0.0;
    Double price2 = 0.0;

    public void comparePrices(String key1, String key2) {
        WebElement element1 = findElement(key1);
        price1 = Double.valueOf(element1.getText().split(" ")[0].trim());
        System.out.println(price1);

        WebElement element2 = findElement(key2);
        price2 = Double.valueOf(element2.getText().split(" ")[0].trim());
        System.out.println(price2);

        Assert.assertEquals(price1, price2);
    }

    public void radioButtonIsSelected(String key) {
        WebElement element = findElement(key);
        if (element.isSelected()) {
            logger.info("Radio button dogru secilmistir");
        } else {
            Assert.fail("Radio button dogru sekilde secilmememistir");
        }
    }

    public void checkBoxIsSelected(String key) {
        WebElement element = findElement(key);
        if (element.isSelected()) {
            logger.info("Check box secilmistir");
        } else {
            Assert.fail("Check box secilmememistir");
        }
    }
    String price = null;

    public String getPrices(String key){
        WebElement element = findElement(key);
        price = element.getText();
        return price;
    }

    public void afterPrice(String key){
        WebElement element = findElement(key);
         String afterPrice = element.getText();
        Assert.assertEquals(price, afterPrice);
    }

    public void popupHunter(String popup, String popupLink){
        WebElement element1 = findElement(popup);
        WebElement element2 = findElement(popupLink);
        try {
            element1.isDisplayed();
            element2.click();
        }catch(NoSuchElementException e){
            throw new NoSuchElementException("Avantajı kacirma popup cikmadi");

        }
    }

    public int[] getTodayDateWithPlus(int plusDay, int plusMonth, int plusYear){
        LocalDate calculatedDate = LocalDate.now().plusDays(plusDay).plusMonths(plusMonth)
                .plusYears(plusYear);
        int day = calculatedDate.getDayOfMonth();
        int month = calculatedDate.getMonthValue();
        int year = calculatedDate.getYear();
        return new int[]{day, month, year};
    }

    public WebElement waitForElementVisibility(String key, int maxWaitTime){
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        By by = ElementHelper.getElementInfoToBy(elementInfo);

        WebDriverWait wait = new WebDriverWait(driver, maxWaitTime);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }



    public boolean doesUrl(String url, int count, String condition){

        int againCount = 0;
        boolean isUrl = false;
        String takenUrl = "";
        logger.info("Beklenen url: " + url);
        while (!isUrl) {
            waitByMilliSeconds(400);
            if (againCount == count) {
                logger.info("Alınan url: " + takenUrl);
                return false;
            }
            takenUrl = driver.getCurrentUrl();
            if (takenUrl != null) {
                isUrl = conditionValueControl(url,takenUrl,condition);
            }
            againCount++;
        }
        logger.info("Alınan url: " + takenUrl);
        return true;
    }
    public boolean conditionValueControl(String expectedValue, String actualValue,String condition){

        boolean result = false;
        switch (condition){
            case "equal":
                result = actualValue.equals(expectedValue);
                break;
            case "contain":
                result = actualValue.contains(expectedValue);
                break;
            case "startWith":
                result = actualValue.startsWith(expectedValue);
                break;
            case "endWith":
                result = actualValue.endsWith(expectedValue);
                break;
            default:
                Assert.fail("hatali durum: " + condition);
        }
        return result;
    }
    public String randomNumberGenerator(){
        Random rnd = new Random();
        int passportNumber = rnd.nextInt(100 )+10471077;
        return "U" + passportNumber;
    }
    public void navigateToBack(){
        driver.navigate().back();
    }
    public void waitUntilPresenceOfElement(String key){
        WebDriverWait wait = new WebDriverWait(driver,30);

        logger.info(key+" elementinin sayfada mevcut olması beklendi.");
        try {
            findElement(key);
            //wait.until(ExpectedConditions.presenceOfElementLocated(ElementHelper.getElementInfoToBy(StoreHelper.INSTANCE.findElementInfoByKey(key))));

        }
        catch(WebDriverException ex)
        {
            wait.until(ExpectedConditions.presenceOfElementLocated(ElementHelper.getElementInfoToBy(StoreHelper.INSTANCE.findElementInfoByKey(key))));
        }

    }

    public void waitUntilElementToBeClickableAndClick(String key){
        WebDriverWait wait = new WebDriverWait(driver,30);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(ElementHelper.getElementInfoToBy(StoreHelper.INSTANCE.findElementInfoByKey(key)))).click();

        }
        catch(ElementClickInterceptedException ex)
        {
            wait.until(ExpectedConditions.elementToBeClickable(ElementHelper.getElementInfoToBy(StoreHelper.INSTANCE.findElementInfoByKey(key)))).click();

        }
        //wait.until(ExpectedConditions.elementToBeClickable(ElementHelper.getElementInfoToBy(StoreHelper.INSTANCE.findElementInfoByKey(key)))).click();
        logger.info(key+" elementinin sayfada tıklanabilir olması beklendi ve tıklandı.");

    }
    public boolean waitUntilIsElementVisible(String key){
        try
        {
            WebDriverWait wait = new WebDriverWait(driver,1);
            wait.until(ExpectedConditions.invisibilityOfElementLocated(ElementHelper.getElementInfoToBy(StoreHelper.INSTANCE.findElementInfoByKey(key))));

            return false;
        }
        catch (Exception e)
        {
            logger.info(key+" Elementi görünür");
            //driver.TestMap.put("isElementInVisibleError", methodsUtil.getStackTraceLog(e));
            return true;
        }
    }

    public void waitUntilElementHoverClick(String key){
        WebDriverWait wait = new WebDriverWait(driver,10);
        WebElement element = findElement(key);
        //actions.moveToElement(element).build().perform();
        wait.until(ExpectedConditions.elementToBeClickable(ElementHelper.getElementInfoToBy(StoreHelper.INSTANCE.findElementInfoByKey(key)))).click();
        logger.info(key+" elementinin sayfada tıklanabilir olması beklendi üstüne gitti ve tıklandı.");

    }

    public void waitScrollOnElement(String key){
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement element = findElement(key);
        //wait.until(ExpectedConditions.elementToBeClickable((WebElement) StoreHelper.INSTANCE.findElementInfoByKey(key)));

        String scrollElementIntoMiddle = "var viewPortHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);"
                + "var elementTop = arguments[0].getBoundingClientRect().top;"
                + "window.scrollBy(0, elementTop-(viewPortHeight/2));";

        ((JavascriptExecutor) driver).executeScript(scrollElementIntoMiddle, element);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //element.click();
        logger.info(element+ "elementine scroll edildi");
    }
    public WebElement waitForElementVisibility(WebElement webElement, int maxWaitTime){
        WebDriverWait wait = new WebDriverWait(driver, maxWaitTime);
        return wait.until(ExpectedConditions.visibilityOf(webElement));
    }
    public void clickElementByKeyWithHover(String key){
        WebElement element = findElement(key);
        hoverElement(element);
        waitBySeconds(2);
        clickElementByKeyWithHover(element);
        logger.info(key + " elementine tıklandı.");
    }
    private void clickElementByKeyWithHover(WebElement element){
        element.click();
    }
    public String getElementText(String key){
        return findElement(key).getText();
    }
    public void javascriptExecutor(String script){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(script);
    }

    public void javascriptExecutor(String script, WebElement webElement){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(script, webElement);
    }

    public WebElement returnFirstElementOfList(String key){
        return findElements(key).get(0);
    }
    public WebElement returnReissueXDay(int X){
        return findElements("CheckinDays").get(X);
    }
    public WebElement returnDepartureDay(int X){
        return findElements("btn_Gidis_Gun").get(X);
    }
    public WebElement returnArrivalDay(int X){
        return findElements("btn_Donus_Gun").get(X);
    }
    public WebElement findFirstClickableElementOfList(String key){
        WebElement webElement = null;
        List<WebElement> webElementList = findElements(key);
        for (WebElement element : webElementList) {
            if (isElementClickable(element)) {

                webElement = element;

            }
            break;
        }
        return webElement;
    }

    public double removePriceSuffixAndConvertDouble(String priceText) {
        String[] elementStringList =findElement(priceText).getText().trim().split(" ");
        String elementString = elementStringList[0].replaceAll(",", "");
        return Double.parseDouble(elementString);
    }


    public double removeAlternatifOdemePriceSuffixAndConvertDouble(String priceText) {
        String elementStringList =findElement(priceText).getText();
        System.out.println(elementStringList);
        //String elementString = elementStringList[0].replaceAll(",", "");
        String priceAlternatifOdeme=elementStringList.substring(0,3);
        System.out.println(priceAlternatifOdeme);
        return Double.parseDouble(priceAlternatifOdeme);

    }



    public double removeCurrencyAndCovertDouble(String key){
        String text = findElement(key).getText().replace(" TL","").replace(" EUR","").replace(" USD","").replace(",","");
        return Double.parseDouble(text);
    }


    public void dismissAlert(){

        driver.switchTo().alert().dismiss();
    }

    public String randomTCNo() {
        Vector<Integer> array = new Vector<Integer>();
        Random randomGenerator = new Random();

        array.add(new Integer(1 + randomGenerator.nextInt(9)));
        for (int i = 1; i < 9; i++) array.add(randomGenerator.nextInt(10));
        int t1 = 0;
        for (int i = 0; i < 9; i += 2) t1 += array.elementAt(i);
        int t2 = 0;
        for (int i = 1; i < 8; i += 2) t2 += array.elementAt(i);
        int x = (t1 * 7 - t2) % 10;
        array.add(new Integer(x));
        x = 0;
        for (int i = 0; i < 10; i++) x += array.elementAt(i);
        x = x % 10;
        array.add(new Integer(x));
        String TCNo = "";
        for (int i = 0; i < 11; i++) TCNo = TCNo + Integer.toString(array.elementAt(i));
        return TCNo;
    }

    public String randomPassword() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }

    public void pressEscape(){
        actionMethods.sendKeysESC();
    }


}

