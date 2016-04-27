package com.danielbchapman.utility.calendars;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import com.danielbchapman.text.Text;

public class CalendarUtil {
	
	/**
	 * A List of the "months" to be printed. This will show fill blank days outside the month so that
	 * it can be printed. 
	 * <code>
	 * Example: (February Leap year) (for each month)
	 * S  M  T  W  R  F  S
	 * X  X  1  2  3  4  5
	 * 6  7  8  9  10 11 12
	 * 13 14 15 16 17 18 19
	 * 20 21 22 23 24 25 26
	 * 27 28 29 X  X  X  X
	 * </code>
	 * 
	 * The nulls will pad out dates so that the calendar can be printed by reporting software. 
	 * 
	 * @param startOfWeek the start date for the week example: <tt>Calendar.MONDAY</tt>
	 * @param events a list of the events to parse.
	 * @return a List of the "months" to be printed. This will show fill blank days outside the month so that
	 * it can be printed. 
	 */
	public static ArrayList<ArrayList<CalendarDay>> createMontlyCalendar(int startOfWeek, List<CalendarEvent> events)
	{
		ArrayList<ArrayList<CalendarDay>> calendar = new ArrayList<>();
		Collections.sort(events);
		
		if(events == null || events.size() == 0)
			return calendar;
		
		Date first = events.get(0).getDate();
		Date last = events.get(events.size()-1).getDate();
		
		if(first == null || last == null)
			return calendar;

		HashMap<Integer, CalendarDay> eventMap = null;
		int currentMonth = -1;
		int currentYear = -1;
		
		for(CalendarEvent event : events)
		{
			if(event == null || event.getDate() == null)
				continue;
			Calendar cal = Calendar.getInstance();
			cal.setTime(event.getDate());
			
			//Finalizer
			if((cal.get(Calendar.MONTH) != currentMonth || cal.get(Calendar.YEAR) != currentYear) && eventMap != null)
			{
				ArrayList<CalendarDay> month = new ArrayList<>();
				ArrayList<Integer> keys = new ArrayList<>();
				for(Integer i : eventMap.keySet())
					keys.add(i);
				
				Collections.sort(keys);
				
				System.out.println("Debugging------------------");
				Calendar padding = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
				padding.set(Calendar.MONTH, currentMonth);
				padding.set(Calendar.YEAR, currentYear);
				padding.set(Calendar.DAY_OF_MONTH, 1);
				cleanDate(padding);
				
				int startDayOfWeek = padding.get(Calendar.DAY_OF_WEEK);
				int totalDays = padding.getActualMaximum(Calendar.DAY_OF_MONTH);
				System.out.println("Padding Start->" + new SimpleDateFormat("MM/dd/yyyy").format(padding.getTime()));
				
				
				padding.set(Calendar.DAY_OF_MONTH, totalDays);
				int endDayOfWeek = padding.get(Calendar.DAY_OF_WEEK);
				System.out.println("Padding End->" + new SimpleDateFormat("MM/dd/yyyy").format(padding.getTime()));
				int padStart = daysToStartOfWeek(startOfWeek, startDayOfWeek);
				int padEnd = daysToEndOfWeek(startOfWeek, endDayOfWeek);
				

				
				System.out.println(padding.toString());
				System.out.println("Debug Start -> " + startOfWeek + ", " + startDayOfWeek + "->" + padStart);
				System.out.println("Debug End -> " + startOfWeek + ", " + endDayOfWeek + "->" + padEnd);
				
				for(int i = 0; i < padStart; i++)
					month.add(null); //padding

				for(Integer i : keys)
					month.add(eventMap.get(i));
				
				for(int i = 0; i < padEnd; i++)
					month.add(null); //padding
				
				calendar.add(month);
				//Reset
				eventMap = null;
			}
			
			//Initializer
			if(eventMap == null)
			{
				currentMonth = cal.get(Calendar.MONTH);
				currentYear = cal.get(Calendar.YEAR);
				
				eventMap = new HashMap<>();
				Calendar firstDay = Calendar.getInstance();
				firstDay.setTime(event.getDate());
				firstDay.set(Calendar.DAY_OF_MONTH, 1);
				
				int days = firstDay.getActualMaximum(Calendar.DAY_OF_MONTH);
				
				for(int i = 1; i < days + 1; i++)
					eventMap.put(i, new CalendarDay());
			}	
			
			CalendarDay day = eventMap.get(cal.get(Calendar.DAY_OF_MONTH));
			day.add(event);
		}
				
		return calendar;
	}
	
	public static int daysToStartOfWeek(int startOfWeek, int currentDay)
	{
		if(startOfWeek < currentDay)
			return currentDay - startOfWeek;
		else
			return (currentDay - startOfWeek + 7) % 7;
	}
	
	public static int daysToEndOfWeek(int startOfWeek, int currentDay)
	{
		if(startOfWeek > currentDay)
			return startOfWeek - currentDay - 1;
		else
			return (startOfWeek - currentDay + 7 - 1) % 7;	
	}

	public static Calendar cleanDate(Calendar cal)
	{
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;//chainable
	}
	
	/**
	 * @param domain the domain to use
	 * @return a UID in a rough iCal format  
	 * 
	 */
	public static String createIcalUid(String domain)
	{
	  return UUID.randomUUID().toString() + (Text.isEmptyOrNull(domain) ? "@null.foo.bar" : domain);
	}
}
