/*
 * @author: Jeston Teo
 * 
 * This class extracts the index argument in the user's input.
 */

package parser;

import java.util.ArrayList;

public class IndexParser {

	/*
	 * This method returns the index present in the String. It throws an
	 * InvalidInputException if no such index is found
	 * 
	 * Assumptions: 1) The index is present 2) The index comes after the action
	 * argument and before the event/task argument 3) The index is always a
	 * positive integer
	 */
	protected static int getIndex(String str) throws InvalidInputException {
		ArrayList<String> arr = Parser.toArrayList(str.trim(), ParserConstants.CHAR_SINGLE_WHITESPACE);
		if (indexPresent(arr)) {
			return Integer.parseInt(arr.get(ParserConstants.INDEX_SECOND));
		} else {
			throw new InvalidInputException("Invalid input: Please enter a valid index");
		}
	}

	/*
	 * Checks if there's an index argument in the String. Returns true if an
	 * integer is present in the second index, false if otherwise
	 * 
	 * ASSUMPTIONS: 1) In the input, the index always belongs to the second
	 * index
	 */
	private static boolean indexPresent(ArrayList<String> arr) throws InvalidInputException {
		try {
			return arr.get(ParserConstants.INDEX_SECOND).matches("^[0-9]*$");
		} catch (IndexOutOfBoundsException e) {
			throw new InvalidInputException("Invalid input: Please enter a valid index");
		}
	}
}
