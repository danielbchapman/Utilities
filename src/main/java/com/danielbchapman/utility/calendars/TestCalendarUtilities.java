package com.danielbchapman.utility.calendars;

import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.junit.Test;

public class TestCalendarUtilities {
	
	
	@Test
	public void testStartOfWeekCalcs()
	{
		int x = CalendarUtil.daysToStartOfWeek(Calendar.SUNDAY,  Calendar.SUNDAY);
		assertTrue("Start value = " + x , x == 0);
		x = CalendarUtil.daysToStartOfWeek(Calendar.SUNDAY,  Calendar.WEDNESDAY);
		assertTrue("Start value = " + x , x == 3);
		x = CalendarUtil.daysToStartOfWeek(Calendar.MONDAY,  Calendar.WEDNESDAY);
		assertTrue("Start value = " + x , x == 2);
		x = CalendarUtil.daysToStartOfWeek(Calendar.MONDAY,  Calendar.SUNDAY);
		assertTrue("Start value = " + x , x == 6);	
	}
	@Test
	public void testEndOfWeekCalcs()
	{
		int x = CalendarUtil.daysToEndOfWeek(Calendar.SUNDAY,  Calendar.SUNDAY);
		assertTrue("Start value = " + x , x == 6);
		x = CalendarUtil.daysToEndOfWeek(Calendar.SUNDAY,  Calendar.SATURDAY);
		assertTrue("Start value = " + x , x == 0);
		x = CalendarUtil.daysToEndOfWeek(Calendar.SUNDAY,  Calendar.WEDNESDAY);
		assertTrue("End value = " + x , x == 3);
		x = CalendarUtil.daysToEndOfWeek(Calendar.MONDAY,  Calendar.WEDNESDAY);
		assertTrue("End value = " + x , x == 4);
		x = CalendarUtil.daysToEndOfWeek(Calendar.MONDAY,  Calendar.SUNDAY);
		assertTrue("End value = " + x , x == 0);	
	}
	@Test 
	public void testMonthlyAlgorithm()
	{	
		ArrayList<CalendarEvent> events = new ArrayList<CalendarEvent>();
		for(int i = 1; i < 100; i++)
		{
			int rand = new Random().nextInt(4) + 1;
			
			for(int j = 0; j < rand; j++)
			{
				Calendar event = Calendar.getInstance();
				event.set(Calendar.YEAR, 2014);
				event.set(Calendar.DAY_OF_YEAR, i);
				CalendarUtil.cleanDate(event);
				event.set(Calendar.HOUR_OF_DAY, j+12);
				events.add(new CalendarEvent(event.getTime()));
			}
		}
		
		ArrayList<ArrayList<CalendarDay>> x = CalendarUtil.createMontlyCalendar(Calendar.SUNDAY, events);
		
		for(ArrayList<CalendarDay> month : x)
			for(CalendarDay d : month)
			{
				if(d != null)
					for(CalendarEvent e : d.getEvents())
						System.out.println(e);
			}

		System.out.println("Testing Calendars");
		for(ArrayList<CalendarDay> month : x)
		{
			Date monthStart = null;
			for(CalendarDay d : month)
				if(d != null && d.events != null && !d.events.isEmpty())
					monthStart = d.getEvents().get(0).getTime();
			
			System.out.print("Month of ");
			System.out.println(new SimpleDateFormat("MMMMM").format(monthStart));
			System.out.println("M  T  W  R  F  S  S  ");
			int day = 1;
			for(int i = 0; i < month.size(); i++)
			{
				if(i % 7 == 0 && i > 0)
					System.out.println();
				 
				if(month.get(i) == null)
					System.out.print(" - ");
				else
				{
					System.out.print(String.format("%02d", day++) + " ");					
				}

			}
			
			System.out.println();
			System.out.println();
			
			assertTrue("Failed checking week contains 7 days", month.size() % 7 == 0);
		}
	}
	
	@Test
	public void testMonthlyMultipleMonthsAlgorithm()
	{
		
	}
	
	@Test
	public void testData()
	{
//		Debug Start -> 2, 5->3
//		Debug Start -> 2, 6->3
//		Debug Start -> 2, 1->6
//		Debug Start -> 2, 6->3
//		Debug Start -> 2, 1->6
//		Debug Start -> 2, 2->0
		
		
	}
}
