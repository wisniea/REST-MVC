package pl.wisniea.restmvc_data.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import pl.wisniea.restmvc_data.exceptions.UserServiceException;
import pl.wisniea.restmvc_data.dto.UserDto;
import pl.wisniea.restmvc_data.entities.UserEntity;

import java.util.List;


public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto userDto) throws UserServiceException;

    UserDto getUserByUserId(String userId) throws UserServiceException;

    UserDto updateUser(String userId, UserDto userDto) throws UserServiceException;

    void deleteUser(String userId) throws UserServiceException;

    List<UserDto> getUsers(int page, int limit);

    UserEntity getUserByEmail(String email) throws UserServiceException;

    void save(UserEntity user);
}
