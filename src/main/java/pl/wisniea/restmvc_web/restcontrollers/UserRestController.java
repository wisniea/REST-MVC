package pl.wisniea.restmvc_web.restcontrollers;

import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.wisniea.restmvc_data.exceptions.UserServiceException;
import pl.wisniea.restmvc_data.dto.UserDto;
import pl.wisniea.restmvc_data.request.UserDetailsRequestModel;
import pl.wisniea.restmvc_data.response.UserRest;
import pl.wisniea.restmvc_data.services.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static pl.wisniea.restmvc_data.exceptions.ErrorMessages.INCORRECT_FIELDS;

@RestController
@RequestMapping("rest/users")
public class UserRestController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private static final String USERS = "users";

    public UserRestController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<UserRest> createUser(@Valid @RequestBody UserDetailsRequestModel userDetails, BindingResult result) throws UserServiceException {

        if (result.hasErrors()) {
            throw new IllegalStateException(INCORRECT_FIELDS.getErrorMessage());
        }

        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        UserDto createdUser = userService.createUser(userDto);

        UserRest returnVal = modelMapper.map(createdUser, UserRest.class);

        Link selfLink = linkTo(UserRestController.class).slash(returnVal.getUserId()).withSelfRel();
        Link address = linkTo(UserRestController.class).slash(returnVal.getUserId()).slash("/addresses").withRel("address");
        Link allUsers = linkTo(WebMvcLinkBuilder.methodOn(UserRestController.class).getAllUsers(0, 25)).withRel(USERS);

        return EntityModel.of(returnVal, List.of(selfLink, allUsers, address));

    }

    @Secured("ROLE_ADMIN")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<UserRest> getAllUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                                 @RequestParam(value = "limit", defaultValue = "25") int limit) throws UserServiceException {

        List<UserRest> returnValue = new ArrayList<>();

        List<UserDto> users = userService.getUsers(page, limit);

        users.forEach(x -> {
            UserRest userModel = modelMapper.map(x, UserRest.class);
            returnValue.add(userModel);
        });

        List<Link> hateoas = new ArrayList<>();

        Link selfLink = linkTo(WebMvcLinkBuilder.methodOn(UserRestController.class).getAllUsers(0, 25)).withSelfRel();
        hateoas.add(selfLink);

        if (!returnValue.isEmpty()) {
            Link userLink = linkTo(methodOn(UserRestController.class).getSpecificUser(returnValue.get(0).getUserId())).withRel("/{userId}");
            hateoas.add(userLink);
        }

        return CollectionModel.of(returnValue, hateoas);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<UserRest> getSpecificUser(@PathVariable String userId) throws UserServiceException {

        UserDto userDto = userService.getUserByUserId(userId);

        UserRest returnVal = modelMapper.map(userDto, UserRest.class);

        Link selfLink = linkTo(UserRestController.class).slash(userId).withSelfRel();
        Link allUsers = linkTo(WebMvcLinkBuilder.methodOn(UserRestController.class).getAllUsers(0, 25)).withRel(USERS);
        Link address = linkTo(UserRestController.class).slash(userId).slash("/addresses").withRel("address");

        return EntityModel.of(returnVal, List.of(selfLink, allUsers, address));
    }

    @Secured("ROLE_ADMIN")
    @PutMapping(path = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<UserRest> updateUser(@RequestBody UserDetailsRequestModel userDetails, @PathVariable String userId) throws UserServiceException {


        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        UserDto updatedUser = userService.updateUser(userId, userDto);

        UserRest returnValue = modelMapper.map(updatedUser, UserRest.class);

        Link selfLink = linkTo(UserRestController.class).slash(userId).withSelfRel();
        Link allUsers = linkTo(WebMvcLinkBuilder.methodOn(UserRestController.class).getAllUsers(0, 25)).withRel(USERS);

        return EntityModel.of(returnValue, List.of(selfLink, allUsers));
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) throws UserServiceException {

        userService.deleteUser(userId);

        return ResponseEntity.noContent().build();


    }

}
