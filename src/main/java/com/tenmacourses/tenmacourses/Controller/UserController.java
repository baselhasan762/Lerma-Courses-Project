package com.tenmacourses.tenmacourses.Controller;


import com.tenmacourses.tenmacourses.DTO.UserLoginDTO;
import com.tenmacourses.tenmacourses.DTO.UserResponseDTO;
import com.tenmacourses.tenmacourses.Entity.Users;
import com.tenmacourses.tenmacourses.Service.JWTservice;
import com.tenmacourses.tenmacourses.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {



    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> GetAllUsers() {
        List<UserResponseDTO> users = userService.getAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> GetUser(@RequestParam int id ) {
        UserResponseDTO users = userService.getUserById(id);
        return ResponseEntity.ok(users);
    }
    @PostMapping
    public ResponseEntity<?> AddNewUser(@RequestBody Users user){
        boolean success = userService.addNewUser(user);
        if (success) {
            return ResponseEntity.ok("User Added Successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Couldn't add user");
        }
    }



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO user){
        String token = userService.verify(user);

        if(token.equals("Authentication Failed")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(token);
        }

        return ResponseEntity.ok(token);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        boolean success = userService.deleteUser(id);

        if (success) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
