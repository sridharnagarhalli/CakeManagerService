package com.wracle.cakemanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wracle.cakemanager.entity.Cake;
import com.wracle.cakemanager.repository.CakeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;

import static com.wracle.cakemanager.controller.CakeMgrController.BASE_URL_CAKES;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SqlGroup({
        @Sql(value = "classpath:init/reset.sql", executionPhase = BEFORE_TEST_METHOD),
        @Sql(value = "classpath:init/data.sql", executionPhase = BEFORE_TEST_METHOD)
})
@AutoConfigureMockMvc
@SpringBootTest
public class CakeMgrControllerIntegrationTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private CakeRepository repository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @WithMockUser("/user-1")
    @Test
    public void shouldCreateCake() throws Exception {
        final File jsonFile = new ClassPathResource("init/cake.json").getFile();
        final String cake = Files.readString(jsonFile.toPath());
        this.mockMvc.perform(post(BASE_URL_CAKES).contentType(APPLICATION_JSON).content(cake))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isMap());
        assertEquals(4, this.repository.findAll().size());
    }

    @Test
    public void testSaveApiShouldThrowNotAuthorised() throws Exception {
        this.mockMvc.perform(get(BASE_URL_CAKES).contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser("/user-1")
    @Test
    public void testGetAllCakes() throws Exception {
        this.mockMvc.perform(get(BASE_URL_CAKES).contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        assertEquals(3, this.repository.findAll().size());
    }

    @WithMockUser("/user-1")
    @Test
    public void testGetOneCake() throws Exception {
        final MvcResult mvcResult = this.mockMvc.perform(get(BASE_URL_CAKES+"/101"))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();
        final Cake cake = mapper.readValue(mvcResult.getResponse().getContentAsString(), Cake.class);
        assertThat(cake.getId(), is(101L));
        assertThat(cake.getPrice().toString(), is("101.20"));
    }

    @WithMockUser("/user-1")
    @Test
    public void testUpdateOneCake() throws Exception {
        final MvcResult mvcResult = this.mockMvc.perform(get(BASE_URL_CAKES+"/101"))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();
        final Cake cake = mapper.readValue(mvcResult.getResponse().getContentAsString(), Cake.class);
        Cake updatedCake = new Cake();
        updatedCake.setName("Changed Cake to Carrot Cake");
        updatedCake.setIngredients("lot of carrots");
        updatedCake.setPrice(new BigDecimal(22.22));
        this.mockMvc.perform(put(BASE_URL_CAKES+"/101").contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedCake)))
                .andDo(print())
                .andExpect(status().isOk());
        assertEquals(3, this.repository.findAll().size());
        final MvcResult updatedCakeMvcResult = this.mockMvc.perform(get(BASE_URL_CAKES+"/101"))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        final Cake updatedCakeFromGetCall = mapper.readValue(updatedCakeMvcResult.getResponse().getContentAsString(), Cake.class);
        assertThat(updatedCakeFromGetCall.getId(), is(101L));
        assertThat(updatedCakeFromGetCall.getIngredients(), is("lot of carrots"));
        assertThat(updatedCakeFromGetCall.getName(), is("Changed Cake to Carrot Cake"));
        assertThat(updatedCakeFromGetCall.getPrice().toString(), is("22.22"));
    }

    @WithMockUser("/user-1")
    @Test
    public void testDeleteOneCake() throws Exception {
        this.mockMvc.perform(delete(BASE_URL_CAKES+"/101"))
                .andDo(print())
                .andExpect(status().isOk());
        assertEquals(2, this.repository.findAll().size());
        this.mockMvc.perform(get(BASE_URL_CAKES+"/101"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


}

