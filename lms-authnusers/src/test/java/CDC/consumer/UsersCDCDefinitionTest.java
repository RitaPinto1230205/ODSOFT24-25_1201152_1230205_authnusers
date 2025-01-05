package CDC.consumer;

import au.com.dius.pact.consumer.MessagePactBuilder;
import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactBuilder;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.junit5.PactConsumerTest;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.Response;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.core.model.messaging.MessagePact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;
import pt.psoft.g1.psoftg1.usermanagement.api.UserRabbitmqController;
import pt.psoft.g1.psoftg1.usermanagement.services.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(PactConsumerTestExt.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {UserRabbitmqController.class, UserService.class})
@PactTestFor(providerName = "user_event-producer", pactVersion = PactSpecVersion.V4)
public class UsersCDCDefinitionTest {

    @MockBean
    private UserService userService;

    @Autowired
    private UserRabbitmqController listener;

    // Método Pact para evento assíncrono de "User Created"
    @Pact(consumer = "user_event-consumer")
    public MessagePact createUserCreatedPact(MessagePactBuilder builder) {
        return builder
                .expectsToReceive("a user created event")
                .withContent(new PactDslJsonBody()
                        .stringType("username", "testUser")
                        .stringType("name", "Test User"))
                .toPact();
    }

    // Método adicional para o evento de "User Updated"
    @Pact(consumer = "user_event-consumer")
    public MessagePact createUserUpdatedPact(MessagePactBuilder builder) {
        return builder
                .expectsToReceive("a user updated event")
                .withContent(new PactDslJsonBody()
                        .stringType("username", "testUser")
                        .stringType("name", "Updated User"))
                .toPact();
    }
}
