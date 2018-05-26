package com.kart.track24;
import java.util.HashMap;
import java.util.Map;

public class utils {
        public static String test(){
       return "1234" ;
    }

    public static Map getUserName(){
    // English to Russian Dictionary
    Map<String,String> engRus = new HashMap<String,String>();

    //добавление элементов
        engRus.put("Monday", "Понедельник");
        engRus.put("Tuesday", "Вторник");
        engRus.put("Wednesday", "Среда");
        engRus.put("Thursday", "Четверг");
        engRus.put("Friday", "Пятница");
        return engRus;
    }}