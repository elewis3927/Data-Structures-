// HumanPlayer by Ethan Lewis, 10/29/17, v 1.0

package hexAPawn;

import java.util.ArrayList;
import java.util.Scanner;

public class HumanPlayer implements Player {
	protected HexMove mv;
	protected int moveTo;
	protected int moveFrom;
	protected char enemyplayerColor;
	protected char userColor;
	protected GameTree newTree;
	
	public HumanPlayer (char color){
		userColor = color;
		char enemyplayerColor = HexBoard.opponent(color);
	}
	
	public Player play(GameTree node, Player opponent) {
	
		// New scanner was created
		Scanner r = new Scanner(System.in);
		
		// if it is a winning game board for the opponent, return the opponent
		// if not, get the move from user, and update the GameTree 
		if (node.board.win(HexBoard.opponent(userColor))){
			System.out.println("You win: "+enemyplayerColor);
			// print out the board, not using current because current it node
			System.out.println(node.board.toString());
			
			return opponent;
		}else{
			// Print out the game board
			System.out.println(node.board.toString());
			// get two values from user
			System.out.println("Move from: ");
			moveFrom = r.nextInt();
			System.out.println("Move to: ");
			moveTo = r.nextInt();
			// create a new move based on variables we just got
			mv = new HexMove(moveFrom, moveTo, 3);
			// update the current board that we are playing on
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
			HumanPlayer human1 = new HumanPlayer('o');
			HumanPlayer human2 = new HumanPlayer('*');
			HexBoard b = new HexBoard(3, 3);
			GameTree play = new GameTree(b, b.WHITE);
			//System.out.println(b.toString());
			human1.play(play, human2);
			// Ask if game should be played again
			System.out.println("Play again? y/n");
			next = r.nextLine();
			
		}	
		System.out.println("Thanks for playing bruh");
	}
}
