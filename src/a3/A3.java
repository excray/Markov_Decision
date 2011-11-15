/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package a3;

/**
 *
 * @author Vivek
 */
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.omg.CORBA.Environment;

public class A3 {

    private static Table<Double> table;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        //Problem1
        runProblem1();
    }

    private static Map<Cell<Double>, CellAction> GetOptimalAction(int xdim, int ydim, double r) {
        //Create Table
        table = createTable(Double.class, xdim, ydim, r);

        table.removeCell(2, 2);
        table.getCellAt(4, 3).setContent(1.0);
        table.getCellAt(4, 2).setContent(-1.0);

        MarkovDecisionProcess<Cell<Double>, CellAction> mdp = createMDP(table);
        ValueIteration<Cell<Double>, CellAction> vi = new ValueIteration<Cell<Double>, CellAction>(
                1.0);

        Map<Cell<Double>, CellAction> optimalAction = new LinkedHashMap<Cell<Double>, CellAction>();

        Map<Cell<Double>, Double> U = vi.valueIteration(mdp, 0.0001, optimalAction);

        return optimalAction;
    }

    private static boolean compareMaps(Map<Cell<Double>, CellAction> a, Map<Cell<Double>, CellAction> b) {
        List<CellAction> values1 = new ArrayList<CellAction>(a.values());
        List<CellAction> values2 = new ArrayList<CellAction>(b.values());
        //boolean equals = true;
        for (int i = 0; i < values1.size(); i++) {
            CellAction a1 = values1.get(i);
            CellAction b1 = values2.get(i);

            if (a1.equals(b1) == false) {

                return false;
            }
        }

        return true;
    }

    private static void runProblem1() {

//        double reward_min = 1;
//        double reward_max = 10;
//        double rmid = trunc((reward_max - reward_min) / 2);

        //BinaryTreeVals(reward_min, rmid, reward_max);

        int xdim = 4;
        int ydim = 3;

        double l = 0.0, r = -2.0, mid = 0.0;

        ArrayList<Double> allThresholds = new ArrayList();
        ArrayList<Double> finalThresholds = new ArrayList();

        ArrayList<Map<Cell<Double>, CellAction>> allActions = new ArrayList<Map<Cell<Double>, CellAction>>();
        Stack<Double> stackHolder = new Stack<Double>();
        stackHolder.push(r);
        stackHolder.push(l);
        Map<Double, Map<Cell<Double>, CellAction>> actionMap = new LinkedHashMap<Double, Map<Cell<Double>, CellAction>>();

        while (stackHolder.empty() == false) {


            l = stackHolder.pop();
            r = stackHolder.pop();

            boolean leftFound = false, rightFound = false;

            if (Math.abs(r - l) > 0.0001) {

                mid = (l + r) / 2.0;

                Map<Cell<Double>, CellAction> optimalActionl = GetOptimalAction(xdim, ydim, l);
                Map<Cell<Double>, CellAction> optimalActionm = GetOptimalAction(xdim, ydim, mid);
                Map<Cell<Double>, CellAction> optimalActionr = GetOptimalAction(xdim, ydim, r);

                if (compareMaps(optimalActionr, optimalActionm) == false) {
                    stackHolder.push(r);
                    stackHolder.push(mid);
                    leftFound = true;
                }

                if (compareMaps(optimalActionl, optimalActionm) == false) {
                    stackHolder.push(mid);
                    stackHolder.push(l);
                    // allThresholds.add(mid);
                    rightFound = true;
                }

                if (leftFound || rightFound) {
                    allThresholds.add(mid);
                    actionMap.put(mid, optimalActionm);
                }
            }
        }


        //   allThresholds.add(0.0);
        // actionMap.put(0.0, GetOptimalAction(xdim, ydim, 0.0));
        Collections.sort(allThresholds);

        int t = 0;
        for (int i = allThresholds.size() - 1; i > 0; i--) {
            Map<Cell<Double>, CellAction> c1 = actionMap.get(allThresholds.get(i));
            Map<Cell<Double>, CellAction> c2 = actionMap.get(allThresholds.get(i - 1));
            if (compareMaps(c1, c2) == false) {
                t = i;
                finalThresholds.add(allThresholds.get(i));
            }
        }

        finalThresholds.add(allThresholds.get(t - 1));


        //  Collections.sort(finalThresholds);

        try {
            // Create file 
            FileWriter fstream = new FileWriter("P1-output.txt");
            BufferedWriter out = new BufferedWriter(fstream);

            //first threshold
            // out.write("R(s)=0.0\n");
            //out.write(System.getProperty("line.separator"));

            // printOptimal(xdim, ydim, actionMap.get(0.0), null, out);
            Map<Cell<Double>, CellAction> t1 = actionMap.get(finalThresholds.get(1));
            Map<Cell<Double>, CellAction> t2;
//            
//            out.write(finalThresholds.get(0).toString() + " > R(s) > " + finalThresholds.get(1).toString() + "\n");
//            out.write(System.getProperty("line.separator"));
//
//           // t2 = actionMap.get(finalThresholds.get(i + 1));
//            printOptimal(xdim, ydim, t1, null, out);
//            printOptimal(xdim, ydim, t1, null, out);


            String lb = "0.0";

            for (int i = 0; i < finalThresholds.size(); i++) {
                String ub = finalThresholds.get(i).toString();
                ub = ub.substring(0,8);
                if (i != finalThresholds.size() - 1) {
                    out.write(lb + " > R(s) > " + ub + "\n");
                } else {
                    out.write("INF > R(s) > " + lb + "\n");
                }
                out.write(System.getProperty("line.separator"));


                t2 = actionMap.get(finalThresholds.get(i));
                if (i == 0) {
                    printOptimal(xdim, ydim, t2, null, out);
                } else {
                    printOptimal(xdim, ydim, t2, t1, out);
                }

                t1 = t2;
                lb = ub;
            }

//            out.write("R(s) > " + finalThresholds.get(finalThresholds.size()-1).toString() + "\n");           //Close the output stream
//            out.write(System.getProperty("line.separator"));
//
//            t2 = actionMap.get(finalThresholds.get(finalThresholds.size()-2));
//            printOptimal(xdim, ydim, t1, t2, out);
            out.close();

        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
        // System.out.println(finalThresholds.size());
    }

    private static CellAction getCellAction(int i, int j, Map<Cell<Double>, CellAction> mp) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            Cell<Double> c = (Cell<Double>) pairs.getKey();
            if (c.getX() == i && c.getY() == j) {
                return (CellAction) pairs.getValue();
            }
            //System.out.println(pairs.getKey() + " = " + pairs.getValue());
            //it.remove(); // avoids a ConcurrentModificationException
        }
        return null;
    }

    private static void printOptimal(int xdim, int ydim, Map<Cell<Double>, CellAction> optimalAction, Map<Cell<Double>, CellAction> optimalAction1, BufferedWriter out) {
        try {
            //  List<CellAction> values1 = new ArrayList<CellAction>(optimalAction.values());
            //   List<CellAction> values2 = new ArrayList<CellAction>(optimalAction1.values());
            //Iterate through each cell and get the optimal policy for that cell(state)
            for (int j = ydim; j > 0; j--) {
                for (int i = 1; i <= xdim; i++) {
                    Cell<Double> c = table.getCellAt(i, j);

                    String toWrite = "";
                    if (c != null) {
                        CellAction ca = getCellAction(i, j, optimalAction);

                        if (ca != null) {
                            toWrite = ca.toString();
                        }
                        if (optimalAction1 != null) {
                            CellAction ca1 = getCellAction(i, j, optimalAction1);
                            if (ca != null && ca1 != null) {
                                if (ca1.equals(ca) == false) {
                                    toWrite = ca.toString() + "*";
                                }
                            }
                        }

                        if (ca != null) {

                            out.write(toWrite + "\t");

                        } else {
                            out.write(c.getContent().toString() + "\t");
                        }
                    } else {
                        out.write("X\t");
                    }
                }

                out.write(System.getProperty("line.separator"));

            }
            out.write(System.getProperty("line.separator"));
            out.write(System.getProperty("line.separator"));

        } catch (IOException ex) {
            Logger.getLogger(A3.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static double trunc(double d) {
        //d= Math.ceil(d);
        double dl = (double) (d * 1);
        dl = Math.ceil(dl);
        double n = (double) (dl / 1);//0.0);
        long nl = (long) (n);
        return (double) (nl / 1.0);
    }

    private static <T> Table< T> createTable(Class<T> c, int xdim, int ydim, Object d) {
        Table<T> cw = new Table<T>(xdim, ydim, c.cast(d));
        return cw;
    }

    private static MarkovDecisionProcess<Cell<Double>, CellAction> createMDP(Table<Double> table) {

        return new MDP<Cell<Double>, CellAction>(table.getCells(),
                table.getCellAt(1, 1), createActionsFunction(table),
                createTransitionProbability(table),
                createRewardFunction());
    }

    private static ActionsFunction<Cell<Double>, CellAction> createActionsFunction(
            final Table<Double> cw) {
        final Set<Cell<Double>> terminals = new HashSet<Cell<Double>>();
        terminals.add(cw.getCellAt(4, 3));
        terminals.add(cw.getCellAt(4, 2));

        ActionsFunction<Cell<Double>, CellAction> af = new ActionsFunction<Cell<Double>, CellAction>() {

            @Override
            public Set<CellAction> actions(Cell<Double> s) {
                // All actions can be performed in each cell
                // (except terminal states)
                if (terminals.contains(s)) {
                    return Collections.emptySet();
                }
                return CellAction.actions();
            }
        };
        return af;
    }

    public static TransitionProbabilityFunction<Cell<Double>, CellAction> createTransitionProbability(
            final Table<Double> cw) {
        TransitionProbabilityFunction<Cell<Double>, CellAction> tf = new TransitionProbabilityFunction<Cell<Double>, CellAction>() {

            private double[] distribution = new double[]{0.8, 0.1, 0.1};

            @Override
            public double probability(Cell<Double> sDelta, Cell<Double> s,
                    CellAction a) {
                double prob = 0;

                List<Cell<Double>> outcomes = possibleOutcomes(s, a);
                for (int i = 0; i < outcomes.size(); i++) {
                    if (sDelta.equals(outcomes.get(i))) {
                        // Note: You have to sum the matches to
                        // sDelta as the different actions
                        // could have the same effect (i.e.
                        // staying in place due to there being
                        // no adjacent cells), which increases
                        // the probability of the transition for
                        // that state.
                        prob += distribution[i];
                    }
                }

                return prob;
            }

            private List<Cell<Double>> possibleOutcomes(Cell<Double> c,
                    CellAction a) {
                // There can be three possible outcomes for the planned action
                List<Cell<Double>> outcomes = new ArrayList<Cell<Double>>();

                outcomes.add(cw.result(c, a));
                outcomes.add(cw.result(c, a.getFirstRightAngledAction()));
                outcomes.add(cw.result(c, a.getSecondRightAngledAction()));

                return outcomes;
            }
        };

        return tf;
    }

    /**
     * 
     * @return the reward function which takes the content of the cell as being
     *         the reward value.
     */
    public static RewardFunction<Cell<Double>> createRewardFunction() {
        RewardFunction<Cell<Double>> rf = new RewardFunction<Cell<Double>>() {

            @Override
            public double reward(Cell<Double> s) {
                return s.getContent();
            }
        };
        return rf;
    }
}
