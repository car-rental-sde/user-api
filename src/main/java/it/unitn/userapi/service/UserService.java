package it.unitn.userapi.service;

import it.unitn.userapi.entity.UserEntity;
import it.unitn.userapi.openapi.model.NewUserModel;
import it.unitn.userapi.openapi.model.UserModel;

import java.util.Optional;

public interface UserService {
    Optional<UserEntity> findById(Long id);
    Optional<UserEntity> addUser(NewUserModel request);
    Optional<UserEntity> editUser(UserModel userModel);
    void deleteUser(Long id);
}
