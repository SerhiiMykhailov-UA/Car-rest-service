package ua.foxminded.specificationJPA;

import javax.persistence.criteria.Join;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import ua.foxminded.entity.Car;
import ua.foxminded.entity.Maker;

@Component
public class SearchByMakerSpecification implements Command<Car>{
	
	private SearchCriteria searchCriteria;
	

	public SearchByMakerSpecification(SearchCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	@Override
	public Specification<Car> execute() {
		return (root, query, criteriaBuilder) -> {
			Join<Car, Maker> carsMaker = root.join("maker");
			return criteriaBuilder.equal(carsMaker.get("name"), searchCriteria.getMaker());
		};
	}

	@Override
	public String keyCommand() {
		return "maker";
	}

}
