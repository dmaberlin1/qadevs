package com.dmadev.qadevs.service;

import com.dmadev.qadevs.entity.DeveloperEntity;
import com.dmadev.qadevs.entity.Status;
import com.dmadev.qadevs.exception.DeveloperNotFoundException;
import com.dmadev.qadevs.exception.DeveloperWithDuplicateEmailException;
import com.dmadev.qadevs.repository.DeveloperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeveloperServiceImpl implements DeveloperService {

    private final DeveloperRepository developerRepository;

    @Override
    public DeveloperEntity saveDeveloper(DeveloperEntity developer) {
        Optional<DeveloperEntity> duplicateCandidate = developerRepository.findByEmail(developer.getEmail());
        if (duplicateCandidate.isPresent()) {
            throw new DeveloperWithDuplicateEmailException("Developer with defined email is already exists");
        }
        developer.setStatus(Status.ACTIVE);
        return developerRepository.save(developer);
    }

    @Override
    public DeveloperEntity updateDeveloper(DeveloperEntity developer) {
        boolean existsById = developerRepository.existsById(developer.getId());
        if (!existsById) {
            throw new DeveloperNotFoundException("Developer with id: %d is not exist".formatted(developer.getId()));
        }
        return developerRepository.save(developer);

    }

    @Override
    public DeveloperEntity getDeveloperById(Integer id) {

        return developerRepository.findById(id).orElseThrow(() ->
                new DeveloperNotFoundException("Developer with id: %d is not exist".formatted(id))
        );
    }

    @Override
    public Optional<DeveloperEntity> getDeveloperByEmail(String email) {
        Optional<DeveloperEntity> byEmail = developerRepository.findByEmail(email);
        if (byEmail.isEmpty()) {
            throw new DeveloperNotFoundException("Developer with  email %s is not exist".formatted(email));
        }

        return byEmail;
    }

    @Override
    public List<DeveloperEntity> getAllDevelopers() {
        return developerRepository.findAll()
                .stream()
                .filter(developer->developer.getStatus().equals(Status.ACTIVE))
                .collect(Collectors.toList());
    }

    @Override
    public List<DeveloperEntity> getAllOnBenchDevelopers() {
        return developerRepository.findAll()
                .stream()
                .filter(developer -> developer.getStatus()==Status.DELETED)
                .toList();
    }


    @Override
    public List<DeveloperEntity> getAllActiveBySpecialty(String specialty) {
        return developerRepository.findAllActiveBySpecialty(specialty);
    }

    @Override
    public void softDeleteById(Integer id) {
        DeveloperEntity obtainedDeveloper = developerRepository.findById(id).orElseThrow(
                () -> new DeveloperNotFoundException("Developer with id: %d not found".formatted(id))
        );
        obtainedDeveloper.setStatus(Status.DELETED);
        developerRepository.save(obtainedDeveloper);
    }

    @Override
    public void hardDeleteById(Integer id) {
        DeveloperEntity obtainedDeveloper = developerRepository.findById(id).orElseThrow(
                () -> new DeveloperNotFoundException("Developer with id: %d not found".formatted(id))
        );
        developerRepository.deleteById(obtainedDeveloper.getId());
    }
}
