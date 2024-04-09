package ua.foxminded.specificationJPA;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import ua.foxminded.entity.Car;

@Component
public class SearchByYearBetweenSpecification implements Command<Car> {
	
	private SearchCriteria searchCriteria;
	

	public SearchByYearBetweenSpecification(SearchCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	@Override
	public Specification<Car> execute() {
		return (root, query, criteriaBuilder) ->
		criteriaBuilder.between(root.get("year"), searchCriteria.getYearMax(), searchCriteria.getYearMin());
	}

	@Override
	public String keyCommand() {
		return "yearBetween";
	}

}
