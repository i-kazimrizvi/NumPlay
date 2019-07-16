package com.mizaklabs.www.numberslearn;

import java.util.Random;

public class NumberResult {

     static int resultNumber = 1;

    public static int getResultNumber() {
        return resultNumber;
    }

    public static void setResultNumber(int resultNumber) {
        NumberResult.resultNumber = resultNumber;
    }

    public  static void generateNumberLevel0(){
        Random rand = new Random();
        setResultNumber(rand.nextInt(10));
    }
}
