package pl.wisniea.restmvc_data.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.wisniea.restmvc_data.entities.RestCartEntity;
import pl.wisniea.restmvc_data.entities.UserEntity;

@Repository
public interface RestCartRepository extends CrudRepository<RestCartEntity, Long> {

    RestCartEntity findByUser(UserEntity user);

}
