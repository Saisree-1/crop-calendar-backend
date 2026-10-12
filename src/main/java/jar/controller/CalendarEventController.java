package jar.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jar.model.CalendarEvent;
import jar.repo.CalendarEventRepo;

@RestController
@RequestMapping("/calendar-events")
public class CalendarEventController {

    @Autowired
    private CalendarEventRepo calendarEventRepo;

    @GetMapping
    public List<CalendarEvent> getCalendarEvents() {
        return calendarEventRepo.findAll();
    }
}