package org.example.api.mapper;

import org.example.api.dto.CarDto;
import org.example.model.Car;
import org.springframework.stereotype.Component;

/**
 * Mapper class to map {@link CarDto} to {@link Car} and vice versa
 *
 * @author julianwalter
 */
@Component
public class CarMapper {

    /**
     * Maps a {@link CarDto} entity to {@link Car}
     *
     * @param carDto the CarDto to be mapped to Car
     * @return the mapped Car
     */
    public Car fromCarDto(CarDto carDto) {
        return Car.builder()
                .id(carDto.getId())
                .make(carDto.getMake().getValue())
                .model(carDto.getModel())
                .mileage(carDto.getMileage())
                .build();
    }

    /**
     * Maps a {@link Car} entity to {@link CarDto}
     * @param car the Car to be mapped to CarDto
     * @return the mapped CarDto
     */
    public CarDto fromCar(Car car) {
        return CarDto.builder()
                .id(car.getId())
                .make(mapMakeEnum(car.getMake()))
                .model(car.getModel())
                .mileage(car.getMileage())
                .build();
    }

    private CarDto.MakeEnum mapMakeEnum(String make) {
        return switch (make) {
            case "mercedes", "bmw", "audi", "vw" -> CarDto.MakeEnum.fromValue(make);
            default -> CarDto.MakeEnum.OTHER;
        };
    }
}
