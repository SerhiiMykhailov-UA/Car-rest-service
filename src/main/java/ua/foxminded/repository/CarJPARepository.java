package ua.foxminded.repository;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import ua.foxminded.entity.Car;

public interface CarJPARepository extends JpaRepository<Car, UUID>, JpaSpecificationExecutor<Car>, QueryByExampleExecutor<Car>{
	
	boolean existsByObjectId(String objectId);
	
	boolean existsByNameAndYear(String name, Date year);
	
	Optional<Car> findByObjectId(String objectId);
	
	Optional<Car> findByNameAndYear(String name, int year);
	
	boolean deleteByObjectId (String objectId);
	
	Page<Car> findByNameOrderByYear(String name, Pageable pageable);
	
	Page<Car> findAll (Pageable pageable);
	
}
