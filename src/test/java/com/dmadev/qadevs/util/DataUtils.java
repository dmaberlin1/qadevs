package com.dmadev.qadevs.util;

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

}
