package ua.foxminded.controller.v1;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
	
	private final Logger logger = LogManager.getLogger();
	
	public CarController(CarService carService) {
		this.carService = carService;
	}
	
	@Operation(description = "Get all car's model by Maker name")
	@ApiResponses(
			value = {
			@ApiResponse(responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
			schema = @Schema(implementation = List.class))}),
			  @ApiResponse(responseCode = "400", description = "Cann't find Maker", 
			    content = @Content(mediaType = "CarExeption/json", schema = @Schema(implementation = MakerException.class)))
			})
	@GetMapping("/{maker}/models")
	public List<String> getAllCarsByMaker(@PathVariable("maker") String makerName) throws MakerException {
		logger.info("IN: Get all cars by maker name = {}", makerName);
		String makerNameWithUpperCase = makerName.substring(0, 1).toUpperCase() + makerName.substring(1);
		return carService.getAllModelCarByMaker(makerNameWithUpperCase);
	}
	
	@GetMapping("/models/{model}")
	@Operation(summary = "Get all cars by model")
	@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "Get cars by model", 
					    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
					      schema = @Schema(ref = "#/components/schemas/PageCarDto"))
					    }),
			  @ApiResponse(responseCode = "400", description = "Cann't find cars", 
			    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CarException.class))) 
			  })
	public Page<CarDto> getCarsListByModel(@PathVariable("model") String modelName,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {
		logger.info("IN: Get cars by Model name = {}", modelName);
		String modelNameWithUpCase = modelName.substring(0, 1).toUpperCase() + modelName.substring(1);
		Page<CarDto> cars = carService.getCarsByModel(modelNameWithUpCase, page, size);
		return cars;
	}
	
	@GetMapping("/models/{model}/{year}")
	@Operation(summary = "Get car by model & year")
	@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "Get car", 
					    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
					      schema = @Schema(implementation = CarDto.class)) }),
			  @ApiResponse(responseCode = "400", description = "Cann't find cars", 
			    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CarException.class))) 
			  })
	public CarDto getCar(@PathVariable("model") String modelName,
			@PathVariable("year") int year) throws CarException {
		logger.info("IN: Get car by model name = {} & year = {}", modelName, year);
		String modelNameWithUpCase = modelName.substring(0, 1).toUpperCase() + modelName.substring(1);
		CarDto carDto = carService.getByNameAndYear(modelNameWithUpCase, year);
		return carDto;
	}
	
	@PostMapping("/models")
	@Operation(summary = "Create new car")
	@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "Create new car", 
					    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
					      schema = @Schema(implementation = CarDto.class)) }),
			  })
	public CarDto createNewCar(@RequestBody CarDto car) {
		logger.info("IN: Post new car = {}", car);
		return carService.add(car);
	}
	
	@DeleteMapping("/models")
	@Operation(summary = "Delete car")
	@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "Delete car", 
					    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
					      schema = @Schema(implementation = CarDto.class)) }),
			  @ApiResponse(responseCode = "400", description = "Cann't find car", 
			    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CarException.class))) 
			  })
	public ResponseEntity<HttpStatus> deleteCar (@RequestBody CarDto car) throws CarException {
		logger.info("IN: delet car = {}", car);
		boolean deleteCar = carService.delete(car);
		logger.info("OUT: delet boolean result = {}", deleteCar);
		return ResponseEntity.ok(HttpStatus.OK);
	}
	
	@PutMapping("/models")
	@Operation(summary = "Update a car by it's id")
	@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "Update the car", 
					    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
					      schema = @Schema(implementation = CarDto.class)) }),
			  @ApiResponse(responseCode = "400", description = "Cann't find car", 
			    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CarException.class))) 
			  })
	public CarDto updateCar (@RequestBody CarDto car) throws CarException {
		logger.info("IN: update car = {}", car);
		return carService.updateCar(car);
	}
	
	@PatchMapping("/models")
	@Operation(summary = "Update car's category")
	@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "Update car's category", 
					    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
					      schema = @Schema(implementation = CarDto.class)) }),
			  @ApiResponse(responseCode = "400", description = "Cann't find car", 
			    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CarException.class))) 
			  })
	public CarDto patchCar (@RequestBody CarDto car) throws CarException {
		logger.info("IN: patch car = {}", car);
		return carService.patchCar(car);
	}
	
	@GetMapping("/cars")
	@Operation(summary = "Serch car's")
//	@ApiResponses(value = { 
//			  @ApiResponse(responseCode = "200", description = "Search car's", 
//					    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
//					    schema = @Schema(ref = "#/components/schemas/PageCarDto"))
//					    })
//			  })
	public Page<CarDto> serchCars (
			@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "yearMax", defaultValue = "0", required = false) int yearMax,
			@RequestParam(name = "yearMin", defaultValue = "0", required = false) int yearMin,
			@RequestParam(name = "maker", required = false) String maker,
			@RequestParam(name = "category", required = false) String category,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) throws CategoryException, MakerException{
		
		logger.info("IN: search cars by name = {}, maker = {}, category = {}, yearMax = {}, yearMin = {}", name, maker, category, yearMax, yearMin);
		List<String> notNullKeyList = new ArrayList<>();
		if (category != null) {
			notNullKeyList.add("category");
			category = category.substring(0, 1).toUpperCase() + category.substring(1);
		}
		if (maker != null) {
			notNullKeyList.add("maker");
			maker = maker.substring(0, 1).toUpperCase() + maker.substring(1);
		}
		if (name != null) {
			notNullKeyList.add("name");
			name = name.substring(0, 1).toUpperCase() + name.substring(1);
		}
		if (yearMax != 0 & yearMin != 0) {
			notNullKeyList.add("yearBetween");
		}else if (yearMax != 0 & yearMin == 0) {
			notNullKeyList.add("yearMax");
		}else if (yearMax == 0 & yearMin != 0) {
			notNullKeyList.add("yearMin");
		}
		SearchCriteria searchCriteria = new SearchCriteria(name, maker, category, yearMax, yearMin);
		return carService.searchCars(notNullKeyList, searchCriteria, page, size);
	}

}
