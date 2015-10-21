/*
 * REFACTORING REQUIRED
 * TWEAKING REQUIRED
 * 
 * ADD SUPPORT FOR "DD MMMM YY" KIND OF FORMAT
 */

package parser;

import java.util.ArrayList;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

public class DateParser {

	enum TYPES {
		TODAY, TOMORROW, OTHERS;
	}

	private static TYPES getType(String str) {
		if (Parser.isPresent(ParserConstants.KW_TODAY, str)) {
			return TYPES.TODAY;
		} else if (Parser.isPresent(ParserConstants.KW_TOMORROW, str)) {
			return TYPES.TOMORROW;
		} else {
			return TYPES.OTHERS;
		}
	}

	/*
	 * Returns the ArrayList<String> with the dates parsed from the String.
	 * 
	 * Test method
	 */
	protected static ArrayList<String> extractDateArray(String str) {
		ArrayList<String> date = new ArrayList<String>();
		ArrayList<String> arr = Parser.toArrayList(str.trim().toLowerCase(), ParserConstants.CHAR_SINGLE_WHITESPACE);
		int startIndex = Parser.indexOf(ParserConstants.KW_START, arr);
		System.out.println(startIndex);
		int endIndex = Parser.lastIndexOf(ParserConstants.KW_END, arr);
		System.out.println(endIndex);

		if (startIndex == -1 && endIndex == -1) {
			return date;
		} else if (startIndex != -1 && endIndex == -1) {
			// TODO only startDate, no endDate
			TYPES type = getType(arr.get(startIndex));
			switch (type) {
			case TODAY:
				date.set(0, getDate(0));
			case TOMORROW:
				date.set(0, getDate(1));
			case OTHERS:
				for (int i = 0; i < startIndex + 1; i++) {
					arr.remove(0);
				}
				for (String s1 : arr) {
					LocalDateTime startDate = null;
					for (String s2 : ParserConstants.FORMAT_DATE) {
						try {
							startDate = DateTimeFormat.forPattern(s2).parseLocalDateTime(s1);
							date.set(0, startDate.toString(ParserConstants.FORMAT_DATE_STORAGE));
						} catch (IllegalArgumentException e) {

						} catch (NullPointerException e) {

						}
					}
				}
			}
			return date;
		} else if (startIndex == -1 && endIndex != -1) {
			// TODO only endDate, no startDate. Is a deadline. Parse the end
			// segment
			TYPES type = getType(arr.get(endIndex + 1));
			switch (type) {
			case TODAY:
				date.set(1, getDate(0));
			case TOMORROW:
				date.set(1, getDate(1));
			case OTHERS:
				for (int i = 0; i < endIndex + 1; i++) {
					arr.remove(0);
				}
				for (String s1 : arr) {
					LocalDateTime endDate = null;
					for (String s2 : ParserConstants.FORMAT_DATE) {
						try {
							endDate = DateTimeFormat.forPattern(s2).parseLocalDateTime(s1);
							date.set(1, endDate.toString(ParserConstants.FORMAT_DATE_STORAGE));
						} catch (IllegalArgumentException e) {

						} catch (NullPointerException e) {

						}
					}
				}
			}
			return date;
		} else {
			// TODO event with start and end date. Have to parse 2 separate
			// segments
			// TYPES type = getType(arr.get(startIndex));
			// switch (type) {
			// case TODAY:
			// date.set(0, getDate(0));
			// case TOMORROW:
			// date.set(0, getDate(1));
			// case OTHERS:
			ArrayList<String> tempStart = new ArrayList<String>();
			ArrayList<String> tempEnd = new ArrayList<String>();
			tempStart.addAll(arr);
			tempEnd.addAll(arr);

			// Extract for start date
			for (int i = 0; i < startIndex + 1; i++) {
				tempStart.remove(0);
			}
			int tempIndex = Parser.lastIndexOf(ParserConstants.KW_END, tempStart);
			for (int i = tempIndex; i < tempStart.size() + 1; i++) {
				tempStart.remove(tempIndex);
			}
			// System.out.println(tempStart.toString());

			// Extract for end date
			for (int i = 0; i < endIndex + 1; i++) {
				tempEnd.remove(0);
			}
			// System.out.println(tempEnd.toString());

			// Parsing for start date
			for (String s1 : tempStart) {
				LocalDateTime startDate = null;
				for (String s2 : ParserConstants.FORMAT_DATE) {
					try {
						startDate = DateTimeFormat.forPattern(s2).parseLocalDateTime(s1);
						date.set(0, startDate.toString(ParserConstants.FORMAT_DATE_STORAGE));
						break;
					} catch (IllegalArgumentException e) {

					} catch (NullPointerException e) {

					}
				}
			}

			// Parsing for end date
			for (String s1 : tempEnd) {
				LocalDateTime endDate = null;
				for (String s2 : ParserConstants.FORMAT_DATE) {
					try {
						endDate = DateTimeFormat.forPattern(s2).parseLocalDateTime(s1);
						date.set(1, endDate.toString(ParserConstants.FORMAT_DATE_STORAGE));
						break;
					} catch (IllegalArgumentException e) {

					} catch (NullPointerException e) {

					}
				}
			}
			return date;
		}

	}

