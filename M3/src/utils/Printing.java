package utils;

public class Printing {

	public static String printTitle(String str) {
		return String.format("%s\n\n", str);
	}
	
	public static String printStringSized(String str, int numChars) {
		return String.format("%-"+numChars+"s", str);
	}
	
	public static String printNumberSized(int number, int numChars) {
		return String.format("%"+numChars+"d", number);
	}
	
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
