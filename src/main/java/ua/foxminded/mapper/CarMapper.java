package ua.foxminded.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;

import ua.foxminded.dto.CarDto;
import ua.foxminded.entity.Car;

@Mapper(componentModel = "spring")
public interface CarMapper {
	
	CarDto carToCarDto (Car car, @Context CycleAvoidingMappingContext context);
	
	Car carDtoToCar(CarDto car, @Context CycleAvoidingMappingContext context);

}
