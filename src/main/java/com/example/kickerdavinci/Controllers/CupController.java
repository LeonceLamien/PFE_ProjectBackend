package com.example.kickerdavinci.Controllers;

import com.example.kickerdavinci.Models.Cup;
import com.example.kickerdavinci.Models.model.NoIdCup;
import com.example.kickerdavinci.Services.CupService;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class CupController {

  private final CupService cupService;

  public CupController(CupService cupService) {
    this.cupService = cupService;
  }

  @PostMapping("/cup")
  public ResponseEntity<Void> createOne(@RequestBody NoIdCup cup) {
    if (cup == null || cup.getStartDate() == null || cup.getEndDate() == null || cup.getName() == null
        || cup.getName().isBlank() || cup.getEndDate().isBefore(cup.getStartDate())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    if (!cupService.createOne(cup)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT);
    }
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping("/cup/{name}")
  public ResponseEntity<Cup> getOne(@PathVariable String name) {
    if (name == null || name.isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    Cup cup = cupService.getOne(name);
    if (cup == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(cup, HttpStatus.OK);
  }

  @GetMapping("/cup")
  public ResponseEntity<Iterable<Cup>> getByStartAndEndDate(@RequestParam LocalDate start,
      @RequestParam LocalDate end) {
    if (start == null || end == null || end.isBefore(start)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    Iterable<Cup> cups = cupService.getByStartAndEndDate(start, end);
    if (cups == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(cups, HttpStatus.OK);
  }

  @GetMapping("/cups")
  public ResponseEntity<Iterable<Cup>> getAllCups() {
    return new ResponseEntity<>(cupService.getAll(), HttpStatus.OK);
  }

  @DeleteMapping("/cup/{name}")
  public ResponseEntity<Void> deleteByName(@PathVariable String name) {
    if (name == null || name.isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    if (!cupService.deleteByName(name)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping("/cup/{name}")
  public ResponseEntity<Void> updateUser(@RequestBody NoIdCup cup, @PathVariable String name) {
    if (cup == null || cup.getStartDate() == null || cup.getEndDate() == null || cup.getName() == null
        || cup.getName().isBlank() || cup.getEndDate().isBefore(cup.getStartDate())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    if (!cupService.existsName(name)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    if (!cupService.update(cup.toCup(), name)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT);
    }
    return new ResponseEntity<>(HttpStatus.OK);

  }

  @GetMapping("/cups/active")
  public Iterable<Cup> getCurrentCups() {
    Iterable<Cup> cups = cupService.getAll();// load cups from a repository or somewhere else
    List<Cup> cupsList = StreamSupport.stream(cups.spliterator(),false).toList();
    return cupService.getCurrentCups(cupsList);
  }
}
