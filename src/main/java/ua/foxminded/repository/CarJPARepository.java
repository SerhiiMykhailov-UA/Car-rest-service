package ua.foxminded.repository;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.foxminded.entity.Car;

public interface CarJPARepository extends JpaRepository<Car, UUID> {
	
	boolean existsByObjectId(String objectId);
	
	boolean existsByNameAndYear(String name, Date year);
	
	Optional<Car> findByObjectId(String objectId);
	
	Optional<Car> findByNameAndYear(String name, Date year);
	
	boolean deleteByObjectId (String objectId);

}
