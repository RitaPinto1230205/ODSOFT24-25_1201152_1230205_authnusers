package pt.psoft.g1.psoftg1.usermanagement.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.usermanagement.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class UserViewAMQPMapper {
    @Mapping(target = "username", source = "username")
    @Mapping(target = "name", expression = "java(user.getName().getName())")

    public abstract UserViewAMQP toUserViewAMQP(User user);

    public abstract List<UserViewAMQP> toUserViewAMQP(List<User> users);
}