package com.dmadev.qadevs.dto;

import com.dmadev.qadevs.entity.DeveloperEntity;
import com.dmadev.qadevs.entity.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class DeveloperDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String specialty;
    private Status status;

    public  DeveloperEntity toEntity(){
     return DeveloperEntity.builder()
             .id(id)
             .firstName(firstName)
             .lastName(lastName)
             .specialty(specialty)
             .email(email)
             .status(status)
             .build();
    }
    public static DeveloperDto fromEntity(DeveloperEntity developer){
        return DeveloperDto.builder()
                .id(developer.getId())
                .firstName(developer.getFirstName())
                .lastName(developer.getLastName())
                .specialty(developer.getSpecialty())
                .email(developer.getEmail())
                .status(developer.getStatus())
                .build();
    }
}
