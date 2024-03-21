package ua.foxminded.dto;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
public class MakerDto {
	
	private UUID id;
	
	@NonNull
	private String name;
	
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@JsonManagedReference
	private List<CarDto> car;

}
