/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author 
 */
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Stack;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class A3Main {

    private static Table<Double> table;
    private static Map<String, String> printactionMap;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        printactionMap = new LinkedHashMap<String, String>();

        printactionMap.put("Up", "^");
        printactionMap.put("Down", "v");
        printactionMap.put("Left", "<-");
        printactionMap.put("Right", "->");
        printactionMap.put("None", "x");


        // TODO code application logic here

        //Problem1
        System.out.println("Running problem 1");
        runProblem1();
        try {
            System.out.println("Running problem 2");

            runProblem2();
        } catch (IOException ex) {
            Logger.getLogger(A3Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Problem3
        System.out.println("Running problem 3");

        runProblem3();

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

        Stack<Double> stackHolder = new Stack<Double>();
        stackHolder.push(r);
        stackHolder.push(l);
        Map<Double, Map<Block<Double>, BlockAction>> actionMap = new LinkedHashMap<Double, Map<Block<Double>, BlockAction>>();

        // GetThreshold(l, r, allThresholds, actionMap);

        while (stackHolder.empty() == false) {


            l = stackHolder.pop();
            r = stackHolder.pop();

            boolean leftFound = false, rightFound = false;

            if (Math.abs(r - l) > 0.0001) {

                mid = (l + r) / 2.0;

                Map<Block<Double>, BlockAction> optimalActionl = GetOptimalAction(xdim, ydim, l);
                Map<Block<Double>, BlockAction> optimalActionm = GetOptimalAction(xdim, ydim, mid);
                Map<Block<Double>, BlockAction> optimalActionr = GetOptimalAction(xdim, ydim, r);

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
            Map<Block<Double>, BlockAction> c1 = actionMap.get(allThresholds.get(i));
            Map<Block<Double>, BlockAction> c2 = actionMap.get(allThresholds.get(i - 1));
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

            out.write("For action Up from Cell 3,1, the two other actions that are possible are left which takes it to cell 2,1 or right which takes it to cell 4,1");
            out.write(System.getProperty("line.separator"));
            out.write("Now even though the utility at 3,2 is higher, the  expected utility is product of the transition probabilty and the utility at that state");
            out.write(System.getProperty("line.separator"));
            out.write("So for action Up, the expected utility is 0.8*0.660+0.1*0.387+0.1*0.655 which is equal to 0.6322");
            out.write(System.getProperty("line.separator"));
            out.write("For action left, it is 0.8*0.655 + 0.1*0.660 + 0.1* 0.611 which is equal to 0.6511");
            out.write(System.getProperty("line.separator"));
            out.write("For action left expected utility is higher, so the opimal policy says to move left");
            out.write(System.getProperty("line.separator"));
            out.write(System.getProperty("line.separator"));

            //first threshold
            // out.write("R(s)=0.0\n");
            //out.write(System.getProperty("line.separator"));

            // printOptimal(xdim, ydim, actionMap.get(0.0), null, out);
            Map<Block<Double>, BlockAction> t1 = actionMap.get(finalThresholds.get(1));
            Map<Block<Double>, BlockAction> t2;
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
                ub = roundString(ub);
                if (i != finalThresholds.size() - 1) {
                    out.write(lb + " > R(s) > " + ub + "\n");
                } else {
                    out.write("-INF > R(s) > " + lb + "\n");
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

    private static void runProblem2() throws IOException {
        table = createTable(Double.class, 4, 3, -0.04);

        table.removeCell(2, 2);
        table.getCellAt(4, 3).setContent(1.0);
        table.getCellAt(4, 2).setContent(-1.0);

        MarkovProc<Block<Double>, BlockAction> mdp = createMDP(table);
        VI<Block<Double>, BlockAction> vi = new VI<Block<Double>, BlockAction>(
                1.0);

        Map<Block<Double>, BlockAction> optimalAction = new LinkedHashMap<Block<Double>, BlockAction>();

        Map<Block<Double>, Double> U = vi.valueIteration(mdp, 0.0001, optimalAction);

        Map<Double, Integer> run10 = new LinkedHashMap();
        Map<Double, Integer> run100 = new LinkedHashMap();
        Map<Double, Integer> run1000 = new LinkedHashMap();

        int i = 0;

        ArrayList<Double> rewardsHolder = new ArrayList<Double>();
        ArrayList<Double> rewardsHolder1 = new ArrayList<Double>();
        ArrayList<Double> rewardsHolder2 = new ArrayList<Double>();

        Double firstRunReward = 0.0;

        while (i++ < 10) {

            Block<Double> start = table.getCellAt(4, 1);
            Block<Double> end1 = table.getCellAt(4, 3);
            Block<Double> end2 = table.getCellAt(4, 2);

            Block<Double> current = start;
            double rewards = 0.0;

            while (current.equals(end1) == false && current.equals(end2) == false) {


                BlockAction a = optimalAction.get(current);
                rewards += current.getContent();
                double r = Math.random() * 100;
                if (r > 20.0) {
                    current = table.result(current, a);
                } else if (r > 10.0 && r <= 20.0) {
                    current = table.result(current, a.getFirstRightAngledAction());
                } else {
                    current = table.result(current, a.getSecondRightAngledAction());
                }

            }

            rewards += current.getContent();

            if (i == 1) {
                firstRunReward = rewards;
            }

            if (run10.containsKey(rewards)) {
                Integer k = run10.get(rewards);
                k++;
                run10.put(rewards, k);
                rewardsHolder.add(rewards);
            } else {
                run10.put(rewards, 1);
                rewardsHolder.add(rewards);

            }

        }

        i = 0;

        while (i++ < 100) {

            Block<Double> start = table.getCellAt(4, 1);
            Block<Double> end1 = table.getCellAt(4, 3);
            Block<Double> end2 = table.getCellAt(4, 2);

            Block<Double> current = start;
            double rewards = 0.0;

            while (current != end1 && current != end2) {

                BlockAction a = optimalAction.get(current);
                rewards += current.getContent();
                double r = Math.random() * 100;
                if (r > 20.0) {
                    current = table.result(current, a);
                } else if (r > 10.0 && r <= 20.0) {
                    current = table.result(current, a.getFirstRightAngledAction());
                } else {
                    current = table.result(current, a.getSecondRightAngledAction());
                }
            }

            rewards += current.getContent();

            if (run100.containsKey(rewards)) {
                Integer k = run100.get(rewards);
                k++;
                run100.put(rewards, k);
                rewardsHolder1.add(rewards);

            } else {
                run100.put(rewards, 1);
                rewardsHolder1.add(rewards);
            }
        }

        i = 0;

        while (i++ < 1000) {

            Block<Double> start = table.getCellAt(4, 1);
            Block<Double> end1 = table.getCellAt(4, 3);
            Block<Double> end2 = table.getCellAt(4, 2);

            Block<Double> current = start;
            double rewards = 0.0;

            while (current != end1 && current != end2) {

                BlockAction a = optimalAction.get(current);
                rewards += current.getContent();
                double r = Math.random() * 100;
                if (r > 20.0) {
                    current = table.result(current, a);
                } else if (r > 10.0 && r <= 20.0) {
                    current = table.result(current, a.getFirstRightAngledAction());
                } else {
                    current = table.result(current, a.getSecondRightAngledAction());
                }

            }

            rewards += current.getContent();

            if (run1000.containsKey(rewards)) {
                Integer k = run1000.get(rewards);
                k++;
                run1000.put(rewards, k);
                rewardsHolder2.add(rewards);
            } else {
                run1000.put(rewards, 1);
                rewardsHolder2.add(rewards);
            }

        }

        Block<Double> start = table.getCellAt(4, 1);
        Block<Double> end1 = table.getCellAt(4, 3);
        Block<Double> end2 = table.getCellAt(4, 2);

        Block<Double> current = start;
        Double exprewards = 0.0;

        while (current != end1 && current != end2) {

            BlockAction a = optimalAction.get(current);
            exprewards += current.getContent();

            current = table.result(current, a);

        }

        exprewards += current.getContent();

        FileWriter fstream = new FileWriter("P2-histogram.txt");
        BufferedWriter out = new BufferedWriter(fstream);

        out.write("10 runs");
        out.write(System.getProperty("line.separator"));

        TreeSet<Double> keys = new TreeSet<Double>(run10.keySet());
        for (Double rewards : keys) {
            Integer count = run10.get(rewards);
            // do something

            String rewstr = rewards.toString();
            if (rewards.toString().length() > 8) {
                rewstr = rewstr.substring(0, 7);
            }
            out.write(rewstr + "\t\t" + count.toString());
            out.write(System.getProperty("line.separator"));

            //System.out.println(pairs.getKey() + " = " + pairs.getValue());
            //it.remove(); // avoids a ConcurrentModificationException
        }

        out.write("100 runs");
        out.write(System.getProperty("line.separator"));


        keys = new TreeSet<Double>(run100.keySet());
        for (Double rewards : keys) {
            Integer count = run100.get(rewards);
            // do something

            String rewstr = rewards.toString();
            if (rewards.toString().length() > 8) {
                rewstr = rewstr.substring(0, 7);
            }
            out.write(rewstr + "\t\t" + count.toString());
            out.write(System.getProperty("line.separator"));

            //System.out.println(pairs.getKey() + " = " + pairs.getValue());
            //it.remove(); // avoids a ConcurrentModificationException
        }

        out.write("1000 runs");
        out.write(System.getProperty("line.separator"));


         keys = new TreeSet<Double>(run1000.keySet());
        for (Double rewards : keys) {
            Integer count = run1000.get(rewards);
            // do something

            String rewstr = rewards.toString();
            if (rewards.toString().length() > 8) {
                rewstr = rewstr.substring(0, 7);
            }
            out.write(rewstr + "\t\t" + count.toString());
            out.write(System.getProperty("line.separator"));

            //System.out.println(pairs.getKey() + " = " + pairs.getValue());
            //it.remove(); // avoids a ConcurrentModificationException
        }

        out.close();

        FileWriter fstream1 = new FileWriter("P2-output.txt");
        BufferedWriter out1 = new BufferedWriter(fstream1);
        out1.write("Optimum utility when  -0.04");
        out1.write(System.getProperty("line.separator"));

        printUtil(4, 3, U, out1);

        out1.write(System.getProperty("line.separator"));
        out1.write("______________________________________________________");


        out1.write(System.getProperty("line.separator"));

        out1.write("First Run reward is " + roundString(firstRunReward.toString()));
        out1.write(System.getProperty("line.separator"));

        out1.write("Mean for 10 runs :");
        //Set<Double> rewVec = (Set<Double>) run10.keySet();
        Double meanval = FindMean(rewardsHolder);
        out1.write(roundString(meanval.toString()));
        out1.write(System.getProperty("line.separator"));

        out1.write("SD for 10 runs: ");
        Double sdVal = FindSD(rewardsHolder, meanval);

        out1.write(roundString(sdVal.toString()));
        out1.write(System.getProperty("line.separator"));

        out1.write("Mean for 100 runs :");
        //rewVec = (Set<Double>) run100.keySet();
        meanval = FindMean(rewardsHolder1);
        out1.write(roundString(meanval.toString()));
        out1.write(System.getProperty("line.separator"));

        out1.write("SD for 100 runs: ");
        sdVal = FindSD(rewardsHolder1, meanval);

        out1.write(roundString(sdVal.toString()));
        out1.write(System.getProperty("line.separator"));

        out1.write("Mean for 1000 runs :");
        //rewVec = (Set<Double>) run1000.keySet();
        meanval = FindMean(rewardsHolder2);
        out1.write(roundString(meanval.toString()));
        out1.write(System.getProperty("line.separator"));

        out1.write("SD for 1000 runs: ");
        sdVal = FindSD(rewardsHolder2, meanval);

        out1.write(roundString(sdVal.toString()));
        out1.write(System.getProperty("line.separator"));
        out1.write(System.getProperty("line.separator"));

        out1.write("Expected reward from start state: ");
        out1.write(roundString(exprewards.toString()));
        out1.close();

    }

    private static String roundString(String s) {
        if (s.length() > 8) {
            return s.substring(0, 7);
        }
        return s;
    }

    private static void runProblem3() {
        table = createTable(Double.class, 3, 3, -1.0);
        table.getCellAt(1, 3).setContent(3.0);
        table.getCellAt(3, 3).setContent(10.0);



        double l = 0.01, r = 0.998, mid = 0.0;

        ArrayList<Double> allThresholds = new ArrayList();
        ArrayList<Double> finalThresholds = new ArrayList();

        ArrayList<Map<Block<Double>, BlockAction>> allActions = new ArrayList<Map<Block<Double>, BlockAction>>();
        Stack<Double> stackHolder = new Stack<Double>();
        stackHolder.push(r);
        stackHolder.push(l);
        Map<Double, Map<Block<Double>, BlockAction>> actionMap = new LinkedHashMap<Double, Map<Block<Double>, BlockAction>>();
        Map<Double, Map<Block<Double>, Double>> utilMap = new LinkedHashMap<Double, Map<Block<Double>, Double>>();

        while (stackHolder.empty() == false) {


            l = stackHolder.pop();
            r = stackHolder.pop();

            boolean leftFound = false, rightFound = false;

            if (Math.abs(r - l) > 0.00001) {

                mid = (l + r) / 2.0;
                Map<Block<Double>, BlockAction> optimalActionl = new LinkedHashMap<Block<Double>, BlockAction>();
                Map<Block<Double>, BlockAction> optimalActionm = new LinkedHashMap<Block<Double>, BlockAction>();
                Map<Block<Double>, BlockAction> optimalActionr = new LinkedHashMap<Block<Double>, BlockAction>();


                Map<Block<Double>, Double> util1 = GetOptimalAction1(table, l, optimalActionl);
                Map<Block<Double>, Double> utilm = GetOptimalAction1(table, mid, optimalActionm);
                Map<Block<Double>, Double> utilr = GetOptimalAction1(table, r, optimalActionr);

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
                    utilMap.put(mid, utilm);
                }
            }
        }


        //   allThresholds.add(0.0);
        // actionMap.put(0.0, GetOptimalAction(xdim, ydim, 0.0));
        Collections.sort(allThresholds);

        int t = 0;
        for (int i = 0; i < allThresholds.size() - 1; i++) {
            Map<Block<Double>, BlockAction> c1 = actionMap.get(allThresholds.get(i));
            Map<Block<Double>, BlockAction> c2 = actionMap.get(allThresholds.get(i + 1));
            if (compareMaps(c1, c2) == false) {
                t = i;
                finalThresholds.add(allThresholds.get(i));
            }
        }

        finalThresholds.add(allThresholds.get(t + 1));

        try {
            // Create file 
            FileWriter fstream = new FileWriter("P3-output.txt");
            BufferedWriter out = new BufferedWriter(fstream);

            //first threshold
            // out.write("R(s)=0.0\n");
            //out.write(System.getProperty("line.separator"));

            // printOptimal(xdim, ydim, actionMap.get(0.0), null, out);
            Map<Block<Double>, BlockAction> t1 = actionMap.get(finalThresholds.get(1));
            Map<Block<Double>, BlockAction> t2;
//            
//            out.write(finalThresholds.get(0).toString() + " > R(s) > " + finalThresholds.get(1).toString() + "\n");
//            out.write(System.getProperty("line.separator"));
//
//           // t2 = actionMap.get(finalThresholds.get(i + 1));
//            printOptimal(xdim, ydim, t1, null, out);
//            printOptimal(xdim, ydim, t1, null, out);


            String lb = "0.0";
            Map<Block<Double>, Double> u1 = utilMap.get(finalThresholds.get(1));


            for (int i = 0; i < finalThresholds.size(); i++) {
                String ub = finalThresholds.get(i).toString();
                ub = roundString(ub);
                if (i != finalThresholds.size() - 1) {
                    out.write(lb + " > R(s) > " + ub + "\n");
                } else {
                    out.write("1 > R(s) > " + lb + "\n");
                }
                out.write(System.getProperty("line.separator"));
                out.write(System.getProperty("line.separator"));

                t2 = actionMap.get(finalThresholds.get(i));
                u1 = utilMap.get(finalThresholds.get(i));
                if (i == 0) {
                    printOptimal(3, 3, t2, null, out);
                    printUtil(3, 3, u1, out);
                } else {
                    printOptimal(3, 3, t2, t1, out);
                    printUtil(3, 3, u1, out);
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

    }

    private static Map<Block<Double>, BlockAction> GetOptimalAction(int xdim, int ydim, double r) {
        //Create Table
        table = createTable(Double.class, xdim, ydim, r);

        table.removeCell(2, 2);
        table.getCellAt(4, 3).setContent(1.0);
        table.getCellAt(4, 2).setContent(-1.0);

        MarkovProc<Block<Double>, BlockAction> mdp = createMDP(table);
        VI<Block<Double>, BlockAction> vi = new VI<Block<Double>, BlockAction>(
                1.0);

        Map<Block<Double>, BlockAction> optimalAction = new LinkedHashMap<Block<Double>, BlockAction>();

        Map<Block<Double>, Double> U = vi.valueIteration(mdp, 0.0001, optimalAction);

        return optimalAction;
    }

    private static boolean compareMaps(Map<Block<Double>, BlockAction> a, Map<Block<Double>, BlockAction> b) {
        List<BlockAction> values1 = new ArrayList<BlockAction>(a.values());
        List<BlockAction> values2 = new ArrayList<BlockAction>(b.values());
        //boolean equals = true;
        for (int i = 0; i < values1.size(); i++) {
            BlockAction a1 = values1.get(i);
            BlockAction b1 = values2.get(i);

            if (a1.equals(b1) == false) {

                return false;
            }
        }

        return true;
    }

    private static BlockAction getCellAction(int i, int j, Map<Block<Double>, BlockAction> mp) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            Block<Double> c = (Block<Double>) pairs.getKey();
            if (c.getX() == i && c.getY() == j) {
                return (BlockAction) pairs.getValue();
            }
            //System.out.println(pairs.getKey() + " = " + pairs.getValue());
            //it.remove(); // avoids a ConcurrentModificationException
        }
        return null;
    }

    private static Double getCellUtil(int i, int j, Map<Block<Double>, Double> mp) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            Block<Double> c = (Block<Double>) pairs.getKey();
            if (c.getX() == i && c.getY() == j) {
                return (Double) pairs.getValue();
            }
            //System.out.println(pairs.getKey() + " = " + pairs.getValue());
            //it.remove(); // avoids a ConcurrentModificationException
        }
        return null;
    }

    private static void printOptimal(int xdim, int ydim, Map<Block<Double>, BlockAction> optimalAction, Map<Block<Double>, BlockAction> optimalAction1, BufferedWriter out) {
        try {
            //  List<CellAction> values1 = new ArrayList<CellAction>(optimalAction.values());
            //   List<CellAction> values2 = new ArrayList<CellAction>(optimalAction1.values());
            //Iterate through each cell and get the optimal policy for that cell(state)
            for (int j = ydim; j > 0; j--) {
                for (int i = 1; i <= xdim; i++) {
                    Block<Double> c = table.getCellAt(i, j);

                    String toWrite = "";
                    if (c != null) {
                        BlockAction ca = getCellAction(i, j, optimalAction);

                        if (ca != null) {
                            toWrite = ca.toString();
                            toWrite = printactionMap.get(toWrite);
                        }
                        if (optimalAction1 != null) {
                            BlockAction ca1 = getCellAction(i, j, optimalAction1);
                            if (ca != null && ca1 != null) {
                                if (ca1.equals(ca) == false) {
                                    toWrite = toWrite + "*";
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
            Logger.getLogger(A3Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static <T> Table< T> createTable(Class<T> c, int xdim, int ydim, Object d) {
        Table<T> cw = new Table<T>(xdim, ydim, c.cast(d));
        return cw;
    }

    private static MarkovProc<Block<Double>, BlockAction> createMDP(Table<Double> table) {

        return new MDP<Block<Double>, BlockAction>(table.getCells(),
                table.getCellAt(1, 1), createActionsFunction(table),
                createTransitionProbability(table),
                createRewardFunction());
    }

    private static ActionSet<Block<Double>, BlockAction> createActionsFunction(
            final Table<Double> cw) {
        final Set<Block<Double>> terminals = new HashSet<Block<Double>>();
        terminals.add(cw.getCellAt(4, 3));
        terminals.add(cw.getCellAt(4, 2));

        ActionSet<Block<Double>, BlockAction> af = new ActionSet<Block<Double>, BlockAction>() {

            @Override
            public Set<BlockAction> actions(Block<Double> s) {
                // All actions can be performed in each cell
                // (except terminal states)
                if (terminals.contains(s)) {
                    return Collections.emptySet();
                }
                return BlockAction.actions();
            }
        };
        return af;
    }

    public static Probability<Block<Double>, BlockAction> createTransitionProbability(
            final Table<Double> cw) {
        Probability<Block<Double>, BlockAction> tf = new Probability<Block<Double>, BlockAction>() {

            private double[] distribution = new double[]{0.8, 0.1, 0.1};

            @Override
            public double probability(Block<Double> sDelta, Block<Double> s,
                    BlockAction a) {
                double prob = 0;

                List<Block<Double>> outcomes = possibleOutcomes(s, a);
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

            private List<Block<Double>> possibleOutcomes(Block<Double> c,
                    BlockAction a) {
                // There can be three possible outcomes for the planned action
                List<Block<Double>> outcomes = new ArrayList<Block<Double>>();

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
    public static Reward<Block<Double>> createRewardFunction() {
        Reward<Block<Double>> rf = new Reward<Block<Double>>() {

            @Override
            public double reward(Block<Double> s) {
                return s.getContent();
            }
        };
        return rf;
    }

    private static MarkovProc<Block<Double>, BlockAction> createMDP1(Table<Double> table) {

        return new MDP<Block<Double>, BlockAction>(table.getCells(),
                table.getCellAt(1, 1), createActionsFunction1(table),
                createTransitionProbability(table),
                createRewardFunction());
    }

    private static ActionSet<Block<Double>, BlockAction> createActionsFunction1(
            final Table<Double> cw) {
        final Set<Block<Double>> terminals = new HashSet<Block<Double>>();
        terminals.add(cw.getCellAt(3, 3));

        ActionSet<Block<Double>, BlockAction> af = new ActionSet<Block<Double>, BlockAction>() {

            @Override
            public Set<BlockAction> actions(Block<Double> s) {
                // All actions can be performed in each cell
                // (except terminal states)
                if (terminals.contains(s)) {
                    return Collections.emptySet();
                }
                return BlockAction.actions();
            }
        };
        return af;
    }

    private static Map<Block<Double>, Double> GetOptimalAction1(Table table, double mid, Map<Block<Double>, BlockAction> optimalAction) {
        MarkovProc<Block<Double>, BlockAction> mdp = createMDP1(table);
        VI<Block<Double>, BlockAction> vi = new VI<Block<Double>, BlockAction>(
                mid);


        Map<Block<Double>, Double> U = vi.valueIteration(mdp, 0.0001, optimalAction);
        return U;

    }

    private static void printUtil(int xdim, int ydim, Map<Block<Double>, Double> util, BufferedWriter out) {
        try {
            //  List<CellAction> values1 = new ArrayList<CellAction>(optimalAction.values());
            //   List<CellAction> values2 = new ArrayList<CellAction>(optimalAction1.values());
            //Iterate through each cell and get the optimal policy for that cell(state)
            for (int j = ydim; j > 0; j--) {
                for (int i = 1; i <= xdim; i++) {
                    Block<Double> c = table.getCellAt(i, j);

                    String toWrite = "";
                    if (c != null) {
                        Double ca = getCellUtil(i, j, util);

                        if (ca != null) {
                            if (ca.toString().length() > 8) {
                                toWrite = ca.toString().substring(0, 7);
                            } else {
                                toWrite = ca.toString();
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
            Logger.getLogger(A3Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static Double FindMean(ArrayList rewVec) {
        Double meanval = 0.0;
        Iterator it = rewVec.iterator();
        while (it.hasNext()) {
            // Get element
            Double element = (Double) it.next();
            meanval += element;
        }
        return meanval / rewVec.size();
    }

    private static Double FindSD(ArrayList rewVec, Double meanval) {
        double sd = 0.0;
        Iterator it = rewVec.iterator();
        while (it.hasNext()) {
            // Get element
            Double element = (Double) it.next();
            sd += Math.pow(element - meanval, 2.0);
        }

        return Math.sqrt(sd / rewVec.size());
    }

    private static void GetThreshold(double l, double r, ArrayList<Double> allThresholds, Map<Double, Map<Block<Double>, BlockAction>> actionMap) {
        if (Math.abs(r - l) > 0.0001) {

            double mid = (l + r) / 2.0;
            boolean leftFound = false, rightFound = false;

            Map<Block<Double>, BlockAction> optimalActionl = GetOptimalAction(4, 3, l);
            Map<Block<Double>, BlockAction> optimalActionm = GetOptimalAction(4, 3, mid);
            Map<Block<Double>, BlockAction> optimalActionr = GetOptimalAction(4, 3, r);

            if (compareMaps(optimalActionl, optimalActionm) == false) {

                r = mid;
                GetThreshold(l, r, allThresholds, actionMap);

                // allThresholds.add(mid);
                rightFound = true;
            }

            if (compareMaps(optimalActionr, optimalActionm) == false) {

                l = mid;
                GetThreshold(l, r, allThresholds, actionMap);
                leftFound = true;
            }



            if (leftFound || rightFound) {
                allThresholds.add(mid);
                actionMap.put(mid, optimalActionm);
            }
        }
    }
}
