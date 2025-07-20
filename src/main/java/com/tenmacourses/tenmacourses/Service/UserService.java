package com.tenmacourses.tenmacourses.Service;

import com.tenmacourses.tenmacourses.DTO.UserLoginDTO;
import com.tenmacourses.tenmacourses.DTO.UserResponseDTO;
import com.tenmacourses.tenmacourses.Entity.Users;
import com.tenmacourses.tenmacourses.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService{

    UserRepo userRepo;
    @Autowired
    private BCryptPasswordEncoder encode =  new BCryptPasswordEncoder(12);

    @Autowired
    private AuthenticationManager authManager;


    @Autowired
    private JWTservice jwTservice;

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;

    }

    public List<UserResponseDTO> getAll() {

        return userRepo.findAll().
                stream()
                .map(user -> new UserResponseDTO(user))
                .collect(Collectors.toList());
    }

    ;

    public UserResponseDTO getUserById(Integer id) {
        return userRepo.findById(id)
                .map(user -> new UserResponseDTO(user))
                .orElse(null);
    }

    public Users getUserByName(String username){
        return userRepo.findByUsername(username);
    }

    public boolean addNewUser(Users user){
        try{
            user.setPassword(encode.encode(user.getPassword()));
            userRepo.save(user);
        }
        catch(Exception ex){
            System.out.println("There Was An Error Adding User");
            ex.printStackTrace();
            return false;
        }
        return true;
    }


   public boolean deleteUser(int id ){

        try{
           userRepo.deleteById(id);
        }
        catch(Exception ex){
            System.out.println("There Was An Error Deleting User");
            return false;
        }
        return true;
    }

    public boolean updateUser(Integer id, Users updatedUser) {
        return userRepo.findById(id).map(existingUser -> {
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPassword(updatedUser.getPassword());
            existingUser.setBalance(updatedUser.getBalance());

            userRepo.save(existingUser);
            return true;
        }).orElse(false);
    }


    public String verify(UserLoginDTO user) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );

            return jwTservice.generateToken(user.getUsername());

        } catch (AuthenticationException e) {
            return "Authentication Failed";
        }
    }

};
