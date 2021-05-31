package com.company;

import stev.booleans.And;
import stev.booleans.BooleanFormula;
import stev.booleans.Implies;
import stev.booleans.Not;
import stev.booleans.Or;
import stev.booleans.PropositionalVariable;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;


public class Main {

    public static void main(String[] args)
    {
        GameTable t = new GameTable("#26###81#3##7#8##64###5###7#5#1#7#9###39#51###4#3#2#5#1###3###25##2#4##9#38###46#");
        System.out.println(t.caseIsValid(3,3, '4'));


    }

    public boolean solve(GameTable tb){
        Random rand = new Random();
        while(tb.completed){
            char val = (char)rand.nextInt(9);
        }


        return false;
    }



}
