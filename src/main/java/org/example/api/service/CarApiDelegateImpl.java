package org.example.api.service;

import lombok.RequiredArgsConstructor;
import org.example.api.controller.CarApiDelegate;
import org.example.api.dto.CarDto;
import org.example.api.dto.MultiCarResponse;
import org.example.api.dto.SingleCarResponse;
import org.example.api.mapper.CarMapper;
import org.example.model.Car;
import org.example.service.CarService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service class implementing the REST API Endpoint of the car api
 *
 * @author julianwalter
 */
@Service
@RequiredArgsConstructor
public class CarApiDelegateImpl implements CarApiDelegate {

    private final CarMapper mapper;
    private final CarService service;

    @Override
    public ResponseEntity<MultiCarResponse> getCars(Integer page, Integer size, String sort) {
        // create a page request based on the given query parameters
        PageRequest pageRequest = getPageRequest(page, size, sort);

        // fetch all cars from the database and map them to dto
        List<CarDto> cars = service.getCars(pageRequest)
                .stream()
                .map(mapper::fromCar)
                .toList();

        // build response and return it to the client
        return ResponseEntity.ok(MultiCarResponse.builder()
                .cars(cars)
                .description("Successfully fetched cars")
                .build());
    }

    private PageRequest getPageRequest(Integer page, Integer size, String sort) {
        Sort.Direction direction = "desc".equals(sort) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return PageRequest.of(page, size, direction, "createdAt");
    }

    @Override
    public ResponseEntity<SingleCarResponse> createCar(CarDto car) {
        // map dto to entity
        Car carEntity = mapper.fromCarDto(car);

        // save the entity to the database
        Car savedCar = service.createCar(carEntity);

        // build the response and return it to the client
        SingleCarResponse response = SingleCarResponse.builder()
                .car(mapper.fromCar(savedCar))
                .description("Successfully created car entity")
                .build();
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(201));
    }

    @Override
    public ResponseEntity<SingleCarResponse> getCar(UUID carId) {
        // fetch the car entity by the given id and map it to dto
        CarDto car = service.getCar(carId)
                .map(mapper::fromCar)
                .orElse(null);

        // if no car was found in the database return 404 not found
        if (car == null) {
            SingleCarResponse response = SingleCarResponse.builder()
                    .description("No car found for id %s".formatted(carId))
                    .build();
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(404));

        }

        // if a car was found return 200 and the mapped car dto
        return ResponseEntity.ok(SingleCarResponse.builder()
                .description("Successfully fetched car from repo")
                .car(car)
                .build());
    }

    @Override
    public ResponseEntity<SingleCarResponse> editCar(CarDto carDto) {
        try {
            // map dto to entity and update entity
            Car mappedCar = mapper.fromCarDto(carDto);
            Car savedCar = service.updateCar(mappedCar);

            // map back to dto and return result
            CarDto returnDto = mapper.fromCar(savedCar);
            return ResponseEntity.ok(SingleCarResponse.builder()
                    .car(returnDto)
                    .description("Successfully updated car with id %s".formatted(savedCar.getId()))
                    .build());
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(SingleCarResponse.builder()
                    .description(e.getMessage())
                    .build());
        }

    }

    @Override
    public ResponseEntity<SingleCarResponse> deleteCar(UUID carId) {
        // try to delete car from database
        Car car = service.deleteCar(carId);

        // if no car was returned, the provided id couldn't be found in the database
        if (car == null) {
            SingleCarResponse response = SingleCarResponse.builder()
                    .description("No car defined for id %s".formatted(carId))
                    .build();
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(404));
        }

        // else build 200 response containing the car entity
        return ResponseEntity.ok(SingleCarResponse.builder()
                .car(mapper.fromCar(car))
                .description("Successfully deleted car with id %s".formatted(carId))
                .build());
    }
}
