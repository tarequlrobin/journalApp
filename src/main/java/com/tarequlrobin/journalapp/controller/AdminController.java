package com.tarequlrobin.journalapp.controller;

import com.tarequlrobin.journalapp.entity.User;
import com.tarequlrobin.journalapp.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin APIs")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUser(){
        List<User> userList = userService.getAllUser();
        if(!userList.isEmpty() && userList != null){
            return new  ResponseEntity<>(userList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/create-admin-user")
    public void createAdminUser(@RequestBody User user){
        userService.saveAdmin(user);
    }
}
