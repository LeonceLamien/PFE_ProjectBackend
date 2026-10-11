package com.example.kickerdavinci.Services;

import com.example.kickerdavinci.Models.Ranking;
import com.example.kickerdavinci.Models.model.NoIdRanking;
import com.example.kickerdavinci.Repository.RankingsRepository;
import org.springframework.stereotype.Service;

@Service
public class RankingService {

  private final RankingsRepository rankingsRepository;

  public RankingService(RankingsRepository rankingsRepository) {
    this.rankingsRepository = rankingsRepository;
  }

  public boolean createOne(NoIdRanking ranking) {
    if (rankingsRepository.existsByName(ranking.getName())) {
      return false;
    }
    rankingsRepository.save(ranking.toRanking());
    return true;
  }

  public Ranking getOne(String name) {
    if (!rankingsRepository.existsByName(name)) {
      return null;
    }
    return rankingsRepository.findByName(name);
  }

  public Iterable<Ranking> getAll() {
    return rankingsRepository.findAll();
  }

  public boolean deleteByName(String name) {
    if (!rankingsRepository.existsByName(name)) {
      return false;
    }
    rankingsRepository.deleteByName(name);
    return true;
  }

  public boolean existsName(String name) {
    return rankingsRepository.existsByName(name);
  }

  public boolean update(Ranking ranking, String name) {
    if (ranking == null || ranking.getName() == null) {
      return false;
    }
    if (!name.equals(ranking.getName()) && rankingsRepository.existsByName(ranking.getName())) {
      return false;
    }
    Ranking divisionDb = rankingsRepository.findByName(name);
    if (divisionDb == null) {
      return false;
    }
    ranking.setId(divisionDb.getId());
    rankingsRepository.save(ranking);
    System.out.println(rankingsRepository.findAll());
    return true;
  }
}
