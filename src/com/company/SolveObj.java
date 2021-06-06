package com.company;

import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;
import stev.booleans.*;

import java.util.*;

public class SolveObj {


    public static ISolver solver;
    static final int MAXVAR = 1000000;
    static final int NBCLAUSES = 500000;
    BooleanFormula rule_one;
    BooleanFormula rule_two;
    BooleanFormula rule_three;
    BooleanFormula rule_four;
    BooleanFormula rule_five;

    BooleanFormula props[][][] = new BooleanFormula[9][9][9];

    public SolveObj() {
//
        for (int j = 0; j <9 ; j++) {
            for (int i = 0; i < 9 ; i++) {
                for (int k = 0; k < 9; k++) {
                    String tmp = ""+(i+1)+(j+1)+(k+1);
                    props[i][j][k] = new PropositionalVariable(tmp);
                }
            }
        }
        rule_one= new And();
        for (int j = 0; j <9 ; j++) {
            for (int i = 0; i < 9 ; i++) {
                List<BooleanFormula> tmp2 = List.of(
                    props[i][j]
                );

                BooleanFormula tmp = new Or(tmp2);
                rule_one = new And(rule_one,tmp);
            }
        }
        System.out.println("Rule 1 : \n -----------------------\n"+ BooleanFormula.toCnf(rule_one));
        BooleanFormula and_arrays_column[] = new BooleanFormula[9];
        BooleanFormula and_arrays_rows[] = new BooleanFormula[9];
        for (int j = 0; j <9 ; j++) {
            for (int i = 0; i < 9 ; i++) {
                for (int k = 0; k < 9; k++) {
                    BooleanFormula tmp[] = new BooleanFormula[8];
                    int index = 0;
                    for (int l = 0; l < 9; l++) {
                        if(k!=l){
                            tmp[index] = new Implies(props[i][j][k],new Not(props[i][j][l]));
                            index++;
                        }
                        continue;
                    }
                    and_arrays_rows[i]= new And(tmp);
                }
            }
            and_arrays_column[j] = new And(and_arrays_rows);
        }
        rule_two = BooleanFormula.toCnf(new And(and_arrays_column));

        System.out.println("Rule 2 : \n -----------------------\n"+ BooleanFormula.toCnf(rule_two));
        and_arrays_column = new BooleanFormula[9];
        and_arrays_rows = new BooleanFormula[9];
        for (int j = 0; j < 9; j++) {
            for (int i = 0; i < 9; i++) {
                BooleanFormula tmp[] = new BooleanFormula[2];
                for (int k = 0; k < 9; k++) {
                    int index = 0;
                    //row
                    BooleanFormula tmp_line[] = new BooleanFormula[8];
                    for (int l = 0; l < 9; l++) {
                        if (i != l) {
                            tmp_line[index] = new Or(new Not(props[i][j][k]), new Not(props[l][j][k]));
                            index++;
                        }
                    }
                    index = 0;

                    tmp[0] = new Or(tmp_line);
                    BooleanFormula tmp_column[] = new BooleanFormula[8];
                    for (int l = 0; l < 9; l++) {
                        if(j!=l){
                            tmp_column[index] = new Implies(props[i][j][k],  new Not(props[i][l][k]));
                            index++;
                        }
                    }
                    tmp[1] = new Or(tmp_line);

                }
                and_arrays_rows[i] = new And(tmp);
            }
            and_arrays_column[j] = new And(and_arrays_rows);
        }
        rule_three = new And(and_arrays_column);

        System.out.println("Rule 3 : \n -----------------------\n"+ BooleanFormula.toCnf(rule_three)); // technically rule 3 and 4
        BooleanFormula columns[] = new BooleanFormula[9];
        for (int j = 0; j < 9; j++) {
            BooleanFormula rows[] = new BooleanFormula[9];
            for (int i = 0; i < 9; i++) {
                int x_init = i-(i%3);
                int y_init = j-(j%3);
                BooleanFormula square[] = new BooleanFormula[8];
                int index = 0;
                for (int k = y_init; k < y_init+3; k++) {
                    for (int l = x_init; l < x_init+3; l++) {
                        if(k==j & l==i){
                            continue;
                        }
                        BooleanFormula implies[] = new BooleanFormula[9];
                        for (int m = 0; m < 9; m++) {
                            implies [m] = new Implies(props[i][j][m], new Not(props[l][k][m]));
                        }
                        square[index] = new And(implies);
                        index++;

                    }
                }
                rows[i] = new And(square);

            }
            columns[j] = new And(rows);
        }
        rule_four = new And(columns);
        System.out.println("Rule 4 : \n -----------------------\n"+ BooleanFormula.toCnf(rule_four));


    }

