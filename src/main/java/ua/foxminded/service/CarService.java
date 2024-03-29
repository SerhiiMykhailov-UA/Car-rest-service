package ua.foxminded.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.foxminded.dto.CarDto;
import ua.foxminded.entity.Car;
import ua.foxminded.entity.Maker;
import ua.foxminded.exception.CarException;
import ua.foxminded.exception.MakerException;
import ua.foxminded.mapper.CarMapper;
import ua.foxminded.mapper.CycleAvoidingMappingContext;
import ua.foxminded.repository.CarJPARepository;
import ua.foxminded.repository.MakerJPARepository;

@Service
@Transactional(readOnly = true)
public class CarService {

	private final CarMapper mapper;
	private final CarJPARepository carJPARepository;
	private final MakerJPARepository makerJPARepository;

	private final Logger logger = LogManager.getLogger();

	private CycleAvoidingMappingContext context = new CycleAvoidingMappingContext();

	public CarService(CarMapper mapper, CarJPARepository carJPARepository, MakerJPARepository makerJPARepository) {
		this.mapper = mapper;
		this.carJPARepository = carJPARepository;
		this.makerJPARepository = makerJPARepository;
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

	public List<CarDto> getAll(int page, int size) {
		logger.info("Get all cars");
		
		List<CarDto> cars = carJPARepository.findAll(PageRequest.of(page, size, Sort.by("name").ascending()))
				.stream()
				.map(el -> mapper.carToCarDto(el, context))
				.collect(Collectors.toList());

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

	public List<CarDto> getCarsByModel(String modelName, int page, int size) {
		logger.info("Get cars by model IN modelName = {}", modelName);
		List<CarDto> cars = carJPARepository.findByNameOrderByYear(modelName, PageRequest.of(page, size, Sort.by("name").ascending()))
				.stream().map(el -> mapper.carToCarDto(el, context))
				.collect(Collectors.toList());
		logger.info("Get cars by model OUT list cars = {}", cars);
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
		return modelCarsList;
	}
}
