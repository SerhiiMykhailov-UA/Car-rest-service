package ua.foxminded.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.foxminded.entity.Maker;

public interface MakerJPARepository extends JpaRepository<Maker, Long> {
	
	boolean existsByName (String name);
	
	Optional<Maker> findByName (String name);
	
	boolean deleteByName (String name);

}
