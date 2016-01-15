package org.moonwave;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

public class Test {
	public static void main(String[] argv) {
		//Calendar calendar = new GregorianCalendar();
		Calendar calendar = Calendar.getInstance();
		TimeZone timeZone = calendar.getTimeZone();

		String[] availableTZs = timeZone.getAvailableIDs();
		List<String> tzs = Arrays.asList(availableTZs);
		Collections.sort(tzs);
		int maxlen = 0;
		for (String tz : tzs) {
			maxlen = Math.max(tz.length(), maxlen);
			System.out.println(tz);
		}
		System.out.println("");
		System.out.println(maxlen);//==>32 varchar(40)
	}
}
