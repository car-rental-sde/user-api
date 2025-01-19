package it.unitn.userapi.service;

import it.unitn.userapi.openapi.model.ReservationModel;
import it.unitn.userapi.openapi.model.ReservationRequestModel;
import it.unitn.userapi.openapi.model.ReservationsSortColumn;
import it.unitn.userapi.openapi.model.SortDirection;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.Optional;

public interface ReservationService {
    Page<ReservationModel> searchReservations(Long carId,
                                               LocalDate startDate,
                                               LocalDate endDate,
                                               ReservationsSortColumn sortBy,
                                               SortDirection sortDirection,
                                               Integer page,
                                               Integer size);
    Optional<ReservationModel> addReservation(ReservationRequestModel putReservationDto);
    Optional<ReservationModel> getReservation(Long id, String currency);
    void deleteReservation(Long id);
    Optional<ReservationModel> updateReservation(Long id, ReservationRequestModel putReservationDto);
}
