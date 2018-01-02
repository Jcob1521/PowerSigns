package redsli.me.powersigns.util;

/**
 * Created by redslime on 16.10.2017
 */
public class Utils {

    /**
     * Checks if given string is a double
     * @param str The string to be checked
     * @return Whether the given string is a double/number
     */
	public static boolean isNumber(String str) {
		try {
			Double.parseDouble(str);
		} catch(NumberFormatException e) {
			return false;
		}
		return true;
	}
}