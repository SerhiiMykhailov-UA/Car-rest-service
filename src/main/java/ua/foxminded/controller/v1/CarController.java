package ua.foxminded.controller.v1;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ua.foxminded.dto.CarDto;
import ua.foxminded.exception.CarException;
import ua.foxminded.exception.CategoryException;
import ua.foxminded.exception.MakerException;
import ua.foxminded.service.CarService;
import ua.foxminded.specificationJPA.SearchCriteria;




@RestController
@RequestMapping("/v1/makers")
public class CarController {

	private final CarService carService;
	private SearchCriteria searchCriteria;
	
	public CarController(CarService carService, SearchCriteria searchCriteria) {
		this.carService = carService;
		this.searchCriteria = searchCriteria;
	}
	
	@GetMapping("/{maker}/models")
	public List<String> getAllCarsByMaker(@PathVariable("maker") String makerName) throws MakerException {
		return carService.getAllModelCarByMaker(makerName);
	}
	
	@GetMapping("/models/{model}")
	public Page<CarDto> getCarsListByModel(@PathVariable("model") String modelName,
			@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {
		String modelNameWithUpCase = modelName.substring(0, 1).toUpperCase() + modelName.substring(1);
		Page<CarDto> cars = carService.getCarsByModel(modelNameWithUpCase, page, size);
		System.out.println(cars);
		return cars;
	}
	
	@GetMapping("/models/{model}/{year}")
	public CarDto getCar(@PathVariable("model") String modelName,
			@PathVariable("year") int year) throws CarException {
		String modelNameWithUpCase = modelName.substring(0, 1).toUpperCase() + modelName.substring(1);
		CarDto carDto = carService.getByNameAndYear(modelNameWithUpCase, year);
		return carDto;
	}
	
	@PostMapping("/models")
	public CarDto createNewCar(@RequestBody CarDto car) {
		return carService.add(car);
	}
	
	@DeleteMapping("/models")
	public boolean deleteCar (@RequestBody CarDto car) throws CarException {
		return carService.delete(car);
	}
	
	@PutMapping("/models")
	public CarDto updateCar (@RequestBody CarDto car) throws CarException {
		return carService.updateCar(car);
	}
	
	@PatchMapping("/models")
	public CarDto patchCar (@RequestBody CarDto car) throws CarException {
		return carService.patchCar(car);
	}
	
	@GetMapping("/cars")
	public Page<CarDto> serchCars (@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "yearMax", defaultValue = "0", required = false) int yearMax,
			@RequestParam(name = "yearMin", defaultValue = "0", required = false) int yearMin,
			@RequestParam(name = "maker", required = false) String maker,
			@RequestParam(name = "category", required = false) String category,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) throws CategoryException, MakerException{
		searchCriteria.setName(name);
		searchCriteria.setCategory(category);
		searchCriteria.setMaker(maker);
		searchCriteria.setYearMax(yearMax);
		searchCriteria.setYearMin(yearMin);
		List<String> notNullKeyList = new ArrayList<>();
		if (category != null) {
			notNullKeyList.add("category");
		}
		if (maker != null) {
			notNullKeyList.add("maker");
		}
		if (name != null) {
			notNullKeyList.add("name");
		}
		if (yearMax != 0 & yearMin != 0) {
			notNullKeyList.add("yearBetween");
		}else if (yearMax != 0 & yearMin == 0) {
			notNullKeyList.add("yearMax");
		}else if (yearMax == 0 & yearMin != 0) {
			notNullKeyList.add("yearMin");
		}
		return carService.searchCars(notNullKeyList, page, size);
	}
}
