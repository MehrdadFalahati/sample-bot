package ir.asta.samplebot.service.userservice;

import ir.asta.samplebot.entities.user.UserEntity;
import ir.asta.samplebot.repository.UserRepository;
import ir.asta.samplebot.service.core.AbstractCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("springdatajpa")
public class UserService extends AbstractCrudService<UserEntity, Integer> {
    @Autowired
    public UserService(UserRepository repository) {
        super.setCrudRepository(repository);
    }

    private UserRepository getMyRepository() {
        return (UserRepository) super.getCrudRepository();
    }
}
