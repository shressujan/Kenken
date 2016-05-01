/**
 * This class is used to form constraints among the variables with same constraint
 * This class contains a default constructor
 * This class contains getter for constraintNum and constraintOperator and constraintVariables
 * This class also contains a toString method
 * @author Sujan
 * last updated: March 21, 2016
 */
import java.util.ArrayList;

public class Constraint {

	private int constraintNum;
	private String constrSign;
	private ArrayList<Variable> constraintVariables = new ArrayList<Variable>();

	/**
	 * This is a default constructor for the Constraint class
	 */
	public Constraint() {
	}

	/**
	 * This method gets the constraint number
	 * 
	 * @return constraintNum
	 */
	public int getConstraintNum() {
		return constraintNum;
	}

	/**
	 * This method gets the constraint operator
	 * 
	 * @return constraintOperator
	 */
	public String getConstraintOperator() {
		return constrSign;
	}

	/**
	 * This method returns the arraylist of constraintVariables
	 * 
	 * @return constraintVariables
	 */
	public ArrayList<Variable> getConstraintVariables() {
		return constraintVariables;
	}

	/**
	 * This method adds the constraints Variables to the arraylist of
	 * constriatVariables
	 * 
	 * @param variable
	 */
	public void addConstraintVariable(Variable variable) {
		constraintVariables.add(variable);
	}

	/**
	 * This method adds the constraint operator and number to the constraint
	 * 
	 * @param number
	 * @param sign
	 */
	public void addConstrSign(int number, String sign) {

		this.constraintNum = number;
		this.constrSign = sign;
	}

	/**
	 * This is a tostring method that returns the collection of string data from
	 * the arraylist
	 * 
	 * @return infoVariables
	 */
	public String toString() {
		String infoVariables = "";
		for (Variable i : this.constraintVariables) {
			infoVariables += i.toString() + " ";
		}
		infoVariables = infoVariables.substring(0, infoVariables.length());
		return "Constraint [constraintVariables = " + infoVariables + "]" + " "
				+ constraintNum + constrSign;
	}

}
