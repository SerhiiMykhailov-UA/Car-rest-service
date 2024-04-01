package ua.foxminded.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.foxminded.dto.CarDto;
import ua.foxminded.dto.CategoryDto;
import ua.foxminded.dto.MakerDto;
import ua.foxminded.entity.Car;
import ua.foxminded.entity.Category;
import ua.foxminded.entity.Maker;
import ua.foxminded.mapper.CarMapper;
import ua.foxminded.mapper.CategoryMapper;
import ua.foxminded.mapper.CycleAvoidingMappingContext;
import ua.foxminded.mapper.MakerMapper;
import ua.foxminded.repository.CarJPARepository;
import ua.foxminded.specificationJPA.SearchSpecification;

@Service
@Transactional(readOnly = true)
public class SearchService {
	
	private final CarMapper carMapper;
	private final CarJPARepository carJPARepository;
	private final MakerMapper makerMapper;
	private final CategoryMapper categoryMapper;

	private final Logger logger = LogManager.getLogger();

	private final CycleAvoidingMappingContext context = new CycleAvoidingMappingContext();

	public SearchService(CarMapper carMapper, CarJPARepository carJPARepository,
			MakerMapper makerMapper, CategoryMapper categoryMapper) {
		this.carMapper = carMapper;
		this.carJPARepository = carJPARepository;
		this.makerMapper = makerMapper;
		this.categoryMapper = categoryMapper;
	}

	public Page<CarDto> findCarByCategory(CategoryDto category, int page, int size) {
		logger.info("Find cars by Category = {} page = {} size = {}", category, page, size);
		Category categoryDao = categoryMapper.categoryDtoToCategory(category, context);
		Specification<Car> specification = SearchSpecification.carByCategory(categoryDao);
		
		Page<CarDto> cars = carJPARepository.findAll(specification, PageRequest.of(page, size, Sort.by("name").and(Sort.by("year"))))
				.map(el->carMapper.carToCarDto(el, context));
		logger.info("-------------------------------------------");
		return cars;
	}
	
	public Page<CarDto> findCarByMaker(MakerDto maker, int page, int size) {
		Maker makerDao = makerMapper.makerDtoToMaker(maker, context);
		Specification<Car> specification = SearchSpecification.carByMaker(makerDao);
		
		Page<CarDto> cars = carJPARepository.findAll(specification, PageRequest.of(page, size, Sort.by("name").and(Sort.by("year"))))
				.map(el -> carMapper.carToCarDto(el, context));
		return cars;
	}
}
