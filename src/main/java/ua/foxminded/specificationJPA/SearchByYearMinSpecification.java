package ua.foxminded.specificationJPA;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import ua.foxminded.entity.Car;

@Component
public class SearchByYearMinSpecification implements Command<Car> {
	
	private SearchCriteria searchCriteria;
	

	public SearchByYearMinSpecification(SearchCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	@Override
	public Specification<Car> execute() {
		return (root, query, criteriaBuilder) ->
		criteriaBuilder.greaterThan(root.get("year"), searchCriteria.getYearMin());
	}

	@Override
	public String keyCommand() {
		return "yearMin";
	}

}
