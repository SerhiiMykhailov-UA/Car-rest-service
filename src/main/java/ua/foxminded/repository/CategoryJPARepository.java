package ua.foxminded.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.foxminded.entity.Category;

public interface CategoryJPARepository extends JpaRepository<Category, Long> {

	boolean existsByName (String name);
	
	Optional<Category> findByName (String name);
	
	boolean deleteByName (String name);
}
