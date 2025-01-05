package pt.psoft.g1.psoftg1.usermanagement.api;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.usermanagement.services.UserService;

@Component
public class UserRabbitmqController {
    private final UserService userService;

    public UserRabbitmqController(UserService userService) {
        this.userService = userService;
    }

    @RabbitListener(queues = "${user.queue.name}")
    public void receiveUserCreatedMsg(Message message) {
        // Implementação
    }

    @RabbitListener(queues = "${user.queue.update.name}")
    public void receiveUserUpdated(Message message) {
        // Implementação
    }
}