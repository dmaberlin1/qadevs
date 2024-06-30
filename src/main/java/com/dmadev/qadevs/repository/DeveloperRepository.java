package com.dmadev.qadevs.repository;

import com.dmadev.qadevs.entity.DeveloperEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DeveloperRepository  extends JpaRepository<DeveloperEntity,Integer> {

    Optional <DeveloperEntity> findByEmail(String email);

    @Query("SELECT d from DeveloperEntity d where d.status='ACTIVE' AND d.specialty=?1")
    List<DeveloperEntity> findAllActiveBySpecialty(String specialty);
}
