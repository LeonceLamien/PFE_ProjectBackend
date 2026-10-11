package com.example.kickerdavinci.Services;

import com.example.kickerdavinci.Models.Competition;
import com.example.kickerdavinci.Models.model.NoIdCompetition;
import com.example.kickerdavinci.Repository.CompetitionsRepository;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

@Service
public class CompetitionService {

  private final CompetitionsRepository competitionsRepository;

  public CompetitionService(CompetitionsRepository competitionsRepository) {
    this.competitionsRepository = competitionsRepository;
  }

  public boolean createOne(NoIdCompetition competition) {
    if (competitionsRepository.existsByDate(competition.getDate())) {
      return false;
    }
    competitionsRepository.save(competition.toCompetition());
    return true;
  }

  public Iterable<Competition> getAll() {
    return competitionsRepository.findAll();
  }

  public Competition getCompetitionByDate(LocalDate date) {
    return competitionsRepository.findByDate(date);
  }

  public Competition findById(long id){
    return competitionsRepository.findById(id);
  }

  public boolean deleteByDate(LocalDate date) {
    if (!competitionsRepository.existsByDate(date)) {
      return false;
    }
    competitionsRepository.deleteByDate(date);
    return true;
  }

  public boolean existsDate(LocalDate date) {
    return competitionsRepository.existsByDate(date);
  }

  public boolean update(Competition competition, LocalDate date) {
    if (competition == null || competition.getDate() == null) {
      return false;
    }
    if (!date.equals(competition.getDate()) && competitionsRepository.existsByDate(competition.getDate())) {
      return false;
    }
    Competition competitionDb = competitionsRepository.findByDate(date);
    if (competitionDb == null) {
      return false;
    }
    competition.setId(competitionDb.getId());
    competitionsRepository.save(competition);
    System.out.println(competitionsRepository.findAll());
    return true;
  }

}
