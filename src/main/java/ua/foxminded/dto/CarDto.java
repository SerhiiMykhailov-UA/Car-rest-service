package ua.foxminded.dto;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ua.foxminded.entity.Maker;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
public class CarDto {


	private UUID id;
	@NonNull
	private String objectId;
	@NonNull
	private String name;

	private int year;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private List<CategoryDto> category;

	@JsonBackReference
	private Maker maker;
}
