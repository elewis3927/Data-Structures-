/**
 * Class representing a cell possibly holding a wood chip.
 * Written by Kim Bruce
 * Date: 11/13/2017
 */
import java.awt.geom.Rectangle2D;

public class Cell {
	// location of cell in world
	int row, col;

	// wood chip if cell has a wood chip
	private Rectangle2D.Double contents;

	/**
	 * Create cell
	 * 
	 * @param row
	 *            row of cell
	 * @param col
	 *            column of cell in world
	 * @param isFilled
	 *            Whether there is a wood chip in the cell
	 */
	public Cell(int row, int col, boolean isFilled) {
		this.row = row;
		this.col = col;
		if (isFilled) {
			contents = new Rectangle2D.Double(col * TermiteWorld.CELLSIZE, row
					* TermiteWorld.CELLSIZE, TermiteWorld.CELLSIZE,
					TermiteWorld.CELLSIZE);
		}
	}

	/**
	 * @return true if there is no wood chip in the cell
	 */
	public synchronized boolean isEmpty() {
		return contents == null;
	}

	/**
	 * pre: there is a wood chip in the cell
	 * post: there is no wood chip in the cell
	 * @return wood chip in the cell
	 */
	public synchronized Rectangle2D.Double takeChip() {
		if (contents == null) {
			throw new RuntimeException("(" + row + "," + col + ")" + "empty");
		}

		Rectangle2D.Double temp = contents;
		contents = null;
		return temp;
	}

	/**
	 * Put a wood chip in the cell
	 * pre: there is no wood chip in the cell
	 * post: wood chip is stored in the cell
	 * @param rect
	 *            rectangle representing the wood chip
	 */
	public synchronized void putChip(Rectangle2D.Double rect) {
		if (contents != null) {
			throw new RuntimeException("(" + row + "," + col + ")"
					+ "already full");
		}
		contents = rect;
		contents.setRect(col * TermiteWorld.CELLSIZE, row
				* TermiteWorld.CELLSIZE, TermiteWorld.CELLSIZE,
				TermiteWorld.CELLSIZE);
	}

	/**
	 * @return rectangle corresponding to the wood chip if one is there
	 * Returns null otherwise
	 */
	public Rectangle2D.Double getContents() {
		return contents;
	}
}
