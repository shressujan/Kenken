/**
 * This class is used to load the file, scan and parse it
 * set up the variables 
 * specify the rows and columns
 * form constraints such as inequality constraint, arc-constraints and node constraints
 * @author Sujan
 *last updated: March 28, 2016
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

import javax.swing.JOptionPane;

public class KenKenPuzzle {

	protected ArrayList<Constraint> allConstraints;
	private ArrayList<Constraint> allInEqualityConstraints;
	private Variable[][] allvars;
	Stack<Variable> unassigned;
	Stack<Variable> assigned;
	protected int rowSize;
	protected int colSize;

	/**
	 * This is the constructor for this class
	 */
	public KenKenPuzzle(File input) {
		allConstraints = new ArrayList<Constraint>();
		allInEqualityConstraints = new ArrayList<Constraint>();
		loadFromFile(input);
		NodeConsistency();
		formInEqualityConstr();

	}

	/**
	 * This method takes a file as input and scans the file and parses the file
	 * for further process
	 * 
	 * @param input
	 */
	public void loadFromFile(File input) {
		try {
			String line;
			Scanner scan = new Scanner(input);
			this.rowSize = scan.nextInt();
			this.colSize = rowSize;
			scan.nextLine();
			// Instantiating all the variables to be used
			allvars = new Variable[rowSize][colSize];
			for (int i = 0; i < rowSize; i++) {
				for (int j = 0; j < colSize; j++) {
					allvars[i][j] = new Variable(i, j);
					allvars[i][j].initDomain(rowSize);
				}
			}

			// Scanning through the data file and parsing it
			while (scan.hasNext()) {
				line = scan.nextLine();
				String[] subString = line.split(":");

				String operator;
				int number;
				Scanner lineScanBeforeColon;
				try {
					lineScanBeforeColon = new Scanner(subString[0]);
					Constraint constraint = new Constraint();

					// Scanning the data before colon in the given line
					while (lineScanBeforeColon.hasNextInt()) {
						int currentRow = lineScanBeforeColon.nextInt();
						int currentCol = lineScanBeforeColon.nextInt();
						constraint
								.addConstraintVariable(allvars[currentRow][currentCol]);
						allvars[currentRow][currentCol]
								.addConstraints(constraint);
					}
					// Scanning the data after colon in the given line
					try {
						// System.out.println(subString[1]);
						String[] innerSubString = subString[1].split("");
						String tempNum = "";
						for (int i = 0; i < innerSubString.length - 1; i++) {
							tempNum += Integer.parseInt(innerSubString[i]) + "";
						}
						number = Integer.parseInt(tempNum);
						operator = innerSubString[innerSubString.length - 1];
						constraint.addConstrSign(number, operator);
					} catch (Exception ArrayIndexOutOfBoundsException) {
					} finally {
						allConstraints.add(constraint);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Variable getVar(int row, int col) {
		return allvars[row][col];
	}

	/**
	 * This method forms inequality constraints for all the rows and columns in
	 * the puzzle
	 */
	public void formInEqualityConstr() {
		// For the inequalities in same row
		for (int i = 0; i < rowSize; i++) {
			for (int j = 0; j < colSize; j++) {
				for (int k = 0; k < colSize; k++) {
					if (j != k) {
						Constraint inEqualityRowconstr = new Constraint();
						inEqualityRowconstr
								.addConstraintVariable(allvars[i][j]);
						inEqualityRowconstr
								.addConstraintVariable(allvars[i][k]);
						allvars[i][j].addConstraints(inEqualityRowconstr);
						allvars[i][k].addConstraints(inEqualityRowconstr);
						inEqualityRowconstr.addConstrSign(0, "!=");
						allInEqualityConstraints.add(inEqualityRowconstr);
					}
				}
			}
		}
		// For inequalities in same column
		for (int a = 0; a < colSize; a++) {
			for (int b = 0; b < rowSize; b++) {
				for (int c = 0; c < rowSize; c++) {
					if (b != c) {
						Constraint inEqualityColConstr = new Constraint();
						inEqualityColConstr
								.addConstraintVariable(allvars[b][a]);
						allvars[b][a].addConstraints(inEqualityColConstr);
						inEqualityColConstr
								.addConstraintVariable(allvars[c][a]);
						allvars[c][a].addConstraints(inEqualityColConstr);
						inEqualityColConstr.addConstrSign(0, "!=");
						allInEqualityConstraints.add(inEqualityColConstr);
					}
				}
			}
		}
	}

	/**
	 * This method performs the node consistency This method is called at the
	 * beginning only once through the entire solving process
	 */

	public void NodeConsistency() {
		for (Constraint c : allConstraints) {
			if (c.getConstraintVariables().size() == 1) {
				int curRow = c.getConstraintVariables().get(0).getRow();
				int curCol = c.getConstraintVariables().get(0).getCol();
				allvars[curRow][curCol].clearDomain();
				allvars[curRow][curCol].addDomain(c.getConstraintNum());
			}

		}
	}

	/**
	 * This ArcConsistency method performs addArcConsistency, subArcConsistency,
	 * mulArcConsistency and divArcConsistency
	 */
	public void ArcConsistency() {

		for (Constraint c : allConstraints) {
			if (c.getConstraintOperator().equals("+")) {
				int numOfSameConstrVariable = c.getConstraintVariables().size();
				switch (numOfSameConstrVariable) {
				case 2: {
					addArcConsistency(c);
					break;
				}
				case 3: {
					threeNaryAddConstraint(c);
					break;
				}
				case 4: {
					fourNaryConsistency(c);
					break;
				}
				case 5: {
					fiveNaryConsistency(c);
					break;
				}
				case 6: {
					sixNaryConsistency(c);
					break;
				}
				default: {
					break;
				}
				}
			} else if (c.getConstraintOperator().equals("-")) {
				subArcConsistency(c);
			} else if (c.getConstraintOperator().equals("x")
					|| c.getConstraintOperator().equals("X")) {
				int numOfSameConstrVariable = c.getConstraintVariables().size();
				switch (numOfSameConstrVariable) {
				case 2: {
					mulArcConsistency(c);
					break;
				}
				case 3: {
					threeNaryMulConsistencey(c);
					break;
				}
				case 4: {
					fourNaryMulConsistency(c);
				}
				default: {
					break;
				}
				}
			}

			else if (c.getConstraintOperator().equals("/")) {
				divArcConsistency(c);
			}
		}

		// Calling constraintSatisfaction method for all the inEquality

		for (Constraint c : allInEqualityConstraints) {
			constraintSatisfaction(c);
		}
	}

	private void sixNaryConsistency(Constraint c) {
		Variable var1 = c.getConstraintVariables().get(0);
		Variable var2 = c.getConstraintVariables().get(1);
		Variable var3 = c.getConstraintVariables().get(2);
		Variable var4 = c.getConstraintVariables().get(3);
		Variable var5 = c.getConstraintVariables().get(4);
		Variable var6 = c.getConstraintVariables().get(5);

		int constrNum = c.getConstraintNum();

		// For var1
		for (int j = 0; j < var1.getDomain().size(); j++) {
			int constrNumplusDomain = constrNum - var1.getDomain().get(j);

			boolean checkIfExists = false;
			for (int k = 0; k < var2.getDomain().size(); k++) {
				if (var1.getDomain().get(j) != var2.getDomain().get(k)) {
					int holdDomain = constrNumplusDomain
							- var2.getDomain().get(k);

					for (int l = 0; l < var3.getDomain().size(); l++) {
						if (var3.getDomain().get(l) != var1.getDomain().get(j)
								&& var2.getDomain().get(k) != var3.getDomain()
										.get(l)) {
							int holdDomain2 = holdDomain
									- var3.getDomain().get(l);

							for (int m = 0; m < var4.getDomain().size(); m++) {
								if (var4.getDomain().get(m) != var1.getDomain()
										.get(j)
										&& var4.getDomain().get(m) != var2
												.getDomain().get(k)
										&& var4.getDomain().get(m) != var3
												.getDomain().get(l)) {
									int holdDomain3 = holdDomain2
											- var4.getDomain().get(m);
									for (int n = 0; n < var5.getDomain().size(); n++) {
										if (var5.getDomain().get(n) != var1
												.getDomain().get(j)
												&& var5.getDomain().get(n) != var2
														.getDomain().get(k)
												&& var5.getDomain().get(n) != var3
														.getDomain().get(l)
												&& var5.getDomain().get(n) != var4
														.getDomain().get(m)) {
											int holdDomain4 = holdDomain3
													- var5.getDomain().get(n);
											for (int p = 0; p < var6
													.getDomain().size(); p++) {
												if (holdDomain4 != var1
														.getDomain().get(j)
														&& holdDomain4 != var2
																.getDomain()
																.get(k)
														&& holdDomain4 != var3
																.getDomain()
																.get(l)
														&& holdDomain4 != var4
																.getDomain()
																.get(m)
														&& holdDomain4 != var5
																.getDomain()
																.get(n)) {
													if (holdDomain4 == var6
															.getDomain().get(p)) {
														checkIfExists = true;
													}
												}

											}
										}
									}
								}
							}
						}
					}
				}
			}
			if (checkIfExists == false) {
				var1.reduceDomainList(var1.getDomain().get(j));
			}

		}

		// For var2
		for (int j = 0; j < var2.getDomain().size(); j++) {
			int constrNumplusDomain = constrNum - var2.getDomain().get(j);

			boolean checkIfExists = false;
			for (int k = 0; k < var3.getDomain().size(); k++) {
				if (var2.getDomain().get(j) != var3.getDomain().get(k)) {
					int holdDomain = constrNumplusDomain
							- var3.getDomain().get(k);

					for (int l = 0; l < var4.getDomain().size(); l++) {
						if (var4.getDomain().get(l) != var2.getDomain().get(j)
								&& var4.getDomain().get(l) != var3.getDomain()
										.get(k)) {
							int holdDomain2 = holdDomain
									- var4.getDomain().get(l);

							for (int m = 0; m < var5.getDomain().size(); m++) {
								if (var5.getDomain().get(m) != var2.getDomain()
										.get(j)
										&& var5.getDomain().get(m) != var3
												.getDomain().get(k)
										&& var5.getDomain().get(m) != var4
												.getDomain().get(l)) {
									int holdDomain3 = holdDomain2
											- var5.getDomain().get(m);
									for (int n = 0; n < var6.getDomain().size(); n++) {
										if (var6.getDomain().get(n) != var2
												.getDomain().get(j)
												&& var6.getDomain().get(n) != var3
														.getDomain().get(k)
												&& var6.getDomain().get(n) != var4
														.getDomain().get(l)
												&& var6.getDomain().get(n) != var5
														.getDomain().get(m)) {
											int holdDomain4 = holdDomain3
													- var6.getDomain().get(n);
											for (int p = 0; p < var1
													.getDomain().size(); p++) {
												if (holdDomain4 != var2
														.getDomain().get(j)
														&& holdDomain4 != var3
																.getDomain()
																.get(k)
														&& holdDomain4 != var4
																.getDomain()
																.get(l)
														&& holdDomain4 != var5
																.getDomain()
																.get(m)
														&& holdDomain4 != var6
																.getDomain()
																.get(n)) {
													if (holdDomain4 == var1
															.getDomain().get(p)) {
														checkIfExists = true;
													}
												}

											}
										}
									}
								}
							}
						}
					}
				}
			}
			if (checkIfExists == false) {
				var2.reduceDomainList(var2.getDomain().get(j));
			}

		}

		// For var3
		for (int j = 0; j < var3.getDomain().size(); j++) {
			int constrNumplusDomain = constrNum - var3.getDomain().get(j);

			boolean checkIfExists = false;
			for (int k = 0; k < var4.getDomain().size(); k++) {
				if (var3.getDomain().get(j) != var4.getDomain().get(k)) {
					int holdDomain = constrNumplusDomain
							- var4.getDomain().get(k);

					for (int l = 0; l < var5.getDomain().size(); l++) {
						if (var5.getDomain().get(l) != var3.getDomain().get(j)
								&& var5.getDomain().get(l) != var4.getDomain()
										.get(k)) {
							int holdDomain2 = holdDomain
									- var5.getDomain().get(l);

							for (int m = 0; m < var6.getDomain().size(); m++) {
								if (var6.getDomain().get(m) != var3.getDomain()
										.get(j)
										&& var6.getDomain().get(m) != var4
												.getDomain().get(k)
										&& var6.getDomain().get(m) != var5
												.getDomain().get(l)) {
									int holdDomain3 = holdDomain2
											- var6.getDomain().get(m);
									for (int n = 0; n < var1.getDomain().size(); n++) {
										if (var1.getDomain().get(n) != var3
												.getDomain().get(j)
												&& var1.getDomain().get(n) != var4
														.getDomain().get(k)
												&& var1.getDomain().get(n) != var5
														.getDomain().get(l)
												&& var1.getDomain().get(n) != var6
														.getDomain().get(m)) {
											int holdDomain4 = holdDomain3
													- var1.getDomain().get(n);
											for (int p = 0; p < var2
													.getDomain().size(); p++) {
												if (holdDomain4 != var3
														.getDomain().get(j)
														&& holdDomain4 != var4
																.getDomain()
																.get(k)
														&& holdDomain4 != var5
																.getDomain()
																.get(l)
														&& holdDomain4 != var6
																.getDomain()
																.get(m)
														&& holdDomain4 != var1
																.getDomain()
																.get(n)) {
													if (holdDomain4 == var2
															.getDomain().get(p)) {
														checkIfExists = true;
													}
												}

											}
										}
									}
								}
							}
						}
					}
				}
			}
			if (checkIfExists == false) {
				var3.reduceDomainList(var3.getDomain().get(j));
			}

		}

		// For var4
		for (int j = 0; j < var4.getDomain().size(); j++) {
			int constrNumplusDomain = constrNum - var4.getDomain().get(j);

			boolean checkIfExists = false;
			for (int k = 0; k < var5.getDomain().size(); k++) {
				if (var4.getDomain().get(j) != var5.getDomain().get(k)) {
					int holdDomain = constrNumplusDomain
							- var5.getDomain().get(k);

					for (int l = 0; l < var6.getDomain().size(); l++) {
						if (var6.getDomain().get(l) != var4.getDomain().get(j)
								&& var6.getDomain().get(l) != var5.getDomain()
										.get(k)) {
							int holdDomain2 = holdDomain
									- var6.getDomain().get(l);

							for (int m = 0; m < var1.getDomain().size(); m++) {
								if (var1.getDomain().get(m) != var4.getDomain()
										.get(j)
										&& var1.getDomain().get(m) != var5
												.getDomain().get(k)
										&& var1.getDomain().get(m) != var6
												.getDomain().get(l)) {
									int holdDomain3 = holdDomain2
											- var1.getDomain().get(m);
									for (int n = 0; n < var2.getDomain().size(); n++) {
										if (var2.getDomain().get(n) != var4
												.getDomain().get(j)
												&& var2.getDomain().get(n) != var5
														.getDomain().get(k)
												&& var2.getDomain().get(n) != var6
														.getDomain().get(l)
												&& var2.getDomain().get(n) != var1
														.getDomain().get(m)) {
											int holdDomain4 = holdDomain3
													- var2.getDomain().get(n);
											for (int p = 0; p < var3
													.getDomain().size(); p++) {
												if (holdDomain4 != var4
														.getDomain().get(j)
														&& holdDomain4 != var5
																.getDomain()
																.get(k)
														&& holdDomain4 != var6
																.getDomain()
																.get(l)
														&& holdDomain4 != var1
																.getDomain()
																.get(m)
														&& holdDomain4 != var2
																.getDomain()
																.get(n)) {
													if (holdDomain4 == var3
															.getDomain().get(p)) {
														checkIfExists = true;
													}
												}

											}
										}
									}
								}
							}
						}
					}
				}
			}
			if (checkIfExists == false) {
				var4.reduceDomainList(var4.getDomain().get(j));
			}

		}

		// For var5
		for (int j = 0; j < var5.getDomain().size(); j++) {
			int constrNumplusDomain = constrNum - var5.getDomain().get(j);

			boolean checkIfExists = false;
			for (int k = 0; k < var6.getDomain().size(); k++) {
				if (var5.getDomain().get(j) != var6.getDomain().get(k)) {
					int holdDomain = constrNumplusDomain
							- var6.getDomain().get(k);

					for (int l = 0; l < var1.getDomain().size(); l++) {
						if (var1.getDomain().get(l) != var5.getDomain().get(j)
								&& var1.getDomain().get(l) != var6.getDomain()
										.get(k)) {
							int holdDomain2 = holdDomain
									- var1.getDomain().get(l);

							for (int m = 0; m < var2.getDomain().size(); m++) {
								if (var2.getDomain().get(m) != var5.getDomain()
										.get(j)
										&& var2.getDomain().get(m) != var6
												.getDomain().get(k)
										&& var2.getDomain().get(m) != var1
												.getDomain().get(l)) {
									int holdDomain3 = holdDomain2
											- var2.getDomain().get(m);
									for (int n = 0; n < var3.getDomain().size(); n++) {
										if (var3.getDomain().get(n) != var5
												.getDomain().get(j)
												&& var3.getDomain().get(n) != var6
														.getDomain().get(k)
												&& var3.getDomain().get(n) != var1
														.getDomain().get(l)
												&& var3.getDomain().get(n) != var2
														.getDomain().get(m)) {
											int holdDomain4 = holdDomain3
													- var3.getDomain().get(n);
											for (int p = 0; p < var4
													.getDomain().size(); p++) {
												if (holdDomain4 != var5
														.getDomain().get(j)
														&& holdDomain4 != var6
																.getDomain()
																.get(k)
														&& holdDomain4 != var1
																.getDomain()
																.get(l)
														&& holdDomain4 != var2
																.getDomain()
																.get(m)
														&& holdDomain4 != var3
																.getDomain()
																.get(n)) {
													if (holdDomain4 == var4
															.getDomain().get(p)) {
														checkIfExists = true;
													}
												}

											}
										}
									}
								}
							}
						}
					}
				}
			}
			if (checkIfExists == false) {
				var5.reduceDomainList(var5.getDomain().get(j));
			}

		}

		// For var6
		for (int j = 0; j < var6.getDomain().size(); j++) {
			int constrNumplusDomain = constrNum - var6.getDomain().get(j);

			boolean checkIfExists = false;
			for (int k = 0; k < var1.getDomain().size(); k++) {
				if (var6.getDomain().get(j) != var1.getDomain().get(k)) {
					int holdDomain = constrNumplusDomain
							- var1.getDomain().get(k);

					for (int l = 0; l < var2.getDomain().size(); l++) {
						if (var2.getDomain().get(l) != var6.getDomain().get(j)
								&& var2.getDomain().get(l) != var1.getDomain()
										.get(k)) {
							int holdDomain2 = holdDomain
									- var2.getDomain().get(l);

							for (int m = 0; m < var3.getDomain().size(); m++) {
								if (var3.getDomain().get(m) != var6.getDomain()
										.get(j)
										&& var3.getDomain().get(m) != var1
												.getDomain().get(k)
										&& var3.getDomain().get(m) != var2
												.getDomain().get(l)) {
									int holdDomain3 = holdDomain2
											- var3.getDomain().get(m);
									for (int n = 0; n < var4.getDomain().size(); n++) {
										if (var4.getDomain().get(n) != var6
												.getDomain().get(j)
												&& var4.getDomain().get(n) != var1
														.getDomain().get(k)
												&& var4.getDomain().get(n) != var2
														.getDomain().get(l)
												&& var4.getDomain().get(n) != var3
														.getDomain().get(m)) {
											int holdDomain4 = holdDomain3
													- var4.getDomain().get(n);
											for (int p = 0; p < var5
													.getDomain().size(); p++) {
												if (holdDomain4 != var6
														.getDomain().get(j)
														&& holdDomain4 != var1
																.getDomain()
																.get(k)
														&& holdDomain4 != var2
																.getDomain()
																.get(l)
														&& holdDomain4 != var3
																.getDomain()
																.get(m)
														&& holdDomain4 != var4
																.getDomain()
																.get(n)) {
													if (holdDomain4 == var5
															.getDomain().get(p)) {
														checkIfExists = true;
													}
												}

											}
										}
									}
								}
							}
						}
					}
				}
			}
			if (checkIfExists == false) {
				var6.reduceDomainList(var6.getDomain().get(j));
			}

		}
		// Reducing the domains here
		var1.reduceDomain();
		var2.reduceDomain();
		var3.reduceDomain();
		var4.reduceDomain();
		var5.reduceDomain();
		var6.reduceDomain();
	}

	private void fiveNaryConsistency(Constraint c) {
		Variable var1 = c.getConstraintVariables().get(0);
		Variable var2 = c.getConstraintVariables().get(1);
		Variable var3 = c.getConstraintVariables().get(2);
		Variable var4 = c.getConstraintVariables().get(3);
		Variable var5 = c.getConstraintVariables().get(4);

		int constrNum = c.getConstraintNum();

		// For var1
		for (int j = 0; j < var1.getDomain().size(); j++) {
			int constrNumplusDomain = constrNum - var1.getDomain().get(j);

			boolean checkIfExists = false;
			for (int k = 0; k < var2.getDomain().size(); k++) {
				if (var1.getDomain().get(j) != var2.getDomain().get(k)) {
					int holdDomain = constrNumplusDomain
							- var2.getDomain().get(k);

					for (int l = 0; l < var3.getDomain().size(); l++) {
						if (var3.getDomain().get(l) != var1.getDomain().get(j)
								&& var2.getDomain().get(k) != var3.getDomain()
										.get(l)) {
							int holdDomain2 = holdDomain
									- var3.getDomain().get(l);

							for (int m = 0; m < var4.getDomain().size(); m++) {
								if (var4.getDomain().get(m) != var1.getDomain()
										.get(j)
										&& var4.getDomain().get(m) != var2
												.getDomain().get(k)
										&& var4.getDomain().get(m) != var3
												.getDomain().get(l)) {
									int holdDomain3 = holdDomain2
											- var4.getDomain().get(m);
									for (int n = 0; n < var5.getDomain().size(); n++) {
										if (holdDomain3 != var1.getDomain()
												.get(j)
												&& holdDomain3 != var2
														.getDomain().get(k)
												&& holdDomain3 != var3
														.getDomain().get(l)
												&& holdDomain3 != var4
														.getDomain().get(m)) {
											if (holdDomain3 == var5.getDomain()
													.get(n)) {
												checkIfExists = true;
											}
										}

									}
								}
							}
						}
					}
				}
			}
			if (checkIfExists == false) {
				var1.reduceDomainList(var1.getDomain().get(j));
			}

		}

		// For var2
		for (int j = 0; j < var2.getDomain().size(); j++) {
			int constrNumplusDomain = constrNum - var2.getDomain().get(j);

			boolean checkIfExists = false;
			for (int k = 0; k < var3.getDomain().size(); k++) {
				if (var2.getDomain().get(j) != var3.getDomain().get(k)) {
					int holdDomain = constrNumplusDomain
							- var3.getDomain().get(k);

					for (int l = 0; l < var4.getDomain().size(); l++) {
						if (var4.getDomain().get(l) != var2.getDomain().get(j)
								&& var4.getDomain().get(l) != var3.getDomain()
										.get(k)) {
							int holdDomain2 = holdDomain
									- var4.getDomain().get(l);

							for (int m = 0; m < var5.getDomain().size(); m++) {
								if (var5.getDomain().get(m) != var2.getDomain()
										.get(j)
										&& var5.getDomain().get(m) != var3
												.getDomain().get(k)
										&& var5.getDomain().get(m) != var4
												.getDomain().get(l)) {
									int holdDomain3 = holdDomain2
											- var4.getDomain().get(m);
									for (int n = 0; n < var1.getDomain().size(); n++) {
										if (holdDomain3 != var2.getDomain()
												.get(j)
												&& holdDomain3 != var3
														.getDomain().get(k)
												&& holdDomain3 != var4
														.getDomain().get(l)
												&& holdDomain3 != var5
														.getDomain().get(m)) {
											if (holdDomain3 == var1.getDomain()
													.get(n)) {
												checkIfExists = true;
											}
										}

									}
								}
							}
						}
					}
				}
			}
			if (checkIfExists == false) {
				var2.reduceDomainList(var2.getDomain().get(j));
			}

		}

		// For var3
		for (int j = 0; j < var3.getDomain().size(); j++) {
			int constrNumplusDomain = constrNum - var3.getDomain().get(j);

			boolean checkIfExists = false;
			for (int k = 0; k < var4.getDomain().size(); k++) {
				if (var3.getDomain().get(j) != var4.getDomain().get(k)) {
					int holdDomain = constrNumplusDomain
							- var4.getDomain().get(k);

					for (int l = 0; l < var5.getDomain().size(); l++) {
						if (var5.getDomain().get(l) != var3.getDomain().get(j)
								&& var5.getDomain().get(l) != var4.getDomain()
										.get(k)) {
							int holdDomain2 = holdDomain
									- var5.getDomain().get(l);

							for (int m = 0; m < var1.getDomain().size(); m++) {
								if (var1.getDomain().get(m) != var3.getDomain()
										.get(j)
										&& var1.getDomain().get(m) != var4
												.getDomain().get(k)
										&& var1.getDomain().get(m) != var5
												.getDomain().get(l)) {
									int holdDomain3 = holdDomain2
											- var1.getDomain().get(m);
									for (int n = 0; n < var2.getDomain().size(); n++) {
										if (holdDomain3 != var3.getDomain()
												.get(j)
												&& holdDomain3 != var4
														.getDomain().get(k)
												&& holdDomain3 != var5
														.getDomain().get(l)
												&& holdDomain3 != var1
														.getDomain().get(m)) {
											if (holdDomain3 == var2.getDomain()
													.get(n)) {
												checkIfExists = true;
											}
										}

									}
								}
							}
						}
					}
				}
			}
			if (checkIfExists == false) {
				var3.reduceDomainList(var3.getDomain().get(j));
			}

		}

		// For var4
		for (int j = 0; j < var4.getDomain().size(); j++) {
			int constrNumplusDomain = constrNum - var4.getDomain().get(j);

			boolean checkIfExists = false;
			for (int k = 0; k < var5.getDomain().size(); k++) {
				if (var4.getDomain().get(j) != var5.getDomain().get(k)) {
					int holdDomain = constrNumplusDomain
							- var5.getDomain().get(k);

					for (int l = 0; l < var1.getDomain().size(); l++) {
						if (var1.getDomain().get(l) != var4.getDomain().get(j)
								&& var1.getDomain().get(l) != var5.getDomain()
										.get(k)) {
							int holdDomain2 = holdDomain
									- var1.getDomain().get(l);

							for (int m = 0; m < var2.getDomain().size(); m++) {
								if (var2.getDomain().get(m) != var4.getDomain()
										.get(j)
										&& var2.getDomain().get(m) != var5
												.getDomain().get(k)
										&& var2.getDomain().get(m) != var1
												.getDomain().get(l)) {
									int holdDomain3 = holdDomain2
											- var2.getDomain().get(m);
									for (int n = 0; n < var3.getDomain().size(); n++) {
										if (holdDomain3 != var4.getDomain()
												.get(j)
												&& holdDomain3 != var5
														.getDomain().get(k)
												&& holdDomain3 != var1
														.getDomain().get(l)
												&& holdDomain3 != var2
														.getDomain().get(m)) {
											if (holdDomain3 == var3.getDomain()
													.get(n)) {
												checkIfExists = true;
											}
										}

									}
								}
							}
						}
					}
				}
			}
			if (checkIfExists == false) {
				var4.reduceDomainList(var4.getDomain().get(j));
			}

		}

		// For var5
		for (int j = 0; j < var5.getDomain().size(); j++) {
			int constrNumplusDomain = constrNum - var5.getDomain().get(j);

			boolean checkIfExists = false;
			for (int k = 0; k < var1.getDomain().size(); k++) {
				if (var5.getDomain().get(j) != var1.getDomain().get(k)) {
					int holdDomain = constrNumplusDomain
							- var1.getDomain().get(k);

					for (int l = 0; l < var2.getDomain().size(); l++) {
						if (var2.getDomain().get(l) != var5.getDomain().get(j)
								&& var2.getDomain().get(l) != var1.getDomain()
										.get(k)) {
							int holdDomain2 = holdDomain
									- var2.getDomain().get(l);

							for (int m = 0; m < var3.getDomain().size(); m++) {
								if (var3.getDomain().get(m) != var5.getDomain()
										.get(j)
										&& var3.getDomain().get(m) != var1
												.getDomain().get(k)
										&& var3.getDomain().get(m) != var2
												.getDomain().get(l)) {
									int holdDomain3 = holdDomain2
											- var3.getDomain().get(m);
									for (int n = 0; n < var4.getDomain().size(); n++) {
										if (holdDomain3 != var5.getDomain()
												.get(j)
												&& holdDomain3 != var1
														.getDomain().get(k)
												&& holdDomain3 != var2
														.getDomain().get(l)
												&& holdDomain3 != var3
														.getDomain().get(m)) {
											if (holdDomain3 == var4.getDomain()
													.get(n)) {
												checkIfExists = true;
											}
										}

									}
								}
							}
						}
					}
				}
			}
			if (checkIfExists == false) {
				var5.reduceDomainList(var5.getDomain().get(j));
			}
		}
		// Reducing the domains here
		var1.reduceDomain();
		var2.reduceDomain();
		var3.reduceDomain();
		var4.reduceDomain();
		var5.reduceDomain();
	}

	private void fourNaryConsistency(Constraint c) {
		Variable var1 = c.getConstraintVariables().get(0);
		Variable var2 = c.getConstraintVariables().get(1);
		Variable var3 = c.getConstraintVariables().get(2);
		Variable var4 = c.getConstraintVariables().get(3);

		int constrNum = c.getConstraintNum();

		// For var1
		for (int j = 0; j < var1.getDomain().size(); j++) {
			int constrNumplusDomain = constrNum - var1.getDomain().get(j);

			boolean checkIfExists = false;
			for (int k = 0; k < var2.getDomain().size(); k++) {
				if (var1.getDomain().get(j) != var2.getDomain().get(k)) {
					int holdDomain = constrNumplusDomain
							- var2.getDomain().get(k);

					for (int l = 0; l < var3.getDomain().size(); l++) {
						if (var3.getDomain().get(l) != var1.getDomain().get(j)
								&& var2.getDomain().get(k) != var3.getDomain()
										.get(l)) {
							int holdDomain2 = holdDomain
									- var3.getDomain().get(l);

							for (int m = 0; m < var4.getDomain().size(); m++) {
								if (holdDomain2 != var1.getDomain().get(j)
										&& holdDomain2 != var2.getDomain().get(
												k)
										&& holdDomain2 != var3.getDomain().get(
												l)) {
									if (holdDomain2 == var4.getDomain().get(m)) {
										checkIfExists = true;
									}
								}
							}
						}
					}
				}
			}
			if (checkIfExists == false) {
				var1.reduceDomainList(var1.getDomain().get(j));
			}

		}

		// For var2
		for (int j = 0; j < var2.getDomain().size(); j++) {
			int constrNumplusDomain = constrNum - var2.getDomain().get(j);

			boolean checkIfExists = false;
			for (int k = 0; k < var3.getDomain().size(); k++) {
				if (var2.getDomain().get(j) != var3.getDomain().get(k)) {
					int holdDomain = constrNumplusDomain
							- var3.getDomain().get(k);

					for (int l = 0; l < var4.getDomain().size(); l++) {
						if (var4.getDomain().get(l) != var2.getDomain().get(j)
								&& var3.getDomain().get(k) != var4.getDomain()
										.get(l)) {
							int holdDomain2 = holdDomain
									- var4.getDomain().get(l);

							for (int m = 0; m < var1.getDomain().size(); m++) {
								if (holdDomain2 != var2.getDomain().get(j)
										&& holdDomain2 != var3.getDomain().get(
												k)
										&& holdDomain2 != var4.getDomain().get(
												l)) {
									if (holdDomain2 == var1.getDomain().get(m)) {
										checkIfExists = true;
									}
								}
							}
						}
					}
				}
			}
			if (checkIfExists == false) {
				var2.reduceDomainList(var2.getDomain().get(j));
			}

		}

		// For var3
		for (int j = 0; j < var3.getDomain().size(); j++) {
			int constrNumplusDomain = constrNum - var3.getDomain().get(j);

			boolean checkIfExists = false;
			for (int k = 0; k < var4.getDomain().size(); k++) {
				if (var3.getDomain().get(j) != var4.getDomain().get(k)) {
					int holdDomain = constrNumplusDomain
							- var4.getDomain().get(k);

					for (int l = 0; l < var1.getDomain().size(); l++) {
						if (var1.getDomain().get(l) != var3.getDomain().get(j)
								&& var4.getDomain().get(k) != var1.getDomain()
										.get(l)) {
							int holdDomain2 = holdDomain
									- var1.getDomain().get(l);

							for (int m = 0; m < var2.getDomain().size(); m++) {
								if (holdDomain2 != var3.getDomain().get(j)
										&& holdDomain2 != var4.getDomain().get(
												k)
										&& holdDomain2 != var1.getDomain().get(
												l)) {
									if (holdDomain2 == var2.getDomain().get(m)) {
										checkIfExists = true;
									}
								}
							}
						}
					}
				}
			}
			if (checkIfExists == false) {
				var3.reduceDomainList(var3.getDomain().get(j));
			}

		}
		// For var 4
		for (int j = 0; j < var4.getDomain().size(); j++) {
			int constrNumplusDomain = constrNum - var4.getDomain().get(j);

			boolean checkIfExists = false;
			for (int k = 0; k < var1.getDomain().size(); k++) {
				if (var4.getDomain().get(j) != var1.getDomain().get(k)) {
					int holdDomain = constrNumplusDomain
							- var1.getDomain().get(k);

					for (int l = 0; l < var2.getDomain().size(); l++) {
						if (var2.getDomain().get(l) != var4.getDomain().get(j)
								&& var1.getDomain().get(k) != var2.getDomain()
										.get(l)) {
							int holdDomain2 = holdDomain
									- var2.getDomain().get(l);

							for (int m = 0; m < var3.getDomain().size(); m++) {
								if (holdDomain2 != var4.getDomain().get(j)
										&& holdDomain2 != var1.getDomain().get(
												k)
										&& holdDomain2 != var2.getDomain().get(
												l)) {
									if (holdDomain2 == var3.getDomain().get(m)) {
										checkIfExists = true;
									}
								}
							}
						}
					}
				}
			}
			if (checkIfExists == false) {
				var4.reduceDomainList(var4.getDomain().get(j));
			}

		}

		// Reducing the domains here
		var1.reduceDomain();
		var2.reduceDomain();
		var3.reduceDomain();
		var4.reduceDomain();

	}

	/**
	 * Arc consistency for inequalities
	 * 
	 * @param c
	 */
	private void constraintSatisfaction(Constraint c) {
		Variable var1 = c.getConstraintVariables().get(0);
		Variable var2 = c.getConstraintVariables().get(1);

		if (var1.getDomain().size() == 1) {
			var2.reduceDomainList(var1.getDomain().get(0));
			var2.reduceDomain();

		}
		if (var2.getDomain().size() == 1) {
			var1.reduceDomainList(var2.getDomain().get(0));
			var1.reduceDomain();

		}
	}

	private void threeNaryAddConstraint(Constraint c) {
		Variable var1 = c.getConstraintVariables().get(0);
		Variable var2 = c.getConstraintVariables().get(1);
		Variable var3 = c.getConstraintVariables().get(2);

		int constrNum = c.getConstraintNum();

		for (int j = 0; j < var1.getDomain().size(); j++) {
			int constrNumplusDomain = constrNum - var1.getDomain().get(j);

			boolean checkIfExists = false;
			for (int k = 0; k < var2.getDomain().size(); k++) {
				if (var1.getDomain().get(j) != var2.getDomain().get(k)) {
					int holdDomain = constrNumplusDomain
							- var2.getDomain().get(k);

					for (int l = 0; l < var3.getDomain().size(); l++) {
						if (holdDomain != var1.getDomain().get(j)
								&& var2.getDomain().get(k) != holdDomain) {
							if (holdDomain == var3.getDomain().get(l)) {

								checkIfExists = true;
							}
						}
					}

				}
			}
			if (checkIfExists == false) {

				var1.reduceDomainList(var1.getDomain().get(j));
			}

		}

		for (int j = 0; j < var2.getDomain().size(); j++) {
			int constrNumplusDomain = constrNum - var2.getDomain().get(j);
			boolean checkIfExists = false;
			for (int k = 0; k < var3.getDomain().size(); k++) {
				if (var2.getDomain().get(j) != var3.getDomain().get(k)) {
					int holdDomain = constrNumplusDomain
							- var3.getDomain().get(k);

					for (int l = 0; l < var1.getDomain().size(); l++) {
						if (holdDomain != var3.getDomain().get(k)
								&& holdDomain != var2.getDomain().get(j)) {
							if (holdDomain == var1.getDomain().get(l)) {
								checkIfExists = true;
							}
						}
					}

				}
			}
			if (checkIfExists == false) {
				var2.reduceDomainList(var2.getDomain().get(j));
			}

		}

		for (int j = 0; j < var3.getDomain().size(); j++) {
			int constrNumplusDomain = constrNum - var3.getDomain().get(j);
			boolean checkIfExists = false;
			for (int k = 0; k < var1.getDomain().size(); k++) {
				if (var3.getDomain().get(j) != var1.getDomain().get(k)) {
					int holdDomain = constrNumplusDomain
							- var1.getDomain().get(k);

					for (int l = 0; l < var2.getDomain().size(); l++) {
						if (holdDomain != var1.getDomain().get(k)
								&& holdDomain != var3.getDomain().get(j)) {
							if (holdDomain == var2.getDomain().get(l)) {
								checkIfExists = true;
							}
						}
					}

				}
			}
			if (checkIfExists == false) {
				var3.reduceDomainList(var3.getDomain().get(j));
			}

		}

		// Reducing the domains here
		var1.reduceDomain();
		var2.reduceDomain();
		var3.reduceDomain();
	}

	/**
	 * This method performs the divide arc consistency for two variables
	 * 
	 * @param c
	 */
	private void divArcConsistency(Constraint c) {

		Variable var1 = c.getConstraintVariables().get(0);
		Variable var2 = c.getConstraintVariables().get(1);
		int constrNum = c.getConstraintNum();

		// For Variable1
		for (int j = 0; j < var1.getDomain().size(); j++) {
			boolean checkIfExists = false;
			for (int k = 0; k < var2.getDomain().size(); k++) {
				// We want the exact value while dividing so we are casting the
				// domains to double in order to avoid integer division
				if (((double) var1.getDomain().get(j) / (double) var2
						.getDomain().get(k)) == constrNum
						|| ((double) var2.getDomain().get(k) / (double) var1
								.getDomain().get(j)) == constrNum) {
					checkIfExists = true;
				}
			}
			if (checkIfExists == false) {
				var1.reduceDomainList(var1.getDomain().get(j));
			}
		}

		// For variable2
		for (int i = 0; i < var2.getDomain().size(); i++) {
			boolean checkIfExists = false;
			for (int k = 0; k < var1.getDomain().size(); k++) {
				// We want the exact value while dividing so we are casting the
				// domains to double in order to avoid integer division
				if ((double) var2.getDomain().get(i)
						/ (double) var1.getDomain().get(k) == constrNum
						|| (double) var1.getDomain().get(k)
								/ (double) var2.getDomain().get(i) == constrNum) {
					checkIfExists = true;
				}
			}
			if (checkIfExists == false) {
				var2.reduceDomainList(var2.getDomain().get(i));
			}
		}

		// Reducing the domains here
		var1.reduceDomain();
		var2.reduceDomain();
	}

	/**
	 * This method performs the multiplicative arc-consistency for 2 variables
	 * 
	 * @param c
	 */
	private void mulArcConsistency(Constraint c) {
		Variable var1 = c.getConstraintVariables().get(0);
		Variable var2 = c.getConstraintVariables().get(1);
		int constrNum = c.getConstraintNum();

		// For Variable1
		for (int j = 0; j < var1.getDomain().size(); j++) {
			// We want the exact value while dividing so we are casting the
			// domains to double in order to avoid integer division
			double constrNumplusDomain = (double) constrNum
					/ (double) var1.getDomain().get(j);
			boolean checkIfExists = false;
			for (int k = 0; k < var2.getDomain().size(); k++) {
				if (constrNumplusDomain != var1.getDomain().get(j)) {
					if (constrNumplusDomain == var2.getDomain().get(k)) {
						checkIfExists = true;
					}
				}
			}
			if (checkIfExists == false) {
				var1.reduceDomainList(var1.getDomain().get(j));
			}
		}

		// For variable2
		for (int i = 0; i < var2.getDomain().size(); i++) {
			// We want the exact value while dividing so we are casting the
			// domains to double in order to avoid integer division
			double constrNumplusDomain2 = (double) constrNum
					/ (double) var2.getDomain().get(i);
			boolean checkIfExists = false;
			for (int k = 0; k < var1.getDomain().size(); k++) {
				if (constrNumplusDomain2 != var2.getDomain().get(i)) {
					if (constrNumplusDomain2 == var1.getDomain().get(k)) {
						checkIfExists = true;
					}
				}
			}
			if (checkIfExists == false) {
				var2.reduceDomainList(var2.getDomain().get(i));
			}
		}

		// Reducing the domains here
		var1.reduceDomain();
		var2.reduceDomain();
	}

	public void threeNaryMulConsistencey(Constraint c) {
		Variable var1 = c.getConstraintVariables().get(0);
		Variable var2 = c.getConstraintVariables().get(1);
		Variable var3 = c.getConstraintVariables().get(2);

		int constrNum = c.getConstraintNum();

		for (int j = 0; j < var1.getDomain().size(); j++) {
			// We want the exact value while dividing so we are casting the
			// domains to double in order to avoid integer division
			double constrNumplusDomain = (double) constrNum
					/ (double) var1.getDomain().get(j);
			boolean checkIfExists = false;
			for (int k = 0; k < var2.getDomain().size(); k++) {
				if (var1.getDomain().get(j) != var2.getDomain().get(k)) {
					double holdDomain = constrNumplusDomain
							/ (double) var2.getDomain().get(k);

					for (int l = 0; l < var3.getDomain().size(); l++) {
						if (holdDomain != var1.getDomain().get(j)
								&& var2.getDomain().get(k) != holdDomain) {
							if (holdDomain == var3.getDomain().get(l)) {
								checkIfExists = true;
							}
						}
					}

				}
			}
			if (checkIfExists == false) {

				var1.reduceDomainList(var1.getDomain().get(j));
			}

		}

		for (int j = 0; j < var2.getDomain().size(); j++) {
			// We want the exact value while dividing so we are casting the
			// domains to double in order to avoid integer division
			double constrNumplusDomain = (double) constrNum
					/ (double) var2.getDomain().get(j);
			boolean checkIfExists = false;
			for (int k = 0; k < var1.getDomain().size(); k++) {
				if (var2.getDomain().get(j) != var1.getDomain().get(k))
				// if(j!=k)
				{
					double holdDomain = constrNumplusDomain
							/ (double) var1.getDomain().get(k);

					for (int l = 0; l < var3.getDomain().size(); l++) {
						if (var1.getDomain().get(k) != holdDomain
								&& holdDomain != var2.getDomain().get(j))

						{
							if (holdDomain == var3.getDomain().get(l)) {
								checkIfExists = true;
							}
						}
					}

				}
			}
			if (checkIfExists == false) {
				var2.reduceDomainList(var2.getDomain().get(j));
			}

		}

		for (int j = 0; j < var3.getDomain().size(); j++) {
			// We want the exact value while dividing so we are casting the
			// domains to double in order to avoid integer division
			double constrNumplusDomain = (double) constrNum
					/ (double) var3.getDomain().get(j);
			boolean checkIfExists = false;
			for (int k = 0; k < var1.getDomain().size(); k++) {
				if (var3.getDomain().get(j) != var1.getDomain().get(k)) {
					double holdDomain = constrNumplusDomain
							/ (double) var1.getDomain().get(k);

					for (int l = 0; l < var2.getDomain().size(); l++) {
						if (var1.getDomain().get(k) != holdDomain
								&& holdDomain != var3.getDomain().get(j))

						{
							if (holdDomain == var2.getDomain().get(l)) {
								checkIfExists = true;
							}
						}
					}

				}
			}
			if (checkIfExists == false) {
				var3.reduceDomainList(var3.getDomain().get(j));
			}

		}

		// Reducing the domains here
		var1.reduceDomain();
		var2.reduceDomain();
		var3.reduceDomain();
	}

	/**
	 * This method performs the fourNary multiplication consistency for 4
	 * variables
	 * 
	 * @param c
	 */
	public void fourNaryMulConsistency(Constraint c) {
		Variable var1 = c.getConstraintVariables().get(0);
		Variable var2 = c.getConstraintVariables().get(1);
		Variable var3 = c.getConstraintVariables().get(2);
		Variable var4 = c.getConstraintVariables().get(3);

		int constrNum = c.getConstraintNum();

		// For var1
		for (int j = 0; j < var1.getDomain().size(); j++) {
			// We want the exact value while dividing so we are casting the
			// domains to double in order to avoid integer division
			double constrNumplusDomain = (double) constrNum
					/ (double) var1.getDomain().get(j);
			boolean checkIfExists = false;
			for (int k = 0; k < var2.getDomain().size(); k++) {
				if (var1.getDomain().get(j) != var2.getDomain().get(k)) {
					double holdDomain = constrNumplusDomain
							/ (double) var2.getDomain().get(k);

					for (int l = 0; l < var3.getDomain().size(); l++) {
						if (var3.getDomain().get(l) != var1.getDomain().get(j)
								&& var3.getDomain().get(l) != var2.getDomain()
										.get(k)) {
							double holdDomain2 = holdDomain
									/ (double) var3.getDomain().get(l);
							{
								for (int m = 0; m < var4.getDomain().size(); m++) {
									if (holdDomain2 != var1.getDomain().get(j)
											&& holdDomain2 != var2.getDomain()
													.get(k)
											&& holdDomain2 != var3.getDomain()
													.get(l)) {
										if (holdDomain2 == var4.getDomain()
												.get(m)) {
											checkIfExists = true;
										}
									}
								}
							}
						}
					}

				}
			}
			if (checkIfExists == false) {
				var1.reduceDomainList(var1.getDomain().get(j));
			}

		}

		// For var2
		for (int j = 0; j < var2.getDomain().size(); j++) {
			// We want the exact value while dividing so we are casting the
			// domains to double in order to avoid integer division
			double constrNumplusDomain = (double) constrNum
					/ (double) var2.getDomain().get(j);
			boolean checkIfExists = false;
			for (int k = 0; k < var3.getDomain().size(); k++) {
				if (var2.getDomain().get(j) != var3.getDomain().get(k)) {
					double holdDomain = constrNumplusDomain
							/ (double) var3.getDomain().get(k);

					for (int l = 0; l < var4.getDomain().size(); l++) {
						if (var4.getDomain().get(l) != var2.getDomain().get(j)
								&& var4.getDomain().get(l) != var3.getDomain()
										.get(k)) {
							double holdDomain2 = holdDomain
									/ (double) var4.getDomain().get(l);
							{
								for (int m = 0; m < var1.getDomain().size(); m++) {
									if (holdDomain2 != var2.getDomain().get(j)
											&& holdDomain2 != var3.getDomain()
													.get(k)
											&& holdDomain2 != var4.getDomain()
													.get(l)) {
										if (holdDomain2 == var1.getDomain()
												.get(m)) {
											checkIfExists = true;
										}
									}
								}
							}
						}
					}

				}
			}
			if (checkIfExists == false) {

				var2.reduceDomainList(var2.getDomain().get(j));
			}

		}

		// For var3
		for (int j = 0; j < var3.getDomain().size(); j++) {
			// We want the exact value while dividing so we are casting the
			// domains to double in order to avoid integer division
			double constrNumplusDomain = (double) constrNum
					/ (double) var3.getDomain().get(j);
			boolean checkIfExists = false;
			for (int k = 0; k < var4.getDomain().size(); k++) {
				if (var3.getDomain().get(j) != var4.getDomain().get(k)) {
					double holdDomain = constrNumplusDomain
							/ (double) var4.getDomain().get(k);

					for (int l = 0; l < var1.getDomain().size(); l++) {
						if (var1.getDomain().get(l) != var3.getDomain().get(j)
								&& var1.getDomain().get(l) != var4.getDomain()
										.get(k)) {
							double holdDomain2 = holdDomain
									/ (double) var1.getDomain().get(l);
							{
								for (int m = 0; m < var2.getDomain().size(); m++) {
									if (holdDomain2 != var3.getDomain().get(j)
											&& holdDomain2 != var4.getDomain()
													.get(k)
											&& holdDomain2 != var1.getDomain()
													.get(l)) {
										if (holdDomain2 == var2.getDomain()
												.get(m)) {
											checkIfExists = true;
										}
									}
								}
							}
						}
					}

				}
			}
			if (checkIfExists == false) {

				var3.reduceDomainList(var3.getDomain().get(j));
			}

		}

		// For var4
		for (int j = 0; j < var4.getDomain().size(); j++) {
			// We want the exact value while dividing so we are casting the
			// domains to double in order to avoid integer division
			double constrNumplusDomain = (double) constrNum
					/ (double) var4.getDomain().get(j);
			boolean checkIfExists = false;
			for (int k = 0; k < var1.getDomain().size(); k++) {
				if (var4.getDomain().get(j) != var1.getDomain().get(k)) {
					double holdDomain = constrNumplusDomain
							/ (double) var1.getDomain().get(k);

					for (int l = 0; l < var2.getDomain().size(); l++) {
						if (var2.getDomain().get(l) != var4.getDomain().get(j)
								&& var2.getDomain().get(l) != var1.getDomain()
										.get(k)) {
							double holdDomain2 = holdDomain
									/ (double) var2.getDomain().get(l);
							{
								for (int m = 0; m < var3.getDomain().size(); m++) {
									if (holdDomain2 != var4.getDomain().get(j)
											&& holdDomain2 != var1.getDomain()
													.get(k)
											&& holdDomain2 != var2.getDomain()
													.get(l)) {
										if (holdDomain2 == var3.getDomain()
												.get(m)) {
											checkIfExists = true;
										}
									}
								}
							}
						}
					}

				}
			}
			if (checkIfExists == false) {

				var4.reduceDomainList(var4.getDomain().get(j));
			}

		}

		// Reducing the domains here
		var1.reduceDomain();
		var2.reduceDomain();
		var3.reduceDomain();
		var4.reduceDomain();
	}

	/**
	 * This method performs the subtraction arc-consistency for 2 variables
	 * 
	 * @param c
	 */
	private void subArcConsistency(Constraint c) {
		Variable var1 = c.getConstraintVariables().get(0);
		Variable var2 = c.getConstraintVariables().get(1);
		int constrNum = c.getConstraintNum();

		// For Variable1
		for (int j = 0; j < var1.getDomain().size(); j++) {
			boolean checkIfExists = false;
			for (int k = 0; k < var2.getDomain().size(); k++) {
				if (Math.abs(var1.getDomain().get(j) - var2.getDomain().get(k)) == constrNum) {
					checkIfExists = true;
				}
			}
			if (checkIfExists == false) {
				var1.reduceDomainList(var1.getDomain().get(j));
			}
		}

		// For variable2
		for (int i = 0; i < var2.getDomain().size(); i++) {
			boolean checkIfExists = false;
			for (int k = 0; k < var1.getDomain().size(); k++) {
				if (Math.abs(var2.getDomain().get(i) - var1.getDomain().get(k)) == constrNum) {
					checkIfExists = true;
				}
			}
			if (checkIfExists == false) {
				var2.reduceDomainList(var2.getDomain().get(i));
			}
		}
		// Reducing the domains here
		var1.reduceDomain();
		var2.reduceDomain();
	}

	/**
	 * This method performs the additive arc-consistency for 2 variables
	 * 
	 * @param c
	 */
	private void addArcConsistency(Constraint c) {
		Variable var1 = c.getConstraintVariables().get(0);
		Variable var2 = c.getConstraintVariables().get(1);
		int constrNum = c.getConstraintNum();

		// For Variable1
		for (int j = 0; j < var1.getDomain().size(); j++) {
			int constrNumplusDomain = constrNum - var1.getDomain().get(j);
			boolean checkIfExists = false;
			for (int k = 0; k < var2.getDomain().size(); k++) {
				if (constrNumplusDomain == var2.getDomain().get(k)) {
					checkIfExists = true;
				}
			}
			if (checkIfExists == false) {
				var1.reduceDomainList(var1.getDomain().get(j));
			}
		}

		// For variable2
		for (int i = 0; i < var2.getDomain().size(); i++) {
			int constrNumplusDomain2 = constrNum - var2.getDomain().get(i);
			boolean checkIfExists = false;
			for (int k = 0; k < var1.getDomain().size(); k++) {
				if (constrNumplusDomain2 == var1.getDomain().get(k)) {
					checkIfExists = true;
				}
			}
			if (checkIfExists == false) {
				var2.reduceDomainList(var2.getDomain().get(i));
			}
		}

		// Reducing the domains here
		var1.reduceDomain();
		var2.reduceDomain();
	}

	/**
	 * This method assigns the value the user enters to the variable Extra
	 * credit
	 * 
	 * @param r
	 * @param c
	 */
	public void generateMove(int r, int c) {

		String displayMesg = JOptionPane.showInputDialog("Enter the number");
		if (displayMesg != null) {
			int assign = Integer.parseInt(displayMesg);
			synchronized (allvars) {
				allvars[r][c].clearDomain();
				allvars[r][c].addDomain(assign);
			}
		}
		// System.out.println(allvars[r][c].getDomain());
	}

	/**
	 * This method is used to solve the puzzle Calls ArcConsistency method Calls
	 * formInEqualityConstr
	 */
	public void solve() {

		ArcConsistency();

		// if (isDeadEnd())
		// {
		// boolean isSolvable = backTrackSearch();
		// if (!isSolvable) {
		// JOptionPane.showMessageDialog(null, "Can't be solved");
		// }
		// }

		// Prints out the domains in the variable every time user clicks the

		for (int i = 0; i < rowSize; i++) {
			for (int j = 0; j < colSize; j++) {
				System.out.print(allvars[i][j].getDomain() + " ");
			}
			System.out.println();
		}
	}

	/**
	 * This method separates the list of assigned and unassigned variables
	 */
	public void AssignedAndUnAssigned() {
		unassigned = new Stack<Variable>();
		assigned = new Stack<Variable>();
		// Stack.push pushes the value on the top of the stack every time
		for (int i = rowSize - 1; i >= 0; i--) {
			for (int j = colSize - 1; j >= 0; j--) {
				if (allvars[i][j].getDomain().size() > 1) {
					unassigned.push(allvars[i][j]);
				} else {
					assigned.push(allvars[i][j]);
				}
			}
		}
	}

	/**
	 * This method performs backTracking
	 */
	public boolean backTrackSearch() {
		boolean done = false;
		Variable var = null;

		if (unassigned.isEmpty()) {
			done = true;
			return done;
		}

		else {
			var = unassigned.pop();
			synchronized (allvars) {
				var.isAssigned = true;
			}
			for (int i : var.getDomain()) {
				synchronized (allvars) {
					var.assignment = i;
				}
				boolean valid = checkConstraints(var);
				if (valid) {
					assigned.push(var);
					if (backTrackSearch() == true) {
						return true;
					}
				} else {
					assigned.pop();
				}
			}
			var.assignment = 0;
			var.isAssigned = false;
			unassigned.push(var);
		}
		return false;
	}

	private boolean checkConstraints(Variable variable) {
		boolean valid = false;
		switch (variable.constraint.getConstraintOperator()) {
		case "+": {
			valid = CheckAddition(variable);
			break;
		}
		case "-": {
			valid = CheckSubtraction(variable);
			break;
		}
		case "x":
		case "X": {
			valid = CheckMultiplication(variable);
			break;
		}
		case "/": {
			valid = CheckDivision(variable);
			break;
		}
		default:
			break;
		}

		return valid;
	}

	private boolean CheckAddition(Variable variable) {
		int sum = variable.assignment;
		int assignedCount = 0;
		Constraint temp = variable.constraint;
		for (Variable hold : temp.getConstraintVariables()) {
			if (hold.isAssigned && hold != variable) {
				sum += hold.assignment;
				assignedCount++;
			}
		}
		if ((sum < temp.getConstraintNum() && assignedCount < temp
				.getConstraintVariables().size())
				|| (sum == temp.getConstraintNum() && assignedCount == temp
						.getConstraintVariables().size())) {

			return true;
		}
		return false;
	}

	private boolean CheckSubtraction(Variable variable) {
		int result = 0;
		int assignedCount = 0;
		Constraint temp = variable.constraint;
		for (Variable hold : temp.getConstraintVariables()) {
			if (hold.isAssigned && hold.assignment != variable.assignment) {
				result = hold.assignment - variable.assignment;
				assignedCount++;
			}
		}

		if ((Math.abs(result) != variable.constraint.getConstraintNum() && assignedCount < temp
				.getConstraintVariables().size())
				|| (Math.abs(result) == variable.constraint.getConstraintNum() && assignedCount == temp
						.getConstraintVariables().size())) {
			return true;
		}
		return false;
	}

	private boolean CheckMultiplication(Variable variable) {
		int mul = variable.assignment;
		int assignedCount = 0;
		Constraint temp = variable.constraint;
		for (Variable hold : temp.getConstraintVariables()) {
			if (hold.isAssigned && hold.assignment != variable.assignment) {
				mul *= hold.assignment;
				assignedCount++;
			}
		}
		if ((mul <= variable.constraint.getConstraintNum() && assignedCount != temp
				.getConstraintVariables().size())
				|| (mul == variable.constraint.getConstraintNum() && assignedCount == temp
						.getConstraintVariables().size())) {
			return true;
		}
		return false;
	}

	private boolean CheckDivision(Variable variable) {
		double result = variable.assignment;
		int assignedCount = 0;
		Constraint temp = variable.constraint;
		for (Variable hold : temp.getConstraintVariables()) {
			if (hold.isAssigned && hold.assignment < variable.assignment) {
				result /= (double) hold.assignment;
				assignedCount++;
			} else if (hold.isAssigned && hold.assignment > variable.assignment) {
				result = (double) hold.assignment / result;
				assignedCount++;
			}
		}
		if ((result < temp.getConstraintNum() && assignedCount < temp
				.getConstraintVariables().size())
				|| (result == temp.getConstraintNum() && assignedCount == temp
						.getConstraintVariables().size())) {
			return true;
		}
		return false;
	}

	/**
	 * This method checks if the puzzle has been solved or not
	 * 
	 * @return true or false
	 */
	public boolean isSolved() {
		for (int i = 0; i < rowSize; i++) {
			for (int j = 0; j < colSize; j++) {
				if (allvars[i][j].getDomain().size() != 1) {
					return false;
				}
			}
		}
		return true;

	}
	// /**
	// * This method checks if there is any variables that have empty domain
	// *
	// * @return deadEnd
	// */
	// public boolean isDeadEnd() {
	// boolean deadEnd = false;
	// for (int i = 0; i < rowSize; i++) {
	// for (int j = 0; j < colSize; j++) {
	// if (allvars[i][j] == null) {
	// deadEnd = true;
	// }
	// }
	// }
	// return deadEnd;
	// }
}
