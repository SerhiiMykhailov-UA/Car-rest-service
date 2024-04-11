package ua.foxminded.specificationJPA;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import ua.foxminded.entity.Car;

@Component
public class SearchByYearMinSpecification implements Command<Car> {
	
	@Override
	public Specification<Car> execute(SearchCriteria searchCriteria) {
		return (root, query, criteriaBuilder) ->
		criteriaBuilder.greaterThanOrEqualTo(root.get("year"), searchCriteria.getYearMin());
	}

	@Override
	public String keyCommand() {
		return "yearMin";
	}

}
