package utils;

public class Utilitys {
	
	/**
	 * Repeats a String x times
	 * http://rosettacode.org/wiki/Repeat_a_string#Java
	 * 
	 * @param text
	 * @param times
	 * @return
	 */
	public static String repeatString(String text,int times) {
		   StringBuilder ret = new StringBuilder();
		   for(int i = 0;i < times;i++){
			   ret.append(text);
		   }
		   return ret.toString();
	}
}
