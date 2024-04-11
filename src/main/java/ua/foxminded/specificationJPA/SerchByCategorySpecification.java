package ua.foxminded.specificationJPA;

import javax.persistence.criteria.Join;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import ua.foxminded.entity.Car;
import ua.foxminded.entity.Maker;

@Component
public class SerchByCategorySpecification implements Command<Car>{
	
	@Override
	public Specification<Car> execute(SearchCriteria searchCriteria) {
		return (root, query, criteriaBuilder)->{
			Join<Car, Maker> carJoin = root.join("category");
			return criteriaBuilder.equal(carJoin.get("name"), searchCriteria.getCategory());
		};
	}

	@Override
	public String keyCommand() {
		return "category";
	}


}
