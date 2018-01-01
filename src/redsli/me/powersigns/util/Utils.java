package redsli.me.powersigns.util;

/**
 * Created by redslime on 16.10.2017
 */
public class Utils {

	public static boolean isNumber(String str) {
		try {
			Double.parseDouble(str);
		} catch(NumberFormatException e) {
			return false;
		}
		return true;
	}
}