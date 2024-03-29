package ua.foxminded.controller.v1;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ua.foxminded.dto.MakerDto;
import ua.foxminded.exception.MakerException;
import ua.foxminded.service.MakerService;


@RestController
@RequestMapping("/v1/makers")
public class MakerControler {

	private final MakerService makerService;
	
	private final Logger logger = LogManager.getLogger();

	public MakerControler(MakerService makerService) {
		this.makerService = makerService;
	}
	
	
	@GetMapping()
	public Page<MakerDto> getListAllMakers(@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {
		return makerService.getAll(page, size);
	}
	
	@GetMapping("/{maker}")
	public MakerDto getMaker (@PathVariable("maker") String makerName) throws MakerException {
		String makerNameWithUpCase = makerName.substring(0, 1).toUpperCase() + makerName.substring(1);
		return makerService.getByName(makerNameWithUpCase);
	}
	
	@PostMapping
	public MakerDto addMakerDto (@RequestBody @Valid MakerDto maker) throws MakerException {
		return makerService.add(maker);
	}
	
	@PatchMapping
	public MakerDto updateMakerName(@RequestBody @Valid MakerDto maker) throws MakerException {
		logger.info("IN: update Maker = {}", maker);
		MakerDto makerResult = makerService.updateName(maker);
		logger.info("OUT: update Maker = {}", makerResult);
 		return makerResult;
	}
	
	
	@DeleteMapping
	public ResponseEntity<HttpStatus> deletMaker (@RequestBody @Valid MakerDto maker) throws MakerException {
		logger.info("IN deletMaker: MakerDto = {}", maker);
		boolean deleteMaker = makerService.delet(maker);
		logger.info("OUT deletMaker: boolean = {}", deleteMaker);
		return ResponseEntity.ok(HttpStatus.OK);
	}
	
}
