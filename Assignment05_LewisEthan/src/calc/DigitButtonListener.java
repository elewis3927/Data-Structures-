package calc;

import java.awt.event.*;

/**
 * Class representing buttons with numbers on them.
 * 
 * TODO: Complete the Docs
 * 
 * @author Kim B. Bruce
 * @version 1/97
 */
public class DigitButtonListener implements ActionListener {
	// TODO: add instance variables
	protected State calcState;
	protected int newVal;
	/**
	 * Button knows own value and the state so can communicate with it.
	 * TODO: Complete the Docs
	 */
	public DigitButtonListener(int newValue, State state) {
		calcState = state;
		newVal = newValue;
		//add code here
	}
	
	/**
	 * 
	 * @pre:  User clicked on the button.
	 * @post:  Informed state that it was clicked on and what its value is.
	 * TODO: Complete the Docs
	 */
	public void actionPerformed(ActionEvent evt) {
		calcState.addDigit(newVal);
		// call appropriate code in State
	}
}
