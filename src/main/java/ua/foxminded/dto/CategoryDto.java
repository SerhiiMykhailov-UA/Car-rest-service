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

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class CategoryDto {

	private UUID id;

	@NonNull
	private String name;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@JsonBackReference
	private List<CarDto> car;
}
