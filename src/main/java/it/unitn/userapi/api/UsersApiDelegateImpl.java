package it.unitn.userapi.api;

import it.unitn.userapi.mapper.Mappers;
import it.unitn.userapi.openapi.api.UsersApiDelegate;
import it.unitn.userapi.openapi.model.NewUserModel;
import it.unitn.userapi.openapi.model.UserModel;
import it.unitn.userapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UsersApiDelegateImpl implements UsersApiDelegate {

    private final UserService userService;
    private final Mappers mappers;

    @Override
    public ResponseEntity<UserModel> addUser(NewUserModel newUserModel) {
        log.debug("Adding new user: [{}]", newUserModel);

        return userService.addUser(newUserModel)
                .map(userEntity -> ResponseEntity.ok(mappers.userToUserModel(userEntity)))
                .orElse(ResponseEntity.badRequest().build());
    }

    @Override
    public ResponseEntity<UserModel> getUser(Long id) {
        log.debug("Getting user with id: [{}]", id);

        return userService.findById(id)
                .map(userEntity -> ResponseEntity.ok(mappers.userToUserModel(userEntity)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<UserModel> updateUser(Long id, UserModel userModel) {
        log.debug("Updating user with id: [{}]", id);

        return userService.editUser(userModel)
                .map(userEntity -> ResponseEntity.ok(mappers.userToUserModel(userEntity)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Void> deleteUser(Long id) {
        log.debug("Deleting user with id: [{}]", id);

        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
