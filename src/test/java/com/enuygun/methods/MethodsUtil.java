package com.enuygun.methods;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.enuygun.base.Driver;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.*;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.PrimitiveIterator;
import java.util.Random;

public class MethodsUtil {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public String randomString(int stringLength){

        Random random = new Random();
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUWVXYZabcdefghijklmnopqrstuwvxyz0123456789".toCharArray();
        String stringRandom = "";
        for (int i = 0 ; i < stringLength ; i++){

            stringRandom = stringRandom + String.valueOf(chars[random.nextInt(chars.length)]);
        }

        return stringRandom;
    }

    public String randomStringExtended(int stringLength, String charType, String startCharType, int startCharLength){

        Random random = new Random();
        StringBuilder stringRandom = new StringBuilder();
        char[] chars = null;

        if(!startCharType.equals("")){

            chars = getChars(startCharType);
            for (int i = 0 ; i < startCharLength; i++) {
                stringRandom.append(String.valueOf(chars[random.nextInt(chars.length)]));
            }
            stringLength = stringLength - startCharLength;
        }

        chars = getChars(charType);

        for (int i = 0 ; i < stringLength; i++){

            stringRandom.append(String.valueOf(chars[random.nextInt(chars.length)]));
        }

        return stringRandom.toString();
    }

    public char[] getChars(String condition){

        String upperChars = "ABCDEFGHIJKLMNOPQRSTUWVXYZ";
        String lowerChars = "abcdefghijklmnopqrstuwvxyz";
        String numbers = "0123456789";
        char[] chars = null;
        switch (condition){
            case "upper":
                chars = upperChars.toCharArray();
                break;
            case "lower":
                chars = lowerChars.toCharArray();
                break;
            case "char":
                chars = (upperChars + lowerChars).toCharArray();
                break;
            case "numeric":
                chars = numbers.toCharArray();
                break;
            case "all":
                chars = (upperChars + lowerChars + numbers).toCharArray();
                break;
            default:
                Assert.fail("");
        }

        return chars;
    }

    public int getRandomNumber(int length){

        Random random = new Random();
        return random.nextInt(length);
    }

    public Integer getRandomInt(int origin, int bound){

        return getRandomIteratorInt(origin, bound).nextInt();
    }

    public PrimitiveIterator.OfInt getRandomIteratorInt(int origin, int bound){

        Random random = new Random();
        return random.ints(origin, bound).iterator();
    }

    public Long getRandomLong(int origin, int bound){

        return getRandomIteratorLong(origin, bound).nextLong();
    }

