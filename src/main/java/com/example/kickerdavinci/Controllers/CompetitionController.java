package com.example.kickerdavinci.Controllers;

import com.example.kickerdavinci.Models.Competition;
import com.example.kickerdavinci.Models.model.NoIdCompetition;
import com.example.kickerdavinci.Services.CompetitionService;
import java.time.LocalDate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class CompetitionController {

  private final CompetitionService competitionService;

  public CompetitionController(CompetitionService competitionService) {
    this.competitionService = competitionService;
  }

  @PostMapping("/competition")
  public ResponseEntity<Void> createOne(@RequestBody NoIdCompetition competition) {
    if (competition == null || competition.getMaxSlots() <= 0 || competition.getNote() == null
        || competition.getNote().isBlank() || competition.getDate() == null
        || competition.getDate().isBefore(LocalDate.now())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    if (!competitionService.createOne(competition)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT);
    }
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping("/competitions")
  public ResponseEntity<Iterable<Competition>> getAll() {
    return new ResponseEntity<>(competitionService.getAll(), HttpStatus.OK);
  }

  @GetMapping("/competitions/{date}")
  public ResponseEntity<Competition> getCompetitionByDate(@PathVariable LocalDate date) {
    if (date == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    Competition competition = competitionService.getCompetitionByDate(date);
    if (competition == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(competition, HttpStatus.OK);
  }

  @DeleteMapping("/competition/{date}")
  public ResponseEntity<Void> deleteByName(@PathVariable LocalDate date) {
    if (date == null || date.isBefore(LocalDate.now())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    if (!competitionService.deleteByDate(date)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping("/competition/{date}")
  public ResponseEntity<Void> updateDivision(@RequestBody NoIdCompetition competition,
      @PathVariable LocalDate date) {
    if (competition == null || competition.getMaxSlots() <= 0 || competition.getNote() == null
        || competition.getNote().isBlank() || competition.getDate() == null
        || competition.getDate().isBefore(LocalDate.now())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    if (!competitionService.existsDate(date)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    if (!competitionService.update(competition.toCompetition(), date)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT);
    }
    return new ResponseEntity<>(HttpStatus.OK);

  }
}
