package com.dmadev.qadevs.service;

import com.dmadev.qadevs.entity.DeveloperEntity;
import com.dmadev.qadevs.entity.Status;
import com.dmadev.qadevs.exception.DeveloperNotFoundException;
import com.dmadev.qadevs.exception.DeveloperWithDuplicateEmailException;
import com.dmadev.qadevs.repository.DeveloperRepository;
import com.dmadev.qadevs.util.DataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeveloperServiceImplTest {
    @Mock
    private DeveloperRepository developerRepository;
    @InjectMocks
    private DeveloperServiceImpl serviceUnderTest;

    @Test
    @DisplayName("Test save developer functionality")
    public void givenDeveloperToSave_whenSaveDeveloper_thenRepositoryIsCalled() {
        //given
        DeveloperEntity johnDoeTransient = DataUtils.getJohnDoeTransient();
        DeveloperEntity johnDoePersisted = DataUtils.getJohnDoePersisted();
        BDDMockito.given(developerRepository.findByEmail(anyString())).willReturn(null);
        BDDMockito.given(developerRepository.save(any(DeveloperEntity.class)))
                .willReturn(DataUtils.getJohnDoePersisted());
        //when
        DeveloperEntity savedDeveloper = serviceUnderTest.saveDeveloper(johnDoeTransient);

        //then
        assertThat(savedDeveloper).isNotNull();
        assertThat(savedDeveloper).isEqualTo(johnDoePersisted);
    }

    @Test
    @DisplayName("Test save developer with duplicate email functionality")
    public void givenDeveloperToSaveWithDuplicateEmail_whenSaveDeveloper_thenExceptionIsThrown() {
        //given
        DeveloperEntity johnDoeTransient = DataUtils.getJohnDoeTransient();
        DeveloperEntity johnDoeTransientDuplicate = DataUtils.getJohnDoeTransient();
        BDDMockito.given(developerRepository.findByEmail(johnDoeTransient.getEmail()))
                .willReturn(DataUtils.getJohnDoePersisted());
        //when
        assertThrows(
                DeveloperWithDuplicateEmailException.class,
                () -> serviceUnderTest.saveDeveloper(johnDoeTransientDuplicate)
        );
        //then
        verify(developerRepository, never()).save(any(DeveloperEntity.class));

    }

    @Test
    @DisplayName("Test update developer functionality")
    public void givenDeveloperToUpdate_whenUpdateDeveloper_thenRepositoryIsCalled() {
        //given
        DeveloperEntity johnDoePersisted = DataUtils.getJohnDoePersisted();
        BDDMockito.given(developerRepository.existsById(anyInt()))
                .willReturn(true);
        BDDMockito.given(developerRepository.save(any(DeveloperEntity.class)))
                .willReturn(johnDoePersisted);
        //when
        DeveloperEntity updatedDeveloper = serviceUnderTest.updateDeveloper(johnDoePersisted);
        //then
        assertThat(updatedDeveloper).isNotNull();
        verify(developerRepository, times(1)).save(any(DeveloperEntity.class));
    }

    @Test
    @DisplayName("test update developer with incorrect id functionality")
    public void givenDeveloperToUpdateWithIncorrectId_whenUpdateDeveloper_thenExceptionIsThrown() {

        //given
        DeveloperEntity johnDoeTransientWithIncorrectId = DataUtils.getJohnDoePersisted();
        BDDMockito.given(developerRepository.existsById(anyInt())).willReturn(false);

        //when
        assertThrows(
                DeveloperNotFoundException.class, () -> serviceUnderTest.updateDeveloper(johnDoeTransientWithIncorrectId)
        );

        //then
        verify(developerRepository, never()).save(any(DeveloperEntity.class));
        verify(developerRepository, times(0)).save(any(DeveloperEntity.class));

    }

    ;


    @Test
    @DisplayName("test get developer by id functionality")
    public void givenId_whenGetById_thenDeveloperIsReturn() {
        //given
        DeveloperEntity johnDoePersisted = DataUtils.getJohnDoePersisted();
        BDDMockito.given(developerRepository.findById(anyInt())).willReturn(Optional.of(DataUtils.getJohnDoePersisted()));
        //when
        DeveloperEntity obtainedDeveloper = serviceUnderTest.getDeveloperById(johnDoePersisted.getId());

        //then
        assertThat(obtainedDeveloper).isNotNull();
        assertThat(obtainedDeveloper).isEqualTo(DataUtils.getJohnDoePersisted());
        verify(developerRepository, times(1)).findById(anyInt());
    }

    @Test
    @DisplayName("test get developer by id functionality")
    public void givenIncorrectId_whenGetById_thenExceptionIsThrown() {
        //given
        DeveloperEntity frankJonesPersisted = DataUtils.getFrankJonesPersisted();

        BDDMockito.given(developerRepository.findById(anyInt())).willThrow(DeveloperNotFoundException.class);

        //when
        assertThrows(DeveloperNotFoundException.class, () -> serviceUnderTest.getDeveloperById(frankJonesPersisted.getId()));

        //then
        verify(developerRepository, times(1)).findById(anyInt());
    }

    @Test
    @DisplayName("test get developer by email functionality")
    public void givenEmail_whenGetDeveloperByEmail_thenDeveloperIsReturn() {
        //given
        DeveloperEntity frankJonesPersisted = DataUtils.getFrankJonesPersisted();
        String email = frankJonesPersisted.getEmail();
        BDDMockito.given(developerRepository.findByEmail(anyString())).willReturn(frankJonesPersisted);

        //when
        DeveloperEntity obtainedDeveloper = serviceUnderTest.getDeveloperByEmail(email);

        //then
        assertThat(obtainedDeveloper).isNotNull();
        assertThat(obtainedDeveloper).isEqualTo(frankJonesPersisted);

    }

    @Test
    @DisplayName("test get developer by email functionality")
    public void givenIncorrectEmail_whenGetDeveloperByEmail_thenExceptionIsThrown() {
        //given
        DeveloperEntity frankJonesPersisted = DataUtils.getFrankJonesPersisted();
        String email = frankJonesPersisted.getEmail();
        BDDMockito.given(developerRepository.findByEmail(anyString())).willReturn(null);
//        BDDMockito.given(developerRepository.findByEmail(anyString())).willReturn(Optional.empty());


        //when
        assertThrows(
                DeveloperNotFoundException.class, () -> serviceUnderTest.getDeveloperByEmail(email)
        );

        //then
        verify(developerRepository, times(1)).findByEmail(anyString());


    }

    @Test
    @DisplayName("test get all developers when active functionality")
    public void givenThreeDevelopers_whenGetAll_thenOnlyActiveAreReturned() {
        //given
        DeveloperEntity frankJonesPersisted = DataUtils.getFrankJonesPersisted();
        DeveloperEntity johnDoePersisted = DataUtils.getJohnDoePersisted();
        DeveloperEntity mikeSmithPersisted = DataUtils.getMikeSmithPersisted();

        List<DeveloperEntity> developers =
                List.of(frankJonesPersisted, johnDoePersisted, mikeSmithPersisted);
        List<DeveloperEntity> developersFilteredActive =
                List.of(frankJonesPersisted, johnDoePersisted, mikeSmithPersisted)
                        .stream()
                        .filter(developer -> developer.getStatus().equals(Status.ACTIVE))
                        .collect(Collectors.toList());

        BDDMockito.given(developerRepository.findAll()).willReturn(developers);
        //when
        List<DeveloperEntity> allDevelopers = serviceUnderTest.getAllDevelopers();

        //then
        assertThat(CollectionUtils.isEmpty(allDevelopers)).isFalse();
        assertThat(allDevelopers.size()).isEqualTo(developersFilteredActive.size());
        assertThat(allDevelopers
                .stream()
                .map(DeveloperEntity::getStatus)
                .toList()
                .size())
                .isEqualTo(developersFilteredActive.size());
        verify(developerRepository, times(1)).findAll();

    }

    @Test
    @DisplayName("test get all active developers filter by speciality functionality")
    public void givenThreeDevelopers_whenGetAllBySpecialty_thenDevelopersAreReturned(){
        //given
        var specialtyJava = "java";
        var frankJonesPersisted = DataUtils.getFrankJonesPersisted();
        var johnDoePersisted = DataUtils.getJohnDoePersisted();
        var mikeSmithPersisted = DataUtils.getMikeSmithPersisted();
        List<DeveloperEntity> developerSortedByActiveAndSpecialty =
                List.of(frankJonesPersisted, johnDoePersisted, mikeSmithPersisted)
                .stream()
                .filter(developer -> developer.getStatus().equals(Status.ACTIVE))
                .filter(developer -> developer.getSpecialty().equals(specialtyJava))
                .collect(Collectors.toList());

        BDDMockito.given(developerRepository.findAllActiveBySpecialty(anyString())).willReturn(developerSortedByActiveAndSpecialty);

        //when
        List<DeveloperEntity> obtainedAllActiveBySpecialty = serviceUnderTest.getAllActiveBySpecialty(specialtyJava);

        //then
        assertThat(CollectionUtils.isEmpty(obtainedAllActiveBySpecialty)).isFalse();
        assertThat(obtainedAllActiveBySpecialty.size()).isEqualTo(developerSortedByActiveAndSpecialty.size());

    }

    @Test
    @DisplayName("test soft delete by id functionality")
    public void givenId_whenSoftDeleteById_thenRepositorySaveMethodIsCalled(){
        //given
        var johnDoePersisted = DataUtils.getJohnDoePersisted();
        BDDMockito.given(developerRepository.findById(anyInt())).willReturn(Optional.ofNullable(johnDoePersisted));

        //when
        serviceUnderTest.softDeleteById(johnDoePersisted.getId());
        //then
        verify(developerRepository,times(1)).save(any(DeveloperEntity.class));
        verify(developerRepository,times(1)).findById(anyInt());
        verify(developerRepository,times(0)).deleteById(anyInt());
        verify(developerRepository,never()).delete(any(DeveloperEntity.class));

    }

    @Test
    @DisplayName("test soft delete by incorrect id functionality")
    public void givenIncorrectId_whenSoftDeleteById_thenRepositorySaveMethodIsCalled(){
        //given
        var johnDoePersisted = DataUtils.getJohnDoePersisted();
//        BDDMockito.given(developerRepository.findById(anyInt())).willThrow(DeveloperNotFoundException.class);
        BDDMockito.given(developerRepository.findById(anyInt())).willReturn(Optional.empty());

        //when
        assertThrows(
                DeveloperNotFoundException.class,()->serviceUnderTest.softDeleteById(johnDoePersisted.getId())
        );

        //then
        verify(developerRepository,times(0)).save(any(DeveloperEntity.class));
        verify(developerRepository,times(1)).findById(anyInt());
        verify(developerRepository,times(0)).deleteById(anyInt());
        verify(developerRepository,never()).delete(any(DeveloperEntity.class));

    }

    @Test
    @DisplayName("test hard delete by id functionality")
    public void givenId_whenHardDeleteById_thenRepositoryDeleteMethodIsCalled(){
        //given
        var johnDoePersisted = DataUtils.getJohnDoePersisted();
        Integer johnDoePersistedId = johnDoePersisted.getId();

        BDDMockito.given(developerRepository.findById(anyInt())).willReturn(Optional.of(johnDoePersisted));

        //when
        serviceUnderTest.hardDeleteById(johnDoePersistedId);
        //then
        verify(developerRepository,times(1)).deleteById(anyInt());
        verify(developerRepository,times(1)).findById(anyInt());
        verify(developerRepository,never()).delete(any(DeveloperEntity.class));
    }

    @Test
    @DisplayName("test hard delete by incorrect id functionality")
    public void givenIncorrectId_whenHardDeleteById_thenRepositoryDeleteMethodIsCalled(){
        //given
        var johnDoePersisted = DataUtils.getJohnDoePersisted();
        BDDMockito.given(developerRepository.findById(anyInt())).willReturn(Optional.empty());

        //when
        assertThrows(
                DeveloperNotFoundException.class,()->serviceUnderTest.hardDeleteById(johnDoePersisted.getId())
        );
        //then
        verify(developerRepository,times(0)).deleteById(anyInt());
        verify(developerRepository,never()).delete(any(DeveloperEntity.class));
        verify(developerRepository,times(1)).findById(anyInt());
    }

    //eof
}