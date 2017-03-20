import java.lang.Math.*;
import java.util.*;

public class Testing {
    public static void main(String[] args) {
        /* String[] expressions = {"sin(x)*cos(x)*e^(x)", "(2+x)*cos(x)", "euler(cos(x*x))"};
        String[] input = {"mult(sin(x),mult(cos(x),exp(x)))", "mult(add(2,x),cos(x))", "exp(cos(mult(x,x)))"};
        String[] derivatives = {"(0.5)*euler(x)*(sin(2*x)+2*cos(2*x))", "cos(x)-(x+2)*sin(x)", "-2*x*sin(x*x)*euler(cos(x*x))"};
        

        for(int p = 0; p < expressions.length; p++){
            System.out.println("Expression " + (p+1) + ": " + expressions[p]);
            System.out.println("Differentiated: " + (p+1) + ": " + derivatives[p]);
            //double calculated, answer, x;
            //double diffCalc, diffAnswer;
        
            boolean forEval = true;
            boolean forDiff = true;
            for(int i = 0; i < 30; i++) {
                double x = Math.random() * 25;
                ExpressionTree f = new ExpressionTree(input[p]);
                ExpressionTree fdiff = f.differentiate();

                double answer = f.evaluate(x);
                double diffAnswer = fdiff.evaluate(x);
                
                double calculated = 0;
                double diffCalc = 0;
                if (p == 0) {
                    calculated = Math.sin(x) * Math.cos(x) * Math.exp(x);
                    diffCalc = (0.5 * Math.exp(x)) * (Math.sin(2*x) + 2*Math.cos(2*x));
                }
                else if (p == 1) {
                    calculated = (2+x)*Math.cos(x);
                    diffCalc = Math.cos(x) - ((x+2)*Math.sin(x));
                }
                else if (p == 2) {
                    calculated = Math.exp(Math.cos(x*x));
                    diffCalc = Math.exp(Math.cos(x*x))*(-Math.sin(x*x))*(2*x);
                } */

                
                /* int boozoo = 0;
                if (i == 20) {
                    boozoo = 2350;
                } */
                /* System.out.println(i+1 + ".");
                System.out.println("x = " + x);
                System.out.println("A, C \t\t" + answer + "\t" + calculated);
                System.out.println("DiffA, DiffC \t" + diffAnswer + "\t" + diffCalc);
                if (Math.abs(answer - calculated) > 0.001*Math.abs(answer)) {
                    //System.out.println("\n**Evaluate is good**");
                    System.out.println("Evaluate is not good!");
                    return;
                }
                if (Math.abs(diffAnswer - diffCalc) > 0.001*Math.abs(diffAnswer)) {
                    //System.out.println("**Differentiate is good**\n");
                    System.out.println("Differentiate is not good!");
                    return;
                }
                System.out.println("-------------------------------------------");
            }
            System.out.println("We were able to reach the end!\n\n");
        }
        System.out.println("Hooray! We are done!"); */

        Scanner reader = new Scanner(System.in);
        ExpressionTree f = new ExpressionTree(reader.next()); //6(5(8(2,6(3,2)),4),1) 6(1,5(4,8(6(3,2),2)))
        ExpressionTree g = new ExpressionTree(reader.next()); 
        
        System.out.println("\n" + isomorphic(f,g));

        //ExpressionTree traversal = new ExpressionTree("7(3(2(1,42),6),41(8(43),9))");
        ExpressionTree traversal = new ExpressionTree("7(3(2(1,2),6),9(8(7),9))");
        //weirdPreOrder(traversal);
        //weirdPostOrder(traversal);
        //queueTraversal(traversal);
        stackTraversal(traversal);


    }

    public static boolean isomorphic(ExpressionTree f, ExpressionTree g) {
        System.out.println();
        System.out.println(f);
        System.out.println(g);
        
        /* if one of the nodes doesn't have any
            children, we compare the values of
            those two nodes */
        if (f.getLeftChild() == null || g.getLeftChild() == null) {
            if(!f.getValue().equals(g.getValue())) {
                //System.out.println("\nUnequal at top");
                return false;
            }
            else {
                return true;
            }
        }

        
        boolean oneLevelF = false;
        if (f.getLeftChild().getLeftChild() == null) {
            oneLevelF = true;
        }

        boolean oneLevelG = false;
        if (g.getLeftChild().getLeftChild() == null) {
            oneLevelG = true;
        }

        /* if the trees are of the form
                    k1             k4
                   /  \           /  \
                  k2  k3         k5  k6 

            where k1, k2 ... k6 are numbers */

        if(oneLevelF && oneLevelG) {
            
            String fLeft = f.getLeftChild().getValue();
            String fRight = f.getRightChild().getValue();

            String gLeft = g.getLeftChild().getValue();
            String gRight = g.getRightChild().getValue();

            boolean switched = fLeft.equals(gRight) && fRight.equals(gLeft);
            boolean same = fLeft.equals(gLeft) && fRight.equals(gRight);
            if(!(switched || same)) {
                //System.out.println("\nNeither same nor switched at a level");
                return false;
            }
        }
        
        else {
            ExpressionTree fLeftBranch = f.getLeftChild().deepCopy();
            ExpressionTree gLeftBranch = g.getLeftChild().deepCopy();

            ExpressionTree fRightBranch = f.getRightChild().deepCopy();
            ExpressionTree gRightBranch = g.getRightChild().deepCopy();

            if (!(isomorphic(fLeftBranch, gLeftBranch) || isomorphic(fLeftBranch, gRightBranch))) {
                //System.out.println("Left branches not isomorphic!");
                return false;
            }
            

            if(!(isomorphic(fRightBranch, gRightBranch) || isomorphic(fRightBranch, gLeftBranch))) {
                //System.out.println("Right branches not isomorphic!");
                return false;
            }
        }

        /* before exiting, we check if the roots of
            the entire trees are the same or not */
        if(!f.getValue().equals(g.getValue())) {
                return false;
        }
        return true;
    }

    public static void weirdPreOrder(ExpressionTree n) {
        if(n != null) {
            System.out.print(n.getValue() + " ");
            weirdPreOrder(n.getRightChild());
            weirdPostOrder(n.getLeftChild());
        }
    }

    public static void weirdPostOrder(ExpressionTree n) {
        if(n != null) {
            weirdPreOrder(n.getRightChild());
            weirdPostOrder(n.getLeftChild());
            System.out.print(n.getValue() + " ");
        }
    }

    public static void queueTraversal(ExpressionTree n) {
        LinkedList<ExpressionTree> q = new LinkedList<ExpressionTree>();
        q.addLast(n);
        while(!q.isEmpty()) {
            ExpressionTree x = q.pollFirst();
            System.out.print(x.getValue() + " ");
            if(x.getLeftChild() != null) {
                q.addLast(x.getLeftChild());
            }
            if(x.getRightChild() != null) {
                q.addLast(x.getRightChild());
            }
        }
    }

    public static void stackTraversal(ExpressionTree n) {
        Stack<ExpressionTree> s = new Stack<ExpressionTree>();
        s.push(n);
        while(!s.empty()) {
            ExpressionTree x = s.pop();
            System.out.print(x.getValue() + " ");
            if (x.getRightChild() != null) {
                s.push(x.getRightChild());
            }
            if (x.getLeftChild() != null) {
                s.push(x.getLeftChild());
            }
        }
    }
}