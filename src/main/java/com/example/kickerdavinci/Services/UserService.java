package com.example.kickerdavinci.Services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.kickerdavinci.AuthenticationProperties;
import com.example.kickerdavinci.Models.model.Credentials;
import com.example.kickerdavinci.Models.model.NoIdUser;
import com.example.kickerdavinci.Models.User;
import com.example.kickerdavinci.Repository.UsersRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
public class UserService {

  private final UsersRepository usersRepository;

  private final Algorithm jwtAlgorithm;
  private final JWTVerifier jwtVerifier;

  public UserService(UsersRepository usersRepository,
      AuthenticationProperties properties) {
    this.usersRepository = usersRepository;
    this.jwtAlgorithm = Algorithm.HMAC512(properties.getSecret());
    this.jwtVerifier = JWT.require(this.jwtAlgorithm).withIssuer("auth0").build();
  }

  public boolean createOne(NoIdUser user) {

    if (usersRepository.existsByEmail(user.getEmail())) {
      return false;
    }
    user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
    usersRepository.save(user.toUser());
    //usersRepository.deleteAll();
    return true;
  }

  public String connect(Credentials credentials) {
    User user = findUser(credentials.getEmail());
    if (user == null) {
      return null;
    }
    if (!BCrypt.checkpw(credentials.getPassword(), user.getPassword())) {
      return null;
    }
    return JWT.create().withIssuer("auth0").withClaim("email", user.getEmail())
        .sign(this.jwtAlgorithm);
  }

  public boolean existsEmail(String email) {
    return usersRepository.existsByEmail(email);
  }

  public boolean update(User user, String email) {
    if (user == null || user.getEmail() == null || user.getPassword() == null) {
      return false;
    }
    if (!email.equals(user.getEmail()) && usersRepository.existsByEmail(user.getEmail())) {
      return false;
    }
    User userDb = findUser(email);
    if (userDb == null) {
      return false;
    }
    user.setId(userDb.getId());
    user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
    usersRepository.save(user);
    return true;
  }

  public User findUser(String email) {
    return usersRepository.findByEmail(email);
  }
  public User findUserbyId(long id){
    return usersRepository.findById(id);
  }

  public void deleteById(long id){
    usersRepository.deleteById(id);
  }

  public Iterable<User> getAll() {
    return usersRepository.findAll();
  }

  public String verify(String token) {
    if (token == null || token.isBlank()) {
      return null;
    }
    try {
      String email = jwtVerifier.verify(token).getClaim("email").asString();
      if (email == null || !usersRepository.existsByEmail(email)) return null;
      return email;
    } catch (JWTVerificationException e) {
      return null;
    }
  }
  public Iterable<User> getAllWithoutClub() {
    return usersRepository.findByClubIsNull();
  }
  public Iterable<User> findAllByFlag(){return usersRepository.findAllByAdminFlag(true);}

  public Iterable<User> getAllSortedByRanking(){
    return usersRepository.findAll().stream().sorted(Comparator.comparing(User::getRankingPoints).reversed()).toList();
  }
}
