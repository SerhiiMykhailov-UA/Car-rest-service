package ua.foxminded.dto;

import java.util.UUID;

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
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class MakerDto {
	
	private UUID id;
	
	@NotEmpty(message = "Name shouldn't be empty")
	@Size(min = 2, max = 255, message = "Name should be between 2 and 255 characters")
	@NonNull
	private String name;
	
}
