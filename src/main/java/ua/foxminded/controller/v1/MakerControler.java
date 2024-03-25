package ua.foxminded.controller.v1;

import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ua.foxminded.dto.MakerDto;
import ua.foxminded.exception.MakerException;
import ua.foxminded.exception.MakerRunTimeException;
import ua.foxminded.service.MakerService;
import ua.foxminded.util.MakerErrorResponse;


@RestController
@RequestMapping("/v1/makers")
public class MakerControler {

	private final MakerService makerService;
	
	private final Logger logger = LogManager.getLogger();

	public MakerControler(MakerService makerService) {
		this.makerService = makerService;
	}
	
	
	@GetMapping()
	public List<MakerDto> getListAllMakers() {
		return makerService.getAll();
	}
	
	@GetMapping("/{name}")
	public MakerDto getMaker (@PathVariable("name") String name) throws MakerException {
		return makerService.getByName(name);
	}
	
	@PostMapping("/add/maker")
	public MakerDto addMakerDto (@RequestBody @Valid MakerDto maker, BindingResult bindingResult) throws MakerException {
		logger.info("IN: Add new Maker = {}", maker);
		if (bindingResult.hasErrors()) {
			StringBuilder errorsMsgBuilder = new StringBuilder();
			List<FieldError> errors = bindingResult.getFieldErrors();
			for (FieldError fieldError : errors) {
				errorsMsgBuilder.append(fieldError.getField())
								.append("-")
								.append(fieldError.getDefaultMessage())
								.append(";");
			}
			throw new MakerRunTimeException(errorsMsgBuilder.toString());
		}
		
		MakerDto makerDtoResult = makerService.add(maker);
		logger.info("OUT: Add new Maker = {}", makerDtoResult);
		return makerDtoResult;
	}
	
	@PostMapping("/update/makername")
	public MakerDto updateMakerName(@RequestBody @Valid MakerDto maker, BindingResult bindingResult) throws MakerException {
		logger.info("IN: update Maker = {}", maker);
		if (bindingResult.hasErrors()) {
			StringBuilder errorsMsgBuilder = new StringBuilder();
			List<FieldError> errors = bindingResult.getFieldErrors();
			for (FieldError fieldError : errors) {
				errorsMsgBuilder.append(fieldError.getField())
								.append("-")
								.append(fieldError.getDefaultMessage())
								.append(";");
			}
			throw new MakerRunTimeException(errorsMsgBuilder.toString());
		}
		MakerDto makerResult = makerService.updateName(maker);
		logger.info("OUT: update Maker = {}", makerResult);
 		return makerResult;
	}
	
	@PostMapping("/update/makercars")
	public MakerDto updateMakerCars(@RequestBody @Valid MakerDto maker, BindingResult bindingResult) throws MakerException {
		logger.info("IN: update Maker = {}", maker);
		if (bindingResult.hasErrors()) {
			StringBuilder errorsMsgBuilder = new StringBuilder();
			List<FieldError> errors = bindingResult.getFieldErrors();
			for (FieldError fieldError : errors) {
				errorsMsgBuilder.append(fieldError.getField())
								.append("-")
								.append(fieldError.getDefaultMessage())
								.append(";");
			}
			throw new MakerRunTimeException(errorsMsgBuilder.toString());
		}
		MakerDto makerResult = makerService.updateCars(maker);
		logger.info("OUT: update Maker = {}", makerResult);
 		return makerResult;
	}
	
	@DeleteMapping("/delete/maker")
	public ResponseEntity<HttpStatus> deletMaker (@RequestBody @Valid MakerDto maker, BindingResult result) throws MakerException {
		result.getFieldError();
		logger.info("IN deletMaker: MakerDto = {}", maker);
		boolean deleteMaker = makerService.delet(maker);
		logger.info("OUT deletMaker: boolean = {}", deleteMaker);
		return ResponseEntity.ok(HttpStatus.OK);
	}
	
	@ExceptionHandler
	private ResponseEntity<MakerErrorResponse> handlerException(MakerRunTimeException e){
		MakerErrorResponse response = new MakerErrorResponse(e.getMessage(), System.currentTimeMillis());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	private ResponseEntity<MakerErrorResponse> handlerException(MakerException e){
		MakerErrorResponse response = new MakerErrorResponse(e.getMessage(), System.currentTimeMillis());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
}