import java.lang.Math.*;

class ExpressionTree {
    private String value;
    private ExpressionTree leftChild, rightChild, parent;
    
    ExpressionTree() {
        value = null; 
        leftChild = rightChild = parent = null;
    }
    
    // Constructor
    /* Arguments: String s: Value to be stored in the node
                  ExpressionTree l, r, p: the left child, right child, and parent of the node to created      
       Returns: the newly created ExpressionTree               
    */
    ExpressionTree(String s, ExpressionTree l, ExpressionTree r, ExpressionTree p) {
        value = s; 
        leftChild = l; 
        rightChild = r;
        parent = p;
    }
    
    /* Basic access methods */
    String getValue() { return value; }

    ExpressionTree getLeftChild() { return leftChild; }

    ExpressionTree getRightChild() { return rightChild; }

    ExpressionTree getParent() { return parent; }


    /* Basic setting methods */ 
    void setValue(String o) { value = o; }
    
    // sets the left child of this node to n
    void setLeftChild(ExpressionTree n) { 
        leftChild = n; 
        n.parent = this; 
    }
    
    // sets the right child of this node to n
    void setRightChild(ExpressionTree n) { 
        rightChild = n; 
        n.parent=this; 
    }
    

    // Returns the root of the tree describing the expression s
    // Watch out: it makes no validity checks whatsoever!
    ExpressionTree(String s) {
        // check if s contains parentheses. If it doesn't, then it's a leaf
        if (s.indexOf("(")==-1) setValue(s);
        else {  // it's not a leaf

            /* break the string into three parts: the operator, the left operand,
               and the right operand. ***/
            setValue( s.substring( 0 , s.indexOf( "(" ) ) );
            // delimit the left operand 2008
            int left = s.indexOf("(")+1;
            int i = left;
            int parCount = 0;
            // find the comma separating the two operands
            while (parCount>=0 && !(s.charAt(i)==',' && parCount==0)) {
                if ( s.charAt(i) == '(' ) parCount++;
                if ( s.charAt(i) == ')' ) parCount--;
                i++;
            }
            int mid=i;
            if (parCount<0) mid--;

        // recursively build the left subtree
            setLeftChild(new ExpressionTree(s.substring(left,mid)));
    
            if (parCount==0) {
                // it is a binary operator
                // find the end of the second operand.F13
                while ( ! (s.charAt(i) == ')' && parCount == 0 ) )  {
                    if ( s.charAt(i) == '(' ) parCount++;
                    if ( s.charAt(i) == ')' ) parCount--;
                    i++;
                }
                int right=i;
                setRightChild( new ExpressionTree( s.substring( mid + 1, right)));
        }
    }
    }


    // Returns a copy of the subtree rooted at this node... 2014
    ExpressionTree deepCopy() {
        ExpressionTree n = new ExpressionTree();
        n.setValue( getValue() );
        if ( getLeftChild()!=null ) n.setLeftChild( getLeftChild().deepCopy() );
        if ( getRightChild()!=null ) n.setRightChild( getRightChild().deepCopy() );
        return n;
    }
    
    // Returns a String describing the subtree rooted at a certain node.
    public String toString() {
        String ret = value;
        if ( getLeftChild() == null ) return ret;
        else ret = ret + "(" + getLeftChild().toString();
        if ( getRightChild() == null ) return ret + ")";
        else ret = ret + "," + getRightChild().toString();
        ret = ret + ")";
        return ret;
    } 