	protected static String getStartDate(String str) {
		ArrayList<String> arr = Parser.toArrayList(str.trim().toLowerCase(), ParserConstants.CHAR_SINGLE_WHITESPACE);
		int index = Parser.indexOf(ParserConstants.KW_START, arr);
		if (index != -1) {
			TYPES type = getType(arr.get(index));
			switch (type) {
			case TODAY:
				return getDate(0);
			case TOMORROW:
				return getDate(1);
			case OTHERS:
				// Removing elements before keyword
				for (int i = 0; i < index + 1; i++) {
					arr.remove(0);
				}
				// ArrayList<String> dateArr = new ArrayList<String>(2);
				for (String s1 : arr) {
					LocalDateTime date = null;
					for (String s2 : ParserConstants.FORMAT_DATE) {
						try {
							date = DateTimeFormat.forPattern(s2).parseLocalDateTime(s1);
							return date.toString(ParserConstants.FORMAT_DATE_STORAGE);
							// dateArr.add(date.toString(ParserConstants.FORMAT_DATE_STORAGE));
						} catch (NullPointerException e) {

						} catch (IllegalArgumentException e) {

						}
					}
				}
				// return dateArr.get(0);
			default:
				return null;
			}
		} else {
			return null;
		}
	}

	protected static String getEndDate(String str) {
		ArrayList<String> arr = Parser.toArrayList(str.trim().toLowerCase(), ParserConstants.CHAR_SINGLE_WHITESPACE);
		int index = Parser.lastIndexOf(ParserConstants.KW_END, arr);
		if (index != -1) {
			// TYPES type = getType(arr.get(index));
			// switch (type) {
			// case TODAY:
			// return getDate(0);
			// case TOMORROW:
			// return getDate(1);
			// case OTHERS:
			for (int i = 0; i < index + 1; i++) {
				arr.remove(0);
			}
			// ArrayList<String> dateArr = new ArrayList<String>(2);
			for (String s1 : arr) {
				LocalDateTime date = null;
				for (String s2 : ParserConstants.FORMAT_DATE) {
					try {
						date = DateTimeFormat.forPattern(s2).parseLocalDateTime(s1);
						return date.toString(ParserConstants.FORMAT_DATE_STORAGE);
						// dateArr.add(date.toString(ParserConstants.FORMAT_DATE_STORAGE));
					} catch (NullPointerException e) {

					} catch (IllegalArgumentException e) {

					}
				}
			}
			// return dateArr.get(0);
			// default:
			// return null;
			//
		}
		return null;
	}

	/*
	 * Adds today's date to the number of days specified and return the sum as a
	 * String in the format "dd/MM/yy"
	 */
	private static String getDate(int plus) {
		LocalDateTime date = LocalDateTime.now();
		date = date.plusDays(plus);
		return date.toString(ParserConstants.FORMAT_DATE_STORAGE);
	}
}
