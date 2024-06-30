package com.dmadev.qadevs.controller;

import com.dmadev.qadevs.dto.DeveloperDto;
import com.dmadev.qadevs.entity.DeveloperEntity;
import com.dmadev.qadevs.exception.DeveloperNotFoundException;
import com.dmadev.qadevs.exception.DeveloperWithDuplicateEmailException;
import com.dmadev.qadevs.service.DeveloperService;
import com.dmadev.qadevs.service.DeveloperServiceImpl;
import com.dmadev.qadevs.util.DataUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest
class DeveloperControllerV1Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DeveloperService developerService;


    final private static int badRequestStatus = 400;
    final private static int notFoundRequestStatus = 404;
    final private String pathApiV1Developers = "/api/v1/developers";


    @Test
    @DisplayName("Test create developer functionality")
    public void givenDeveloperDto_whenCreateDeveloper_thenSuccessResponse() throws Exception {
        //given
        DeveloperDto johnDoeDtoPersisted = DataUtils.getJohnDoeDtoPersisted();
        DeveloperEntity johnDoePersisted = DataUtils.getJohnDoePersisted();
        BDDMockito.given(developerService.saveDeveloper(any(DeveloperEntity.class))).willReturn(johnDoePersisted);

        //when
        ResultActions resultActions = mockMvc.perform(post(pathApiV1Developers)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(johnDoeDtoPersisted))
        );

        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(johnDoeDtoPersisted.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(johnDoeDtoPersisted.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(johnDoeDtoPersisted.getStatus().toString())));
    }

    @Test
    @DisplayName("Test create developer with duplicate email functionality")
    public void givenDeveloperDtoWithDuplicateEmail_whenCreateDeveloper_thenErrorResponse() throws Exception {
        //given
        DeveloperDto johnDoeDtoPersisted = DataUtils.getJohnDoeDtoPersisted();
        String message = "Developer with defined email is already exist";
        BDDMockito.given(developerService.saveDeveloper(any(DeveloperEntity.class)))
                .willThrow(new DeveloperWithDuplicateEmailException(message));

        //when
        ResultActions resultActions = mockMvc.perform(post(pathApiV1Developers)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(johnDoeDtoPersisted))
        );

        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(badRequestStatus)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(message)));

    }

    @Test
    @DisplayName("Test update developer functionality")
    public void givenDeveloperDto_whenUpdateDeveloper_thenSuccessResponse() throws Exception {
        //given
        DeveloperDto johnDoeDtoPersisted = DataUtils.getJohnDoeDtoPersisted();
        DeveloperEntity johnDoePersisted = DataUtils.getJohnDoePersisted();
        BDDMockito.given(developerService.updateDeveloper(any(DeveloperEntity.class))).willReturn(johnDoePersisted);

        //when
        ResultActions resultActions = mockMvc.perform(put(pathApiV1Developers)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(johnDoeDtoPersisted))
        );

        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id",
                        CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                        CoreMatchers.is(johnDoeDtoPersisted.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                        CoreMatchers.is(johnDoeDtoPersisted.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status",
                        CoreMatchers.is(johnDoeDtoPersisted.getStatus().toString())));
    }

    @Test
    @DisplayName("Test update developer with incorrect id functionality")
    public void givenDeveloperDtoWithIncorrectId_whenUpdateDeveloper_thenErrorResponse() throws Exception {
        //given
        DeveloperDto johnDoeDtoPersisted = DataUtils.getJohnDoeDtoPersisted();
        DeveloperEntity johnDoePersisted = DataUtils.getJohnDoePersisted();
        var message = "Developer with id: %d".formatted(johnDoeDtoPersisted.getId());
        BDDMockito.given(developerService.updateDeveloper(any(DeveloperEntity.class)))
                .willThrow(new DeveloperNotFoundException(message));

        //when
        ResultActions resultActions = mockMvc.perform(put(pathApiV1Developers)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(johnDoeDtoPersisted))
        );

        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(badRequestStatus)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is(message)));

    }

    @Test
    @DisplayName("Test get developer by Id functionality")
    public void givenId_whenGetById_thenSuccessResponse() throws Exception {
        //given
        DeveloperEntity developer = DataUtils.getJohnDoePersisted();
        BDDMockito.given(developerService.getDeveloperById(anyInt())).willReturn(developer);
        //when
        ResultActions resultActions = mockMvc.perform(get(pathApiV1Developers + "/" + developer.getId())
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(developer.getId())));

    }

    @Test
    @DisplayName("Test get developer by Id functionality")
    public void givenIncorrectId_whenGetById_thenErrorResponse() throws Exception {
        //given
        DeveloperEntity developer = DataUtils.getJohnDoePersisted();
        String errorMessage = "Developer with id: %d not found".formatted(developer.getId());
        BDDMockito.given(developerService.getDeveloperById(anyInt())).willThrow(new DeveloperNotFoundException(errorMessage));
        //when
        ResultActions resultActions = mockMvc.perform(get(pathApiV1Developers + "/" + developer.getId())
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(notFoundRequestStatus)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(errorMessage)));

    }

    @Test
    @DisplayName("Test soft delete developer by id functionality")
    public void givenId_whenSoftDelete_ThenSuccessResponse() throws Exception {
        //given
        BDDMockito.doNothing().when(developerService).softDeleteById(anyInt());
        //when
        ResultActions resultActions = mockMvc.perform(delete(pathApiV1Developers + "/1")
                .contentType(MediaType.APPLICATION_JSON));
        //then
        verify(developerService,times(1)).softDeleteById(anyInt());
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    @DisplayName("Test soft delete developer by incorrect id functionality")
    public void givenIncorrectId_whenSoftDelete_ThenErrorResponse() throws Exception {
        //given
        String errorMessage = "Developer not found";
        BDDMockito.doThrow(new DeveloperNotFoundException(errorMessage)).when(developerService).softDeleteById(anyInt());

        //when
        ResultActions resultActions = mockMvc.perform(delete(pathApiV1Developers + "/1")
                .contentType(MediaType.APPLICATION_JSON));
        //then

        verify(developerService,times(1)).softDeleteById(anyInt());
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status",CoreMatchers.is(badRequestStatus)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",CoreMatchers.is(errorMessage)));
    }


    @Test
    @DisplayName("Test hard delete developer by id functionality")
    public void givenId_whenHardDelete_ThenSuccessResponse() throws Exception {
        //given
        BDDMockito.doNothing().when(developerService).hardDeleteById(anyInt());
        //when
        ResultActions resultActions = mockMvc.perform(delete(pathApiV1Developers + "/1?isHard=true")
                .contentType(MediaType.APPLICATION_JSON));
        //then
        verify(developerService,times(1)).hardDeleteById(anyInt());
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Test hard delete developer by incorrect id functionality")
    public void givenIncorrectId_whenHardDelete_ThenErrorResponse() throws Exception {
        //given
        String errorMessage = "Developer not found";
        BDDMockito.doThrow(new DeveloperNotFoundException(errorMessage)).when(developerService).hardDeleteById(anyInt());

        //when
        ResultActions resultActions = mockMvc.perform(delete(pathApiV1Developers + "/1?isHard=true")
                .contentType(MediaType.APPLICATION_JSON));
        //then

        verify(developerService,times(1)).hardDeleteById(anyInt());
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status",CoreMatchers.is(badRequestStatus)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",CoreMatchers.is(errorMessage)));
    }

    //eof
}