    public PrimitiveIterator.OfLong getRandomIteratorLong(long origin, long bound){

        Random random = new Random();
        return random.longs(origin, bound).iterator();
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
            case "notEqual":
                result = !actualValue.equals(expectedValue);
                break;
            case "notContain":
                result = !actualValue.contains(expectedValue);
                break;
            case "notStartWith":
                result = !actualValue.startsWith(expectedValue);
                break;
            case "notEndWith":
                result = !actualValue.endsWith(expectedValue);
                break;
            default:
                Assert.fail("hatali durum: " + condition);
        }
        return result;
    }

    public String getTime(String format){

        return DateTime.now()//.withZone(DateTimeZone.UTC)
                .toString(format);//"dd/MM/yyyy HH:mm:ss,SSS");
    }

    public long getTimeMillis(){

        DateTime utc = new DateTime();
        return utc.getMillis();
    }

    public long currentTimeMillis(){

        return System.currentTimeMillis();
    }

    public Long getTimeMillisFromTime(String time, String format){

        return org.joda.time.LocalDateTime.parse(time, DateTimeFormat.forPattern(format))
                .toDateTime().getMillis();
    }

    public Long getTimeMillisFromTime(String time, String format, int forOffsetHours){

        /** LocalDateTime localDateTime = LocalDateTime.parse(time,DateTimeFormatter.ofPattern(format));
         //"dd/MM/yyyy HH:mm:ss,SSS"
         return localDateTime.toInstant(ZoneOffset.ofHours(forOffsetHours)).toEpochMilli();*/
        return org.joda.time.LocalDateTime.parse(time, DateTimeFormat.forPattern(format))
                .toDateTime(DateTimeZone.forOffsetHours(forOffsetHours)).getMillis();
    }

    public Long getTimeMillisFromTime(String time, String format, String zoneID){

        return org.joda.time.LocalDateTime.parse(time, DateTimeFormat.forPattern(format))
                .toDateTime(DateTimeZone.forID(zoneID)).getMillis();
    }

    public String getTimeFromMillis(String format, long millis){

        DateTime dateTime = new DateTime(millis);
        //"dd/MM/yyyy HH:mm:ss,SSS"
        return dateTime.toString(format);
    }

    public String getTimeFromMillis(String format, long millis, int forOffsetHours){

        DateTime dateTime = new DateTime(millis, DateTimeZone.forOffsetHours(forOffsetHours));
        //"dd/MM/yyyy HH:mm:ss,SSS"
        return dateTime.toString(format);
    }

    public String getTimeFromMillisWithZoneId(String format, long millis, String zoneID){

        DateTime dateTime = new DateTime(millis, DateTimeZone.forID(zoneID));
        //"dd/MM/yyyy HH:mm:ss,SSS"
        return dateTime.toString(format);
    }

    public String getTimeWithZoneId(String format, String zoneID){

        //"Europe/Istanbul"
        return DateTime.now(DateTimeZone.forID(zoneID))//.withZone(DateTimeZone.UTC)
                .toString(format);//"dd/MM/yyyy HH:mm:ss,SSS");
    }

    public String getTimeWithForOffsetHours(String format, int forOffsetHours){

        return DateTime.now(DateTimeZone.forOffsetHours(forOffsetHours))
                .toString(format);
    }

    public String getStackTraceLog(Throwable e){

        StackTraceElement[] stackTraceElements = e.getStackTrace();
        String error = e.toString() + "\r\n";
        for (int i = 0; i < stackTraceElements.length; i++) {

            error = error + "\r\n" + stackTraceElements[i].toString();
        }
        return error;
    }

    public String getCopiedText() {

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = clipboard.getContents(null);
        String copiedText = null;
        try {
            copiedText = contents.getTransferData(DataFlavor.stringFlavor).toString();
        } catch (IOException | UnsupportedFlavorException e) {
            logger.error(getStackTraceLog(e));
        }
        return copiedText;
    }

    private void setDoubleGsonFormat(GsonBuilder gsonBuilder){

        gsonBuilder.registerTypeAdapter(BigDecimal.class, (JsonSerializer<BigDecimal>) (src, typeOfSrc, context) -> {

            Number n = //src.scale() >= 8 ? (
                    new Number() {

                        @Override
                        public long longValue() {
                            return 0;
                        }

                        @Override
                        public int intValue() {
                            return 0;
                        }

                        @Override
                        public float floatValue() {
                            return 0;
                        }

                        @Override
                        public double doubleValue() {
                            return 0;
                        }

                        @Override
                        public String toString() {
                            return new BigDecimal(String.valueOf(src)).toPlainString();
                        }
                    }; //) : src
            return new JsonPrimitive(n);
        });
    }

    public <PANDA> Boolean writeJson(PANDA panda, String fileLocation, boolean prettyPrint, boolean serializeNulls, boolean isAppend){

        try {
            Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(Driver.userDir
                            + (Driver.slash.equals("/") ? fileLocation
                            : fileLocation.replace("/","\\"))
                            , isAppend), StandardCharsets.UTF_8));
            GsonBuilder gsonBuilder = new GsonBuilder();
            setDoubleGsonFormat(gsonBuilder);
            if (prettyPrint) { gsonBuilder.setPrettyPrinting(); }
            if (serializeNulls) { gsonBuilder.serializeNulls(); }
            Gson gson = gsonBuilder.create();
            gson.toJson(panda, writer);
            writer.close();
            return true;
        } catch (IOException e) {
            logger.error(getStackTraceLog(e));
        }
        return false;
    }

    public <PANDA> PANDA readJson(Type type, String fileLocationOrStringJson, boolean isFile){

        GsonBuilder gsonBuilder = new GsonBuilder();
        setDoubleGsonFormat(gsonBuilder);
        Gson gson = gsonBuilder.create();
        try {
            if(isFile){
                FileReader fileReader = new FileReader(new File(Driver.userDir
                        + (Driver.slash.equals("/") ? fileLocationOrStringJson
                        : fileLocationOrStringJson.replace("/","\\"))));
                return gson.fromJson(fileReader, type);
            }
            return gson.fromJson(fileLocationOrStringJson, type);
        } catch (FileNotFoundException e) {
            logger.error(getStackTraceLog(e));
        }
        return null;
    }

    public Type getClassTypeToken(Type panda, Type... pandaClasses){
        // Type elementType = new TypeToken<>(){}.getType();
        if (pandaClasses.length != 0)
            return TypeToken.getParameterized(panda, pandaClasses).getType();
        return TypeToken.getParameterized(panda).getType();
    }

    public String getJsonStringWithBufferedReader(String fileLocation){

        StringBuilder jsonStringBuilder = new StringBuilder();
        InputStream propertiesStream = null;
        try {
            propertiesStream = new FileInputStream(Driver.userDir
                    + (Driver.slash.equals("/") ? fileLocation : fileLocation.replace("/","\\")));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(propertiesStream, StandardCharsets.UTF_8));
            String jsonString;
            while(true){
                if ((jsonString = bufferedReader.readLine()) == null) break;
                jsonStringBuilder.append(jsonString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonStringBuilder.toString();
    }

    public void waitByMilliSeconds(long milliSeconds){

        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void waitBySeconds(long seconds){

        waitByMilliSeconds(seconds*1000);
    }

}