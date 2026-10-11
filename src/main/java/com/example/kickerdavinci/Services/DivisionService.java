package com.example.kickerdavinci.Services;

import com.example.kickerdavinci.Models.Division;
import com.example.kickerdavinci.Models.model.NoIdDivision;
import com.example.kickerdavinci.Repository.DivisionsRepository;
import org.springframework.stereotype.Service;

@Service
public class DivisionService {

  private final DivisionsRepository divisionsRepository;

  public DivisionService(DivisionsRepository divisionsRepository) {
    this.divisionsRepository = divisionsRepository;
  }

  public boolean createOne(NoIdDivision division) {
    if (divisionsRepository.existsByName(division.getName())) {
      return false;
    }
    divisionsRepository.save(division.toDivision());
    return true;
  }

  public Division getOne(String name) {
    if (!divisionsRepository.existsByName(name)) {
      return null;
    }
    return divisionsRepository.findByName(name);
  }

  public Iterable<Division> getAll() {
    return divisionsRepository.findAll();
  }

  public boolean deleteByName(String name) {
    if (!divisionsRepository.existsByName(name)) {
      return false;
    }
    divisionsRepository.deleteByName(name);
    return true;
  }

  public boolean existsName(String name) {
    return divisionsRepository.existsByName(name);
  }

  public boolean update(Division division, String name) {
    if (division == null || division.getName() == null) {
      return false;
    }
    if (!name.equals(division.getName()) && divisionsRepository.existsByName(division.getName())) {
      return false;
    }
    Division divisionDb = divisionsRepository.findByName(name);
    if (divisionDb == null) {
      return false;
    }
    division.setId(divisionDb.getId());
    divisionsRepository.save(division);
    System.out.println(divisionsRepository.findAll());
    return true;
  }


  public Division findByName(String divisionName) {
    return divisionsRepository.findByName(divisionName);
  }
}
