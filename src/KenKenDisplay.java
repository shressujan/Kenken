/**
 * This class extends the JPanel 
 * create the JPanel that is added to the JFrame
 * @author Sujan
 * last updated: March 23, 2016
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class KenKenDisplay extends JPanel {

	private int cellSize = 70;
	private int divWid = 4;
	private int start_X = 50;
	private int start_Y = 50;
	private int screenright = 480;
	private int screentop = 70;
	private int letterOffSet_Y = 18;
	private int letterOffSet_X = 2;
	private int offSetMiddle_X = 18;
	private int offSetMiddle_Y = 45;
	private KenKenPuzzle puzzle;
	protected int win_wid = 750;
	protected int win_hei = 750;

	Font bigFont = new Font("Arial", 1, 35);
	Font smallFont = new Font("Arial", 1, 22);

	/**
	 * This is a constructor for this class
	 */
	public KenKenDisplay(KenKenPuzzle puzzle) {
		this.puzzle = puzzle;
	}

	public void Manual() {
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				if (me.getX() >= start_X
						&& me.getX() <= start_X + (cellSize + divWid)
								* puzzle.colSize + divWid
						&& me.getY() >= start_Y
						&& me.getY() <= start_Y + (cellSize + divWid)
								* puzzle.rowSize + divWid) {
					processClick(me);
				}
			}
		});

	}

	public void processClick(MouseEvent me) {
		int x = me.getX();
		int y = me.getY();

		int selectedRow = (y - start_Y - divWid) / (cellSize + divWid);
		int selectedCol = (x - start_X - divWid) / (cellSize + divWid);
		puzzle.generateMove(selectedRow, selectedCol);
		repaint();
		puzzle.SearchSpace();
		if (puzzle.isSolvable()) {
			puzzle.solve();
			// Checks for the correct solution and eliminates incorrect
			// assigments for the cells
			if (puzzle.isSolvable()) {
				JOptionPane.showMessageDialog(null,
						"Congratulation! You Solved the puzzle");
			} else {
				JOptionPane.showMessageDialog(null,
						"Sorry! You didn't solve the puzzle");
			}
		}
	}

	/**
	 * This method creates the display for the puzzle
	 * 
	 * @param g
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		start_X = (win_wid / 2) - (puzzle.rowSize * (cellSize + divWid) / 2);
		start_Y = (win_hei / 2) - (puzzle.colSize * (cellSize + divWid) / 2);
		g.fillRect(start_X, start_Y, (cellSize + divWid) * puzzle.colSize
				+ divWid, (cellSize + divWid) * puzzle.rowSize + divWid);
		for (int row = 0; row < puzzle.rowSize; row++) {
			for (int col = 0; col < puzzle.colSize; col++) {
				g.setColor(Color.WHITE);
				g.fillRect(start_X + divWid + (cellSize + divWid) * col,
						start_Y + divWid + (cellSize + divWid) * row, cellSize,
						cellSize);
			}
		}

		for (int i = 0; i < puzzle.allConstraints.size(); i++) {
			// For building the borders of connected constraint cells
			int currentConstraintRow;
			int currentConstraintCol;
			for (int j = 0; j < puzzle.allConstraints.get(i)
					.getConstraintVariables().size(); j++) {
				g.setColor(Color.WHITE);
				currentConstraintRow = (puzzle.allConstraints.get(i)
						.getConstraintVariables().get(j).getRow());
				currentConstraintCol = (puzzle.allConstraints.get(i)
						.getConstraintVariables().get(j).getCol());
				if (puzzle.allConstraints.get(i).getConstraintVariables()
						.size() == 1) {
					g.fillRect(start_X + divWid + (cellSize + divWid)
							* currentConstraintCol, start_Y + divWid
							+ (cellSize + divWid) * currentConstraintRow,
							cellSize, cellSize);
				}
				try {
					for (int k = j + 1; k < puzzle.allConstraints.get(i)
							.getConstraintVariables().size(); k++) {
						int nextConstraintRow = puzzle.allConstraints.get(i)
								.getConstraintVariables().get(k).getRow();
						int nextConstraintCol = puzzle.allConstraints.get(i)
								.getConstraintVariables().get(k).getCol();
						if (currentConstraintRow == nextConstraintRow) {
							g.fillRect(start_X + divWid + (cellSize + divWid)
									* currentConstraintCol, start_Y + divWid
									+ (cellSize + divWid)
									* currentConstraintRow, cellSize + 3,
									cellSize);
						} else if (currentConstraintCol == nextConstraintCol) {
							g.fillRect(start_X + divWid + (cellSize + divWid)
									* currentConstraintCol, start_Y + divWid
									+ (cellSize + divWid)
									* currentConstraintRow, cellSize,
									cellSize + 3);
						}
					}
				} catch (Exception e) {
				}
			}

			// For writing out the constraints in specific cells in the puzzle

			for (int k = 0; k < puzzle.allConstraints.size(); k++) {
				g.setColor(Color.BLACK);
				g.setFont(smallFont);
				String constraintString;
				if (puzzle.allConstraints.get(k).getConstraintOperator()
						.equals("=")) {
					constraintString = puzzle.allConstraints.get(k)
							.getConstraintNum() + "";
				} else {
					constraintString = puzzle.allConstraints.get(k)
							.getConstraintNum()
							+ puzzle.allConstraints.get(k)
									.getConstraintOperator();
				}
				int firstConstraintCellRow = puzzle.allConstraints.get(k)
						.getConstraintVariables().get(0).getRow();
				int firstConstraintCellCol = puzzle.allConstraints.get(k)
						.getConstraintVariables().get(0).getCol();
				g.drawString("" + constraintString, start_X + divWid
						+ (cellSize + divWid) * firstConstraintCellCol
						+ letterOffSet_X, start_Y + divWid
						+ (cellSize + divWid) * firstConstraintCellRow
						+ letterOffSet_Y);
			}

			// For writing the value in the cell
			for (int row = 0; row < puzzle.rowSize; row++) {
				for (int col = 0; col < puzzle.colSize; col++) {
					if (puzzle.allvars[row][col].domain.list.size() == 1
							|| (puzzle.allvars[row][col].isAssigned)) {
						g.setColor(Color.GREEN);
						g.setFont(bigFont);
						if (puzzle.allvars[row][col].assignment != 0) {
							g.drawString(""
									+ puzzle.allvars[row][col].assignment,
									start_X + divWid + (cellSize + divWid)
											* col + offSetMiddle_X, start_Y
											+ divWid + (cellSize + divWid)
											* row + offSetMiddle_Y);
						}
					}
				}
			}
			g.setColor(Color.BLACK);
			g.setFont(smallFont);
			g.drawString("SearchSpace= " + puzzle.searchSpace, screenright,
					screentop);
		}
	}
}