    // Returns the value of the expression rooted at a given node
    // when x has a certain value
    double evaluate(double x) {
	// WRITE YOUR CODE HERE
        
        /* we copy the string expression so
            that we can replace all the characters 'x'
            with the actual value of 'x'

            Note: in doing so, we end up changing 'exp'
            to e + value of 'x' + p, that is 'evaluep'

            therefore we use exampleString.charAt(0) == 'e'
            to see if the operation is exp or not */ 
        String expr = this.toString();
        String variable = x + "";
        String exprWithVal = expr.replaceAll("x", variable);
        ExpressionTree f = new ExpressionTree(exprWithVal);
        double answer = 0;
        String left, right, operation;
        double leftVal, rightVal;
        
        /* base case: the most fundamental expression - 
            this is for cases like 'x' or
            'c', where c is some constant */ 
        if(f.getLeftChild() == null && f.getRightChild() == null) {
            return Double.parseDouble(exprWithVal);
        }


        /* this is used for sin, cos and exp - 
                tree for sin      tree for cos      tree for exp
                    sin                 cos             exp
                     |                   |               |
                     k                   k               k

            where k is a number. 
            k has no children and sin,cos,exp don't have any right children
                    |                                          |
                    |                                          |
                    V                                          V        */  
        if (f.getLeftChild().getLeftChild() == null && f.getRightChild() == null) {
            left = f.getLeftChild().getValue();
            leftVal = Double.parseDouble(left);
            operation = f.getValue();

            if (operation.equals("sin")) {
                answer = Math.sin(leftVal);
            }
            else if (operation.equals("cos")) {
                answer = Math.cos(leftVal);
            }
            else if (operation.charAt(0) == 'e') {
                answer = Math.exp(leftVal);
            }
            return answer;
        }


        /* this is used for add, minus and mult - 
                tree for add        tree for minus      tree for mult
                    +                   -                   *
                  /   \               /   \               /   \
                 k1   k2             k1   k2             k1   k2
            
            where k1 and k2 are 2 numbers
            k1 and k2 have no children --------
                                              |
                                              V */
        else if (f.getLeftChild().getLeftChild() == null && f.getRightChild().getLeftChild() == null) {
            left = f.getLeftChild().getValue();
            leftVal = Double.parseDouble(left);
            right = f.getRightChild().getValue();
            rightVal = Double.parseDouble(right);

            operation = f.getValue();

            if(operation.equals("add")) {
                answer = leftVal + rightVal;
            }
            else if (operation.equals("minus")) {
                answer = leftVal - rightVal;
            }
            else if (operation.equals("mult")) {
                answer = leftVal * rightVal;
            }

	        return answer;
        }

        
        /* this big else block forms 
            the recursive part */
        else {
            double leftBranchVal = 0;
            double rightBranchVal = 0;

            // EVALUATING LEFT BRANCH

            /* if the child has no children, then
                that means that it's a number */
            if(f.getLeftChild().getLeftChild() == null) {
                leftBranchVal = Double.parseDouble(f.getLeftChild().getValue());
            }
            else {
                leftBranchVal = f.getLeftChild().deepCopy().evaluate(x);
                ExpressionTree leftCalc = new ExpressionTree("" + leftBranchVal);
                f.setLeftChild(leftCalc);
                String toDo = f.getValue();
                if (toDo.equals("cos")) {
                    return Math.cos(leftBranchVal);
                }
                else if (toDo.equals("sin")) {
                    return Math.sin(leftBranchVal);
                }
                else if (toDo.charAt(0) == 'e') {
                    return Math.exp(leftBranchVal);
                }
            }

            // EVALUATING RIGHT BRANCH

            /* if the child has no children, then
                that means that it's a number */
            if (f.getRightChild().getLeftChild() == null) {
                rightBranchVal = Double.parseDouble(f.getRightChild().getValue());
            }
            else {
                rightBranchVal = f.getRightChild().deepCopy().evaluate(x);
                ExpressionTree rightCalc = new ExpressionTree("" + rightBranchVal);
                f.setRightChild(rightCalc);
            }

            return f.evaluate(x);
        }
        
    }                                                 

