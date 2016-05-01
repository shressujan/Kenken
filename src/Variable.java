/**
 * This is the variable class that creates variables for the kenken puzzle
 * This class contains a constuctor that takes two parameter
 * This class contains an initDomain method that initialized the domain
 * This class contains an addDomain method that adds domain to the domainlist
 * This class contains a reduceDomain method that reduces the domain from the domainlist
 * This class contains a getDomain method that returns the arraylist of the domains for the specific variable or cell
 * This class contains a clearDomain method that clears the arraylist of the domain for the specific variable
 * This class contains addConstraint method that adds the constraint to the specific variable
 * @author Sujan
 *last updated: March 20, 2016
 */
import java.util.ArrayList;

public class Variable {

	private int row;
	private int col;
	private ArrayList<Integer> reduceDomList;

	protected int assignment = 0;
	protected boolean isAssigned = false;
	protected Constraint constraint = null;
	protected Domain domain;

	/**
	 * This a constructor for the Cell class
	 * 
	 * @param nextInt
	 * @param nextInt2
	 */
	public Variable(int nextInt, int nextInt2) {
		this.row = nextInt;
		this.col = nextInt2;
		// Instantiating the Domain object
		this.domain = new Domain();
		reduceDomList = new ArrayList<Integer>();
		constraint = new Constraint();
	}

	/**
	 * This method reduces the domainList
	 */
	public void reduceDomain() {
		domain.reduce(reduceDomList);
	}

	/**
	 * This method add the val to reducDomList
	 * 
	 * @param val
	 */
	public void reduceDomainList(Integer val) {
		reduceDomList.add(val);
	}

	/**
	 * This is a getter for row
	 * 
	 * @return row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * This methods gets the column
	 * 
	 * @return col
	 */
	public int getCol() {
		return col;
	}

	/**
	 * This is a toString method
	 * 
	 * @return "Cell [row=" + row + ", col=" + col + "]"
	 */
	public String toString() {
		return "Cell [row=" + row + ", col=" + col + "]";
	}

	/**
	 * This method adds the constraint to the variable
	 * 
	 * @param constraint
	 */
	public void addConstraints(Constraint constraint) {

		this.constraint = (constraint);
	}
}
