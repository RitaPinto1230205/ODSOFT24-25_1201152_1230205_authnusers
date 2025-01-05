package CDC.consumer;

import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.consumer.junit5.PactConsumerTest;
import au.com.dius.pact.consumer.junit5.ProviderType;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pt.psoft.g1.psoftg1.usermanagement.api.UserRabbitmqController;
import pt.psoft.g1.psoftg1.usermanagement.services.UserService;

@ExtendWith(PactConsumerTestExt.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {UserRabbitmqController.class, UserService.class}
)
@PactConsumerTest
@PactTestFor(providerName = "user_event-producer", providerType = ProviderType.ASYNCH)
public class UsersCDCDefinitionTest {
    @MockBean
    UserService userService;

    @Autowired
    UserRabbitmqController listener;
}