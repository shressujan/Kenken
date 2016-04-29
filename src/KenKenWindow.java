/**
 * This class extends the JFrame window
 * This class containts the main method
 * Purpose: This class will display a GUI panel where user can select desired options
 * Last Updated: March 23, 2016
 * @author Sujan
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class KenKenWindow extends JFrame {
	/**
	 * These are all the variables used in this class
	 */
	private int win_wid = 600;
	private int win_hei = 600;
	private String starterFile = "KK_33.txt";

	private JMenuBar mBar;
	private JMenu file;
	private JMenu solve;

	private JMenuItem loadFile;
	private JMenuItem saveFile;
	private JMenuItem exit;
	private JMenuItem with_constr_consist;

	private File input;
	private KenKenPuzzle kenkenPuzzle;
	private KenKenDisplay kenkenDisplay;

	private String intro = "This is a kenken solver program \n"
			+ "Asks the user to choose the kenken puzzle\n"
			+ "Uses Artificial intelligence (Arc, Node-consistency) to solve the puzzle";

	/**
	 * This is the main method of the program contains only an instance of the
	 * kenkenDriver class
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		KenKenWindow window = new KenKenWindow();
	}

	/**
	 * This is the constructor for this class
	 */
	public KenKenWindow() {
		JOptionPane.showMessageDialog(null, intro);
		this.setTitle("KenKen Puzzle");
		this.setSize(win_wid, win_hei);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);

		// Create the menu bar and put it in the window

		JMenuBar bar = buildMenuBar();
		setJMenuBar(bar);

		// So that the puzzle always start with a 3*3 kenken

		File file = new File(starterFile);
		kenkenPuzzle = new KenKenPuzzle(file);
		kenkenDisplay = new KenKenDisplay(kenkenPuzzle);
		add(kenkenDisplay);
		repaint();
		this.setVisible(true);
	}

	/**
	 * This sets the menu option in the JFrame window
	 * 
	 * @return mBar
	 */
	private JMenuBar buildMenuBar() {
		mBar = new JMenuBar();
		file = buildFileMenu();
		solve = buildSolveMenu();
		// Adding the Menus to the menuBar
		mBar.add(file);
		mBar.add(solve);
		return mBar;
	}

	/**
	 * This method builds the menu items in the menu option in the JFrame window
	 * 
	 * @return solve
	 */
	private JMenu buildSolveMenu() {
		solve = new JMenu("Solve");
		with_constr_consist = new JMenuItem("Run Constraint Consistency");

		// Hook up the menu items with the listener

		MyListener listener = new MyListener();
		with_constr_consist.addActionListener(listener);

		// Adding the JMenuItem to the JMenu
		solve.add(with_constr_consist);
		return solve;
	}

	/**
	 * This method builds the menu items in the menu option in the JFrame window
	 * 
	 * @return file
	 */
	private JMenu buildFileMenu() {
		file = new JMenu("File");

		loadFile = new JMenuItem("Load File");
		saveFile = new JMenuItem("Save File");
		exit = new JMenuItem("Exit");

		// Hook up the menu items with the listener
		MyListener listener = new MyListener();
		loadFile.addActionListener(listener);
		saveFile.addActionListener(listener);
		exit.addActionListener(listener);

		// Adding the JMenuItem to the menus
		file.add(loadFile);
		file.add(saveFile);
		file.add(exit);

		return file;
	}

	/**
	 * This class implements the listener interface
	 *
	 */
	public class MyListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// Checks for the items clicked in the JMenu
			if (e.getSource() == loadFile) {
				input = getFile();

				if (kenkenPuzzle != null) {
					remove(kenkenDisplay);
				}
				kenkenPuzzle = new KenKenPuzzle(input);
				kenkenDisplay = new KenKenDisplay(kenkenPuzzle);
				add(kenkenDisplay);
				repaint();
				setVisible(true);
			}
			// // Checks if the saveMenuItem is clicked
			// if (e.getSource() == saveFile) {
			// // Doesn't work right now
			// }

			// Checks if the exitMenuItem is clicked
			if (e.getSource() == exit) {
				System.exit(0);
			}
			// Checks if the with_constr_consist is clicked
			if (e.getSource() == with_constr_consist) {
				kenkenPuzzle.solve();
				repaint();
				// Displays the winning message
				if (kenkenPuzzle.isSolved()) {
					JOptionPane.showMessageDialog(null,
							"Congratulation! You Solved the puzzle");
				}
			}
		}
	}

	/**
	 * This method lets the user choose the file that user wants to load for the
	 * program
	 * 
	 * @return chooser.getSelectedFile()
	 */
	public static File getFile() {
		JFileChooser chooser;
		try {

			// Get the filename using JFileChoose
			chooser = new JFileChooser();
			int status = chooser.showOpenDialog(null);
			if (status != JFileChooser.APPROVE_OPTION) {
				System.out.println("No File Chosen");
				System.exit(0);
			}
			return chooser.getSelectedFile();
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			System.exit(0);
		}
		return null; // should never get here, but makes compiler happy
	}
}
