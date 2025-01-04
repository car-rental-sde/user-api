package it.unitn.userapi.facade;

import it.unitn.userapi.carrentalapi.model.*;

public interface CarRentalApiFacade {
    HttpClientErrorsAwareResponse<CarsPaginationResponseModel> searchCars(Long id);
    HttpClientErrorsAwareResponse<CarModel> getCar(Long id);
    HttpClientErrorsAwareResponse<ReservationModel> getReservation(Long id);
    HttpClientErrorsAwareResponse<ReservationsPaginationResponseModel> getReservationsByUserId(Long id); // But do i have this data in rental api?
    HttpClientErrorsAwareResponse<ReservationModel> makeReservation(ReservationRequestModel reservationRequestModel);
    HttpClientErrorsAwareResponse<ReservationModel> editReservation(Long id, ReservationRequestModel reservationRequestModel);
    HttpClientErrorsAwareResponse<Void> deleteReservation(Long id);

    // get all internal - not sure
}
