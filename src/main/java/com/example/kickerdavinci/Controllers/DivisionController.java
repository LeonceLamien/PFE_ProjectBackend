package com.example.kickerdavinci.Controllers;

import com.example.kickerdavinci.Models.Club;
import com.example.kickerdavinci.Models.Competition;
import com.example.kickerdavinci.Models.Division;
import com.example.kickerdavinci.Models.Game;
import com.example.kickerdavinci.Models.model.NoIdDivision;
import com.example.kickerdavinci.Services.ClubService;
import com.example.kickerdavinci.Services.CompetitionService;
import com.example.kickerdavinci.Services.DivisionService;
import com.example.kickerdavinci.Services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.ArrayList;
import java.util.List;

@RestController
public class DivisionController {

  private final DivisionService divisionService;
  @Autowired
  private final CompetitionService competitionService;
  @Autowired
  private final ClubService clubService;
  @Autowired
  private final GameService gameService;


  public DivisionController(DivisionService divisionService, CompetitionService competitionService, ClubService clubService, GameService gameService) {
    this.divisionService = divisionService;
    this.competitionService = competitionService;
    this.clubService = clubService;
    this.gameService = gameService;
  }

  @PostMapping("/division")
  public ResponseEntity<Void> createOne(@RequestBody NoIdDivision division) {
    if (division == null || division.getName() == null || division.getName().isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    if (!divisionService.createOne(division)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT);
    }
    return new ResponseEntity<>(HttpStatus.CREATED);
  }


  @GetMapping("/divisions")
  public ResponseEntity<Iterable<Division>> getAllDivisions() {
    return new ResponseEntity<>(divisionService.getAll(), HttpStatus.OK);
  }

  @DeleteMapping("/division/{name}")
  public ResponseEntity<Void> deleteByName(@PathVariable String name) {
    if (name == null || name.isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    if (!divisionService.deleteByName(name)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping("/division/{name}")
  public ResponseEntity<Void> updateDivision(@RequestBody NoIdDivision division,
      @PathVariable String name) {
    if (division == null || division.getName() == null || division.getName().isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    if (!divisionService.existsName(name)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    if (!divisionService.update(division.toDivision(), name)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT);
    }
    return new ResponseEntity<>(HttpStatus.OK);

  }

  @GetMapping("/divisions/{divisionName}")
  public List<Competition> getCompetitionsByDivision(@PathVariable String divisionName) {
    // First, get the division with the given name
    Division division = divisionService.findByName(divisionName);

    // Next, get all the clubs that belong to this division
    List<Club> clubs = clubService.findByDivisionId(division);

    // Create a list to store the competitions that we will return
    List<Competition> competitions = (List<Competition>) competitionService.getAll();

    List<Competition> finalList = new ArrayList<>();
    // Loop through each competition, get all clubs from it and get the clubs inside the clubs_from_division
    for (Competition competition: competitions) {
      for (Club club:competition.getTeams()) {
        if (clubs.contains(club) && !finalList.contains(competition)) finalList.add(competition);
      }
    }
    // Loop through each competition and get all the games that belong to it
    for (Competition competition : finalList) {
      List<Game> games = gameService.findByCompetitionId(competition);
      competition.setGamesPlayed(games);
    }
    // Return the list of competitions with their games and clubs
    return finalList;
  }



}
