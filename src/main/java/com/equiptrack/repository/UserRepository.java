package com.equiptrack.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.equiptrack.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    
    Optional<User> findByEmail(String email);
    
    Boolean existsByEmail(String email);
    
    Optional<User> findByVerificationToken(String token);
    
    Optional<User> findByResetPasswordToken(String token);
    
    List<User> findByRole(User.UserRole role);
    
    List<User> findByStatus(User.AccountStatus status);
    
    List<User> findByRoleAndStatus(User.UserRole role, User.AccountStatus status);
    
    @Query(value = "{'role': 'CUSTOMER'}", count = true)
    Long countCustomers();
    
    @Query("{ $or: [ { 'fullName': { $regex: ?0, $options: 'i' } }, { 'email': { $regex: ?0, $options: 'i' } } ] }")
    List<User> searchUsers(String keyword);
}