    /* returns the root of a new expression tree representing the derivative of the
       original expression */
    ExpressionTree differentiate() {
	// WRITE YOUR CODE HERE
        ExpressionTree diff = this.deepCopy();

        /* this forms the base case - 
                tree for x              differentiated tree for x
                    x                               1

                tree for c              differentiated tree for c
                    c                               0

            where c is a constant */
        
        // the node has no children
        if (diff.getLeftChild() == null && diff.getRightChild() == null) {
            String baseVal = diff.getValue();
        
            if (baseVal.equals("x")) {
                diff.setValue("1");
                return diff;
            }

            else {
                try{
                    /* if the string is parsable to a Double
                        that means we have a number stored
                        in the string */
                    double value = Double.parseDouble(baseVal);
                    diff.setValue("0");
                    return diff;
                }
                catch(NumberFormatException e) {
                    return diff;
                }
            }

        }


        /* this is the case if we are using add or minus - 
                tree for add            differentiated tree for add
                    +                               +
                  /   \                           /   \
                 c   f(x)                       (c)'  f'(x)

                tree for minus          differentiated tree for minus
                    -                               -
                  /   \                           /   \
                 c   f(x)                       (c)'  f'(x)

            where f'(x) denotes differentiation of f(x)
                  c is a constant
                  (c)' denotes differentiation of c */

        if(diff.getValue().equals("add") || diff.getValue().equals("minus")) {
            ExpressionTree left = diff.getLeftChild().differentiate();
            diff.setLeftChild(left);
            ExpressionTree right = diff.getRightChild().differentiate();
            diff.setRightChild(right);
            return diff;
        }
	    

        /* this is the case if we are using mult - 
                tree for mult           differentiated tree for mult
                    *                                +
                  /   \                           /     \
                 c   f(x)                       *        *
                                               /  \     /  \
                                              c f'(x)  f(x) (c)'
            
            where f'(x) denotes differentiation of f(x)
                  c is a constant
                  (c)' denotes differentiation of c */

        else if(diff.getValue().equals("mult")) {
            ExpressionTree leftHalf = diff.getLeftChild().deepCopy();
            ExpressionTree rightHalf = diff.getRightChild().deepCopy();
            diff.setValue("add");
            ExpressionTree leftMultiply = new ExpressionTree();
            ExpressionTree rightMultiply = new ExpressionTree();
            leftMultiply.setValue("mult"); rightMultiply.setValue("mult");

            leftMultiply.setLeftChild(leftHalf); leftMultiply.setRightChild(rightHalf.differentiate());
            rightMultiply.setLeftChild(rightHalf); rightMultiply.setRightChild(leftHalf.differentiate());

            diff.setLeftChild(leftMultiply); diff.setRightChild(rightMultiply);
            return diff;
        }
                             
        
        /* this is the case if we are using cos or sin - 
                tree for cos        differentiated tree for cos
                    cos                         -
                     |                        /   \ 
                    f(x)                     0     *
                                                 /   \
                                                sin  f'(x)
                                                 |
                                                f(x)

                tree for sin       differentiated tree for sin
                    sin                         +
                     |                        /   \ 
                    f(x)                     0     *
                                                 /   \
                                                cos  f'(x)
                                                 |
                                                f(x)

            f'(x) denotes differentiation of f(x) */

        else if(diff.getValue().equals("cos") || diff.getValue().equals("sin")) {
            boolean isCos;
            ExpressionTree expression = diff.getLeftChild().deepCopy();
            if(diff.getValue().equals("cos")) {
                diff.setValue("minus");
                isCos = true;
            }
            else {
                diff.setValue("add");
                isCos = false;
            }
            ExpressionTree zero = new ExpressionTree();
            ExpressionTree multiply = new ExpressionTree();
            zero.setValue("0"); multiply.setValue("mult");
            diff.setLeftChild(zero); diff.setRightChild(multiply);

            ExpressionTree diffOfExpression = expression.differentiate();
            diff.getRightChild().setRightChild(diffOfExpression);
            if(isCos) {
                ExpressionTree sine = new ExpressionTree();
                sine.setValue("sin");
                sine.setLeftChild(expression);
                diff.getRightChild().setLeftChild(sine);
            }
            else {
                ExpressionTree cosine = new ExpressionTree();
                cosine.setValue("cos");
                cosine.setLeftChild(expression);
                diff.getRightChild().setLeftChild(cosine);
            }
            return diff;
        }


        /* this is the case for exp - 
            tree for exp      differentiated tree for exp
                exp                         * 
                 |                        /   \
                f(x)                     exp  f'(x)
                                          |
                                        f(x)
            
            f'(x) denotes differentiation of f(x) */

        else {
            ExpressionTree exponential = diff.deepCopy();
            diff.setValue("mult");
            diff.setLeftChild(exponential);
            ExpressionTree diffOfExponentialFunc = exponential.getLeftChild().differentiate();
            diff.setRightChild(diffOfExponentialFunc);
            return diff;
        }

    }

