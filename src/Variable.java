/**
 * This is the Cell class that creates cells for the kenken puzzle
 * @author Sujan
 *last updated: March 20, 2016
 */
import java.util.ArrayList;

public class Variable {

	private int row;
	private int col;
	private Domain domain;
	protected int assignment = 0;
	protected boolean isAssigned = false;

	private ArrayList<Integer> reduceDomList;
	protected Constraint constraint = null;

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

	public void initDomain(int size) {
		for (int i = 1; i <= size; i++) {
			domain.add(i);
		}
	}

	/**
	 * This method adds val to the domainlist
	 * 
	 * @param val
	 */
	public void addDomain(int val) {
		domain.add(val);
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
	 * This is a getter for domain list
	 * 
	 * @return domain.list()
	 */
	public ArrayList<Integer> getDomain() {
		return domain.list();
	}

	/**
	 * This method clears the domain
	 */
	public void clearDomain() {
		this.domain.list().clear();
		;
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

	public void addConstraints(Constraint constraint) {

		this.constraint = (constraint);
	}
}
