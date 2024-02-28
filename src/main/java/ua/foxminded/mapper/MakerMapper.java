package ua.foxminded.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;

import ua.foxminded.dto.MakerDto;
import ua.foxminded.entity.Maker;

@Mapper(componentModel = "spring")
public interface MakerMapper {

	MakerDto makerToMakerDto(Maker maker, @Context CycleAvoidingMappingContext context);
	
	Maker makerDtoToMaker(MakerDto maker, @Context CycleAvoidingMappingContext context);
}
