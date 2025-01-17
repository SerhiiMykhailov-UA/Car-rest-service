package ua.foxminded.dto;

import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
public class CarDto {


	private UUID id;
	
	@NotBlank(message = "ObjectId shouldn't be empty")
	@NonNull
	private String objectId;
	
	@NotBlank(message = "Name shouldn't be empty")
	@Size(min = 2, max = 255, message = "Name should be between 2 and 255 characters")
	@NonNull
	private String name;
	
	@NotBlank(message = "Name shouldn't be empty")
	@Size(min = 1900, max = 2500, message = "Year should be between 1900 and 2500")
	@NonNull
	private int year;

	@NotBlank(message = "Category shouldn't be empty")
	@NonNull
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private List<CategoryDto> category;

	@NotEmpty(message = "Maker shouldn't be empty")
	@NonNull
	private MakerDto maker;
}
