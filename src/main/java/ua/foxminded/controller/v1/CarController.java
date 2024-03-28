package ua.foxminded.controller.v1;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ua.foxminded.dto.CarDto;
import ua.foxminded.dto.MakerDto;
import ua.foxminded.exception.CarException;
import ua.foxminded.exception.MakerException;
import ua.foxminded.service.CarService;




@RestController
@RequestMapping("/v1/makers")
public class CarController {

	private final CarService carService;

	public CarController(CarService carService) {
		this.carService = carService;
	}
	
	@GetMapping("/{maker}/models")
	public List<String> getAllCarsByMaker(@PathVariable("maker") String makerName,
			@RequestBody @Valid MakerDto maker) throws MakerException {
		return carService.getAllModelCarByMaker(maker);
	}
	

	@GetMapping("/{maker}/models/{model}")
	public List<CarDto> getCarsListByModel(@PathVariable("maker") String makerName,
			@PathVariable("model") String modelName) {
		String modelNameWithUpCase = modelName.substring(0, 1).toUpperCase() + modelName.substring(1);
		List<CarDto> cars = carService.getCarsByModelList(modelNameWithUpCase);
		System.out.println(cars);
		return cars;
	}
	
	@GetMapping("/{maker}/models/{model}/{year}")
	public CarDto getCar(@PathVariable("maker") String makerName, 
			@PathVariable("model") String modelName,
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
	
	@PatchMapping("/models")
	public CarDto updateCarNameMaker (@RequestBody CarDto car) throws CarException {
		return carService.updateNameMaker(car);
	}
	
	@PatchMapping("/models")
	public CarDto updateCarCategory (@RequestBody CarDto car) throws CarException {
		return carService.updateCategory(car);
	}
}
