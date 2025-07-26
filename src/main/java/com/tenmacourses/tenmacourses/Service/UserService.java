package com.tenmacourses.tenmacourses.Service;

import com.tenmacourses.tenmacourses.DTO.UserLoginDTO;
import com.tenmacourses.tenmacourses.DTO.UserRequestDTO;
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

import java.time.LocalDate;
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

    public List<Users> getAll() {

        return userRepo.findAll();
    }


    public Users getUserById(Integer id) {
        return userRepo.findById(id).orElseThrow(()->new RuntimeException("user not found"));
    }

    public Users getUserByName(String username){
        return userRepo.findByUsername(username);
    }

    public boolean addNewUser(UserRequestDTO dto){
        try {
            Users user = new Users();
            user.setUsername(dto.getUsername());
            user.setEmail(dto.getEmail());
            user.setPhone(dto.getPhone());
            user.setPassword(encode.encode(dto.getPassword()));
            user.setRole(dto.getRole());
            user.setBalance(dto.getBalance());
            user.setCreatedAt(LocalDate.now());
            user.setUpdatedAt(LocalDate.now());

            userRepo.save(user);
            return true;
        } catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
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
