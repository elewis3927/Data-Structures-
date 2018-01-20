/**
 * Class to demonstrate how simple behavior by "termites" can result
 * in seemingly intelligent behavior.
 * Written by Kim Bruce
 * 11/13/2017
 */

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class TermiteWorld extends JPanel {

	private static final double TERMITE_FRACTION = 0.03;

	private static final double CHIP_FRACTION = 0.25;

	// Size of cells for each woodchip
	public static final int CELLSIZE = 6;
	
	// Number of rows and columns of cells in the world
	public static final int WORLDSIZE = 20;
	  
	// world holding cells that may contain wood chips
	private Cell[][] world;
	
	// list of termites in simulation
	private List<Termite> termite = new ArrayList<Termite>();
	
	/**
	 *  Initialize world by placing wood chips in world. Create termites
	 *  and start them crawling
	 */
	public TermiteWorld() {
		// random number generator used to distribute wood chips and termites
		Random foodGen = new Random();
		world = new Cell[WORLDSIZE][WORLDSIZE];

		// Use random number generator to populate a fraction of the cells
		// with wood chips
		for (int row = 0; row < WORLDSIZE; row++) {
			for (int col = 0; col < WORLDSIZE; col++) {
				if (foodGen.nextDouble() < CHIP_FRACTION) {
					world[row][col] = new Cell(row, col, true);
				} else
					world[row][col] = new Cell(row, col, false);
			}
		}
		
		// Create termites in world and start them
		for (int row = 0; row < WORLDSIZE; row++) {
			for (int col = 0; col < WORLDSIZE; col++) {
				if (foodGen.nextDouble() < TERMITE_FRACTION) {
					Termite t = new Termite(row, col, world, this);
					termite.add(t);
					t.start();
				}
			}
		}
		repaint();
	}
	
	/**
	 * Paint the wood chips and termites on the screen after any move.
	 */
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setPaint(Color.BLACK);
		for (int row = 0; row < WORLDSIZE; row++) {
			for (int col = 0; col < WORLDSIZE; col++) {
				// synchronize where the paint methods checks for emptyness 
				synchronized(world[row][col]){
					if (!world[row][col].isEmpty()) {
					g2.fill(world[row][col].getContents());
					}
				}
			}
		}	
		g2.setPaint(Color.RED);
		for (Termite t: termite) {
			Rectangle2D.Double c;
			c = new Rectangle2D.Double(t.getCol() * CELLSIZE,t.getRow() * CELLSIZE,
					CELLSIZE, CELLSIZE);
			if (t.gotOne()) {
				g2.setPaint(Color.GREEN);
			} else {
				g2.setPaint(Color.RED);
			}
			g2.fill(c);
		}
	}

	/**
	 * Create frame and TermiteWorld panel in the frame.
	 * @param s
	 */
	public static void main(String[] s) {
		JFrame f = new JFrame("Racing with Termites");
		f.add(new TermiteWorld());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(CELLSIZE * WORLDSIZE, CELLSIZE * WORLDSIZE);
		f.setVisible(true);
	}

}
