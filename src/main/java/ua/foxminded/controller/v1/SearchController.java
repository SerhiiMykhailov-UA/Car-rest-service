package ua.foxminded.controller.v1;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ua.foxminded.dto.CarDto;
import ua.foxminded.dto.CategoryDto;
import ua.foxminded.dto.MakerDto;
import ua.foxminded.exception.CategoryException;
import ua.foxminded.service.SearchService;
import ua.foxminded.specificationJPA.SearchCriteria;

@RestController
@RequestMapping("/v1/search")
public class SearchController {

	private final SearchService service;
	private SearchCriteria searchCriteria;
	
	private final Logger logger = LogManager.getLogger();

	public SearchController(SearchService service, SearchCriteria searchCriteria) {
		this.service = service;
		this.searchCriteria = searchCriteria;
	}
	
	@GetMapping
	public Page<CarDto> serch (@RequestBody CategoryDto category,
			@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) throws CategoryException{

		Page<CarDto> cars = service.findCarByCategory(category,page, size);
		return cars;
	}
	
	@GetMapping("/bymaker")
	public Page<CarDto> serch (@RequestBody MakerDto maker,
			@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) throws CategoryException{

		Page<CarDto> cars = service.findCarByMaker(maker, page, size);
		return cars;
	}
}
