package ua.foxminded.specificationJPA;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import ua.foxminded.entity.Car;

@Component
public class SearchInterface {

	private Map<String, Command<Car>> searchList;
	
	public SearchInterface (List<Command<Car>> commands) {
		this.searchList = new HashMap<>();
		for (Command<Car> command : commands) {
			searchList.put(command.keyCommand(), command);
		}
	}
	
	public Specification<Car> getSpecification(String s) {
		return searchList.get(s).execute();
	}
}
