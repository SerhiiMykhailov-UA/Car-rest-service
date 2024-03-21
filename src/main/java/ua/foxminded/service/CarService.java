package ua.foxminded.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.foxminded.dto.CarDto;
import ua.foxminded.entity.Car;
import ua.foxminded.exception.CarException;
import ua.foxminded.mapper.CarMapper;
import ua.foxminded.mapper.CycleAvoidingMappingContext;
import ua.foxminded.repository.CarJPARepository;

@Service
@Transactional(readOnly = true)
public class CarService {
	
	private final CarMapper mapper;
	private final CarJPARepository repository;
	
	private final Logger logger = LogManager.getLogger();
	
	private CycleAvoidingMappingContext context = new CycleAvoidingMappingContext();

	public CarService(CarMapper mapper, CarJPARepository repository) {
		this.mapper = mapper;
		this.repository = repository;
	}

	@Transactional(readOnly = false)
	public CarDto add (CarDto car) {
		logger.info("Add new car = {}", car);
		Car carDao = mapper.carDtoToCar(car, context);
		
		Car carResult = repository.saveAndFlush(carDao);
		
		CarDto carDto = mapper.carToCarDto(carResult, context);
		logger.info("OUT car = {}", carDto);
		logger.info("---------------------------------------");
		return carDto;
	}
	
	public List<CarDto> getAll(){
		logger.info("Get all cars");
		
		List<CarDto> cars = repository.findAll()
				.stream()
				.map(el->mapper.carToCarDto(el, context))
				.collect(Collectors.toList());
		
		logger.info("OUT list of cars = {}", cars);
		logger.info("-------------------------------------------");
		return cars;
	}
	
	public CarDto get(UUID id) throws CarException {
		logger.info("Get car by id = {}", id);
		
		Car car = repository.findById(id)
				.orElseThrow(()-> new CarException("Cann't find the car id = " + id));
		
		CarDto carDto = mapper.carToCarDto(car, context);
		logger.info("OUT get car = {}", carDto);
		logger.info("-------------------------------------------");
		return carDto;
	}
	
	@Transactional(readOnly = false)
	public boolean delet(CarDto car) {
		logger.info("Delet car = {}", car);
		
		repository.deleteByObjectId(car.getObjectId());
		
		boolean delet = repository.existsById(car.getId());
		
		logger.info("OUT result delet car = {}", delet);
		logger.info("-------------------------------------------");
		return delet;
	}
	
	@Transactional(readOnly = false)
	public CarDto update (CarDto car) throws CarException {
		logger.info("Update car = {}", car);
		Car carDao = mapper.carDtoToCar(car, context);
		
		Car carTemp = repository.findByObjectId(car.getObjectId())
				.orElseThrow(()-> new CarException("Cann't find car id = " + car.getId()));
		
		carTemp.setCategory(carDao.getCategory());
		carTemp.setMaker(carDao.getMaker());
		
		Car carResult = repository.saveAndFlush(carTemp);
		
		CarDto carDto = mapper.carToCarDto(carResult, context);
		logger.info("OUT update car = {}", carDto);
		logger.info("-------------------------------------------");
		return carDto;
	}
}
