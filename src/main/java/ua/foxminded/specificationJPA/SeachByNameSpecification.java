package ua.foxminded.specificationJPA;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import ua.foxminded.entity.Car;

@Component
public class SeachByNameSpecification implements Command<Car> {
	
	

	@Override
	public Specification<Car> execute(SearchCriteria searchCriteria) {
		return (root, query, criteriaBuilder) -> 
		criteriaBuilder.like(root.get("name"), "%" + searchCriteria.getName() + "%");
	}

	@Override
	public String keyCommand() {
		return "name";
	}

}
