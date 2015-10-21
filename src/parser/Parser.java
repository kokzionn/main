/*
 * TODO Description of class
 */

package parser;

import java.util.ArrayList;

import logic.AddTask;
import logic.Command;
import logic.DeleteTask;
import logic.DoneTask;
import logic.ExitTask;
import logic.HelpTask;
import logic.InvalidTask;
import logic.SearchTask;
import logic.ShowTask;
import logic.UndoTask;
import logic.UpdateTask;
import parser.ActionParser.ACTION_TYPE;

public class Parser {

	public static Command setCommand(String str) {
		ACTION_TYPE action = ActionParser.setUserAction(str);

		ArrayList<String> date = new ArrayList<String>();
		ArrayList<String> time = new ArrayList<String>();

		switch (action) {
		case ADD:
			AddTask add = new AddTask();
			try {
				add.setEventTask(EventTaskParser.getEventTask(str));
				date.add(DateParser.getStartDate(str));
				date.add(DateParser.getEndDate(str));
				time.add(TimeParser.getStartTime(str));
				time.add(TimeParser.getEndTime(str));
			} catch (IndexOutOfBoundsException | InvalidInputException e) {
				System.err.println(e.getMessage());
			}
			add.setDate(date);
			add.setTime(time);
			return add;
		case SHOW:
			ShowTask show = new ShowTask();
			show.setDate(DateParser.getStartDate(str));
			return show;
		case SEARCH:
			SearchTask search = new SearchTask();
			try {
				search.setEventTask(EventTaskParser.getEventTask(str));
			} catch (IndexOutOfBoundsException | InvalidInputException e) {
				System.err.println(e.getMessage());
			}
			return search;
		case UPDATE:
			UpdateTask update = new UpdateTask();
			try {
				update.setIndex(IndexParser.getIndex(str));
				update.setEventTask(EventTaskParser.getEventTask(str));
				date.add(DateParser.getStartDate(str));
				date.add(DateParser.getEndDate(str));
				time.add(TimeParser.getStartTime(str));
				time.add(TimeParser.getEndTime(str));
			} catch (IndexOutOfBoundsException | InvalidInputException e) {
				System.err.println(e.getMessage());
			}
			update.setDate(date);
			update.setTime(time);
			return update;
		case DONE:
			DoneTask done = new DoneTask();
			done.setIndex(IndexParser.getIndex(str));
			return done;
		case DELETE:
			DeleteTask delete = new DeleteTask();
			delete.setIndex(IndexParser.getIndex(str));
			return delete;
		case UNDO:
			// TODO
			UndoTask undo = new UndoTask();
			return undo;
		case EXIT:
			// TODO
			ExitTask exit = new ExitTask();
			return exit;
		case HELP:
			// TODO
			HelpTask help = new HelpTask();
			return help;
		case INVALID:
			// TODO
			InvalidTask invalid = new InvalidTask();
			return invalid;
		default:
			return null;
		}

	}

	/*
	 * Takes a String input and return it as an ArrayList with the specified
	 * String as the delimiter
	 */
	protected static ArrayList<String> toArrayList(String str, String delim) {
		String[] strArr = str.split(delim);
		ArrayList<String> strArrList = new ArrayList<String>();
		for (String strTransfer : strArr) {
			strArrList.add(strTransfer);
		}
		return strArrList;
	}

	/*
	 * Takes an ArrayList and appends its elements into a String and returns it
	 */
	protected static String toString(ArrayList<String> arr) {
		String toReturn = "";
		for (int i = 0; i < arr.size(); i++) {
			toReturn = toReturn + ParserConstants.CHAR_SINGLE_WHITESPACE + arr.get(i);
		}
		return toReturn;
	}

	/*
	 * Returns the index of the first occurrence of the specified element in the
	 * String, or -1 if it does not contain the element.
	 */
	protected static int indexOf(String[] arr, ArrayList<String> str) {
		ArrayList<String> temp = cloneToLowerCase(str);
		int index = str.size();
		for (String s : arr) {
			if (temp.indexOf(s) < index && temp.indexOf(s) != -1) {
				index = temp.indexOf(s);
			}
		}
		if (index == temp.size()) {
			return -1;
		} else {
			return index;
		}
	}

	/*
	 * Returns the index of the last occurrence of the specified element in the
	 * String, or -1 if it does not contain the element.
	 */
	protected static int lastIndexOf(String[] arr, ArrayList<String> str) {
		ArrayList<String> temp = cloneToLowerCase(str);
		int index = -1;
		for (String s : arr) {
			if (temp.lastIndexOf(s) > index && temp.lastIndexOf(s) != -1) {
				index = temp.lastIndexOf(s);
			}
		}
		if (index == -1) {
			return -1;
		} else {
			return index;
		}
	}

	/*
	 * Checks if the elements in an Array exist within the String input Returns
	 * true if it is, false if otherwise
	 */
	protected static boolean isPresent(String[] arr, String str) {
		ArrayList<String> strArr = toArrayList(str.trim().toLowerCase(), ParserConstants.CHAR_SINGLE_WHITESPACE);
		for (String s : arr) {
			if (strArr.contains(s)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * Checks if there's an index argument in the String
	 * 
	 * ASSUMPTIONS: 1) The index argument always comes before the event/task
	 * argument and after the action argument
	 */
	protected static boolean indexPresent(ArrayList<String> arr) throws IndexOutOfBoundsException {
		return arr.get(ParserConstants.INDEX_SECOND).matches(".*\\d+/*");
	}

	private static ArrayList<String> cloneToLowerCase(ArrayList<String> str) {
		ArrayList<String> temp = new ArrayList<String>();
		for (int i = 0; i < str.size(); i++) {
			temp.add(str.get(i).toLowerCase());
		}
		return temp;
	}
}
