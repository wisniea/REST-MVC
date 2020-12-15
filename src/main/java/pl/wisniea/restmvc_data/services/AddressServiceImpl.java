package pl.wisniea.restmvc_data.services;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import pl.wisniea.restmvc_data.repositories.AddressRepository;
import pl.wisniea.restmvc_data.entities.AddressEntity;
import pl.wisniea.restmvc_data.entities.UserEntity;
import pl.wisniea.restmvc_data.response.AddressRest;
import pl.wisniea.restmvc_data.repositories.UserRepository;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;

    public AddressServiceImpl(UserRepository userRepository, AddressRepository addressRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<AddressRest> getAddressesByUserId(String userId) {
        UserEntity user = userRepository.findByUserId(userId);

        List<AddressEntity> addresses = user.getAddresses();

        Type listType = new TypeToken<List<AddressRest>>() {
        }.getType();

        return modelMapper.map(addresses, listType);
    }

    @Override
    public List<AddressRest> getAllAddresses() {
        List<AddressEntity> all = (List<AddressEntity>) addressRepository.findAll();

        Type listType = new TypeToken<List<AddressRest>>() {
        }.getType();

        return modelMapper.map(all, listType);
    }
}
