package com.example.kickerdavinci.Controllers;

import com.example.kickerdavinci.Models.Ranking;
import com.example.kickerdavinci.Models.model.NoIdRanking;
import com.example.kickerdavinci.Services.RankingService;
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
public class RankingController {

  private final RankingService rankingService;

  public RankingController(RankingService rankingService) {
    this.rankingService = rankingService;
  }

  @PostMapping("/ranking")
  public ResponseEntity<Void> createOne(@RequestBody NoIdRanking ranking) {
    if (ranking == null || ranking.getName() == null || ranking.getName().isBlank()
        || ranking.getMaxPoints() <= 0 || ranking.getMinPoints() <= 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    if (!rankingService.createOne(ranking)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT);
    }
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping("/ranking/{name}")
  public ResponseEntity<Ranking> getOne(@PathVariable String name) {
    if (name == null || name.isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    Ranking ranking = rankingService.getOne(name);
    if (ranking == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(ranking, HttpStatus.OK);
  }

  @GetMapping("/rankings")
  public ResponseEntity<Iterable<Ranking>> getAllRankings() {
    return new ResponseEntity<>(rankingService.getAll(), HttpStatus.OK);
  }

  @DeleteMapping("/ranking/{name}")
  public ResponseEntity<Void> deleteByName(@PathVariable String name) {
    if (name == null || name.isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    if (!rankingService.deleteByName(name)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping("/ranking/{name}")
  public ResponseEntity<Void> updateRanking(@RequestBody NoIdRanking ranking,
      @PathVariable String name) {
    if (ranking == null || ranking.getName() == null || ranking.getName().isBlank()
        || ranking.getMaxPoints() <= 0 || ranking.getMinPoints() <= 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    if (!rankingService.existsName(name)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    if (!rankingService.update(ranking.toRanking(), name)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT);
    }
    return new ResponseEntity<>(HttpStatus.OK);

  }
}
