package com.tenmacourses.tenmacourses.Service;

import com.tenmacourses.tenmacourses.Entity.UserPrincipal;
import com.tenmacourses.tenmacourses.Entity.Users;
import com.tenmacourses.tenmacourses.Repository.UserRepo;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {




    UserRepo repo;

    public MyUserDetailsService(UserRepo repo){
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = repo.findByUsername(username);

        if(user == null){
            System.out.println("User not Found");
            throw new UsernameNotFoundException("User not Found");
        }

        return new UserPrincipal(user);

    }
}
