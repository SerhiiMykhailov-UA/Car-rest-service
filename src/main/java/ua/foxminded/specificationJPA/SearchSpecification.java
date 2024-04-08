package ua.foxminded.specificationJPA;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import ua.foxminded.entity.Car;
import ua.foxminded.entity.Category;
import ua.foxminded.entity.Maker;

public class SearchSpecification implements Specification<Car> {
	
    public static Specification<Car> carByCategory(Category category) {
        return (root, query, criteriaBuilder) -> {
          Join<Car, Category> carsCategory = root.join("category");
          return criteriaBuilder.equal(carsCategory.get("name"), category.getName());
          };
    }
    
    public static Specification<Car> carByMaker(Maker maker) {
		return (root, query, criteriaBuilder) -> {
			Join<Car, Maker> carsMaker = root.join("maker");
			return criteriaBuilder.equal(carsMaker.get("name"), maker.getName());
		};
	}

    public static Specification<Car> carByModel(String name) {
		return (root, query, criteriaBuilder) -> 
			criteriaBuilder.like(root.get("name"), "%" + name + "%");
		
	}
    
    public static Specification<Car> carByYearMAX(int yearMax) {
		return (root, query, criteriaBuilder) ->
			criteriaBuilder.lessThan(root.get("year"), yearMax);
	}
    
    public static Specification<Car> carByYearMIN(int yearMin) {
		return (root, query, criteriaBuilder) ->
			criteriaBuilder.greaterThan(root.get("year"), yearMin);
	}
    
    public static Specification<Car> carByYearMIN(int yearMax, int yearMin) {
		return (root, query, criteriaBuilder) ->
			criteriaBuilder.between(root.get("year"), yearMax, yearMin);
	}

	@Override
	public Predicate toPredicate(Root<Car> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		// TODO Auto-generated method stub
		return null;
	}
}
