/**
 * This class extends the JFrame window
 * This class contains the main method
 * This class contains method for creating the JFrame
 * Purpose: This class will display a GUI panel where user can select desired options
 * Last Updated: April 29, 2016
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
	protected int win_wid = 750;
	protected int win_hei = 750;
	private String starterFile = "KK_33.txt";

	private JMenuBar mBar;
	private JMenu file;
	private JMenu solve;
	private JMenu help;
	private JMenu mode;

	private JMenuItem loadFile;
	private JMenuItem playAgain;
	private JMenuItem saveFile;
	private JMenuItem exit;
	private JMenuItem run;
	private JMenuItem backTrack;
	private JMenuItem helpMenu;
	protected JMenuItem userMode;

	private File input;
	private KenKenPuzzle kenkenPuzzle;
	private KenKenDisplay kenkenDisplay;

	private String intro = "This is a kenken solver program \n"
			+ "Asks the user to choose the kenken puzzle\n"
			+ "has a Auto-mode to solve the puzzle using Artificial Intelligence\n"
			+ "(Arc, Node-consistency,inequality constraint, k consistency and backtracking) to solve the puzzle\n"
			+ "has a Manual mode \n User can click on desired cells of the puzzle to enter any number that may satisfy the constraints";

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
		JOptionPane.showMessageDialog(null, intro, "Kenken-Program", 1);
		this.setTitle("KenKen Puzzle");
		this.setSize(win_wid, win_hei);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);

		// Create the menu bar and put it in the window

		JMenuBar bar = buildMenuBar();
		setJMenuBar(bar);

		// So that the puzzle always start with a 3*3 kenken

		input = new File(starterFile);
		kenkenPuzzle = new KenKenPuzzle(input);
		kenkenDisplay = new KenKenDisplay(kenkenPuzzle);
		add(kenkenDisplay);
		repaint();
		this.setVisible(true);
	}

	/**
	 * This is the constructor for this class
	 */
	public KenKenWindow(File input) {
		if (kenkenPuzzle != null) {
			remove(kenkenDisplay);
		}
		kenkenPuzzle = new KenKenPuzzle(input);
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
		mode = buildModeMenu();
		help = buildHelpMenu();
		// Adding the Menus to the menuBar
		mBar.add(file);
		mBar.add(solve);
		mBar.add(mode);
		mBar.add(help);
		return mBar;
	}

	/**
	 * This method builds the menu items in the menu option in the JFrame window
	 * 
	 * @return mode
	 */
	private JMenu buildModeMenu() {
		mode = new JMenu("Manual");
		userMode = new JMenuItem("User-Mode");

		// Hook up the menu items with the listener

		MyListener listener = new MyListener();
		userMode.addActionListener(listener);
		// Adding the JMenuItem to the JMenu
		mode.add(userMode);
		return mode;
	}

	/**
	 * This method builds the menu items in the menu option in the JFrame window
	 * 
	 * @return help
	 */
	private JMenu buildHelpMenu() {
		help = new JMenu("Game-Assist");
		helpMenu = new JMenuItem("Help");
		// Hook up the menu items with the listener

		MyListener listener = new MyListener();
		helpMenu.addActionListener(listener);
		// Adding the JMenuItem to the JMenu
		help.add(helpMenu);
		return help;
	}

	/**
	 * This method builds the menu items in the menu option in the JFrame window
	 * 
	 * @return solve
	 */
	private JMenu buildSolveMenu() {
		solve = new JMenu("Auto-Mode");
		playAgain = new JMenuItem("Play Again");
		backTrack = new JMenuItem("BackTrack-Solve");
		run = new JMenuItem("CSP-Solve ");

		// Hook up the menu items with the listener

		MyListener listener = new MyListener();
		playAgain.addActionListener(listener);
		run.addActionListener(listener);
		backTrack.addActionListener(listener);

		// Adding the JMenuItem to the JMenu
		solve.add(run);
		solve.add(backTrack);
		solve.add(playAgain);
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
		exit = new JMenuItem("Exit");

		// Hook up the menu items with the listener
		MyListener listener = new MyListener();
		loadFile.addActionListener(listener);
		exit.addActionListener(listener);

		// Adding the JMenuItem to the menus
		file.add(loadFile);
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

			// Checks if the exitMenuItem is clicked
			if (e.getSource() == exit) {
				System.exit(0);
			}

			// Checks if the with_constr_consist is clicked
			if (e.getSource() == run) {
				kenkenPuzzle.solve();

				repaint();
				// Displays the winning message
				if (kenkenPuzzle.isSolvable()) {
					JOptionPane.showMessageDialog(null,
							"Congratulation! You Solved the puzzle");
				}
			}
			if (e.getSource() == playAgain) {
				input = input;
				if (kenkenPuzzle != null) {
					remove(kenkenDisplay);
				}
				kenkenPuzzle = new KenKenPuzzle(input);
				kenkenDisplay = new KenKenDisplay(kenkenPuzzle);
				add(kenkenDisplay);
				repaint();
				setVisible(true);
			}
			// Checks if backTrack is clicked
			if (e.getSource() == backTrack) {
				kenkenPuzzle.runBacktracking();

				repaint();
				// Displays the winning message
				if (kenkenPuzzle.isSolvable()) {
					JOptionPane.showMessageDialog(null,
							"Congratulation! You Solved the puzzle");
				}
			}
			// Checks if userMode is clicked
			if (e.getSource() == userMode) {
				kenkenDisplay.Manual();
			}
			if (e.getSource() == helpMenu) {
				JOptionPane.showMessageDialog(null,
						"First run the CSP search\n"
								+ "If no changes in the domain and\n"
								+ "If no change in search space\n"
								+ "Run backTrack");
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
			chooser.setCurrentDirectory(new File("../AI-HW2"));
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
