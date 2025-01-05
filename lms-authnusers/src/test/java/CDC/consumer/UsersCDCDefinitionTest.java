package CDC.consumer;

import au.com.dius.pact.consumer.dsl.PactBuilder;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "user_event-producer")
public class UsersCDCDefinitionTest {

    @Pact(provider = "user_event-producer", consumer = "user_event-consumer")
    public V4Pact userCreatedPact(PactBuilder builder) throws Exception {
        Map<String, Object> message = Map.of(
                "username", "testUser",
                "name", "Test User"
        );

        return builder
                .given("a user created event exists")
                .hasPactWith("user_event-producer")
                .synchronousMessage("a user created event")
                .withContent(message)
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "userCreatedPact")
    void testUserCreatedEvent(V4Pact pact) {
        var message = pact.getInteractions().get(0).asMessage().contentsAsString();
        assertTrue(message.contains("testUser"));
        assertTrue(message.contains("Test User"));
    }

    @Pact(provider = "user_event-producer", consumer = "user_event-consumer")
    public V4Pact userUpdatedPact(PactBuilder builder) throws Exception {
        Map<String, Object> message = Map.of(
                "username", "testUser",
                "name", "Updated User"
        );

        return builder
                .given("a user updated event exists")
                .hasPactWith("user_event-producer")
                .synchronousMessage("a user updated event")
                .withContent(message)
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "userUpdatedPact")
    void testUserUpdatedEvent(V4Pact pact) {
        var message = pact.getInteractions().get(0).asMessage().contentsAsString();
        assertTrue(message.contains("testUser"));
        assertTrue(message.contains("Updated User"));
    }
}