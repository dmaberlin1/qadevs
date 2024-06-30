package com.dmadev.qadevs.controller;

import com.dmadev.qadevs.dto.DeveloperDto;
import com.dmadev.qadevs.dto.ErrorDto;
import com.dmadev.qadevs.entity.DeveloperEntity;
import com.dmadev.qadevs.exception.DeveloperNotFoundException;
import com.dmadev.qadevs.exception.DeveloperWithDuplicateEmailException;
import com.dmadev.qadevs.service.DeveloperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/developers")
public class DeveloperControllerV1 {

    private final DeveloperService developerService;

    @PostMapping
    public ResponseEntity<?> createDeveloper(@RequestBody DeveloperDto dto) {
        try {
            DeveloperEntity entity = dto.toEntity();
            DeveloperEntity createdDeveloper = developerService.saveDeveloper(entity);
            DeveloperDto result = DeveloperDto.fromEntity(createdDeveloper);
            return ResponseEntity.ok(result);
        } catch (DeveloperWithDuplicateEmailException exception) {
            return ResponseEntity.badRequest().body(ErrorDto.builder()
                    .status(400)
                    .message(exception.getMessage())
                    .build());
        }
    }

    @PutMapping
    public ResponseEntity<?> updateDeveloper(@RequestBody DeveloperDto dto) {
        try {
            DeveloperEntity entity = dto.toEntity();
            DeveloperEntity updatedDeveloper = developerService.updateDeveloper(entity);
            DeveloperDto result = DeveloperDto.fromEntity(updatedDeveloper);
            return ResponseEntity.ok(result);
        } catch (DeveloperNotFoundException exception) {
            return ResponseEntity.badRequest()
                    .body(ErrorDto.builder()
                            .status(400)
                            .message(exception.getMessage())
                            .build()
                    );
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDeveloperById(@PathVariable("id") Integer id) {
        try {
            DeveloperEntity developerById = developerService.getDeveloperById(id);
            DeveloperDto developerDto = DeveloperDto.fromEntity(developerById);
            return ResponseEntity.ok(developerDto);

        } catch (DeveloperNotFoundException exception) {
            int status = 404;
            return ResponseEntity.status(status)
                    .body(ErrorDto.builder()
                            .status(status)
                            .message(exception.getMessage())
                            .build());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllDevelopers() {
        List<DeveloperEntity> allDevelopers = developerService.getAllDevelopers();
        List<DeveloperDto> developerDtos = allDevelopers.stream()
                .map(DeveloperDto::fromEntity).toList();
        return ResponseEntity.ok(developerDtos);
    }

    @GetMapping("/specialty/{specialty}")
    public ResponseEntity<?> getAllDevelopersBySpecialty(@PathVariable("specialty") String specialty) {
        List<DeveloperEntity> allActiveBySpecialty = developerService.getAllActiveBySpecialty(specialty);
        List<DeveloperDto> developerDtos = allActiveBySpecialty.stream().map(DeveloperDto::fromEntity)
                .toList();
        return ResponseEntity.ok(developerDtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDeveloperById(@PathVariable("id") Integer id,
                                                 @RequestParam(value = "isHard", defaultValue = "false") boolean isHard) {

        try {
            if (isHard) {
                developerService.hardDeleteById(id);
            } else {
                developerService.softDeleteById(id);
            }
            return ResponseEntity.ok().build();
        } catch (DeveloperNotFoundException exception) {
            return ResponseEntity.badRequest()
                    .body(ErrorDto.builder()
                    .status(400)
                    .message(exception.getMessage())
                    .build());
        }
    }

    //eof
}
