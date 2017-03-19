import java.lang.Math.*;

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

        ExpressionTree f = new ExpressionTree("5(8(2,6(3,2)),4)");
        ExpressionTree g = new ExpressionTree("5(4,8(6(3,2),2))");
        
        System.out.println("\n" + isomorphic(f,g));

    }

    public static boolean isomorphic(ExpressionTree f, ExpressionTree g) {
        System.out.println(f);
        System.out.println(g);
        
        if (f.getLeftChild() == null && f.getRightChild() == null && g.getLeftChild() == null && g.getRightChild() == null) {
            if(!f.getValue().equals(g.getValue())) {
                System.out.println("\nUnequal at top");
                return false;
            }
        }
        boolean oneLevelF = false;
        if (f.getLeftChild().getLeftChild() == null && f.getRightChild().getLeftChild() == null) {
            oneLevelF = true;
        }

        boolean oneLevelG = false;
        if (g.getLeftChild().getLeftChild() == null && g.getRightChild().getLeftChild() == null) {
            oneLevelG = true;
        }
        if(oneLevelF && oneLevelG) {

            String fLeft = f.getLeftChild().getValue();
            String fRight = f.getRightChild().getValue();

            String gLeft = g.getLeftChild().getValue();
            String gRight = g.getRightChild().getValue();

            boolean switched = fLeft.equals(gRight) && fRight.equals(gLeft);
            boolean same = fLeft.equals(gLeft) && fRight.equals(gRight);
            if(!(switched || same)) {
                System.out.println("\nNot same/switched at a level");
                return false;
            }
        }
        
        else {
            ExpressionTree fLeftBranch = f.getLeftChild().deepCopy();
            ExpressionTree gLeftBranch = g.getLeftChild().deepCopy();

            if (!isomorphic(fLeftBranch, gLeftBranch)) {
                System.out.println("Left branches not isomorphic!");
                return false;
            }
            ExpressionTree fRightBranch = f.getRightChild().deepCopy();
            ExpressionTree gRightBranch = g.getRightChild().deepCopy();

            if(!isomorphic(fRightBranch, gRightBranch)) {
                System.out.println("Right branches not isomorphic!");
                return false;
            }
        }

        return true;
    }
}