package it.unitn.userapi.facade;

import it.unitn.userapi.carrentalapi.model.*;

import java.time.LocalDate;

public interface CarRentalApiFacade {
    HttpClientErrorsAwareResponse<CarsPaginationResponseModel> searchCars(String brand,
                                                                          String model,
                                                                          String carType,
                                                                          String fuelType,
                                                                          Boolean isGearboxAutomatic,
                                                                          Integer seatsMin,
                                                                          Integer seatsMax,
                                                                          Integer yearMin,
                                                                          Integer yearMax,
                                                                          Long dayPriceMin,
                                                                          Long dayPriceMax,
                                                                          LocalDate startDate,
                                                                          LocalDate endDate,
                                                                          String place,
                                                                          CarsSortColumn sortBy,
                                                                          SortDirection sortDirection,
                                                                          Integer page,
                                                                          Integer size);
    HttpClientErrorsAwareResponse<CarModel> getCar(Long id);
    HttpClientErrorsAwareResponse<ReservationModel> getReservation(Long id);
    HttpClientErrorsAwareResponse<ReservationsPaginationResponseModel> getReservationsByUserId(Long customerExternalId,
                                                                                               Long carId,
                                                                                               LocalDate startDate,
                                                                                               LocalDate endDate,
                                                                                               ReservationsSortColumn sortBy,
                                                                                               SortDirection sortDirection,
                                                                                               Integer page,
                                                                                               Integer size); // But do i have this data in rental api?
    HttpClientErrorsAwareResponse<ReservationModel> makeReservation(ReservationRequestModel reservationRequestModel);
    HttpClientErrorsAwareResponse<ReservationModel> editReservation(Long id, ReservationRequestModel reservationRequestModel);
    HttpClientErrorsAwareResponse<Void> deleteReservation(Long id);
}
