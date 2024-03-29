package ua.foxminded.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import ua.foxminded.entity.Car;
import ua.foxminded.entity.Maker;

public interface CarJPARepository extends JpaRepository<Car, UUID> {
	
	boolean existsByObjectId(String objectId);
	
	boolean existsByNameAndYear(String name, Date year);
	
	Optional<Car> findByObjectId(String objectId);
	
	Optional<Car> findByNameAndYear(String name, int year);
	
	boolean deleteByObjectId (String objectId);
	
	List<Car> findByNameOrderByYear(String name, Pageable pageable);
	
	Page<Car> findAll (Pageable pageable);
}
