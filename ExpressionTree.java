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
        
        /* we copy the string expresssion so
            that we can replace all the characters 'x'
            with the actual value of 'x' */ 
        String expr = this.toString();
        String variable = x + "";
        String exprWithVal = expr.replaceAll("x", variable);
        ExpressionTree f = new ExpressionTree(exprWithVal);
        double answer = 0;
        String left, right, operation;
        double leftVal, rightVal;
        
        // base case: the most fundamental expression
        if (this.getLeftChild().getLeftChild() == null && this.getRightChild() == null) {
            left = f.getLeftChild().getValue();
            leftVal = Double.parseDouble(left);
            //System.out.println("if: " + leftVal);
            //right = f.getRightChild().getValue();
            //rightVal = Double.parseDouble(right);
            //double rightVal = x;
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

        else if (this.getLeftChild().getLeftChild() == null && this.getRightChild().getLeftChild() == null) {
            left = f.getLeftChild().getValue();
            leftVal = Double.parseDouble(left);
            right = f.getRightChild().getValue();
            rightVal = Double.parseDouble(right);

            //System.out.println("Else if: " + leftVal + "  " + rightVal);
            //double rightVal = x;
            operation = f.getValue();

            //System.out.println(operation);
            //double answer = 0;

            if(operation.equals("add")) {
                answer = leftVal + rightVal;
            }
            else if (operation.equals("minus")) {
                answer = leftVal - rightVal;
            }
            else if (operation.equals("mult")) {
                answer = leftVal * rightVal;
            }

	    // AND CHANGE THIS RETURN STATEMENT
	        return answer;
        }


        else {
            double leftBranchVal = 0;
            double rightBranchVal = 0;
            if(this.getLeftChild().getLeftChild() == null) {
                leftBranchVal = Double.parseDouble(this.getLeftChild().getValue());
            }
            else {
                leftBranchVal = this.getLeftChild().deepCopy().evaluate(x);
                ExpressionTree leftCalc = new ExpressionTree("" + leftBranchVal);
                this.setLeftChild(leftCalc);
                //System.out.println("Else: (left) " + leftCalc.getValue());
                String toDo = this.getValue();
                if (toDo.equals("cos")) {
                    return Math.cos(leftBranchVal);
                }
                else if (toDo.equals("sin")) {
                    return Math.sin(leftBranchVal);
                }
                else if (toDo.charAt(0) == 'e') {
                    return Math.exp(leftBranchVal);
                }
                //this.getLeftChild().setValue("" + branchVal);
                //this.getLeftChild().setLeftChild(null);
                //this.getLeftChild().setRightChild(null);
            }

            if (this.getRightChild().getLeftChild() == null) {
                rightBranchVal = Double.parseDouble(this.getRightChild().getValue());
            }
            else {
                rightBranchVal = this.getRightChild().deepCopy().evaluate(x);
                ExpressionTree rightCalc = new ExpressionTree("" + rightBranchVal);
                this.setRightChild(rightCalc);
                //System.out.println("Else: (right) " + rightCalc.getValue());
                //this.getRightChild().setValue("" + branchVal);
                //this.getRightChild().setLeftChild(null);
                //this.getRightChild().setRightChild(null);
            }

            return this.evaluate(x);
        }
        
    }                                                 

    /* returns the root of a new expression tree representing the derivative of the
       original expression */
    ExpressionTree differentiate() {
	// WRITE YOUR CODE HERE

	// AND CHANGE THIS RETURN STATEMENT                        
        return null;
    }
        
    
    public static void main(String args[]) {
        /* ExpressionTree e = new ExpressionTree("mult(add(2,x),cos(x))");
        System.out.println(e);
        System.out.println(e.evaluate(1));
        System.out.println(e.differentiate()); */

        /* samples: mult(add(2,x),cos(x)); x = 6;       Answer: 7.6813622932
                    mult(minus(4,x),add(5,x)); x = 6;   Answer: -22.0
                    cos(x); x = 6;                      Answer: 0.96017028665
                    cos(add(5,x)); x = 6;               Answer: 0.00442569798
                    add(6,cos(add(5,x))); x = 6;        Answer: 6.00442569798*/
        ExpressionTree e = new ExpressionTree("cos(mult(x,x))");
        System.out.println(e);
        double x = 6;
        System.out.println("Evaluated at x = " + x);
        System.out.println(e.evaluate(x));
        //System.out.println(Math.exp(5+2));
   
 }
}