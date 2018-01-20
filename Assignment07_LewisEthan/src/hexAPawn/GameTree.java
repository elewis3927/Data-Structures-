// GameTree by Ethan Lewis, 10/29/17, v 1.0

package hexAPawn;
import java.util.ArrayList;

public class GameTree {
// board variable 
protected HexBoard board;
// color variable 
protected char color;
// possible moves from hexboard
private int moves;
// arraylist of game trees for build to return
protected  ArrayList<GameTree> trees = new ArrayList<GameTree>();
// variable to keep track of the number of nodes
protected int numNodes = 1;

// declare the original Board for build()
protected static HexBoard orig = new HexBoard();


//Constructor
public GameTree(HexBoard b, char c){
	
	// redeclare instance variables
	board = b;
	color = c;
	
	// based off char, moves is the list of black or white moves
	moves = board.moves(color).size();

	build();

}

public static void main(String[] args) {
	HexBoard b = new HexBoard();
	GameTree tree = new GameTree(b, 'o');
	System.out.println(tree.numNodes);
}

public int size(){
	return trees.size();
}
/**
 * build generates a tree
 * tree consists of game trees
 * each game tree has an array list of boards that are one move away from the current tree
 * the boards are the nodes
 * the winning game trees are the leafs of the tree 
 * 
 */
public void build(){
	int indexOfMoves = 0;
	if (!board.win(board.opponent(color))){
		while (indexOfMoves < moves){
		
		// Create new HexBoards based of possible moves and original HexBoard
		HexBoard temp = new HexBoard(board, board.moves(color).get(indexOfMoves));
		
		// Create new GameTree with the opponents turn now
		GameTree tree1 = new GameTree(temp, board.opponent(color));
	
		// add the tree to the array list
		trees.add(tree1);
		
		// Increase index counter
		indexOfMoves++;
		numNodes += tree1.numNodes;
		//current = this;
		}
	}
}

/**
 * find returns the game tree / board that is generated when the specified move is executed
 * @param f
 * @return a game tree
 */
public GameTree find(HexMove f){

	// get the to value and turn it into an integer using methods in HexMove
	int to = f.to();
	// get the from value and turn it into an integer
	int fr = f.from;
	// runs through possible moves that the game tree has
	// if the to and from values match the values on the game trees board, return that tree
	for (int i = 0; i < trees.size(); i++){
		if (color == (trees.get(i).board.board[to]) && ' ' == (trees.get(i).board.board[fr])){
			return trees.get(i);
		}	
	}
	return null;

}

	
}
