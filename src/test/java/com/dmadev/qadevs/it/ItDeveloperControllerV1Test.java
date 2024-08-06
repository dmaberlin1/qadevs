package com.dmadev.qadevs.it;

import com.dmadev.qadevs.dto.DeveloperDto;
import com.dmadev.qadevs.entity.DeveloperEntity;
import com.dmadev.qadevs.entity.Status;
import com.dmadev.qadevs.repository.DeveloperRepository;
import com.dmadev.qadevs.util.DataUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ItDeveloperControllerV1Test extends AbstractRestControllerBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DeveloperRepository developerRepository;

    @BeforeEach
    public void setUp() {
        developerRepository.deleteAll();
    }

    final private static int badRequestStatus = 400;
    final private static int notFoundRequestStatus = 404;
    final private static String pathApiV1Developers = "/api/v1/developers";


    @Test
    @DisplayName("Test create developer functionality")
    public void givenDeveloperDto_whenCreateDeveloper_thenSuccessResponse() throws Exception {
        //given
        DeveloperDto johnDoeDtoPersisted = DataUtils.getJohnDoeDtoPersisted();

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
        DeveloperEntity developerTransient = DataUtils.getJohnDoeTransient();
        DeveloperDto developerDto = DataUtils.getJohnDoeDtoPersisted();
        String duplicateMail = "duplicate@gmail.com";
        String message = "Developer with defined email is already exists";

        developerTransient.setEmail(duplicateMail);
        developerRepository.save(developerTransient);
        developerDto.setEmail(duplicateMail);
        //when
        ResultActions resultActions = mockMvc.perform(post(pathApiV1Developers)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(developerDto))
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
        String updatedMail = "updated@gmail.com";
        DeveloperEntity johnDoeEntity = DataUtils.getJohnDoeTransient();
        developerRepository.save(johnDoeEntity);
        DeveloperDto johnDoeDto = DataUtils.getJohnDoeDtoPersisted();
        johnDoeDto.setId(johnDoeEntity.getId());
        johnDoeDto.setEmail(updatedMail);

        //when
        ResultActions resultActions = mockMvc.perform(put(pathApiV1Developers)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(johnDoeDto))
        );

        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id",
                        CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                        CoreMatchers.is(johnDoeDto.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                        CoreMatchers.is(johnDoeDto.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                        CoreMatchers.is(johnDoeDto.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status",
                        CoreMatchers.is(johnDoeDto.getStatus().toString())));
    }


    @Test
    @DisplayName("Test update developer with incorrect id functionality")
    public void givenDeveloperDtoWithIncorrectId_whenUpdateDeveloper_thenErrorResponse() throws Exception {
        //given
        DeveloperDto johnDoeDto = DataUtils.getJohnDoeDtoPersisted();
        var message = "Developer with id: %d is not exist".formatted(johnDoeDto.getId());

        //when
        ResultActions resultActions = mockMvc.perform(put(pathApiV1Developers)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(johnDoeDto))
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
        DeveloperEntity developer = DataUtils.getJohnDoeTransient();
        developerRepository.save(developer);
        //when
        ResultActions resultActions = mockMvc.perform(get(pathApiV1Developers + "/" + developer.getId())
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(developer.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(developer.getFirstName())));

    }

    @Test
    @DisplayName("Test get developer by Id functionality")
    public void givenIncorrectId_whenGetById_thenErrorResponse() throws Exception {
        //given
        DeveloperEntity developer = DataUtils.getJohnDoePersisted();
        String errorMessage = "Developer with id: %d is not exist".formatted(developer.getId());

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
        DeveloperEntity developer = DataUtils.getJohnDoeTransient();
        developerRepository.save(developer);
        //when
        ResultActions resultActions = mockMvc.perform(delete(pathApiV1Developers + "/" + developer.getId())
                .contentType(MediaType.APPLICATION_JSON));
        //then
        DeveloperEntity obtainedDeveloper = developerRepository.findById(developer.getId()).orElse(null);
        assertThat(obtainedDeveloper).isNotNull();
        assertThat(obtainedDeveloper.getStatus()).isEqualTo(Status.DELETED);
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Test soft delete developer by incorrect id functionality")
    public void givenIncorrectId_whenSoftDelete_ThenErrorResponse() throws Exception {
        //given
        String errorMessage = "Developer with id: 555 not found";

        //when
        ResultActions resultActions = mockMvc.perform(delete(pathApiV1Developers + "/555")
                .contentType(MediaType.APPLICATION_JSON));
        //then

        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(badRequestStatus)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(errorMessage)));
    }


    @Test
    @DisplayName("Test hard delete developer by id functionality")
    public void givenId_whenHardDelete_ThenSuccessResponse() throws Exception {
        //given
        DeveloperEntity developerEntity = DataUtils.getJohnDoeTransient();
        Integer id = developerEntity.getId();
        developerRepository.save(developerEntity);
        //when
        ResultActions resultActions = mockMvc.
                perform(delete(pathApiV1Developers + "/" + id + "?isHard=true")
                        .contentType(MediaType.APPLICATION_JSON));
        //then
        DeveloperEntity obtainedDeveloper = developerRepository.findById(id).orElse(null);
        assertThat(obtainedDeveloper).isNull();
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    @DisplayName("Test hard delete developer by incorrect id functionality")
    public void givenIncorrectId_whenHardDelete_ThenErrorResponse() throws Exception {
        //given
        int id = 1;
        String errorMessage = "Developer with id: %d not found".formatted(id);

        //when

        ResultActions resultActions = mockMvc.perform(delete(pathApiV1Developers + "/" + id + "?isHard=true")
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(badRequestStatus)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(errorMessage)));
    }
    //eof
}
