package ro.stefannemtoc.soaproject.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import ro.stefannemtoc.soaproject.models.domain.EventLogger;
import ro.stefannemtoc.soaproject.models.models.EventRequest;
import ro.stefannemtoc.soaproject.repository.EventRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@CrossOrigin(origins = {"http://localhost:4200", "localhost:4200"})
@RestController
@RequestMapping("/api/events")
public class EventController {

    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    private Integer counter = 0;

    @Autowired
    EventRepository eventRepository;

    private final RabbitTemplate rabbitTemplate;

    private final SimpMessagingTemplate simpTemplate;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public EventController(RabbitTemplate rabbitTemplate, SimpMessagingTemplate simpTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.simpTemplate = simpTemplate;
    }

    @PostMapping("")
    public ResponseEntity<?> sendEventElsewhere(@RequestBody EventRequest eventRequest) {
        counter++;
        rabbitTemplate.convertAndSend("", "event-happened", eventRequest.getEventSender());
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send("event_counter", counter.toString());
        future.whenComplete(((stringStringSendResult, throwable) -> {
            if (throwable == null) {
                logger.info("Sent message=[" + counter.toString() + "]");
            } else {
                logger.error("Unable to send kafka message due to: " + throwable.getMessage());
            }
        }));


        logger.info("Event called by " + eventRequest.getEventSender() + " Count: " + counter);

        this.simpTemplate.convertAndSend("/notification", "New event");
        return ResponseEntity.ok().build();
    }


    @GetMapping("")
    public ResponseEntity<List<EventLogger>> getAllEvents() {
        var allEvents = eventRepository.findAll();

        return ResponseEntity.ok(allEvents);
    }
}
