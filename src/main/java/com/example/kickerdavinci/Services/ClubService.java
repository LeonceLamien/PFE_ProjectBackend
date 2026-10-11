package com.example.kickerdavinci.Services;

import com.example.kickerdavinci.Models.Club;
import com.example.kickerdavinci.Models.Division;
import com.example.kickerdavinci.Models.model.NoIdClub;
import com.example.kickerdavinci.Repository.ClubsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubService {

  private final ClubsRepository clubsRepository;

  public ClubService(ClubsRepository clubsRepository) {
    this.clubsRepository = clubsRepository;
  }

  public boolean createOne(NoIdClub club) {
    System.out.println(clubsRepository.findAll());
    if (clubsRepository.existsByName(club.getName())) {
      return false;
    }
    clubsRepository.save(club.toClub());
    return true;
  }

  public Club getClubById(long id){ return clubsRepository.findById(id);}

  public Iterable<Club> getAll() {
    return clubsRepository.findAll();
  }

  public Club getClubByName(String name) {
    return clubsRepository.findByName(name);
  }
  public boolean deleteByName(String name) {
    if (!clubsRepository.existsByName(name)) {
      return false;
    }
    clubsRepository.deleteByName(name);
    return true;
  }

  public boolean existsName(String name) {
    return clubsRepository.existsByName(name);
  }

  public boolean update(Club club, String name) {
    if (club == null || club.getName() == null) {
      return false;
    }
    if (!name.equals(club.getName()) && clubsRepository.existsByName(club.getName())) {
      return false;
    }
    Club clubDb = clubsRepository.findByName(name);
    if (clubDb == null) {
      return false;
    }
    club.setId(clubDb.getId());
    clubsRepository.save(club);
    System.out.println(clubsRepository.findAll());
    return true;
  }

  public List<Club> findByDivisionId(Division division) {
    return clubsRepository.findAllByDivision(division);
  }



}
