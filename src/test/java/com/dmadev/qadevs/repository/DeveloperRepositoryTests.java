package com.dmadev.qadevs.repository;

import com.dmadev.qadevs.entity.DeveloperEntity;
import com.dmadev.qadevs.entity.Status;
import com.dmadev.qadevs.util.DataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DeveloperRepositoryTests {

    @Autowired
    private DeveloperRepository developerRepository;

    @BeforeEach
    public void setUp() {
        developerRepository.deleteAll();
    }

    @Test
    @DisplayName("Test save developer functionality")
    public void givenDeveloperObject_whenSave_thenDeveloperIsCreated() {
        //given
        DeveloperEntity developerToSave = DataUtils.getJohnDoeTransient();
        //when
        DeveloperEntity savedDeveloper = developerRepository.save(developerToSave);
        //then
        assertThat(savedDeveloper).isNotNull();
        assertThat(savedDeveloper.getId()).isNotNull();

    }

    @Test
    @DisplayName("Test update developer functionality")
    public void givenDeveloperToUpdate_whenSave_thenEmailIsChanged() {
        //given
        String updatedEmail = "updated@gmail.com";
        DeveloperEntity developerToCreate = DataUtils.getJohnDoeTransient();
        developerRepository.save(developerToCreate);
        //when
        DeveloperEntity developerToUpdate = developerRepository.findById(developerToCreate.getId()).orElse(null);
//        assert developerToUpdate != null;
        developerToUpdate.setEmail(updatedEmail);
        DeveloperEntity updatedDeveloper = developerRepository.save(developerToUpdate);
        //then
        assertThat(updatedDeveloper).isNotNull();
        assertThat(updatedDeveloper.getEmail()).isEqualTo(updatedEmail);
    }

    @Test
    @DisplayName("Test get developer by id functionality")
    public void givenDeveloperCreated_whenGetById_thenDeveloperIsReturned() {
        //given
        String mail = "john.doe@gmail.com";
        DeveloperEntity developerToSave = DataUtils.getJohnDoeTransient();
        developerRepository.save(developerToSave);
        //when
        DeveloperEntity obtainedDeveloper = developerRepository.findById(developerToSave.getId()).orElse(null);

        //then
        assertThat(obtainedDeveloper).isNotNull();
        assertThat(obtainedDeveloper.getEmail()).isEqualTo(mail);

    }


    @Test
    @DisplayName("Test developer not found functionality")
    public void givenDeveloperIsNotCreated_whenGetById_thenOptionalIsEmpty() {
        //given

        //when
        DeveloperEntity obtainedDeveloper = developerRepository.findById(1).orElse(null);
        //then
        assertThat(obtainedDeveloper).isNull();
    }

    @Test
    @DisplayName("Test get all developers functional")
    public void givenThreeDevelopersAreStored_whenFindAll_thenAllDeveloperAreReturned() {
        //given
        DeveloperEntity developerJohn = DataUtils.getJohnDoeTransient();
        DeveloperEntity developerMike = DataUtils.getMikeSmithTransient();
        DeveloperEntity developerFrank = DataUtils.getFrankJonesTransient();
        developerRepository.saveAll(List.of(developerFrank, developerMike, developerJohn));
        //when
        List<DeveloperEntity> obtainedDevelopers = developerRepository.findAll();
        //then
        assertThat(CollectionUtils.isEmpty(obtainedDevelopers)).isFalse();
    }

    @Test
    @DisplayName("Test get developer by email functionality")
    public void givenDeveloperSaved_whenGetByEmail_thenDeveloperIsReturned() {
        //given
        DeveloperEntity frankJonesTransient = DataUtils.getFrankJonesTransient();
        developerRepository.save(frankJonesTransient);
        //when
        Optional<DeveloperEntity> obtainedDeveloperByEmail = developerRepository.findByEmail(frankJonesTransient.getEmail());
        //then
        assertThat(obtainedDeveloperByEmail).isNotEmpty();
        assertThat(obtainedDeveloperByEmail.get().getEmail()).isEqualTo(frankJonesTransient.getEmail());
    }

    @Test
    @DisplayName("Test get  all active developers by speciality functionality")
    public void givenThreeDevelopersAndTwoAreActive_whenFindAllActiveBySpecialty_thenReturnOnlyTwoDevelopers() {
        //given
        DeveloperEntity developerJohn = DataUtils.getJohnDoeTransient();
        DeveloperEntity developerMike = DataUtils.getMikeSmithTransient();
        DeveloperEntity developerFrank = DataUtils.getFrankJonesTransient();
        developerRepository.saveAll(List.of(developerFrank, developerMike, developerJohn));

        //when
        List<DeveloperEntity> obtainedDevelopers = developerRepository.findAllActiveBySpecialty("java");

        //then
        assertThat(CollectionUtils.isEmpty(obtainedDevelopers)).isFalse(); //not empty
        assertThat(obtainedDevelopers.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Test delete developer by id functionality")
    public void givenDeveloperIsSaved_whenDeleteById_ThenDeveloperIsRemoveFromDB() {
        //given
        DeveloperEntity johnDoeTransient = DataUtils.getJohnDoeTransient();

        developerRepository.save(johnDoeTransient);
        //when
        developerRepository.deleteById(johnDoeTransient.getId());

        //then
        DeveloperEntity obtainedDeveloper = developerRepository.findById(johnDoeTransient.getId()).orElse(null);
        List<DeveloperEntity> developerRepositoryAll = developerRepository.findAll();

        assertThat(obtainedDeveloper).isNull();
        assertThat(CollectionUtils.isEmpty(developerRepositoryAll)).isTrue();
        assertThat(CollectionUtils.isEmpty(developerRepository.findAll())).isTrue();


    }

    //eof
}
