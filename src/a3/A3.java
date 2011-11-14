/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package a3;

/**
 *
 * @author Vivek
 */
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;


public class A3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        //Problem1
        runProblem1();
    }

    private static void runProblem1() {

        double reward_min = 1;
        double reward_max = 10;
        double rmid = trunc((reward_max - reward_min) / 2);

        ArrayList<Double> rewardVals = new ArrayList<Double>();
        BinaryTreeVals(reward_min, rmid, reward_max, rewardVals);

        int xdim = 4;
        int ydim = 3;

        //Create Table
        Table<Double> table = createTable(Double.class, xdim, ydim, -0.04);

        table.removeCell(2, 2);
        table.getCellAt(4, 3).setContent(1.0);
        table.getCellAt(4, 2).setContent(-1.0);

        MarkovDecisionProcess<Cell<Double>, CellAction> mdp = createMDP(table);
        ValueIteration<Cell<Double>, CellAction> vi = new ValueIteration<Cell<Double>, CellAction>(
                1.0);

        Map<Cell<Double>, CellAction> optimalAction = new LinkedHashMap<Cell<Double>, CellAction>();

        Map<Cell<Double>, Double> U = vi.valueIteration(mdp, 0.0001, optimalAction);


        //Iterate through each cell and get the optimal policy for that cell(state)
        for (int j = ydim; j > 0; j--) {
            for (int i = 1; i <= xdim; i++) {
                Cell<Double> c = table.getCellAt(i, j);

                if (c != null) {
                    CellAction ca = optimalAction.get(c);

                    if (ca != null) {
                        System.out.print(ca.toString() + "\t");
                    } else {
                        System.out.print(c.getContent().toString() + "\t");
                    }
                } else {
                    System.out.print("X\t");
                }
            }

            System.out.println();
        }

        System.out.println();
    }

    private static double trunc(double d) {
        //d= Math.ceil(d);
        double dl = (double) (d * 1);
        dl = Math.ceil(dl);
        double n = (double) (dl / 1);//0.0);
        long nl = (long)(n);
        return (double)(nl/1.0);
    }

    private static void BinaryTreeVals(Double l, Double mid, Double r, ArrayList<Double> v) {

        l = trunc(l);
        r = trunc(r);
        mid = trunc(mid);

        if (mid.compareTo(l) <= 0 || mid.compareTo(r) >= 0) {
            return;
        }


        r = mid;
        mid = (r - l) / 2;
        BinaryTreeVals(l, mid, r, v);
        v.add(r);

        l = mid;
        mid = l + (r - l) / 2;
        BinaryTreeVals(l, mid, r, v);

        v.add(l);

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
