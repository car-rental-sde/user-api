package it.unitn.userapi.repository;

import it.unitn.userapi.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username); //select * from user where username = :username
/*
    User findByUsernameAndEmail(String username, String email, PageRequest.); //select * from user where username = :username and email = :email

    @Query(value = "select token from user where username like ?2 and email in :emails ")
    List<User> getToken(@Param("emails") Set<String> emails, String username, Pageable pageable);*/

    @Query(value = "select u from UserEntity u " +
            "where u.name like :firstName " +
            "and u.surname like :lastName ")
    Page<UserEntity> searchUser(@Param("firstName") String firstName,
                          @Param("lastName") String lastName,
                          @Param("userType") String userType,
                          Pageable pageable);
}
