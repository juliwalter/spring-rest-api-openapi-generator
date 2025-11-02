package org.example.service;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.example.model.Car;
import org.example.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class managing business logic for the {@link Car} entity
 *
 * @author julianwalter
 */
@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository repository;

    /**
     * Get a car entity by its id
     *
     * @param id the id to search for
     * @return the car to the given id
     */
    public Optional<Car> getCar(@Nonnull UUID id) {
        return repository.findById(id);
    }

    /**
     * Gets all car entities from the database
     *
     * @return the list of all cars in the database
     */
    public List<Car> getCars() {
        return repository.findAll();
    }

    /**
     * Creates a car in the database
     *
     * @param car the car to be created
     * @return the created car
     */
    public Car createCar(@Nonnull Car car) {
        // the id must be null when creating a entity to the database
        if (car.getId() != null) {
            throw new IllegalArgumentException("Cars id must be null when creating car");
        }
        // else save the car
        return repository.save(car);
    }

    /**
     * Updates a car in the database
     *
     * @param car the car to be updated
     * @return the updated car
     */
    public Car updateCar(@Nonnull Car car) {
        // if the car does not exist, the provided car is illegal input
        if (car.getId() == null || !repository.existsById(car.getId())) {
            throw new IllegalArgumentException("No car found to the provided id");
        }

        // else save the car
        return repository.save(car);
    }

    /**
     * Deletes a car by its id
     *
     * @param id the car identifier
     * @return the deleted car
     */
    public Car deleteCar(UUID id) {
        return getCar(id).map(car -> {
                    repository.delete(car);
                    return car;
                })
                .orElse(null);
    }
}
