package it.unitn.userapi.facade.impl;

import it.unitn.userapi.carrentalapi.client.CarsApi;
import it.unitn.userapi.carrentalapi.model.CarModel;
import it.unitn.userapi.carrentalapi.model.CarsPaginationResponseModel;
import it.unitn.userapi.facade.CarRentalApiFacade;
import it.unitn.userapi.facade.HttpClientErrorsAwareResponse;
import it.unitn.userapi.facade.RestInvoker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CarRentalApiFacadeImpl implements CarRentalApiFacade {

    private final CarsApi carsApi;

    // TODO: Make it simple
    @Override
    public HttpClientErrorsAwareResponse<CarsPaginationResponseModel> searchCars(Long id) {
        HttpClientErrorsAwareResponse<CarsPaginationResponseModel> awareResponse =
                new RestInvoker<CarsPaginationResponseModel>().invoke(() ->
                        carsApi.searchCarsWithHttpInfo(
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null
                                )
                );

        return HttpClientErrorsAwareResponse.<CarsPaginationResponseModel>builder()
                .statusCode(awareResponse.getStatusCode())
                .body(awareResponse.getBody())
                .error(awareResponse.getError())
                .build();
//        return null;
    }
}
