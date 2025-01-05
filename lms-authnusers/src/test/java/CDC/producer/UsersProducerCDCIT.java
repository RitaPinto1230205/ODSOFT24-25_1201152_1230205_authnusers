package CDC.producer;

import au.com.dius.pact.provider.MessageAndMetadata;
import au.com.dius.pact.provider.PactVerifyProvider;
import au.com.dius.pact.provider.junit5.MessageTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
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

    @BeforeEach
    void setUp(PactVerificationContext context) {
        context.setTarget(new MessageTestTarget());
    }

    @TestTemplate
    void testTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @PactVerifyProvider("a user created event")
    public MessageAndMetadata verifyUserCreatedEvent() {
        // Usando o método factory em vez do builder
        User user = User.newUser(
                "testUser",
                "password123", // senha necessária
                "Test User"    // nome completo
        );

        userEventsPublisher.sendUserCreated(user);

        return new MessageAndMetadata(
                "{\"username\":\"testUser\",\"name\":\"Test User\"}".getBytes(),
                null
        );
    }

    @PactVerifyProvider("a user updated event")
    public MessageAndMetadata verifyUserUpdatedEvent() {
        // Usando o método factory em vez do builder
        User user = User.newUser(
                "testUser",
                "password123", // senha necessária
                "Updated User" // nome atualizado
        );

        userEventsPublisher.sendUserUpdated(user, 1L);

        return new MessageAndMetadata(
                "{\"username\":\"testUser\",\"name\":\"Updated User\"}".getBytes(),
                null
        );
    }
}