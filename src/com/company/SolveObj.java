package com.company;

import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;
import stev.booleans.*;

import java.lang.reflect.Array;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    List<BooleanFormula> base_props;
    BooleanFormula Rules;

    public SolveObj() {
//
        one = new PropositionalVariable("1");
        two = new PropositionalVariable("2");
        three = new PropositionalVariable("3");
        four = new PropositionalVariable("4");
        five = new PropositionalVariable("5");
        six = new PropositionalVariable("6");
        seven = new PropositionalVariable("7");
        eight = new PropositionalVariable("8");
        nine = new PropositionalVariable("9");
        base_props = List.of(
            new Equivalence(one, new Not(new Or(two,three,four,five,six,seven,eight,nine))),
            new Equivalence(two, new Not(new Or(one,three,four,five,six,seven,eight,nine))),
            new Equivalence(three, new Not(new Or(one,two,four,five,six,seven,eight,nine))),
            new Equivalence(four, new Not(new Or(one,two,three,five,six,seven,eight,nine))),
            new Equivalence(five, new Not(new Or(one,two,three,four,six,seven,eight,nine))),
            new Equivalence(six, new Not(new Or(one,two,three,four,five,seven,eight,nine))),
            new Equivalence(seven, new Not(new Or(one,two,three,four,five,six,eight,nine))),
            new Equivalence(eight, new Not(new Or(one,two,three,four,five,six,seven,nine))),
            new Equivalence(nine, new Not(new Or(one,two,three,four,five,six,seven,eight))),
            new Or(one,two,three,four,five,six,seven,eight,nine)
        );
        Rules = new And(base_props);

    }

    public void solve(GameTable tb){
        BooleanFormula[][] props = new BooleanFormula[9][9];
        char Values[][] = tb.getValues();
        for(int j = 0; j<9; j++) {
            for (int i = 0; i < 9; i++) {
                if(Values[i][j] != '#'){
                    props = addConstraintSquare(props, i , j , Values[i][j]);
                    props = addConstraintColumn(props, i , j , Values[i][j]);
                    props = addConstraintRow(props, i , j , Values[i][j]);
                }
            }
        }
        for(int j = 0; j<9; j++) {
            for (int i = 0; i < 9; i++) {
                props = addConstraintSquare(props, i , j , props[i][j]);
                props = addConstraintRow(props, i , j , props[i][j]);
                props = addConstraintColumn(props, i , j , props[i][j]);
            }
//            Ce block de code ajoute les contraintes de carre, lignes et column pour les cases vides, mais si on le decommente => stack overflow
        }
        // prepare the solver to accept MAXVAR variables. MANDATORY for MAXSAT solving
        String solved[][] = new String[9][9];
        int MAXVAR = 1000000;
        int NBCLAUSES = 500000;
        for(int j = 0; j<9; j++) {
            for (int i = 0; i < 9; i++) {
                ISolver solver = SolverFactory.newDefault();
                solver.newVar(MAXVAR);
                System.out.println("-----------------------------");
                props[i][j] = new And(props[i][j], Rules);
                System.out.println(props[i][j]);
                props[i][j]=BooleanFormula.toCnf(props[i][j]);
                int[][] clauses = props[i][j].getClauses();
                System.out.println("Clauses");
                solver.setExpectedNumberOfClauses(NBCLAUSES);
                Map<String,Integer> associations = props[i][j].getVariablesMap();
                for (int k = 0; k < clauses.length; k++) {
                    System.out.println(Arrays.toString(clauses[k]));
                    try {
                        solver.addClause(new VecInt(clauses[k]));
                    } catch (ContradictionException e) {
                        e.printStackTrace();
                    }
                }

                // solving
                IProblem problem = solver;
                try {
                    if (problem.isSatisfiable()) {
                        int[] model = problem.model();
                        System.out.println("\nsolution");
                        System.out.println(Arrays.toString(model));
                        int key = getPositiveNumbers(model);
                        int pos = -1;
                        for (int k = 0; k<associations.values().toArray().length; k++){
                            if (key == (int) associations.values().toArray()[k]){
                                pos = k;
                            }
                        }
                        System.out.println(associations.values()+" -> "+associations.keySet());
                        solved[i][j]= (String) associations.keySet().toArray()[pos];



                    } else {
                        System.out.println("unsatisfiable");
                    }
                }catch (TimeoutException e) {
                    e.printStackTrace();
                }
                System.out.println();
            }
        }



        System.out.println(" FInal soluton ------------------------------------------------------------");
        for (int j = 0; j < 9 ; j++) {
            for (int i = 0; i<9 ; i++) {
                System.out.print(solved[i][j]+",");
            }
            System.out.println();
        }
    }




    public BooleanFormula[][] addConstraintSquare(BooleanFormula[][] props, int x, int y, char val){
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
        return props;
    }
    public BooleanFormula[][] addConstraintSquare(BooleanFormula[][] props, int x, int y, BooleanFormula constr){
        int x_init = x-(x%3);
        int y_init = y-(y%3);
        for(int j = y_init; j< y_init+3; j++){
            for(int i = x_init; i< x_init+3; i++){
                if(i==x & j==y){
                    continue;
                }
                if(props[i][j]==null){
                    props[i][j] =  new Not(constr);
                    continue;
                }
                props[i][j] = new And(props[i][j], new Not(constr));

            }
        }
        return props;

    }

    public BooleanFormula[][] addConstraintRow(BooleanFormula[][] props, int x, int y, char val){
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
        return props;
    }
    public BooleanFormula[][] addConstraintRow(BooleanFormula[][] props, int x, int y, BooleanFormula constr) {
        int j = y;

        for(int i = 0; i<9; i++){
            if(i==x & j==y){
                continue;
            }
            if(props[i][j]==null){
                props[i][j] =  new Not(constr);

                continue;
            }
            props[i][j] = new And(props[i][j], new Not(constr));


        }
        return props;
    }
    public BooleanFormula[][] addConstraintColumn(BooleanFormula[][] props, int x, int y, char val){
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
        return props;
    }
    public BooleanFormula[][] addConstraintColumn(BooleanFormula[][] props, int x, int y, BooleanFormula constr) {
        int i = x;

        for(int j =0 ; j< 9; j++){
            if(i==x & j==y){
                continue;
            }
            if(props[i][j]==null){
                props[i][j] =  new Not(constr);
                continue;
            }
            props[i][j] = new And(props[i][j], new Not(constr));


        }
        return props;
    }
        public PropositionalVariable getProp(char val){
        switch (val){
            case '1':
                return this.one;
            case '2':
                return this.two;
            case '3':
                return this.three;
            case '4':
                return this.four;
            case '5':
                return this.five;
            case '6':
                return this.six;
            case '7':
                return this.seven;
            case '8':
                return this.eight;
            case '9':
                return this.nine;
            default:
                return null;
        }
    }
    public static int getPositiveNumbers(int[] model){

        for (int i = 0; i < model.length; i++) {
            if(model[i]>0){
                return model[i];
            }
        }
        return -10;
    }
}
