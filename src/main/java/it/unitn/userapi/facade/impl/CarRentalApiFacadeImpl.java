package it.unitn.userapi.facade.impl;

import it.unitn.userapi.carrentalapi.client.CarsApi;
import it.unitn.userapi.carrentalapi.client.ReservationsApi;
import it.unitn.userapi.carrentalapi.model.*;
import it.unitn.userapi.facade.CarRentalApiFacade;
import it.unitn.userapi.facade.HttpClientErrorsAwareResponse;
import it.unitn.userapi.facade.RestInvoker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class CarRentalApiFacadeImpl implements CarRentalApiFacade {

    private final CarsApi carsApi;
    private final ReservationsApi reservationsApi;

    @Override
    public HttpClientErrorsAwareResponse<CarsPaginationResponseModel> searchCars(String brand,
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
                                                                                 Integer size) {
        HttpClientErrorsAwareResponse<CarsPaginationResponseModel> awareResponse =
                new RestInvoker<CarsPaginationResponseModel>().invoke(() ->
                        carsApi.searchCarsWithHttpInfo(
                                brand, model, carType, fuelType, isGearboxAutomatic, seatsMin, seatsMax,
                                yearMin, yearMax, dayPriceMin, dayPriceMax, startDate, endDate, place,
                                sortBy, sortDirection, page, size
                        )
                );

        return HttpClientErrorsAwareResponse.<CarsPaginationResponseModel>builder()
                .statusCode(awareResponse.getStatusCode())
                .body(awareResponse.getBody())
                .error(awareResponse.getError())
                .build();
    }

    @Override
    public HttpClientErrorsAwareResponse<CarModel> getCar(Long id) {
        HttpClientErrorsAwareResponse<CarModel> awareResponse =
                new RestInvoker<CarModel>().invoke(() ->
                        carsApi.getCarWithHttpInfo(id, "EUR")
                );

        return HttpClientErrorsAwareResponse.<CarModel>builder()
                .statusCode(awareResponse.getStatusCode())
                .body(awareResponse.getBody())
                .error(awareResponse.getError())
                .build();
    }

    @Override
    public HttpClientErrorsAwareResponse<ReservationModel> getReservation(Long id) {
        HttpClientErrorsAwareResponse<ReservationModel> awareResponse =
                new RestInvoker<ReservationModel>().invoke(() ->
                        reservationsApi.getReservationWithHttpInfo(id)
                );

        return HttpClientErrorsAwareResponse.<ReservationModel>builder()
                .statusCode(awareResponse.getStatusCode())
                .body(awareResponse.getBody())
                .error(awareResponse.getError())
                .build();
    }

    @Override
    public HttpClientErrorsAwareResponse<ReservationsPaginationResponseModel> getReservationsByUserId(Long customerExternalId,
                                                                                                      Long carId,
                                                                                                      LocalDate startDate,
                                                                                                      LocalDate endDate,
                                                                                                      ReservationsSortColumn sortBy,
                                                                                                      SortDirection sortDirection,
                                                                                                      Integer page,
                                                                                                      Integer size) {
        HttpClientErrorsAwareResponse<ReservationsPaginationResponseModel> awareResponse =
                new RestInvoker<ReservationsPaginationResponseModel>().invoke(() ->
                        reservationsApi.searchReservationsWithHttpInfo(
                                customerExternalId,
                                carId, startDate, endDate,
                                sortBy, sortDirection, page, size
                        )
                );

        return HttpClientErrorsAwareResponse.<ReservationsPaginationResponseModel>builder()
                .statusCode(awareResponse.getStatusCode())
                .body(awareResponse.getBody())
                .error(awareResponse.getError())
                .build();
    }

    @Override
    public HttpClientErrorsAwareResponse<ReservationModel> makeReservation(ReservationRequestModel reservationRequestModel) {
        HttpClientErrorsAwareResponse<ReservationModel> awareResponse =
                new RestInvoker<ReservationModel>().invoke(() ->
                        reservationsApi.addReservationWithHttpInfo(reservationRequestModel)
                );

        return HttpClientErrorsAwareResponse.<ReservationModel>builder()
                .statusCode(awareResponse.getStatusCode())
                .body(awareResponse.getBody())
                .error(awareResponse.getError())
                .build();
    }

    @Override
    public HttpClientErrorsAwareResponse<ReservationModel> editReservation(Long id, ReservationRequestModel reservationRequestModel) {
        HttpClientErrorsAwareResponse<ReservationModel> awareResponse =
                new RestInvoker<ReservationModel>().invoke(() ->
                        reservationsApi.updateReservationWithHttpInfo(id, reservationRequestModel)
                );

        return HttpClientErrorsAwareResponse.<ReservationModel>builder()
                .statusCode(awareResponse.getStatusCode())
                .body(awareResponse.getBody())
                .error(awareResponse.getError())
                .build();
    }

    @Override
    public HttpClientErrorsAwareResponse<Void> deleteReservation(Long id) {
        HttpClientErrorsAwareResponse<Void> awareResponse =
                new RestInvoker<Void>().invoke(() ->
                        reservationsApi.deleteReservationWithHttpInfo(id)
                );

        return HttpClientErrorsAwareResponse.<Void>builder()
                .statusCode(awareResponse.getStatusCode())
                .body(awareResponse.getBody())
                .error(awareResponse.getError())
                .build();
    }
}
