// RandPlayer by Ethan Lewis, 10/29/17, v 1.0

package hexAPawn;

import java.util.Random;
import java.util.Scanner;

public class RandPlayer implements Player {
	protected HexMove mv;
	protected char EnemyplayerColor;
	protected char userColor;
	protected GameTree newTree;
	
	// constructs a player and designates the colors to variables used in program 
	public RandPlayer (char color){
		EnemyplayerColor = HexBoard.opponent(color);
		userColor = color; 
	}
	
	public Player play(GameTree node, Player opponent) {
		// if it is a winning game board for the opponent, return the opponent
		// if not, get the move from user, and update the GameTree 
		if (node.board.win(HexBoard.opponent(userColor))){
			System.out.println("Winner!");
			// print out the board, not using current because current it node
			System.out.println(node.board.toString());
			
			return opponent;
			
		}else{
		// Create new random generator
		Random r = new Random();
			
		// Print out game board
		System.out.println(node.board.toString());
		System.out.println("Random Move: ");
		
		// Ran is the random move from the possible moves of trees in Game Tree class
		int ran = r.nextInt(node.board.moves(userColor).size());
		
		// using the random value, get the random move from the arraylist 
		mv = node.board.moves(userColor).get(ran);
		System.out.println(mv);
		
		// update board using find method
		newTree = node.find(mv);

		}
		// after your move, call the opponent to make a play
		return opponent.play(newTree, this);
		
	}
	
	public static void main(String[] args) {
		new AutograderCompTest();
		String next = "y";
		while (next.equals("y")){
			Scanner r = new Scanner(System.in);
			HumanPlayer human2 = new HumanPlayer('o');
			RandPlayer random2 = new RandPlayer('o');
			RandPlayer random1 = new RandPlayer('*');
			HexBoard b = new HexBoard(3, 3);
			GameTree play = new GameTree(b, b.WHITE);
			random2.play(play, random1);
			// Ask if game should be played again
			System.out.println("Play again? y/n");
			next = r.nextLine();
			
			
		}	
		System.out.println("Thanks for playing bruh");
	}
}
