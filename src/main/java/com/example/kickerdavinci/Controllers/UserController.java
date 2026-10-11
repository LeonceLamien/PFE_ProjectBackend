package com.example.kickerdavinci.Controllers;

import com.example.kickerdavinci.Models.User;
import com.example.kickerdavinci.Models.model.Credentials;
import com.example.kickerdavinci.Models.model.NoIdUser;
import com.example.kickerdavinci.Services.UserService;
import java.time.LocalDate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
@CrossOrigin(origins = { "https://kickerdavinci.azurewebsites.net" })
@RestController
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/user")
  public ResponseEntity<Void> createUser(@RequestBody NoIdUser user) {
    if (user == null || user.getLastname() == null || user.getLastname().isBlank()
        || user.getFirstname() == null || user.getFirstname().isBlank()
        || user.getEmail() == null || user.getEmail().isBlank()
        || user.getPassword() == null || user.getPassword().isBlank()
        || user.getBirthDate() == null || user.getBirthDate().isAfter(LocalDate.now())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    if (!userService.createOne(user)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT);
    }
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PutMapping("/user/{email}")
  public ResponseEntity<Void> updateUser(@RequestBody NoIdUser user, @PathVariable String email,
      @RequestHeader("Authorization") String token) {
    if (email == null || email.isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    String userVerified = userService.verify(token);
    if (userVerified == null || !userVerified.equals(email)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    if (user == null || user.getLastname() == null || user.getLastname().isBlank()
        || user.getFirstname() == null || user.getFirstname().isBlank()
        || user.getEmail() == null || user.getEmail().isBlank()
        || user.getPassword() == null || user.getPassword().isBlank()
        || user.getBirthDate() == null || user.getBirthDate().isAfter(LocalDate.now())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    if (!userService.existsEmail(email)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    if (!userService.update(user.toUser(), email)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT);
    }
    return new ResponseEntity<>(HttpStatus.OK);

  }

  @PostMapping("/authentication/connect")
  public String connect(@RequestBody Credentials credentials) {
    if (credentials == null || credentials.getEmail() == null || credentials.getPassword() == null
        || credentials.getPassword().isBlank() || credentials.getEmail().isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    String token = userService.connect(credentials);
    if (token == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
    //return the token inside a json object
    return "{\"token\":\"" + token + "\"}";
  }
  @GetMapping("/usersWithoutClub")
  public ResponseEntity<Iterable<User>> getAllWithoutClub() {
    return new ResponseEntity<>(userService.getAllWithoutClub(), HttpStatus.OK);
  }
  @GetMapping("/users")
  public ResponseEntity<Iterable<User>> getAll() {
    return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
  }

  @GetMapping("/users/admin")
  public ResponseEntity<Iterable<User>> findAllByFlag(){return new ResponseEntity<>(userService.findAllByFlag(), HttpStatus.OK);}

  @GetMapping("/users/ranking")
  public ResponseEntity<Iterable<User>> getAllSortedByRanking() {
    return new ResponseEntity<>(userService.getAllSortedByRanking(), HttpStatus.OK);
  }

}
