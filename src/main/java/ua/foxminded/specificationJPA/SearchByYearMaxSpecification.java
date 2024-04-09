package ua.foxminded.specificationJPA;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import ua.foxminded.entity.Car;

@Component
public class SearchByYearMaxSpecification implements Command<Car> {
	
	private SearchCriteria searchCriteria;
	

	public SearchByYearMaxSpecification(SearchCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	@Override
	public Specification<Car> execute() {
		return (root, query, criteriaBuilder) ->
		criteriaBuilder.lessThan(root.get("year"), searchCriteria.getYearMax());
	}

	@Override
	public String keyCommand() {
		return "yearMax";
	}

}
