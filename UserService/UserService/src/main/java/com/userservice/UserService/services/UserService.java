package com.userservice.UserService.services;

import com.userservice.UserService.entities.User;
import com.userservice.UserService.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    PasswordEncoder encoder;

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }


    public User createUser(User user){
        System.err.println(user.getRole());
        if ((user.getRole().toString() == "ADMIN") || (user.getRole().toString()=="USER")){
            if (userRepository.existsByUsername(user.getUsername())) {
                throw new RuntimeException("Error: Username is already taken!") ;
            }
            // Create new user's account
            User newUser = new User(
                    user.getUsername(),
                    encoder.encode(user.getPassword()),
                    user.getRole()
            );
            return userRepository.save(newUser);
            //"User registered successfully!";
        }
        throw new RuntimeException("A user must have a role  ADMIN or USER");
    }

    public Optional<User> getUserById(Long id){
        if (userRepository.findById(id).isPresent()){
            return userRepository.findById(id);
        }
        else {
            throw new RuntimeException("User with id: " +id+ " doesn't exist");
        }

    }

    public void deleteUserById(Long id){
        if (userRepository.findById(id).isPresent()){
            userRepository.deleteById(id);
        }else{
            throw new RuntimeException("User with id: " +id+ " doesn't exist");
        }

    }


}