    public void solve(GameTable tb) {
        char Values[][] = tb.getValues();
        int nb_values = 0;
        for (int j = 0; j < 9; j++) {
            for (int i = 0; i < 9; i++) {
                if (Values[i][j] != '#') {
                    nb_values++;
                }
            }
        }
        BooleanFormula game_values[] = new BooleanFormula[nb_values];
        int index = 0;
        for (int j = 0; j < 9; j++) {
            for (int i = 0; i < 9; i++) {
                if (Values[i][j] != '#') {
                    int val = Character.getNumericValue(Values[i][j]);
                    game_values[index] = props[i][j][val - 1];
                    index++;
                }
            }
        }
        System.out.println("Rule 5 : \n -----------------------\n" + rule_five);
        rule_five = BooleanFormula.toCnf(new And(game_values));
        BooleanFormula big_formula = new And(rule_one,rule_two,rule_three,rule_four, rule_five);
        System.out.println(rule_one);
        System.out.println(rule_two);
        System.out.println(rule_three);
        System.out.println(rule_four);
        System.out.println(rule_five);
        System.out.println("Big formula \n" + big_formula);
        BooleanFormula cnf= BooleanFormula.toCnf(big_formula);
        solver = SolverFactory.newDefault();
        solver.newVar(MAXVAR);
        solver.setExpectedNumberOfClauses(NBCLAUSES);
        int [][]clauses = cnf.getClauses();
        for (int i=0; i<clauses.length; i++){
            try {
                solver.addClause(new VecInt(clauses[i]));
            } catch (ContradictionException e) {
                e.printStackTrace();
            }
        }


        IProblem problem = solver;
        ArrayList<Integer> posNum = new ArrayList<>();
        try {
            if (problem.isSatisfiable()) {
                int[] model = problem.model();
                getPositiveNumbers(model, posNum);
                System.out.println(" length : " + posNum.size() + " " + posNum);
            } else {
                System.err.println(" Unsatisfiable");
            }
        } catch (TimeoutException e) {
            e.printStackTrace();


        }

        Map<String,Integer> associations = cnf.getVariablesMap();
        Set<String> keys =  associations.keySet();
        Collection<Integer> clause_values = associations.values();
        ArrayList<String> result_values = new ArrayList<>();
        for (Integer val: posNum) {
            index = 0;
            for (Integer key: clause_values) {
                if (key.equals(val)){
                    result_values.add((String) keys.toArray()[index]);
                }
                index++;
            }
        }

        System.out.println(result_values);
        index=0;
        char[][] solved_table= new char[9][9];
        for (String r: result_values) {
            solved_table[Character.getNumericValue(r.charAt(0))-1][Character.getNumericValue(r.charAt(1))-1] = r.charAt(2);
        }

        for (int j = 0; j < 9; j++) {
            for (int i = 0; i < 9; i++) {
                index++;
                System.out.print(solved_table[i][j]+",");
            }
            System.out.println();
        }

//
//        for(String keys: associations.keySet()){
//            System.out.println(" key = " + keys + " value = " + associations.get(keys));
//        }
//        System.err.println(" POSNUM size = " + posNum.size());
//        for( int n: posNum){
//            System.out.println(associations.keySet().toArray()[n]);
//            t.set((((String)associations.keySet().toArray()[n]).charAt(0))-'0'-1,
//                    ((String)associations.keySet().toArray()[n]).charAt(1)-'0'-1,
//                    ((String)associations.keySet().toArray()[n]).charAt(2));
//        }


//        System.out.println(t);


    }
    public static void getPositiveNumbers(int[] model, ArrayList<Integer> posNum){
        for (int i = 0; i < model.length; i++) {
            if(model[i]>0){
                posNum.add(model[i]);
            }
        }
    }



}
