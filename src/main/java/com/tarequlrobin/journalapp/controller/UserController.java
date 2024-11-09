package com.tarequlrobin.journalapp.controller;

import com.tarequlrobin.journalapp.api.response.WeatherResponse;
import com.tarequlrobin.journalapp.entity.User;
import com.tarequlrobin.journalapp.service.UserService;
import com.tarequlrobin.journalapp.service.WeatherService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@Tag(name = "User APIs", description = "Read, Update & Delete UserDTO")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private WeatherService  weatherService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        Optional<User> optionalUser = userService.getAllUser().stream().findFirst();
        if (optionalUser.isPresent()) {
            return new ResponseEntity<>(userService.getAllUser(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("id/{id}")
    public ResponseEntity<?> getUserById(@PathVariable ObjectId id) {
        Optional<User> optionalUser = Optional.ofNullable(userService.getUserById(id));
        if (optionalUser.isPresent()) {
            return new ResponseEntity<>(optionalUser.get(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping("id/{id}")
    public ResponseEntity<?> updateUser(@PathVariable ObjectId id, @RequestBody User newUser) {
        User oldUser = userService.getUserById(id);
        if (oldUser != null) {
            oldUser.setUserName(newUser.getUserName());
            oldUser.setPassword(newUser.getPassword());
        }
        userService.saveEntry(oldUser);
        return new ResponseEntity<>(oldUser,HttpStatus.OK);
    }

    @DeleteMapping("id/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable ObjectId id) {
        Optional<User> optionalUser = Optional.ofNullable(userService.getUserById(id));
        if (optionalUser.isPresent()) {
            userService.deleteUserById(id);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateByName(@RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User oldUser = userService.findUserByUserName(userName);
        oldUser.setUserName(user.getUserName());
        oldUser.setPassword(user.getPassword());
        userService.saveNewUser(oldUser);
        return new ResponseEntity<>(oldUser,HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteByUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        userService.deleteByUserName(userName);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/greetings")
    public ResponseEntity<?> greeting() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        WeatherResponse weatherResponse = weatherService.getWeather("Dhaka");
        String greetings = "";
        if (weatherResponse != null) {
            greetings = "Weather of Entered City :"+weatherResponse.getCurrent().getTemperature();
        }
        return new ResponseEntity<>(authentication.getName() +", "+ greetings, HttpStatus.OK);
    }
}