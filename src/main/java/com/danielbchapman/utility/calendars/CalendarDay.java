package com.danielbchapman.utility.calendars;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

public class CalendarDay {
	@Getter
	@Setter
	public ArrayList<CalendarEvent> events = new ArrayList<>();

	public <T extends CalendarEvent> void add(T event) {
		events.add(event);
	}

	public <T extends CalendarEvent> void remove(T event) {
		events.remove(event);
	}
}
