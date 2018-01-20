/**
 * Termite, by Ethan Lewis 11/19/17, vs 1.0
 */
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class Termite extends Thread {
	// current row and column of termite
	private int row, col;
	
	// Random number generatore to determine how termite wanders
	private Random motionGen;
	
	// World containing all of the cells to be displayed
	private Cell world[][];
	
	// Class responsible for writing paint method for repainting the screen
	private TermiteWorld drawing;
	
	// True if termite is carrying around a wood chip.
	private boolean hasAChip = false;
	
	// The wood chip carried by the termite if it has one, null otherwise
	private Rectangle2D.Double myChip = null;
	
	/**
	 * Construct a termite 
	 * @param row
	 * 		row where termite starts
	 * @param col
	 * 		column where termite starts
	 * @param world
	 * 		world of cells where chips are
	 * @param tw
	 * 		object responsible for repainting canvas
	 */
	public Termite(int row, int col, Cell[][] world, TermiteWorld tw) {
		this.world = world;
		this.row = row;
		this.col = col;
		motionGen = new Random();
		drawing = tw;
	}

	/**
	 * move one cell over in a random direction from current position and pause
	 * Can move up, down, left, right, in diagonal directions or stay in the
	 * same spot.
	 */
	private void move() {
		// INSERT CODE TO MAKE TERMITE MOVE ONE STEP
		
		//Changes col and row based of randomly generated numbers
		int randMove1 = -1 + motionGen.nextInt(3);
		int randMove2 = -1 + motionGen.nextInt(3);
		// uses modulo to wrap the termite around the edge of the board
		col = (randMove1 + col + 19) % 19;
		row = (randMove2 + row + 19) % 19;
		
		drawing.repaint();
		try{
			sleep (30);
		}catch (InterruptedException exc) {
			System.out.println("sleep interrupted!");
		}
	}
	/**
	 * run, takes in no parameters
	 * simply makes a move while the termite doesn't find a chip
	 * if it does find a chip, pick it up and move until over a space without a chip again
	 * drops the chip in that empty space and repeats steps
	 */
	public void run() {
	while (true){
		// while the termite does not have a chip
		while (!hasAChip){
			// lock up the specified cell
			synchronized (world[row][col]){
				// if its over a chip
				if (!world[row][col].isEmpty()){
					// pick up the chip, and set hasAChip to true 
					myChip = world[row][col].takeChip();
					hasAChip = true; 
					move();
				}
			}
			// make a move after hasAChip was changed in the while loop
			move();
		}
		
		
		// while has a chip is true
		while (hasAChip){
			// lock up the specified cell
			synchronized (world[row][col]){
				// if over an empty chip, place the chip down
				// set hasAChip to false
				// and myChip to null
				if  (world[row][col].isEmpty()){
					world[row][col].putChip(myChip);
					hasAChip = false; 
					myChip = null;
				}
			}
			// If over another chip, make another move
			move(); 
		}
//		// makes another move to restart loop
		move();
		}
	}
		
	/**
	 * @return true iff termite is carrying a wood chip
	 */
	public boolean gotOne() {
		return hasAChip;
	}

	/**
	 * @return row where termite is currently
	 */
	public int getRow() {
		return row;
	}
	
	/**
	 * @return column where termite is currently
	 */
	public int getCol() {
		return col;
	}

}