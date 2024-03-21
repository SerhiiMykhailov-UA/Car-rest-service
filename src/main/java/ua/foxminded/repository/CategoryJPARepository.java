package ua.foxminded.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.foxminded.entity.Category;

public interface CategoryJPARepository extends JpaRepository<Category, UUID> {

	boolean existsByName (String name);
	
	Optional<Category> findByName (String name);
	
	boolean deleteByName (String name);
}
