package ro.stefannemtoc.soalistener.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.stefannemtoc.soalistener.model.domain.EventLogger;

@Repository
public interface EventRepository extends JpaRepository<EventLogger, Long> {

}
