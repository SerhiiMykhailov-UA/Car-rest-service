package ua.foxminded.specificationJPA;

import org.springframework.data.jpa.domain.Specification;

public interface Command<T> {

	public Specification<T> execute();
	
	public String keyCommand();
}
