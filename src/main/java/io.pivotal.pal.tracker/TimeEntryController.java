package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
import java.sql.Time;
import java.util.List;

@RestController
public class TimeEntryController {

    private TimeEntryRepository timeEntryRepository;

    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;

    }

    @GetMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable("id") long timeEntryId) {
        TimeEntry entry = timeEntryRepository.find(timeEntryId);
        if(entry != null) {
            return new ResponseEntity<TimeEntry>(entry, HttpStatus.OK);
        }
            return new ResponseEntity<TimeEntry>(entry, HttpStatus.NOT_FOUND) ;
    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {
        return new ResponseEntity(timeEntryRepository.list(), HttpStatus.OK);
    }

    @PutMapping("/time-entries/{time_entry_id}")
    public ResponseEntity update(@PathVariable("time_entry_id") long timeEntryId,@RequestBody TimeEntry timeEntry) {
        TimeEntry entry = timeEntryRepository.update(timeEntryId, timeEntry);
        if (entry != null) {
            return new ResponseEntity<TimeEntry>(entry, HttpStatus.OK);
        }
        return new ResponseEntity<TimeEntry>(entry, HttpStatus.NOT_FOUND) ;

    }

    @DeleteMapping("/time-entries/{id}")
    public ResponseEntity delete(@PathVariable("id") long timeEntryId) {
        timeEntryRepository.delete(timeEntryId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/time-entries")
    public ResponseEntity create( @RequestBody TimeEntry timeEntryToCreate) {
        return new ResponseEntity<TimeEntry>(timeEntryRepository.create(timeEntryToCreate),HttpStatus.CREATED);
    }
}
