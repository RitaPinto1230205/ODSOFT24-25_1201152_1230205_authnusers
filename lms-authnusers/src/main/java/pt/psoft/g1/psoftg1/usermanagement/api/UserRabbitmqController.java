package pt.psoft.g1.psoftg1.usermanagement.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.usermanagement.services.UserService;

import pt.psoft.g1.psoftg1.usermanagement.model.User;

@Component
public class UserRabbitmqController {
    private final UserService userService;
    private final ObjectMapper objectMapper;

    public UserRabbitmqController(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "${user.queue.name}")
    public void receiveUserCreatedMsg(Message message) {
        try {
            User user = objectMapper.readValue(message.getBody(), User.class);
            userService.handleUserUpdated(user);
        } catch (Exception e) {

        }
    }

    @RabbitListener(queues = "${user.queue.update.name}")
    public void receiveUserUpdated(Message message) {
        try {
            User user = objectMapper.readValue(message.getBody(), User.class);
            userService.handleUserUpdated(user);
        } catch (Exception e) {

        }
    }
}