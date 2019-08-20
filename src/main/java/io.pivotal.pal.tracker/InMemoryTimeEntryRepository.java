package io.pivotal.pal.tracker;

import io.pivotal.pal.tracker.TimeEntry;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

public class InMemoryTimeEntryRepository implements TimeEntryRepository  {

   private List<TimeEntry> entryList = new ArrayList<TimeEntry>();

   private Long idCounter= 1L;

    public TimeEntry create(TimeEntry timeEntry)
    {
        TimeEntry entry = new TimeEntry(idCounter,timeEntry.getProjectId(), timeEntry.getUserId(), timeEntry.getDate(), timeEntry.getHours());
        idCounter++;
        entryList.add(entry);
        return entry;

    }

   public InMemoryTimeEntryRepository()
   {

   }

    public TimeEntry find(Long id)
    {
       TimeEntry entry = entryList.stream().filter(TimeEntry -> id.equals(TimeEntry.getId())).findAny().orElse(null);
        return entry;
    }


    public List<TimeEntry> list()
    {
        return entryList;
    }

    public TimeEntry update(Long id, TimeEntry entry)
    {
        TimeEntry updatedEntry = entryList.stream().filter(TimeEntry -> id.equals(TimeEntry.getId())).findAny().orElse(null);
        if(updatedEntry != null) {
            entryList.remove(updatedEntry);
            updatedEntry = new TimeEntry(id, entry.getProjectId(), entry.getUserId(), entry.getDate(), entry.getHours());

            entryList.add(updatedEntry);
        }
        return updatedEntry;
    }

    public void delete(Long id)
    {

        TimeEntry entry = entryList.stream().filter(TimeEntry -> id.equals(TimeEntry.getId())).findAny().orElse(null);
        entryList.remove(entry);
    }
}
