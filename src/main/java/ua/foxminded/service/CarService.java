package ua.foxminded.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.data.domain.ExampleMatcher.MatcherConfigurer;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.foxminded.dto.CarDto;
import ua.foxminded.dto.CategoryDto;
import ua.foxminded.entity.Car;
import ua.foxminded.entity.Category;
import ua.foxminded.entity.Maker;
import ua.foxminded.exception.CarException;
import ua.foxminded.exception.CategoryException;
import ua.foxminded.exception.MakerException;
import ua.foxminded.mapper.CarMapper;
import ua.foxminded.mapper.CycleAvoidingMappingContext;
import ua.foxminded.repository.CarJPARepository;
import ua.foxminded.repository.CategoryJPARepository;
import ua.foxminded.repository.MakerJPARepository;
import ua.foxminded.specificationJPA.SearchSpecification;

@Service
@Transactional(readOnly = true)
public class CarService {

	private final CarMapper mapper;
	private final CarJPARepository carJPARepository;
	private final MakerJPARepository makerJPARepository;
	private final CategoryJPARepository categoryJPARepository;

	private final Logger logger = LogManager.getLogger();

	private CycleAvoidingMappingContext context = new CycleAvoidingMappingContext();

	public CarService(CarMapper mapper, CarJPARepository carJPARepository, MakerJPARepository makerJPARepository, CategoryJPARepository categoryJPARepository) {
		this.mapper = mapper;
		this.carJPARepository = carJPARepository;
		this.makerJPARepository = makerJPARepository;
		this.categoryJPARepository = categoryJPARepository;
	}

	@Transactional(readOnly = false)
	public CarDto add(CarDto car) {
		logger.info("Add new car = {}", car);
		Car carDao = mapper.carDtoToCar(car, context);

		Car carResult = carJPARepository.saveAndFlush(carDao);

		CarDto carDto = mapper.carToCarDto(carResult, context);
		logger.info("OUT car = {}", carDto);
		logger.info("---------------------------------------");
		return carDto;
	}

	public Page<CarDto> getAll(int page, int size) {
		logger.info("Get all cars");
		
		Page<CarDto> cars = carJPARepository.findAll(PageRequest.of(page, size, Sort.by("name").ascending()))
				.map(el -> mapper.carToCarDto(el, context));

		logger.info("OUT list of cars = {}", cars);
		logger.info("-------------------------------------------");
		return cars;
	}

	public CarDto get(UUID id) throws CarException {
		logger.info("Get car by id = {}", id);

		Car car = carJPARepository.findById(id).orElseThrow(() -> new CarException("Cann't find the car id = " + id));

		CarDto carDto = mapper.carToCarDto(car, context);
		logger.info("OUT get car = {}", carDto);
		logger.info("-------------------------------------------");
		return carDto;
	}
	
	public CarDto getByNameAndYear(String name, int year) throws CarException {
		logger.info("Get car by name = {} & year = {}", name, year);

		Car car = carJPARepository.findByNameAndYear(name, year)
				.orElseThrow(() -> new CarException("Cann't find the car name = " + name + " year = " + year));

		CarDto carDto = mapper.carToCarDto(car, context);
		logger.info("OUT get car = {}", carDto);
		logger.info("-------------------------------------------");
		return carDto;
	}

	@Transactional(readOnly = false)
	public boolean delete(CarDto car) throws CarException {
		UUID id = car.getId();
		logger.info("Delet car = {}", car);
		if (id  == null) {
			throw new CarException("Field : id shouldn't be NULL");
		}
		
		if (carJPARepository.existsById(id)) {
			carJPARepository.deleteById(car.getId());
		}else {
			throw new CarException("Car id = " + id + " wasn't found");
		}
		
		boolean deleteCheck = carJPARepository.existsById(car.getId());

		if(deleteCheck){
			throw new CarException("Delet wasn't seccessful. Contact administrator");
		}
		logger.info("OUT result delet car = {}", true);
		logger.info("-------------------------------------------");
		return true;
	}

