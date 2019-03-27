package ir.asta.samplebot.repository;

import ir.asta.samplebot.entities.user.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {
    UserEntity findByTelegramIdAndDeletedIsFalse(Integer telegramId);
}

