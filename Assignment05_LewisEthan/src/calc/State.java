package calc;

import java.util.Deque;
import javax.swing.*;
import java.util.ArrayDeque;
import java.lang.*;

/**
 * Class representing the internal state of the calculator. It is responsible
 * for keeping track of numbers entered and performing operations when buttons
 * are clicked. It tells the display what number to show.
 * 
 * State Class:
 * By Ethan Lewis 
 * 
 */
public class State {
	// Display on which results are written
	protected JTextField calcDisplay;
	
	// The stack to on which to store numbers.
	//
	// Note 1: If you implement the extra credit of adding floating point operations,
	// then you will need to change the type parameter here, otherwise this
	// field should not be changed.
	//
	// Note 2: Deque<Integer> is an interface two reasonable implementing classes to use
	// would be ArrayDeque and LinkedList.
	protected Deque<Integer> stack;
	// Variable created to keep track of entered value
	protected int entered;
	// a value that entered gets multiplied by when another value is entered into calculator
	protected int tenths = 1;


	/**
	 * @param display is the text box where numbers are shown
	 * Declare stack as a new ArrayDeque
	 * Set calcDisplay as display to modify in program 
	 * Set the displau to 0
	 */
	public State(JTextField display) {
		stack = new ArrayDeque<Integer>();
		calcDisplay = display;
		calcDisplay.setText("0");
		// TODO: add code
	}

	/**
	 * User clicked on a digit button ...
	 * @param int value
	 * set entered to the be value multiplied by tenths
	 * set calcDisplay to entered 
	 */
	public void addDigit(int value) {
		// entered gets multiplied by 10
		entered = entered * tenths + value;
		// multiply tenths by ten because if enter another value, 5 7 becomes 57
		tenths = tenths * 10;
		// display entered
		calcDisplay.setText(Integer.toString(entered));
	}

	/**
	 * User has clicked on operator button ...
	 * @param char op
	 * performs the specified operation on the first two values of the stack
	 * pushed result back onto the stack
	 */
	public void doOp(char op) throws ArithmeticException{
		// if the user presses an operation without pressing enter on a digit first,
		// push it onto the stack 
		if (entered != 0){
			stack.push(entered);
		}
		// if the stack has less than two items, an operation cannot used
		// display error on calcDisplay
		// reset calculator: clear stack, entered = 0, and tenths goes back down to 1
		if (stack.size() < 2){
			calcDisplay.setText("Error");
			entered = 0;
			tenths = 1;
			stack.clear();
			return;
		}
		// pop first two values and set them to result's 
		int result1 = stack.pop();
		int result2 = stack.pop();
		// declare a result3
		int result3 = 0;
		// declare a switch case for the specified operations 
		switch(op){
		case '/': 
			try{ // try to perform division operation
				result3 = result2 / result1;
			} // if result1 is 0, then catch the exception, display error, and reset calculator
			 catch(ArithmeticException e){
				 calcDisplay.setText("Error");
				 entered = 0;
				 tenths = 1;
				 stack.clear();
				 return;
			 }
	
			break;
		case '*':
			result3 = result2 * result1;
			break;
		case '-':
			result3 = result2 - result1;
			break;
		case '+':
			result3 = result2 + result1;
			break;
		case '^': // own button that was created: power function
			if (result1 == 0){
				result3 = 1;
			}else{
				result3 = (int) Math.pow(result2, result1);
			}
		}	
		// after operation was completed, push result three onto stack
		stack.push(result3);
		// display the result onto the calculator
		calcDisplay.setText(Integer.toString(result3));
	}

	/**
	 * User clicked on enter button ...
	 * enter pushes selected digit onto the stack
	 * reset tenths and entered 
	 */
	public void enter() {
		// push entered onto stack
		stack.push(entered);
		tenths = 1;
		entered = 0;
		// display the entered digit 
		calcDisplay.setText(Integer.toString(stack.peek()));
		
	}

	/**
	 * User clicked on clear key ...
	 * stack is cleared
	 * 
	 */
	public void clear() {
		// clear the stack
		stack.clear();
		// reset tenths and stack to original values
		tenths = 1;
		entered = 0;
		// display 0 onto the calculator
		calcDisplay.setText("0");
	}

	/**
	 * User clicked on pop key ...
	 * removes top value from stack
	 */
	public void pop() {
		// if stack is empty or the sick equals 1
		// set the display to to zero
		if (stack.isEmpty() || stack.size() == 1){
			calcDisplay.setText("0");
			// else pop the top value 
			// display the top value of stack using peek 
		}else{
			stack.pop();
			calcDisplay.setText(Integer.toString(stack.peek()));
		}
	}

	/**
	 * User clicked on exchange key ...
	 * two top values of stack are switched
	 * 
	 */
	public void exchange() {
		// get two top values of the stack
		int temp1 = stack.pop();
		int temp2 = stack.pop();
		// push the two values back on in the reverse order they were popped
		stack.push(temp1);
		stack.push(temp2);
		// display the new top value using peek
		calcDisplay.setText(Integer.toString(stack.peek()));

	}
}
