package ua.foxminded.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.foxminded.dto.MakerDto;
import ua.foxminded.entity.Maker;
import ua.foxminded.exception.MakerException;
import ua.foxminded.mapper.CycleAvoidingMappingContext;
import ua.foxminded.mapper.MakerMapper;
import ua.foxminded.repository.MakerJPARepository;

@Service
@Transactional(readOnly = true)
public class MakerService {

	private final MakerMapper mapper;
	private final MakerJPARepository repository;
	
	private final Logger logger = LogManager.getLogger();
	private final CycleAvoidingMappingContext context = new CycleAvoidingMappingContext();
	
	public MakerService(MakerMapper mapper, MakerJPARepository repository) {
		this.mapper = mapper;
		this.repository = repository;
	}
	
	@Transactional(readOnly = false)
	public MakerDto add (MakerDto maker) {
		logger.info("Add new maker = {}", maker);
		Maker makerDao = mapper.makerDtoToMaker(maker, context);
		
		Maker makerResult = repository.saveAndFlush(makerDao);
		
		MakerDto makerDto = mapper.makerToMakerDto(makerResult, context);
		logger.info("OUT maker = {}", makerDto);
		logger.info("---------------------------------------");
		return makerDto;
	}
	
	public List<MakerDto> getAll(){
		logger.info("Get all makers");
		
		List<MakerDto> makers = repository.findAll()
				.stream()
				.map(el->mapper.makerToMakerDto(el, context))
				.collect(Collectors.toList());
		
		logger.info("OUT list of makers = {}", makers);
		logger.info("-------------------------------------------");
		return makers;
	}	
	
	public MakerDto get(UUID id) throws MakerException {
		logger.info("Get maker by id = {}", id);
		
		Maker maker = repository.findById(id)
				.orElseThrow(()-> new MakerException("Cann't find the car id = " + id));
		
		MakerDto makerDto = mapper.makerToMakerDto(maker, context);
		logger.info("OUT get maker = {}", makerDto);
		logger.info("-------------------------------------------");
		return makerDto;
	}
	
	public MakerDto getByName(String name) throws MakerException {
		logger.info("Get maker by name = {}", name);
		
		Maker maker = repository.findByName(name)
				.orElseThrow(()-> new MakerException("Cann't find the car id = " + name));
		
		MakerDto makerDto = mapper.makerToMakerDto(maker, context);
		logger.info("OUT get maker = {}", makerDto);
		logger.info("-------------------------------------------");
		return makerDto;
	}
	
	@Transactional(readOnly = false)
	public boolean delet(MakerDto maker) {
		logger.info("Delet maker = {}", maker);
		
		repository.deleteById(maker.getId());
		
		boolean delet = repository.existsById(maker.getId());
		
		logger.info("OUT result delet maker = {}", delet);
		logger.info("-------------------------------------------");
		return delet;
	}
	
	@Transactional(readOnly = false)
	public MakerDto update (MakerDto maker) throws MakerException {
		logger.info("Update category = {}", maker);
		Maker makerDao = mapper.makerDtoToMaker(maker, context);
		
		Maker makerTemp = repository.findById(maker.getId())
				.orElseThrow(()-> new MakerException("Cann't find car id = " + maker.getId()));
		
		makerTemp.setCar(makerDao.getCar());
		
		Maker makerResult = repository.saveAndFlush(makerTemp);
		
		MakerDto makerDto = mapper.makerToMakerDto(makerResult, context);
		logger.info("OUT update category = {}", makerDto);
		logger.info("-------------------------------------------");
		return makerDto;
	}
}
