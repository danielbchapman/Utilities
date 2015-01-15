package com.danielbchapman.utility.calendars;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class CalendarEvent implements Comparable<CalendarEvent>
{
	@Getter
	@Setter
	private Date date;

	public CalendarEvent()
	{
		
	}
	
	public CalendarEvent(Date time)
	{
		this.date = time;
	}
	
	
	@Override
	public int compareTo(CalendarEvent o) {
		if(o == null)
			return -1;
		
		Date a = date;
		Date b = o.getDate();
		
		if(a == null && b == null)
			return 0;
		
		if(a == null)
			return -1;
		
		if(b == null)
			return 1;
		
		return a.compareTo(b);
	}
}
