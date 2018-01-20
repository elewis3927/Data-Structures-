// CompPlayer by Ethan Lewis, 10/29/17, v 1.0

package hexAPawn;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class CompPlayer implements Player {
	protected HexMove mv;
	protected char enemyPlayerColor;
	protected char userColor;
	protected GameTree newTree;
	protected GameTree oldTree;
	protected int trackerSize = 0;
	
	protected ArrayList<HexMove> moveTracker = new ArrayList<HexMove>();
	protected int numOfMoves;

	//arayList of trees to for trim to access
	protected ArrayList<GameTree> treeTracker = new ArrayList<GameTree>();
	protected int numOfTrees;
	
	
	public CompPlayer (char color){
		enemyPlayerColor = HexBoard.opponent(color);
		userColor = color; 
	}
	
	public Player play(GameTree node, Player opponent) {
		Scanner in = new Scanner(System.in);
		// if it is a winning game board for the opponent, return the opponent
		// if not, get the move from user, and update the GameTree 
		if (node.board.win(HexBoard.opponent(userColor))){
			System.out.println("You win: "+enemyPlayerColor);
			// print out the board, not using current because current it node
			 
			// create the losing game tree by calling move on the previous game tree
			GameTree lost = oldTree.find(moveTracker.get(trackerSize - 1));
			// remove the losing game tree from the array list of game trees
			oldTree.trees.remove(lost);
			// remove the move from the list of moves in that specific game tree by accessing
			// the previous move from the array list 
			oldTree.board.moves(userColor).remove(moveTracker.get(trackerSize - 1));
			// set the new tree to be played with to the trimmed old one
			newTree = oldTree;
			trackerSize = 0;
			System.out.println(node.board.toString());
			
			// if lost return the opponent player
			return opponent;
		}else{
		
		oldTree = node;
		// Create new random generator
		Random r = new Random();
			
		// Print out game board
		System.out.println(node.board.toString());
		System.out.println("Comp Move: ");
		
		// Ran is the random move from the possible moves of trees in Game Tree class
		int ran = r.nextInt(node.board.moves(userColor).size());
		mv = node.board.moves(userColor).get(ran);
		
		// add the previous move into an an array list
		// increase tracker by 1
		moveTracker.add(mv);
		trackerSize++;
		
		// update board using find method
		newTree = node.find(mv);

		}
		// after your move, call the opponent to make a play
		return opponent.play(newTree, this);
	}
	
	public static void main(String[] args) {
		new AutograderCompTest();
		String next = "y";
		CompPlayer human1 = new CompPlayer('o');
		RandPlayer comp1 = new RandPlayer('*');
		HexBoard b = new HexBoard(3, 3);
		GameTree play = new GameTree(b, b.WHITE);
		// while the user presses y, run the game 
		while (next.equals("y")){
			Scanner r = new Scanner(System.in);
			// play game with declared parameters above
			human1.play(play, comp1);
			// Ask if game should be played again
			System.out.println("Play again? y/n");
			next = r.nextLine();
			
		}	
		System.out.println("Thanks for playing bruh");
	}
}