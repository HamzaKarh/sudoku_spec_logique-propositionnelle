package com.company;

import stev.booleans.And;
import stev.booleans.BooleanFormula;
import stev.booleans.Not;
import stev.booleans.PropositionalVariable;

import javax.naming.PartialResultException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;


public class Main {

    public static void main(String[] args) {
        GameTable t = new GameTable("#26###81#3##7#8##64###5###7#5#1#7#9###39#51###4#3#2#5#1###3###25##2#4##9#38###46#");
//        System.out.println(t.CaseEval( 3,3, '4'));
        SolveObj s = new SolveObj();
//        ArrayList<int[][]> props = (s.solve(t));
        s.solve(t);


    }
}









