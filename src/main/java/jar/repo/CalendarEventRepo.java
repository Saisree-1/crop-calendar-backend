package jar.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jar.model.CalendarEvent;

public interface CalendarEventRepo extends JpaRepository<CalendarEvent, Long> {

    List<CalendarEvent> findByPlanId(Long planId);
}