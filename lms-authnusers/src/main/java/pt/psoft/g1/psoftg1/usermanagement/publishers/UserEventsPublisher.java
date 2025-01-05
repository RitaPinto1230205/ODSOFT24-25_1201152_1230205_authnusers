package pt.psoft.g1.psoftg1.usermanagement.publishers;

import pt.psoft.g1.psoftg1.usermanagement.api.UserViewAMQP;
import pt.psoft.g1.psoftg1.usermanagement.model.User;

public interface UserEventsPublisher {
    UserViewAMQP sendUserCreated(User user);
    UserViewAMQP sendUserUpdated(User user, Long version);
}