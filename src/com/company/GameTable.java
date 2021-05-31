package com.company;

import java.util.Arrays;

public class GameTable {


    public boolean completed;

    private char[][] Values;

    public GameTable(String value) {
        if (value.length() != 81){
            throw new IllegalStateException("Unvalid Game Table");
        }
        Values = new char[9][9];
        int pos = 0;
        for (int j =0; j<9; j++){
            for (int i=0; i<9; i++){
                Values[i][j]=value.charAt(pos);
                pos++;
            }
        }

    }

    public Boolean caseIsValid(int x, int y, char val){
        System.out.println("Checking case "+x+", "+y +" value: " + Values[x][y]);

        if (checkSquare(x,y,val) && checkColumn(x,y, val) && checkRow(x,y,val)){
            return true;
        }
        return false;
    }















    private boolean checkSquare(int x, int y, char val) {
        if(Values[x][y] =='#'){
            return true;
        }
        int x_init = x-(x%3);
        int y_init = y-(y%3);
        for(int j = y_init; j< y_init+3; j++){
            for(int i = x_init; i< x_init+3; i++){
                if(i==x & j==y){
                    continue;
                }
                if((Values[i][j]==val)){
                    return false;
                }
            }
        }

        return true;
    }

    private boolean checkRow(int x, int y, char val) {
        int j = y;

        for(int i = 0; i<9; i++){
            if(i==x & j==y){
                continue;
            }
            if(Values[i][j]==val){
                return false;
            }
        }
        return true;
    }

    private boolean checkColumn(int x, int y, char val) {
        int i = x;

        for(int j =0 ; j< 9; j++){
            if(i==x & j==y){
                continue;
            }
            if(Values[i][j]==val){
                return false;
            }
        }
        return true;
    }


    //getters and setters


    public char[][] getValues() {
        return Values;
    }

    public void setValues(char[][] values) {
        Values = values;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        for (int j =0; j<9; j++){
            for (int i=0; i<9; i++){
                str.append(" {").append(Values[i][j]).append("} ");
            }
            str.append(",\n");
        }
        return str.toString();
    }
}
