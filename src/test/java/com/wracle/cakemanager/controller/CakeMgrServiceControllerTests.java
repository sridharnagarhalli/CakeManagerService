package com.wracle.cakemanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wracle.cakemanager.entity.Cake;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.ArrayList;

import static com.wracle.cakemanager.controller.CakeMgrController.BASE_URL_CAKES;
import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CakeMgrServiceControllerTests {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private TestRestTemplate template;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
	}

	@WithMockUser("/user-1")
	@Test
	public void testSaveCake() throws Exception {
		Cake cake = new Cake();
		cake.setName("Black forest");
		cake.setDescription("Delicious");
		cake.setPrice(new BigDecimal(10.99));

		String jsonRequest = mapper.writeValueAsString(cake);

		MvcResult result = mockMvc
				.perform(post(BASE_URL_CAKES).content(jsonRequest).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andReturn();

		assertEquals(201, result.getResponse().getStatus());

	}

	@WithMockUser("/user-1")
	@Test
	public void testGetAllCakes() throws Exception {
		MvcResult result = mockMvc.perform(get(BASE_URL_CAKES).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		assertEquals(200, result.getResponse().getStatus());
	}

	@Test
	public void testGetAllCakesWithTestRestTemplate() {
		ResponseEntity<?> response = template.withBasicAuth("user1", "P@ssw0rd")
				.getForEntity(BASE_URL_CAKES, ArrayList.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
}
