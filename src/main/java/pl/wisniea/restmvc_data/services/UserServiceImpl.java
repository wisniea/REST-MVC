package pl.wisniea.restmvc_data.services;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.wisniea.restmvc_data.repositories.RestCartRepository;
import pl.wisniea.restmvc_data.dto.AddressDto;
import pl.wisniea.restmvc_data.dto.UserDto;
import pl.wisniea.restmvc_data.entities.RestCartEntity;
import pl.wisniea.restmvc_data.entities.UserEntity;
import pl.wisniea.restmvc_data.entities.VerificationTokenEntity;
import pl.wisniea.restmvc_data.exceptions.UserServiceException;
import pl.wisniea.restmvc_data.repositories.UserRepository;
import pl.wisniea.restmvc_data.utils.Utils;


import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static pl.wisniea.restmvc_data.exceptions.ErrorMessages.NO_RECORD_FOUND;
import  static pl.wisniea.restmvc_data.exceptions.ErrorMessages.RECORD_ALREADY_EXISTS;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RestCartRepository restCartRepository;
    private final ModelMapper modelMapper;
    private final Utils utils;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;


    public UserServiceImpl(UserRepository userRepository, RestCartRepository restCartRepository, ModelMapper modelMapper, Utils utils, PasswordEncoder passwordEncoder, VerificationTokenService verificationTokenService, EmailService emailService) {
        this.userRepository = userRepository;
        this.restCartRepository = restCartRepository;
        this.modelMapper = modelMapper;
        this.utils = utils;
        this.passwordEncoder = passwordEncoder;
        this.verificationTokenService = verificationTokenService;
        this.emailService = emailService;
    }

    @Transactional
    @Override
    public UserDto createUser(UserDto user) throws UserServiceException {

        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new UserServiceException(RECORD_ALREADY_EXISTS.getErrorMessage());
        }

        if (!user.getAddresses().isEmpty()) {

            for (int i = 0; i < user.getAddresses().size(); i++) {
                AddressDto address = user.getAddresses().get(i);
                address.setUserDetails(user);
                address.setAddressId(utils.generateAddressId(20));
                user.getAddresses().set(i, address);
            }

        }

        UserEntity userEntity = modelMapper.map(user, UserEntity.class);

        userEntity.setUserId(utils.generateUserId(15));
        userEntity.setEncryptedPassword(passwordEncoder.encode(user.getPassword()));
        userEntity.setEmailVerificationStatus(false);

        RestCartEntity cartEntity = new RestCartEntity();
        cartEntity.setCartId(String.valueOf(UUID.randomUUID()));
        cartEntity.setUser(userEntity);

        restCartRepository.save(cartEntity);
        userEntity.setCart(cartEntity);


        try {
            // email activation token
            String token = utils.generateVerificationToken(25);
            VerificationTokenEntity savedToken = verificationTokenService.save(userEntity, token);
            userEntity.setEmailVerificationToken(savedToken);

            // send verification email
            emailService.sendHtmlMail(userEntity);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        UserEntity stored = userRepository.save(userEntity);

        return modelMapper.map(stored, UserDto.class);
    }

    @Override
    public List<UserDto> getUsers(int page, int limit) {
        List<UserDto> returnVal = new ArrayList<>();
        Pageable pageableRequest = PageRequest.of(page, limit);
        Page<UserEntity> userEntities = userRepository.findAll(pageableRequest);

        userEntities.getContent().stream().forEach(x -> {
            UserDto userDto = modelMapper.map(x, UserDto.class);
            returnVal.add(userDto);
        });


        return returnVal;
    }

    @Override
    public UserEntity getUserByEmail(String email) throws UserServiceException {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserServiceException(NO_RECORD_FOUND.getErrorMessage());
        }
        return user;

    }

    @Override
    public void save(UserEntity user) {
        userRepository.save(user);
    }

    @Override
    public UserDto getUserByUserId(String userId) throws UserServiceException {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            throw new UserServiceException(NO_RECORD_FOUND.getErrorMessage());
        }
        return modelMapper.map(userEntity, UserDto.class);
    }


    @Override
    public UserDto updateUser(String userId, UserDto userDto) throws UserServiceException {

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null) {
            throw new UserServiceException(NO_RECORD_FOUND.getErrorMessage());
        }

        mapper.map(userDto, userEntity);

        userRepository.save(userEntity);

        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public void deleteUser(String userId) throws UserServiceException {

        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null) {
            throw new UserServiceException(NO_RECORD_FOUND.getErrorMessage());
        }

        userRepository.delete(userEntity);
    }


    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) {

        UserEntity user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UserServiceException(NO_RECORD_FOUND.getErrorMessage());
        }
        return user;
    }
}
