package CDC.producer;

import au.com.dius.pact.provider.MessageAndMetadata;
import au.com.dius.pact.provider.PactVerifyProvider;
import au.com.dius.pact.provider.junit5.MessageTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pt.psoft.g1.psoftg1.usermanagement.api.UserViewAMQPMapper;
import pt.psoft.g1.psoftg1.usermanagement.infrastructure.publishers.impl.UserEventsRabbitmqPublisherImpl;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.publishers.UserEventsPublisher;
import pt.psoft.g1.psoftg1.usermanagement.services.UserService;

import java.util.HashMap;
import java.util.Map;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@SpringBootTest(
        classes = {
                UserEventsRabbitmqPublisherImpl.class,
                UserService.class,
                UserViewAMQPMapper.class
        }
)
@Provider("user_event-producer")
@PactFolder("pacts")
public class UsersProducerCDCIT {

    @Autowired
    private UserEventsPublisher userEventsPublisher;

    @MockBean
    private RabbitTemplate template;

    @MockBean
    private DirectExchange direct;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp(PactVerificationContext context) {
        context.setTarget(new MessageTestTarget());
        doNothing().when(template).convertAndSend(
                any(String.class),
                any(String.class),
                any(Object.class)
        );
    }

    @PactVerifyProvider("a user created event")
    public MessageAndMetadata verifyUserCreatedEvent() {
        User user = User.newUser(
                "testUser",
                "password123",
                "Test User"
        );

        try {
            Map<String, String> message = new HashMap<>();
            message.put("username", user.getUsername());

            Map<String, String> metadata = new HashMap<>();
            metadata.put("contentType", "application/json");
            metadata.put("type", "user.created");

            return new MessageAndMetadata(
                    objectMapper.writeValueAsBytes(message),
                    metadata
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao criar mensagem", e);
        }
    }

    @PactVerifyProvider("a user updated event")
    public MessageAndMetadata verifyUserUpdatedEvent() {
        User user = User.newUser(
                "testUser",
                "password123",
                "Updated User"
        );

        try {
            Map<String, String> message = new HashMap<>();
            message.put("username", user.getUsername());

            Map<String, String> metadata = new HashMap<>();
            metadata.put("contentType", "application/json");
            metadata.put("type", "user.updated");

            return new MessageAndMetadata(
                    objectMapper.writeValueAsBytes(message),
                    metadata
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao criar mensagem", e);
        }
    }

    // Verifica a interação do pacto
    @Test
    void verifyPacts(PactVerificationContext context) {
        context.verifyInteraction();
    }
}