	@Transactional(readOnly = false)
	public CarDto updateCar(CarDto car) throws CarException {
		UUID id = car.getId();
		logger.info("Update car's name & maker = {}", car);
		if (id  == null) {
			throw new CarException("Field : id shouldn't be NULL");
		}
		Car carDao = mapper.carDtoToCar(car, context);

		Car carTemp = carJPARepository.findById(car.getId())
				.orElseThrow(() -> new CarException("Cann't find car id = " + car.getId()));

		carTemp.setObjectId(carDao.getObjectId());
		carTemp.setName(carDao.getName());
		carTemp.setMaker(carDao.getMaker());
		carTemp.setCategory(carDao.getCategory());

		Car carResult = carJPARepository.saveAndFlush(carTemp);

		CarDto carDto = mapper.carToCarDto(carResult, context);
		logger.info("OUT update car = {}", carDto);
		logger.info("-------------------------------------------");
		return carDto;
	}
	
	@Transactional(readOnly = false)
	public CarDto patchCar(CarDto car) throws CarException {
		UUID id = car.getId();
		logger.info("Update car's category = {}", car);
		if (id  == null) {
			throw new CarException("Field : id shouldn't be NULL");
		}
		Car carDao = mapper.carDtoToCar(car, context);

		Car carTemp = carJPARepository.findById(car.getId())
				.orElseThrow(() -> new CarException("Cann't find car id = " + car.getId()));

		carTemp.setCategory(carDao.getCategory());

		Car carResult = carJPARepository.saveAndFlush(carTemp);

		CarDto carDto = mapper.carToCarDto(carResult, context);
		logger.info("OUT update car = {}", carDto);
		logger.info("-------------------------------------------");
		return carDto;
	}

	public Page<CarDto> getCarsByModel(String modelName, int page, int size) {
		logger.info("Get cars by model IN modelName = {}", modelName);
		Page<CarDto> cars = carJPARepository.findByNameOrderByYear(modelName, PageRequest.of(page, size, Sort.by("year").ascending()))
				.map(el -> mapper.carToCarDto(el, context));
		logger.info("Get cars by model OUT list cars = {}", cars);
		logger.info("-------------------------------------------");
		return cars;
	}

	public List<String> getAllModelCarByMaker(String makerName) throws MakerException {
		logger.info("Get cars by Maker IN Maker = {}", makerName);
		Maker makerDao = makerJPARepository.findByName(makerName)
				.orElseThrow(() -> new MakerException("Cann't find maker = " + makerName));
		List<String> modelCarsList = makerDao.getCar()
				.stream()
				.map(el->el.getName())
				.distinct()
				.sorted()
				.collect(Collectors.toList());
		logger.info("Get car's model by Maker OUT list String name list = {}", modelCarsList);
		logger.info("-------------------------------------------");
		return modelCarsList;
	}
	
	public Page<CarDto> searchCars(String name, int yearMax, int yearMin,
			int page, int size, String makerName, String categoryName) throws CategoryException, MakerException {
		Car car = new Car();
		if (name != null) {
			car.setName(name);
		}
		if (categoryName != null) {
			Category category = categoryJPARepository.findByName(categoryName)
					.orElseThrow(()-> new CategoryException(""));
			car.setCategory(new ArrayList<>(Arrays.asList(category)));
		}
		if (makerName != null) {
			Maker maker = makerJPARepository.findByName(makerName)
					.orElseThrow(()-> new MakerException(""));
			car.setMaker(maker);
		}
		car.setYear(2019);
		System.out.println(car);
		ExampleMatcher exampleMatcher = ExampleMatcher.matching()
				.withIgnoreNullValues()
				.withMatcher("name", new GenericPropertyMatcher().contains().ignoreCase())
				.withMatcher("category", new GenericPropertyMatcher().contains().ignoreCase())
				.withMatcher("maker", new GenericPropertyMatcher().exact().ignoreCase())
				.withMatcher("year", new GenericPropertyMatcher().exact());
		Example<Car> example = Example.of(car, exampleMatcher);
		Page<Car> cars = carJPARepository.findAll(example, PageRequest.of(page, size));
		logger.info("Cars = {}", cars.toList());
		Page<CarDto> carsDto =	cars.map(el->mapper.carToCarDto(el, context));
		return carsDto;
	}
	
}
