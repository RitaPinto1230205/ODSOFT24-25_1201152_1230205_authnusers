package CDC.consumer;

import au.com.dius.pact.consumer.MessagePactBuilder;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.junit5.PactConsumerTest;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.core.model.messaging.MessagePact;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import pt.psoft.g1.psoftg1.usermanagement.api.UserRabbitmqController;
import pt.psoft.g1.psoftg1.usermanagement.services.UserService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@ExtendWith(PactConsumerTestExt.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {UserRabbitmqController.class, UserService.class})
@PactConsumerTest
@PactTestFor(providerName = "user_event-consumer", pactVersion = PactSpecVersion.V4)
public class UsersCDCDefinitionTest {


    @Pact(provider = "author_event-producer", consumer = "author_event-consumer")
    public MessagePact createPact(MessagePactBuilder builder) {
        PactDslJsonBody createdBody = new PactDslJsonBody()
                .stringValue("authorId", "123")
                .stringValue("name", "Test Author")
                .stringValue("email", "author@test.com");

        PactDslJsonBody updatedBody = new PactDslJsonBody()
                .stringValue("authorId", "123")
                .stringValue("name", "Updated Author")
                .stringValue("email", "author@test.com");

        return builder
                .expectsToReceive("an author created event")
                .withContent(createdBody)
                .expectsToReceive("an author updated event")
                .withContent(updatedBody)
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "createPact")
    void testAuthorEvents(MessagePact pact) {

        String createdMessage = new String(pact.getMessages().get(0).contentsAsBytes());
        assertTrue(createdMessage.contains("Test Author"));
        assertTrue(createdMessage.contains("author@test.com"));

        String updatedMessage = new String(pact.getMessages().get(1).contentsAsBytes());
        assertTrue(updatedMessage.contains("Updated Author"));
        assertTrue(updatedMessage.contains("author@test.com"));
    }

    @Test
    @PactTestFor(pactMethod = "createPact")
    void testUserEvents(MessagePact pact) {
        String createdMessage = new String(pact.getMessages().get(0).contentsAsBytes());
        assertTrue(createdMessage.contains("Test Author"));
        assertTrue(createdMessage.contains("author@test.com"));

      /*  JSONObject createdJson = new JSONObject(Integer.parseInt(createdMessage));
        assertEquals("123", createdJson.getString("authorId"));
        assertEquals("Test Author", createdJson.getString("name"));

        String updatedMessage = new String(pact.getMessages().get(1).contentsAsBytes());
        assertTrue(updatedMessage.contains("Updated Author"));
        assertTrue(updatedMessage.contains("author@test.com"));

        JSONObject updatedJson = new JSONObject(Integer.parseInt(updatedMessage));
        assertEquals("123", updatedJson.getString("authorId"));
        assertEquals("Updated Author", updatedJson.getString("name"));*/
    }
}
