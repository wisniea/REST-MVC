package pl.wisniea.restmvc_data.repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.wisniea.restmvc_data.entities.AddressEntity;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long> {
}
