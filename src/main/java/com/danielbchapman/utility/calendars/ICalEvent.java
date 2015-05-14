package com.danielbchapman.utility.calendars;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * This class is implemented as a record-format that returns the basic information
 * needed for an event in an iCal style calendar.
 * </p>
 * <p>
 * Note: the field "time" is used as the start time for the event.
 * </p> 
 ***************************************************************************
 * @author Daniel B. Chapman 
 * @since Jan 15, 2015
 * @version 2 Development
 * @link http://www.lightassistant.com | http://danielbchapman.com
 * copyright Daniel B. Chapman
 ***************************************************************************
 */
public class ICalEvent extends CalendarEvent
{
  private static String UUID_SUFFIX ="@pmdb.danielbchapman.com";
  
  @Getter
  @Setter
  private boolean allDay;
  @Getter
  @Setter
  private String description;
  @Getter
  @Setter
  private Date endDate;
  @Getter
  @Setter
  private String title;
  @Getter
  @Setter
  private String uid;
  
  public ICalEvent()
  {
    this(null, null, false, null, null, null);//UUID
  }
  
  public ICalEvent(Date start, Date endDate, boolean allDay, String title, String description)
  {
    this(start, endDate, allDay, title, description, null);
  }
  
  public ICalEvent(Date start, Date endDate, boolean allDay, String title, String description, String uid)
  {
    setDate(start);
    this.endDate = endDate;
    this.title = title;
    this.description = description;
    this.allDay = allDay;
    if(uid != null)
      this.uid = uid;
    else
      this.uid = CalendarUtil.createIcalUid(UUID_SUFFIX);
  }

  @Override
  public int compareTo(CalendarEvent event) 
  {
    if(event == null || !ICalEvent.class.isInstance(event))
      return super.compareTo(event);
      
    //FIXME Implement Custom sort operations for iCal | 
    //Technically we should just compare UUID and update but...
    return super.compareTo(event);
  }
}
