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
}
