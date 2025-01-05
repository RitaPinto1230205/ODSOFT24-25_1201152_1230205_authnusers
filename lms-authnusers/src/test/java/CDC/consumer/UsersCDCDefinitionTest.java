package CDC.consumer;

import au.com.dius.pact.consumer.MessagePactBuilder;
import au.com.dius.pact.consumer.dsl.PactBuilder;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.core.model.messaging.MessagePact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "user_event-producer")
public class UsersCDCDefinitionTest {

    @Pact(consumer = "user_event-consumer")
    public MessagePact userCreatedPact(MessagePactBuilder builder) {
        return builder
                .given("a user created event")
                .expectsToReceive("user created message")
                .withContent("{" +
                        "\"username\": \"testUser\"," +
                        "\"name\": \"Test User\"" +
                        "}")
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "userCreatedPact")
    void testUserCreatedEvent(MessagePact pact) {
        // Obtém a mensagem do contrato
        String message = new String(pact.getMessages().get(0).contentsAsBytes());

        // Verifica se a mensagem contém os campos esperados
        assertTrue(message.contains("testUser"));
        assertTrue(message.contains("Test User"));
    }

    @Pact(consumer = "user_event-consumer")
    public MessagePact userUpdatedPact(MessagePactBuilder builder) {
        return builder
                .given("a user updated event")
                .expectsToReceive("user updated message")
                .withContent("{" +
                        "\"username\": \"testUser\"," +
                        "\"name\": \"Test User\"" +
                        "}")
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "userUpdatedPact")
    void testUserUpdatedEvent(MessagePact pact) {
        // Obtém a mensagem do contrato
        String message = new String(pact.getMessages().get(0).contentsAsBytes());

        // Verifica se a mensagem contém os campos esperados
        assertTrue(message.contains("testUser"));
        assertTrue(message.contains("Updated User"));
    }
}