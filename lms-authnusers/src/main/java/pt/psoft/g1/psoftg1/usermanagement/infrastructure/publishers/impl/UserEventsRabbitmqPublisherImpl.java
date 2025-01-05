package pt.psoft.g1.psoftg1.usermanagement.infrastructure.publishers.impl;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.usermanagement.api.UserViewAMQP;
import pt.psoft.g1.psoftg1.usermanagement.api.UserViewAMQPMapper;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.publishers.UserEventsPublisher;

@Component
public class UserEventsRabbitmqPublisherImpl implements UserEventsPublisher {
    private final RabbitTemplate template;
    private final DirectExchange exchange;
    private final UserViewAMQPMapper mapper;

    public UserEventsRabbitmqPublisherImpl(RabbitTemplate template, DirectExchange exchange, UserViewAMQPMapper mapper) {
        this.template = template;
        this.exchange = exchange;
        this.mapper = mapper;
    }

    @Override
    public UserViewAMQP sendUserCreated(User user) {
        UserViewAMQP userView = mapper.toUserViewAMQP(user);
        template.convertAndSend(exchange.getName(), "user.created", userView);
        return userView;
    }

    @Override
    public UserViewAMQP sendUserUpdated(User user, Long version) {
        UserViewAMQP userView = mapper.toUserViewAMQP(user);
        template.convertAndSend(exchange.getName(), "user.updated", userView);
        return userView;
    }
}