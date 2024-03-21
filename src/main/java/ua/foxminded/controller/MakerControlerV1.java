package ua.foxminded.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ua.foxminded.dto.MakerDto;
import ua.foxminded.exception.MakerException;
import ua.foxminded.service.MakerService;


@RestController
@RequestMapping("/car-rest-service/V1/manufacturers")
public class MakerControlerV1 {

	private final MakerService makerService;
	
	private final Logger logger = LogManager.getLogger();

	public MakerControlerV1(MakerService makerService) {
		this.makerService = makerService;
	}
	
	
	@GetMapping()
	public List<MakerDto> getListAllManufacturers() {
		return makerService.getAll();
	}
	
	@GetMapping("/{name}")
	public MakerDto makerPage (@RequestParam("name") String name) throws MakerException {
		return makerService.getByName(name);
	}
	
}
