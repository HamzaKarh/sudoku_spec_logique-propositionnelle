package com.company;

import stev.booleans.*;

import java.util.ArrayList;

public class SolveObj {
    PropositionalVariable one;
    PropositionalVariable two;
    PropositionalVariable three;
    PropositionalVariable four;
    PropositionalVariable five;
    PropositionalVariable six;
    PropositionalVariable seven;
    PropositionalVariable eight;
    PropositionalVariable nine;
    public SolveObj() {
        ArrayList <BooleanFormula> base_props = new ArrayList<BooleanFormula>();
        one = new PropositionalVariable("1");
        base_props.add(one);
        base_props.add(new Implies(one, new Not(new Or(two,three,four,five,six,seven,eight,nine))));
        two = new PropositionalVariable("2");
        base_props.add(two);
        base_props.add(new Implies(two, new Not(new Or(one,three,four,five,six,seven,eight,nine))));
        three = new PropositionalVariable("3");
        base_props.add(three);
        base_props.add(new Implies(three, new Not(new Or(one,two,four,five,six,seven,eight,nine))));
        four = new PropositionalVariable("4");
        base_props.add(four);
        base_props.add(new Implies(four, new Not(new Or(one,two,three,five,six,seven,eight,nine))));
        five = new PropositionalVariable("5");
        base_props.add(five);
        base_props.add(new Implies(five, new Not(new Or(one,two,three,four,six,seven,eight,nine))));
        six = new PropositionalVariable("6");
        base_props.add(six);
        base_props.add(new Implies(six, new Not(new Or(one,two,three,four,five,seven,eight,nine))));
        seven = new PropositionalVariable("7");
        base_props.add(seven);
        base_props.add(new Implies(seven, new Not(new Or(one,two,three,four,five,six,eight,nine))));
        eight = new PropositionalVariable("8");
        base_props.add(eight);
        base_props.add(new Implies(eight, new Not(new Or(one,two,three,four,five,six,seven,nine))));
        nine = new PropositionalVariable("9");
        base_props.add(nine);
        base_props.add(new Implies(nine, new Not(new Or(one,two,three,four,five,six,seven,eight))));




    }

    public ArrayList<int[][]> solve(GameTable tb){
        BooleanFormula[][] props= new BooleanFormula[9][9];
        char Values[][] = tb.getValues();
        for(int j = 0; j<9; j++) {
            for (int i = 0; i < 9; i++) {
                if(Values[i][j] != '#'){
                    addConstraintSquare(props, i , j , Values[i][j]);
                    addConstraintColumn(props, i , j , Values[i][j]);
                    addConstaintRow(props, i , j , Values[i][j]);
                }
            }
        }
        for(int j = 0; j<9; j++) {
            for (int i = 0; i < 9; i++) {
                System.out.println(BooleanFormula.toCnf(props[i][j]));
            }
        }
        ArrayList<int[][]> clauses = new ArrayList<int[][]>();
        for(int j = 0; j<9; j++) {
            for (int i = 0; i < 9; i++) {
                props[i][j]=BooleanFormula.toCnf(props[i][j]);
                for (int k = 0; k < props[i][j].getClauses().length; k++) {
                    clauses.add(props[i][j].getClauses());
                }
            }
        }


        return clauses;
    }




    public void addConstraintSquare(BooleanFormula[][] props, int x, int y, char val){
        int x_init = x-(x%3);
        int y_init = y-(y%3);
        for(int j = y_init; j< y_init+3; j++){
            for(int i = x_init; i< x_init+3; i++){
                if(i==x & j==y){
                    props[i][j] = getProp(val);
                    continue;
                }
                if(props[i][j]==null){
                    props[i][j] = new Not(getProp(val));
                    continue;
                }
                props[i][j] = new And(props[i][j], new Not(getProp(val)));
            }
        }
    }
    public void addConstaintRow(BooleanFormula[][] props, int x, int y, char val){
        int j = y;

        for(int i = 0; i<9; i++){
            if(i==x & j==y){
                props[i][j] = getProp(val);
                continue;
            }
            if(props[i][j]==null){
                props[i][j] = new Not(getProp(val));
                continue;
            }
            props[i][j] = new And(props[i][j], new Not(getProp(val)));
        }
    }
    public void addConstraintColumn(BooleanFormula[][] props, int x, int y, char val){
        int i = x;

        for(int j =0 ; j< 9; j++){
            if(i==x & j==y){
                props[i][j] = getProp(val);
                continue;
            }
            if(props[i][j]==null){
                props[i][j] = new Not(getProp(val));
                continue;
            }
            props[i][j] = new And(props[i][j], new Not(getProp(val)));

        }
    }

    public PropositionalVariable getProp(char val){
        switch (val){
            case '1':
                return one;
            case '2':
                return two;
            case '3':
                return three;
            case '4':
                return four;
            case '5':
                return five;
            case '6':
                return six;
            case '7':
                return seven;
            case '8':
                return eight;
            case '9':
                return nine;
            default:
                return null;
        }
    }
}
