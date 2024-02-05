package ro.stefannemtoc.soaproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.stefannemtoc.soaproject.models.domain.EventLogger;

public interface EventRepository extends JpaRepository<EventLogger, Long> {

}
