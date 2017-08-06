package personal.bw.shopper;

import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static java.text.DateFormat.*;

public class CalendarConverterTest
{
	@Test
	public void calendarTest()
	{
		int m = 2;
		int d = 10;
		int y = 1999;

		String date = ""+d+m+y;
		Calendar cal = new GregorianCalendar(y, m, d);

		System.out.println(getDateInstance(SHORT).format(cal.getTime()));
		System.out.println(getDateInstance(LONG).format(cal.getTime()));
		System.out.println(getDateInstance(FULL).format(cal.getTime()));
		try
		{
			System.out.println(getDateInstance(FULL).format(getDateInstance(LONG).parse(date)));
		} catch (ParseException e)
		{
			e.printStackTrace();
		}

	}

	@Test
	public void dayMonthYearTest() throws Exception
	{
		CalendarConverter calendarConverter = new CalendarConverter();
		Calendar calendar = calendarConverter.toCalendar(calendarConverter.toString(new Date()));
		System.out.println(calendar.get(Calendar.DAY_OF_MONTH));

	}
}