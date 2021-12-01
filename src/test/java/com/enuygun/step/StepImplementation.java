package com.enuygun.step;

import com.enuygun.base.Driver;
import com.enuygun.methods.Methods;
import com.thoughtworks.gauge.Step;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.enuygun.utils.ReadProperties;

import java.util.ArrayList;
import java.util.List;

public class StepImplementation extends Driver {

    private Logger logger = LoggerFactory.getLogger(getClass());

    Methods methods = new Methods();

    String passengerSurname;
    public static String packageSelection = "";
    public static String flightSelection, paymentType;
    double priceBeforeSeatSelection;
    double priceAfterSeatSelection;


    @Step("Sayfada <x> ve <y> koordinatlarına scroll yap")
    public void scrollTo(int x, int y) {
        String script = String.format("window.scrollTo(%d, %d);", x, y);
        executeJS(script, true);
    }

    @Step("<y> koordinatına y düzleminde scroll yap")
    public void scrollBy(String y) {
        executeJS("window.scrollBy(0," + y + ")", true);

    }

    private Object executeJS(String script, boolean wait) {

        return wait ? getJSExecutor().executeScript(script, "") : getJSExecutor().executeAsyncScript(script, "");
    }

    private JavascriptExecutor getJSExecutor() {
        return (JavascriptExecutor) driver;
    }


    @Step("<key> elementine tıkla")
    public void clickElement(String key) {
        methods.waitUntilPresenceOfElement(key);
        methods.waitUntilElementToBeClickableAndClick(key);
    }

    @Step("<key> elementine tıklaaa")
    public void clickElement2(String key) {

        methods.clickElement2(key);
    }

    @Step("Click coordinate")
    public void clickByCoordinate() {
        getJSExecutor().executeScript("document.elementFromPoint(800, 300).click();");
        //getJSExecutor().executeScript("document.elementFromPoint(250, 250).click();");
    }

    @Step("<key> elementini <adet> tane arttır")
    public void increaseOf(String key, int adet) {
        for (int i = 0; i < adet; i++) {
            methods.clickElement(key);
        }
    }

    @Step("<key> elementi üzerinde bekle ve tıkla")
    public void focusAndClickElement(String key) {

        methods.isElementVisible(key);
        methods.focusToElement(key);
        methods.clickElement(key);
    }

    @Step("<key> elementine js ile tıkla")
    public void clickByElementWithJS(String key) {
        WebElement element = methods.findElement(key);
        getJSExecutor().executeScript("arguments[0].click();", element);
    }

    @Step("<element> web elementine js ile tıkla")
    public void clickByElementWithJSNoKey(WebElement element) {
        getJSExecutor().executeScript("arguments[0].click();", element);
    }

    @Step("<key> elementine scroll yap")
    public void scrollToElement(String key) {

        Point location = methods.findElement(key).getLocation();
        scrollTo(location.getX(), location.getY());
    }

    @Step("<key> elementine scroll yap ve tıkla")
    public void scrollToElementClick(String key) {

        WebElement element = methods.findElement(key);
        scrollTo(element.getLocation().getX(), element.getLocation().getY());
        waitBySeconds(2);
        element.click();
    }

    @Step("<key> element listesinin <index> sıradaki elemanına scroll yap")
    public void scrollToElementByIndex(String key, int index) {

        Point location = methods.findElements(key).get(index - 1).getLocation();
        scrollTo(location.getX(), location.getY());
    }

    @Step("<key> element listesinin <index> sıradaki elemanına scroll yap ve tıkla")
    public void scrollToElementByIndexClick(String key, int index) {

        WebElement webElement = methods.findElements(key).get(index - 1);
        scrollTo(webElement.getLocation().getX(), webElement.getLocation().getY());
        waitBySeconds(2);
        webElement.click();
    }


    @Step("<key> element listesinin <index> sıradaki elemanına tıkla")
    public void clickElementByIndex(String key, int index) {

        methods.findElements(key).get(index - 1).click();
    }

    @Step("<key> elementine <text> ini yolla")
    public void sendKeysElement(String key, String text) {
        methods.sendKeys(key, text);
    }

    @Step("<key> elementine güvenlik noyu yolla")
    public void sendKeysElementText(String key) {
        methods.isElementVisible(key);
        String text = methods.findElement("txt_Random_No").getText();
        methods.waitBySeconds(3);
        getJSExecutor().executeScript(String.format("arguments[0].value='%s';", text), methods.findElement(key));
        methods.waitBySeconds(5);
    }


    @Step("<key> elementine odaklan <text> ini yolla")
    public void focusandSendKeys(String key, String text) {
        methods.isElementVisible(key);
        // methods.focusToElement(key);
        methods.sendKeys(key, text);

    }

