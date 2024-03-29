package ua.foxminded.service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
	public MakerDto add (MakerDto maker) throws MakerException {
		logger.info("Add new maker = {}", maker);
		Maker makerDao = mapper.makerDtoToMaker(maker, context);
		Maker makerResult = new Maker();
		if (!repository.existsByName(maker.getName())) {
			makerResult = repository.saveAndFlush(makerDao);
		} else {
			logger.info("OUT Error : Maker : {} already exists", maker);
			throw new MakerException("Maker : " + maker.getName() + " - already exists");
		}
		MakerDto makerDto = mapper.makerToMakerDto(makerResult, context);
		logger.info("OUT new Maker = {}", makerDto);
		logger.info("---------------------------------------");
		return makerDto;
	}
	
	public List<MakerDto> getAll(int page, int size){
		logger.info("Get all makers");
		
		List<MakerDto> makers = repository.findAll(PageRequest.of(page, size, Sort.by("name").ascending()))
				.stream()
				.map(el->mapper.makerToMakerDto(el, context))
				.sorted(Comparator.comparing(MakerDto::getName))
				.collect(Collectors.toList());
		
		logger.info("OUT list of makers = {}", makers);
		logger.info("-------------------------------------------");
		return makers;
	}	
	
	public MakerDto get(UUID id) throws MakerException {
		logger.info("Get maker by id = {}", id);
		if (id  == null) {
			throw new MakerException("Field : id shouldn't be NULL");
		}
		
		Maker maker = repository.findById(id)
				.orElseThrow(()-> new MakerException("Cann't find the car id = " + id));
		
		MakerDto makerDto = mapper.makerToMakerDto(maker, context);
		logger.info("OUT get Maker = {}", makerDto);
		logger.info("-------------------------------------------");
		return makerDto;
	}
	
	public MakerDto getByName(String name) throws MakerException {
		logger.info("Get maker by name = {}", name);
		
		Maker maker = repository.findByName(name)
				.orElseThrow(()-> new MakerException("Cann't find maker = " + name));
		
		MakerDto makerDto = mapper.makerToMakerDto(maker, context);
		logger.info("OUT get Maker = {}", makerDto);
		logger.info("-------------------------------------------");
		return makerDto;
	}
	
	@Transactional(readOnly = false)
	public boolean delet(MakerDto maker) throws MakerException {
		UUID id = maker.getId();
		logger.info("Delet maker = {}", maker);
		if (id  == null) {
			throw new MakerException("Field : id shouldn't be NULL");
		}
		
		if (repository.existsById(id)) {
			repository.deleteById(maker.getId());
		}else {
			throw new MakerException("Maker id = " + id + " wasn't found");
		}
		
		
		boolean deleteCheck = repository.existsById(maker.getId());
		
		if(deleteCheck){
			throw new MakerException("Delet wasn't seccessful. Contact administrator");
		}
		
		logger.info("OUT result delet Maker = {}", true);
		logger.info("-------------------------------------------");
		return true;
	}
	
	@Transactional(readOnly = false)
	public MakerDto updateName (MakerDto maker) throws MakerException {
		UUID id = maker.getId();
		logger.info("Update Maker's name by id = {}", id);
		Maker makerDao = mapper.makerDtoToMaker(maker, context);
		if (id  == null) {
			throw new MakerException("Field : id shouldn't be NULL");
		}
		
		Maker makerTemp = repository.findById(id)
				.orElseThrow(()-> new MakerException("Cann't find Maker id = " + id));
		
		
		makerTemp.setName(makerDao.getName());
		
		Maker makerResult = repository.saveAndFlush(makerTemp);
		
		MakerDto makerDto = mapper.makerToMakerDto(makerResult, context);
		logger.info("OUT update Maker = {}", makerDto);
		logger.info("-------------------------------------------");
		return makerDto;
	}
	
	@Transactional(readOnly = false)
	public MakerDto updateCars (MakerDto maker) throws MakerException {
		UUID id = maker.getId();
		logger.info("Update Maker's cars by id = {}", id);
		Maker makerDao = mapper.makerDtoToMaker(maker, context);
		if (id  == null) {
			throw new MakerException("Field : id shouldn't be NULL");
		}
		
		Maker makerTemp = repository.findById(id)
				.orElseThrow(()-> new MakerException("Cann't find Maker id = " + id));
		
		
		makerTemp.setCar(makerDao.getCar());
		
		Maker makerResult = repository.saveAndFlush(makerTemp);
		
		MakerDto makerDto = mapper.makerToMakerDto(makerResult, context);
		logger.info("OUT update Maker = {}", makerDto);
		logger.info("-------------------------------------------");
		return makerDto;
	}
}
