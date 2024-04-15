package ua.foxminded.controller;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
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

import ua.foxminded.dto.CarDto;
import ua.foxminded.dto.CategoryDto;
import ua.foxminded.dto.MakerDto;
import ua.foxminded.service.CarService;
import ua.foxminded.service.MakerService;

@WebMvcTest
class CarControllerTest {

	@Autowired
	private MockMvc mvc;
	@MockBean
	private CarService carService;
	@MockBean
	private MakerService makerService;
	
	private CarDto car = new CarDto("1111", "TestCar-1", 2002, Arrays.asList(new CategoryDto("Sedan")), new MakerDto("Serg"));
	private CarDto carResult = new CarDto(UUID.randomUUID(), "1111", "TestCar-1", 2002, Arrays.asList(new CategoryDto("Sedan")), new MakerDto("Serg"));
	private List<CarDto> cars;
	private Page<CarDto> carsPage;
	
	ObjectMapper mapper;
	String jsonObj;
	
	{
		cars = new ArrayList<>(Arrays.asList(
				new CarDto("1111", "TestCar-1", 2002, Arrays.asList(new CategoryDto("Sedan")), new MakerDto("Serg")),
				new CarDto("2222", "TestCar-1", 2000, Arrays.asList(new CategoryDto("Sedan")), new MakerDto("Serg")),
				new CarDto("3333", "TestCar-1", 1995, Arrays.asList(new CategoryDto("Sedan")), new MakerDto("Serg")),
				new CarDto("4444", "TestCar-1", 2018, Arrays.asList(new CategoryDto("Sedan")), new MakerDto("Serg"))
				));
		carsPage = new PageImpl<CarDto>(cars);
		mapper = new ObjectMapper();
		try {
			jsonObj = mapper.writeValueAsString(car);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	void givenMakerName_whenGetModelsByMaker_thenReturnJsonArray() throws Exception {
		List<String> cars = Arrays.asList("TestModel-1", "TestModel-2", "TestModel-3");
		String makerName = "Serg";
		
		when(carService.getAllModelCarByMaker(Mockito.anyString())).thenReturn(cars);
		
		mvc.perform(get("/v1/makers/{maker}/models", makerName)
				.param("maker", makerName)
				.contentType(MediaType.APPLICATION_JSON))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0]", is("TestModel-1")))
		.andExpect(jsonPath("$", hasItem("TestModel-3")));
	}

	@Test
	void givenMogel_whenGetCarsByModel_thenReturnPageJsonArray() throws Exception {
		String modelName = "TestCar-1";
		
		when(carService.getCarsByModel(modelName, 0, 10)).thenReturn(carsPage);
		
		mvc.perform(get("/v1/makers/models/{model}", modelName)
				.param("model", modelName).param("page", "0").param("size", "10")
				.contentType(MediaType.APPLICATION_JSON))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.content[0].name", is("TestCar-1")))
		.andExpect(jsonPath("$.content[2].year", is(1995)))
		.andExpect(jsonPath("$.content[3].year", is(2018)));
	}

	@Test
	void givenModeAndYear_whenGetCarByModelAndYear_thenReturnJsonCar() throws Exception {
		String modelName = "TestCar-1";
		when(carService.getByNameAndYear(Mockito.anyString(), Mockito.anyInt())).thenReturn(car);
		
		mvc.perform(get("/v1/makers/models/{model}/{year}", modelName, 2002)
				.contentType(MediaType.APPLICATION_JSON))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.objectId", is(car.getObjectId())));
	}

	@Test
	void givenCar_whenAddNewCar_thenReturnJsonCar() throws Exception {
		when(carService.add(Mockito.any())).thenReturn(carResult);
		

		
		mvc.perform(post("/v1/makers/models")
				.accept(MediaType.APPLICATION_JSON)
				.content(jsonObj)
				.contentType(MediaType.APPLICATION_JSON))
		.andDo(print())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id", notNullValue()));
	}

	@Test
	void givenCar_whenDeleteCar_thenReturnBoolean() throws Exception {
		when(carService.delete(Mockito.any())).thenReturn(true);
		
		mvc.perform(delete("/v1/makers/models")
				.accept(MediaType.APPLICATION_JSON)
				.content(jsonObj)
				.contentType(MediaType.APPLICATION_JSON))
		.andDo(print())
		.andExpect(status().isOk());
	}

	@Test
	void givenCar_whenUpdateCar_thenReturnJsonCar() throws Exception {
		when(carService.updateCar(Mockito.any())).thenReturn(carResult);
		
		mvc.perform(put("/v1/makers/models")
				.accept(MediaType.APPLICATION_JSON)
				.content(jsonObj)
				.contentType(MediaType.APPLICATION_JSON))
		.andDo(print())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id", notNullValue()))
		.andExpect(jsonPath("$.name", is("TestCar-1")));
	}

	@Test
	void givenCar_whenPatchCar_thenReturnJsonCar() throws Exception {
		when(carService.patchCar(Mockito.any())).thenReturn(carResult);
		
		mvc.perform(patch("/v1/makers/models")
				.accept(MediaType.APPLICATION_JSON)
				.content(jsonObj)
				.contentType(MediaType.APPLICATION_JSON))
		.andDo(print())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id", notNullValue()))
		.andExpect(jsonPath("$.name", is("TestCar-1")));
	}

	@Test
	void testSerchCars() throws Exception {
		when(carService.searchCars(Mockito.anyList(), Mockito.any(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(carsPage);
		
		mvc.perform(get("/v1/makers/cars")
				.accept(MediaType.APPLICATION_JSON)
				.param("name", "TestCar-1")
				.contentType(MediaType.APPLICATION_JSON))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.content[0].year", is(2002)));
	}

}
