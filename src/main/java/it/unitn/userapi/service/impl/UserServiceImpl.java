package it.unitn.userapi.service.impl;

import it.unitn.userapi.entity.UserEntity;
import it.unitn.userapi.entity.UserRole;
import it.unitn.userapi.openapi.model.NewUserModel;
import it.unitn.userapi.openapi.model.UserModel;
import it.unitn.userapi.repository.UserRepository;
import it.unitn.userapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<UserEntity> addUser(NewUserModel request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            log.warn("User with username [{}] already exists", request.getUsername());
            return Optional.empty();
        }

        var user = UserEntity.builder()
                .username(request.getUsername())
                .name(request.getName())
                .surname(request.getSurname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.valueOf(request.getUserRole().name()))
                .isBlocked(false)
                .build();

        return Optional.of(userRepository.save(user));
    }

    @Override
    public Optional<UserEntity> editUser(UserModel userModel) {
        var user = userRepository.findById(userModel.getId());
        if (user.isEmpty()) {
            log.warn("User with id [{}] not found", userModel.getId());
            return Optional.empty();
        }

        user.get().setName(userModel.getName());
        user.get().setSurname(userModel.getSurname());
        user.get().setEmail(userModel.getEmail());
        user.get().setRole(UserRole.valueOf(userModel.getUserRole().name()));
        return Optional.of(userRepository.save(user.get()));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
