package pl.wisniea.restmvc_data.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.wisniea.restmvc_data.entities.UserEntity;
import pl.wisniea.restmvc_data.entities.VerificationTokenEntity;

@Repository
public interface VerificationTokenRepository extends CrudRepository<VerificationTokenEntity, Long> {

    VerificationTokenEntity findByToken(String token);

    VerificationTokenEntity findByUserEntity(UserEntity user);
}
