package ua.foxminded.controller.v1;

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
}
