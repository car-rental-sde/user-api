package it.unitn.userapi.facade;

import it.unitn.userapi.carrentalapi.model.CarsPaginationResponseModel;

public interface CarRentalApiFacade {
    HttpClientErrorsAwareResponse<CarsPaginationResponseModel> searchCars(Long id);
}
