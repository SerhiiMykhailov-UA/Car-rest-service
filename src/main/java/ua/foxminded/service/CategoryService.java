package ua.foxminded.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.foxminded.dto.CategoryDto;
import ua.foxminded.entity.Category;
import ua.foxminded.exception.CategoryException;
import ua.foxminded.mapper.CategoryMapper;
import ua.foxminded.mapper.CycleAvoidingMappingContext;
import ua.foxminded.repository.CategoryJPARepository;

@Service
@Transactional(readOnly = true)
public class CategoryService {
	
	private final CategoryMapper mapper;
	private final CategoryJPARepository repository;
	
	private final Logger logger = LogManager.getLogger();
	
	private final CycleAvoidingMappingContext context = new CycleAvoidingMappingContext();

	public CategoryService(CategoryMapper mapper, CategoryJPARepository repository) {
		this.mapper = mapper;
		this.repository = repository;
	}
	
	@Transactional(readOnly = false)
	public CategoryDto add (CategoryDto category) {
		logger.info("Add new category = {}", category);
		Category categoryDao = mapper.categoryDtoToCategory(category, context);
		Category categoryResult = repository.saveAndFlush(categoryDao);
		CategoryDto categoryDto = mapper.categoryToCategoryDto(categoryResult, context);
		logger.info("OUT car = {}", categoryDto);
		logger.info("---------------------------------------");
		return categoryDto;
	}
	
	public List<CategoryDto> getAll(){
		logger.info("Get all categories");
		List<CategoryDto> categories = repository.findAll()
				.stream()
				.map(el->mapper.categoryToCategoryDto(el, context))
				.collect(Collectors.toList());
		logger.info("OUT list of categories = {}", categories);
		logger.info("-------------------------------------------");
		return categories;
	}	

	public CategoryDto get(long id) throws CategoryException {
		logger.info("Get category by id = {}", id);
		Category category = repository.findById(id)
				.orElseThrow(()-> new CategoryException("Cann't find the car id = " + id));
		CategoryDto categoryDto = mapper.categoryToCategoryDto(category, context);
		logger.info("OUT get category = {}", categoryDto);
		logger.info("-------------------------------------------");
		return categoryDto;
	}
	
	@Transactional(readOnly = false)
	public boolean delet(CategoryDto category) {
		logger.info("Delet category = {}", category);
		
		boolean delet = repository.deleteByName(category.getName());
		
		logger.info("OUT result delet category = {}", delet);
		logger.info("-------------------------------------------");
		return delet;
	}
	
	@Transactional(readOnly = false)
	public CategoryDto update (CategoryDto category) throws CategoryException {
		logger.info("Update category = {}", category);
		Category categoryDao = mapper.categoryDtoToCategory(category, context);
		
		Category categoryTemp = repository.findById(category.getId())
				.orElseThrow(()-> new CategoryException("Cann't find car id = " + category.getId()));
		
		categoryTemp.setCar(categoryDao.getCar());
		
		Category categoryResult = repository.saveAndFlush(categoryTemp);
		
		CategoryDto categoryDto = mapper.categoryToCategoryDto(categoryResult, context);
		logger.info("OUT update category = {}", categoryDto);
		logger.info("-------------------------------------------");
		return categoryDto;
	}
}
