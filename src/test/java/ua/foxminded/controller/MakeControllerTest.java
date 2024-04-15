package ua.foxminded.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ua.foxminded.dto.MakerDto;
import ua.foxminded.service.CarService;
import ua.foxminded.service.MakerService;

@WebMvcTest
class MakeControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private MakerService makerService;
	
	@MockBean
	private CarService carService;
	
	private MakerDto maker;
	private ObjectMapper mapper;
	private String objectJson;
	private Page<MakerDto> makerPage;
	private List<MakerDto> makerList;
	
	{
		makerList = Arrays.asList(new MakerDto("Toyota"), new MakerDto("Nisan"), new MakerDto("Ford"), new MakerDto("Tesla"));
		makerPage = new PageImpl<MakerDto>(makerList);
		maker = new MakerDto(UUID.randomUUID(),"Toyota");
		mapper = new ObjectMapper();
		try {
			objectJson = mapper.writeValueAsString(maker);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Test
	void getAllMakers_thenReturnJsonArray() throws Exception {
		when(makerService.getAll(0, 10)).thenReturn(makerPage);
		
		mvc.perform(get("/v1/makers")
				.param("page", "0")
				.param("size", "10")
				.contentType(MediaType.APPLICATION_JSON))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.content[0].name", is("Toyota")));
	}

	@Test
	void givenMakerName_whenGetMakerByName_thenReturnJsonMaker() throws Exception {
		when(makerService.getByName(Mockito.anyString())).thenReturn(new MakerDto("Toyota"));
		
		mvc.perform(get("/v1/makers/{maker}", "toyota")
				.param("maker", "toyota")
				.contentType(MediaType.APPLICATION_JSON))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.name", is("Toyota")));
	}

	@Test
	void givenJsonMaker_whenAddNewMaker_thenReturnJsonMaker() throws Exception {
		when(makerService.add(Mockito.any())).thenReturn(maker);
		
		mvc.perform(post("/v1/makers")
				.accept(MediaType.APPLICATION_JSON)
				.content(objectJson)
				.contentType(MediaType.APPLICATION_JSON))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id", notNullValue()));
	}

	@Test
	void givenJsonMaker_whenUpdateMaker_thenReturnJsonMaker() throws Exception {
		when(makerService.updateName(Mockito.any())).thenReturn(maker);
		
		mvc.perform(patch("/v1/makers")
				.accept(MediaType.APPLICATION_JSON)
				.content(objectJson)
				.contentType(MediaType.APPLICATION_JSON))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id", notNullValue()))
		.andExpect(jsonPath("$.name", is("Toyota")));
	}

	@Test
	void givenJsonMaker_whenDeleteMaker_thenReturnJsonMaker() throws Exception {
		when(makerService.delet(Mockito.any())).thenReturn(true);
		
		mvc.perform(delete("/v1/makers")
				.accept(MediaType.APPLICATION_JSON)
				.content(objectJson)
				.contentType(MediaType.APPLICATION_JSON))
		.andDo(print())
		.andExpect(status().isOk());
	}

}
