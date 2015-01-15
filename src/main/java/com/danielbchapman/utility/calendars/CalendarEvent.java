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
	private Date time;

	public CalendarEvent()
	{
		
	}
	
	public CalendarEvent(Date time)
	{
		this.time = time;
	}
	
	
	@Override
	public int compareTo(CalendarEvent o) {
		if(o == null)
			return -1;
		
		Date a = time;
		Date b = o.getTime();
		
		if(a == null && b == null)
			return 0;
		
		if(a == null)
			return -1;
		
		if(b == null)
			return 1;
		
		return a.compareTo(b);
	}
}