    @Step("<key> tiklaaa")
    public void tikla(String key){
       WebElement e= (WebElement) methods.findElements(key);
       e.click();
    }


    @Step("<key> element listesinin ilk elemanına tıkla")
    public void focusandSendKeys(String key) {
        methods.isElementVisible(key);
        WebElement element = methods.findElements(key).get(0);
        waitBySeconds(1);
        getJSExecutor().executeScript("arguments[0].click();", element);
    }

    @Step("<url> adresine git")
    public void navigateTo(String url) {
        //driver.manage().window().maximize();
        methods.navigateTo(url);
    }

    @Step("<key> elementinin <attr> değerini loga yaz")
    public void attrToLog(String key, String attr) {

        passengerSurname = methods.getAttribute(key, attr);

        logger.info("-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
        logger.info("YOLCUNUN SOYİSİM BİLGİSİ : " + passengerSurname);
        logger.info("-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
    }

    @Step({"<key> elementinin text değeri <expectedText> değerine eşit mi",
            "get text <key> element and control <expectedText>"})
    public void getElementText(String key, String expectedText) {

        methods.waitElementIsVisible(key);

        String actualText;
        if (methods.getAttribute(key, "value") != null) {
            actualText = methods.getAttribute(key, "value").trim().replace("\r", "").replace("\n", "");
        } else {
            actualText = methods.getText(key).trim().replace("\r", "").replace("\n", "");
        }

        logger.info("Beklenen text: " + expectedText);
        logger.info("Alınan text: " + actualText);
        Assert.assertEquals("Text değerleri eşit değil", expectedText.replace(" ", ""), actualText.replace(" ", ""));
        logger.info("Text değerleri eşit");
    }

    @Step({"<key> elementinin text değeri <expectedText> değerini içeriyor mu",
            "get text <key> element and control contains <expectedText>"})
    public void getElementTextContain(String key, String expectedText) {
        methods.waitElementIsVisible(key);

        String actualText;
        if (methods.getAttribute(key, "value") != null) {
            actualText = methods.getAttribute(key, "value").trim().replace("\r", "").replace("\n", "");
        } else {
            actualText = methods.getText(key).trim().replace("\r", "").replace("\n", "");
        }

        logger.info("Beklenen text: " + expectedText);
        logger.info("Alınan text: " + actualText);
        Assert.assertTrue("Text değerleri eşit değil", actualText.replace(" ", "").contains(expectedText.replace(" ", "")));
        logger.info("Text değerleri eşit");
    }

    @Step("<seconds> saniye bekle")
    public void waitBySeconds(long seconds) {

        methods.waitBySeconds(seconds);
    }

    @Step("<zoomNumber> js")
    public void js(String zoomNumber) {

        ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='" + zoomNumber + "'");
    }

    @Step("<by> elementinin görünür olması kontrol edilir")
    public void controlIsElementVisible(String key) {

        Assert.assertTrue("Element görünür değil", methods.isElementVisible(key));

    }

    @Step("<key> elementinin sayfada mevcut olması kontrol edilir")
    public void controlIsElementPresent(String key) {

        Assert.assertTrue("Element mevcut değil", methods.isElementPresent(key));
    }

    @Step("<by> elementinin tıklanabilir olması kontrol edilir")
    public void controlIsElementClickable(String key) {

        Assert.assertTrue("Element tıklanabilir değil", methods.isElementClickable(key));

    }

    @Step("<by> elementinin tıklanabilir olması kontrolü")
    public void isElementClickable(String key) {

        if (!(methods.isElementClickable(key))) {
            logger.info(key + " elementi tıklanabilir değil.");
        } else {
            logger.info("tıklanabilir");
        }
    }

    @Step("<by> elementinin görünür olmaması kontrol edilir")
    public void controlIsElementInVisible(String key) {

        if (methods.findElements("BKMExpress").size() < 1) {
            logger.info("BKM ödeme seçeneği beklendiği şekilde gelmedi");
        } else {
            Assert.fail("BKM ödeme seçeneği gelmemesi gerekirken geldi. HATA!!!");
        }


    }

    @Step("<key> elementinin text alanını temizle")
    public void clearToElement(String key) {


        methods.clearElement(key);

    }

    @Step("<by> elementinin üzerine gel")
    public void hoverElement(String key) {

        methods.hoverElement(key);

    }

    @Step("<zoomNumber> kadar pencere boyutu ayarla")
    public void zoomSetting(String zoomNumber) {

        ((JavascriptExecutor) driver).executeScript("document.body.style.zoom='" + zoomNumber + "'");

    }

    @Step("Yeni <key>. acilan sayfaya git")
    public void swichtoNextPage(int key) {
        methods.switchTab(key);
    }

    @Step("<aspectUrl> sayfa urli dogru mu kontrol et")
    public void currentUrl(String aspectUrl) {
        Assert.assertEquals(driver.getCurrentUrl(), aspectUrl);
    }

    @Step({"<key> elementinin text değeri <expectedText> değerinden farklı mı"})
    public void checkElementNotEquals(String key, String expectedText) {


        methods.waitElementIsVisible(key);


        String actualText = methods.getText(key).trim().replace("\r", "").replace("\n", "");

        logger.info("Beklenen text: " + expectedText);
        logger.info("Alınan text: " + actualText);
        Assert.assertNotEquals("Text değerleri eşit", expectedText.replace(" ", ""), actualText.replace(" ", ""));
        logger.info("Text değerleri eşit değil");
    }

    @Step("<key1> elementinin fiyatini <key2> elementi ile karsilastir")
    public void getPrice(String key1, String key2) {
        methods.comparePrices(key1, key2);
    }

    @Step("Zoom out %80")
    public void zoomOut() {
        String zoomScript = "document.body.style.zoom='80%'";
        getJSExecutor().executeScript(zoomScript);
    }

    @Step("Get Page Source")
    public void pageSource() {
        methods.getPageSources();
    }

    @Step("<key> Radio button elementinin secilip secilmedigini kontrol et")
    public void radioButtonControl(String key) {
        methods.radioButtonIsSelected(key);
    }

    @Step("<key> Check box elementinin secilip secilmedigini kontrol et")
    public void checkBoxControl(String key) {
        methods.checkBoxIsSelected(key);
    }

    @Step("Acilan uyari mesajini kabul et")
    public void acceptMessage() {
        methods.acceptAlertMessage();
    }

    @Step("Reklamları kapat ve <tabNumber> tabını sec")
    public void closeAddsTab(int tabNumber) {

        methods.closeAddsTab(tabNumber, "flypgs.com", "trivago");
    }

    @Step("<key> elementinin fiyatini al")
    public void getPriceofElement(String key) {
        methods.getPrices(key);
    }

    @Step("<key> elementi ile fiyatini karsilastir")
    public void getAfterPriceofElement(String key) {
        methods.afterPrice(key);
    }

    @Step("<text1> text degeri <text2> degerine degismis mi")
    public void getTextChanging(String text1, String text2) {
        Assert.assertEquals(methods.getText(text1), methods.getText(text2));
    }

    @Step("<key> elementinin <attribute> niteliği <expectedValue> değerine eşit mi")
    public void checkElementAttribute(String key, String attribute, String expectedValue) {

        String attributeValue = methods.getAttribute(key, attribute);
        Assert.assertNotNull("Elementin değeri bulunamadi", attributeValue);
        Assert.assertEquals("Elementin değeri eslesmedi", expectedValue, attributeValue);
    }


    @Step("<key> elementinin text değerini gör")
    public void getTextValue(String key) {
        logger.info("=======================================");
        logger.info(key + " elementinin text değeri -->> " + methods.getText(key));
        logger.info("=======================================");
    }

    List<Double> priceList = new ArrayList<>();

    @Step("<key> elementinin text değerini double yapıp ArrayListe ekle")
    public void storeStringAsIntInArrayList(String key) {

        priceList.add(methods.removeCurrencyAndCovertDouble(key));
    }







    @Step("Şu anki url <url> ile aynı mı")
    public void doesUrlEqual(String url) {

        Assert.assertTrue("Beklenen url, sayfa url ine eşit değil", methods.doesUrl(url, 75, "equal"));
    }

    @Step("Şu anki url <url> içeriyor mu")
    public void doesUrlContain(String url) {

        Assert.assertTrue("Beklenen url, sayfa url ine eşit değil", methods.doesUrl(url, 75, "contain"));
    }

    @Step("DebugHelper")
    public void implementation1() {
        System.out.println("DebugHelper");
    }

    @Step("<key> elementinin değerini temizle")
    public void clearElement(String key) {

        methods.clearElement(key);
    }


    @Step("<key> element listenin ilk elementine tıkla")
    public void clickFirstElementOfList(String key) {

        methods.findFirstClickableElementOfList(key).click();

    }

    @Step("<X> ve <Y> koordinatına scroll yap")
    public void CoordinateScroll(int x, int y) {
        //getJSExecutor().executeScript("document.elementFromPoint(400, 400).click();");
        //getJSExecutor().executeScript("document.scrollTo("+X+", "+Y+");");
        String script = String.format("window.scrollTo(%d, %d);", x, y);
        executeJS(script, true);
    }

    public ArrayList<Double> priceArray = new ArrayList<>();

    }




