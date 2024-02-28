package ua.foxminded.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;

import ua.foxminded.dto.CategoryDto;
import ua.foxminded.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
	
	CategoryDto categoryToCategoryDto(Category category, @Context CycleAvoidingMappingContext context);
	
	Category categoryDtoToCategory(CategoryDto category, @Context CycleAvoidingMappingContext context);

}