    /* I am including my code for Q4 about Tree Isomorphism here
        because the pseudocode might be hard to read with all the
        indentations and stuff. I have tested this code and it works
        
        Should return true for : 5(8(2,6(3,2)),4)
                                 5(4,8(6(3,2),2))

        Should return false for : 5(8(2,3(6,2)),4)
                                  5(4,8(6(3,2),2)) */
    public static boolean isomorphic(ExpressionTree f, ExpressionTree g) {
        System.out.println();
        System.out.println(f);
        System.out.println(g);
        
        /* if one of the nodes doesn't have any
            children, we compare the values of
            those two nodes */
        if (f.getLeftChild() == null || g.getLeftChild() == null) {
            if(!f.getValue().equals(g.getValue())) {
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
                return false;
            }
        }
        
        /* if the trees have more than one level */
        else {
            ExpressionTree fLeftBranch = f.getLeftChild().deepCopy();
            ExpressionTree gLeftBranch = g.getLeftChild().deepCopy();

            ExpressionTree fRightBranch = f.getRightChild().deepCopy();
            ExpressionTree gRightBranch = g.getRightChild().deepCopy();

            if (!(isomorphic(fLeftBranch, gLeftBranch) || isomorphic(fLeftBranch, gRightBranch))) {
                return false;
            }
            

            if(!(isomorphic(fRightBranch, gRightBranch) || isomorphic(fRightBranch, gLeftBranch))) {
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
        
    
    public static void main(String args[]) {
        ExpressionTree e = new ExpressionTree("mult(add(2,x),cos(x))");
        System.out.println(e);
        System.out.println(e.evaluate(1));
        System.out.println(e.differentiate());

        /* samples: mult(add(2,x),cos(x)); x = 6;       Answer: 7.6813622932
                    mult(minus(4,x),add(5,x)); x = 6;   Answer: -22.0
                    cos(add(5,x)); x = 6;               Answer: 0.00442569798
                    add(6,cos(add(5,x))); x = 6;        Answer: 6.00442569798 */
        /* String[] samples = {"mult(add(2,x),cos(x))", "mult(minus(4,x),add(5,x))", "cos(add(5,x))", "add(6,cos(add(5,x)))"};
        double x = 6;
        System.out.println("@ x = " + x);
        for (int i = 0; i < samples.length; i++) {
            ExpressionTree e = new ExpressionTree(samples[i]);
            System.out.println(e);
            System.out.println("Evaluated answer: " + e.evaluate(x));
        }
        
        ExpressionTree f = new ExpressionTree("mult(sin(x),mult(cos(x),exp(x)))");
        System.out.println(f);
        double x = 13.39664737826625;

        System.out.println("Evaluated answer: " + f.evaluate(x));
        ExpressionTree fdiff = f.differentiate();
        System.out.println("Differentiated answer: " + fdiff);
        System.out.println("Evaluate diff. answer (@ x = " + x + ") : " + fdiff.evaluate(x)); */
   
 }
}