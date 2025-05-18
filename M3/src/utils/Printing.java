package utils;

public class Printing {
	
	// Returns a line of X quantity of the character indicated
	public static String printLineChar(char character, int numChars) {
		String line = "";
		
		for (int i = 0; i < numChars; i++) {
			line += character;
		}
		
		line += "\n";
		
		return line;
	}
	
	// Returns a message with two line "jumps" at the end
	public static String printTitle(String str) {
		return String.format("%s\n\n", str);
	}
	
	// Returns a message formatted to be aligned to the left in a space of a certain amount of characters
	public static String printStringSized(String str, int numChars) {
		return String.format("%-"+numChars+"s", str);
	}
	
	// Returns a number formatted as a String to be aligned to the right in a space of a certain amount of characters
	public static String printNumberSized(int number, int numChars) {
		return String.format("%"+numChars+"d", number);
	}
	
	// Returns a String centered in between a line of the indicated character that will occupy what is indicated
	public static String printStringCentred(String str, char character, int numChars) {
		int length_str = str.length();
		int chars_each_side = (int) ((numChars - length_str) / 2);
		
		String final_str = "";
		
		for (int i = 0; i < chars_each_side; i++) {
			final_str += character;
		}
		
		final_str += str;
		
		for (int i = 0; i < chars_each_side; i++) {
			final_str += character;
		}
		
		if (final_str.length() != numChars) {
			final_str += character;
		}
		
		return final_str;
	}
}
