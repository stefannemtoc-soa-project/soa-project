package ro.stefannemtoc.soalistener.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvokeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ro.stefannemtoc.soalistener.model.domain.EventLogger;
import ro.stefannemtoc.soalistener.repository.EventRepository;

import java.time.LocalDateTime;

@Service
public class EventListener {

    private static final Logger logger = LoggerFactory.getLogger(EventListener.class);

    @Value(value = "${aws.iam.user.accessKey}")
    private String accessKey;

    @Value(value = "${aws.iam.user.secret}")
    private String secret;

    @Autowired
    EventRepository eventRepository;

    @RabbitListener(queues = {"event-happened"})
    public void onEventHappened(String eventSender) {
        logger.info("Event received: " + eventSender);
        var eventToSaveInDB = new EventLogger(LocalDateTime.now(), eventSender);
        eventRepository.save(eventToSaveInDB);
    }

    @KafkaListener(topics = "event_counter")
    public void onEventReceivedOnKafka(String message) {
        logger.info("Event kafka received: " + message);
        if (Integer.parseInt(message) % 10 == 0) {
            sendMessageToAws();
        }
    }

    private void sendMessageToAws() {
        var region = Regions.EU_NORTH_1;
        var credentials = new BasicAWSCredentials(accessKey, secret);
        var builder = AWSLambdaClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region);


        var client = builder.build();
        var req = new InvokeRequest()
                .withFunctionName("arn:aws:lambda:eu-north-1:339712949612:function:SendEmail");
        client.invoke(req);
    }

}
