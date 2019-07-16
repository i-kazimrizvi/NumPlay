package com.mizaklabs.www.numberslearn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class Utility {

     static List<String> arrayList = Arrays.asList("#b71c1c","#d81b60","#c51162","#880e4f","#4a148c","#aa00ff","#ba68c8","#512da8","#6200ea"
            ,"#303f9f","#304ffe","#5c6bc0","#2196f3","#0d47a1","#2979ff","#00b8d4","#80deea","#00897b","#00bfa5",
            "#4caf50","#2e7d32","#76ff03","#689f38","#c0ca33","#f57f17","#ffd600","#ffab00","#ff8f00","#e65100","#dd2c00","#bf360c","#795548","#546e7a");

    static String getRandomColour(){

        Random random = new Random();
        int i = random.nextInt(arrayList.size());
        return arrayList.get(i);

    }
}
