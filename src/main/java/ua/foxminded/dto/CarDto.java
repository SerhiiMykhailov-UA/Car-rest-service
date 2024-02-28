package ua.foxminded.dto;

import java.util.Date;
import java.util.List;

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


	private long id;
	@NonNull
	private String objectId;
	@NonNull
	private String name;
	@NonNull
	private Date year;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private List<CategoryDto> category;

	private Maker maker;
}
