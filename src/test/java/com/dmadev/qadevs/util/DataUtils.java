package com.dmadev.qadevs.util;

import com.dmadev.qadevs.dto.DeveloperDto;
import com.dmadev.qadevs.entity.DeveloperEntity;
import com.dmadev.qadevs.entity.Status;

public abstract class DataUtils {

    public static DeveloperEntity getJohnDoeTransient() {
        return DeveloperEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@gmail.com")
                .specialty("java")
                .status(Status.ACTIVE)
                .build();
    }
    public static DeveloperEntity getMikeSmithTransient() {
        return DeveloperEntity.builder()
                .firstName("Mike")
                .lastName("Smith")
                .email("mike.smith@gmail.com")
                .specialty("java")
                .status(Status.ACTIVE)
                .build();
    }
    public static DeveloperEntity getFrankJonesTransient() {
        return DeveloperEntity.builder()
                .firstName("Frank")
                .lastName("Jones")
                .email("frank.jones@gmail.com")
                .specialty("c#")
                .status(Status.DELETED)
                .build();
    }

    public static DeveloperEntity getJohnDoePersisted() {
        return DeveloperEntity.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@gmail.com")
                .specialty("java")
                .status(Status.ACTIVE)
                .build();
    }
    public static DeveloperEntity getMikeSmithPersisted() {
        return DeveloperEntity.builder()
                .id(2)
                .firstName("Mike")
                .lastName("Smith")
                .email("mike.smith@gmail.com")
                .specialty("java")
                .status(Status.ACTIVE)
                .build();
    }
    public static DeveloperEntity getFrankJonesPersisted() {
        return DeveloperEntity.builder()
                .id(3)
                .firstName("Frank")
                .lastName("Jones")
                .email("frank.jones@gmail.com")
                .specialty("c#")
                .status(Status.DELETED)
                .build();
    }

    public static DeveloperDto getJohnDoeDtoTransient() {
        return DeveloperDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@mail.com")
                .specialty("Java")
                .status(Status.ACTIVE)
                .build();
    }

    public static DeveloperDto getMikeSmithDtoTransient() {
        return DeveloperDto.builder()
                .firstName("Mike")
                .lastName("Smith")
                .email("mike.smith@mail.com")
                .specialty("Java")
                .status(Status.ACTIVE)
                .build();
    }

    public static DeveloperDto getFrankJonesDtoTransient() {
        return DeveloperDto.builder()
                .firstName("Frank")
                .lastName("Jones")
                .email("frank.jones@mail.com")
                .specialty("Java")
                .status(Status.DELETED)
                .build();
    }

    public static DeveloperDto getJohnDoeDtoPersisted() {
        return DeveloperDto.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@mail.com")
                .specialty("Java")
                .status(Status.ACTIVE)
                .build();
    }

    public static DeveloperDto getMikeSmithDtoPersisted() {
        return DeveloperDto.builder()
                .id(2)
                .firstName("Mike")
                .lastName("Smith")
                .email("mike.smith@mail.com")
                .specialty("Java")
                .status(Status.ACTIVE)
                .build();
    }

    public static DeveloperDto getFrankJonesDtoPersisted() {
        return DeveloperDto.builder()
                .id(3)
                .firstName("Frank")
                .lastName("Jones")
                .email("frank.jones@mail.com")
                .specialty("Java")
                .status(Status.DELETED)
                .build();
    }

}
