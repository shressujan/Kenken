/**
 * This class is called inside the Variable class
 * Used to form the domains for each variables in the kenken puzzle
 * @author Sujan
 * last updated: March 21, 2016
 */
import java.util.ArrayList;

public class Domain {

	private ArrayList<Integer> values;

	/**
	 * This is a constructor for this class
	 */
	public Domain() {
		// Defines the domains for the variable (Cell)
		values = new ArrayList<Integer>();
	}

	/**
	 * This method add the domain to the domain list
	 * 
	 * @return domain
	 */
	public void add(int v) {
		values.add(v);
	}

	/**
	 * This method returns the domains as an arraylist of integers
	 * 
	 * @return values
	 */
	public ArrayList<Integer> list() {
		return values;
	}

	/**
	 * This method removes the domain that doesn't satisfy the constraint
	 */
	public void reduce(ArrayList<Integer> val) {
		for (int i = 0; i < val.size(); i++) {
			for (int j = 0; j < values.size(); j++) {
				if (val.get(i) == values.get(j)) {
					values.remove(j);
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
		for (Integer i : this.values) {
			info += i.toString() + " ";
		}
		info = info.substring(0, info.length());
		return "Domain [domains=" + info + "]";
	}

}
