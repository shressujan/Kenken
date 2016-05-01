/**
 * This class is called inside the Variable class
 * Used to form the domains for each variables in the kenken puzzle
 * This class contains a constructor 
 * This class contains an add method that add domains to the domainlist
 * This class contains reduce method that remove the domain from the domainlist
 * @author Sujan
 * last updated: April 29, 2016
 */
import java.util.ArrayList;

public class Domain {

	protected ArrayList<Integer> list;

	/**
	 * This is a constructor for this class Initialized the variable as a new
	 * Arraylist<Integer>
	 */
	public Domain() {
		// Defines the domains for the variable (Cell)
		list = new ArrayList<Integer>();
	}

	/**
	 * This method adds the values in the domain
	 * 
	 * @param size
	 */
	public void initDomain(int size) {
		for (int i = 1; i <= size; i++) {
			list.add(i);
		}
	}

	/**
	 * This method clears the domain
	 */
	public void clearDomain() {
		this.list.clear();
	}

	/**
	 * This method clears the domain list Then adds the domain to the domain
	 * list
	 * 
	 * @return domain
	 */
	public void add(int v) {
		list.clear();
		list.add(v);
	}

	/**
	 * This method removes the domain that doesn't satisfy the constraint
	 */
	public void reduce(ArrayList<Integer> val) {
		for (int i = 0; i < val.size(); i++) {
			for (int j = 0; j < list.size(); j++) {
				if (val.get(i) == list.get(j)) {
					list.remove(j);
				}
			}
		}

	}

	/**
	 * This is a tostring method that returns the collection of domains data
	 * from the arraylist
	 */
	public String toString() {
		String info = "";
		for (Integer i : this.list) {
			info += i.toString() + " ";
		}
		info = info.substring(0, info.length());
		return "Domain [domains=" + info + "]";
	}

}
