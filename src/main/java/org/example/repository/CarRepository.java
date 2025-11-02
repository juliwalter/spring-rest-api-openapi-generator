package org.example.repository;

import org.example.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * The repository managing the {@link Car} entity in the database
 *
 * @author julianwalter
 */
@Repository
public interface CarRepository extends JpaRepository<Car, UUID> {
}
