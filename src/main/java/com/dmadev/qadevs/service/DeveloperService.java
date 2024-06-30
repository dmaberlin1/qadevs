package com.dmadev.qadevs.service;

import com.dmadev.qadevs.entity.DeveloperEntity;

import java.util.List;
import java.util.Optional;

public interface DeveloperService {

    DeveloperEntity saveDeveloper(DeveloperEntity developer);

    DeveloperEntity updateDeveloper(DeveloperEntity developer);

    DeveloperEntity getDeveloperById(Integer id);

    Optional<DeveloperEntity> getDeveloperByEmail(String email);

    List<DeveloperEntity> getAllDevelopers();
    List<DeveloperEntity> getAllOnBenchDevelopers();


    List<DeveloperEntity> getAllActiveBySpecialty(String specialty);

    void softDeleteById(Integer id);

    void hardDeleteById(Integer id);
}
