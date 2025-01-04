package it.unitn.userapi.api;

import it.unitn.userapi.entity.ReservationEntity;
import it.unitn.userapi.mapper.Mappers;
import it.unitn.userapi.openapi.api.ReservationsApiDelegate;
import it.unitn.userapi.openapi.model.*;
import it.unitn.userapi.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationsApiImpl implements ReservationsApiDelegate {

    private final ReservationService reservationService;
    private final Mappers mappers;

    @Override
    public ResponseEntity<ReservationsPaginationResponseModel> searchReservations(Long carId,
                                                                                  LocalDate startDate,
                                                                                  LocalDate endDate,
                                                                                  String startPlace,
                                                                                  String endPlace,
                                                                                  ReservationsSortColumn sortBy,
                                                                                  SortDirection sortDirection,
                                                                                  Integer page,
                                                                                  Integer size) {

        log.debug("Searching reservations with carId: [{}], startDate: [{}], endDate: [{}], startPlace: [{}], " +
                        "endPlace: [{}], sortBy: [{}], sortDirection: [{}], page: [{}], size: [{}]",
                carId, startDate, endDate, startPlace, endPlace, sortBy, sortDirection, page, size);

        Page<ReservationModel> reservations = reservationService.searchReservations(carId, startDate, endDate, startPlace, endPlace, sortBy, sortDirection, page, size);

        ReservationsPaginationResponseModel response = new ReservationsPaginationResponseModel();
        response.setReservations(reservations.getContent());
        response.setPageNumber(reservations.getNumber() + 1);
        response.setPageSize(reservations.getSize());
        response.setTotalRecords(reservations.getTotalElements());

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ReservationModel> getReservation(Long id) {
        log.debug("Getting reservation with id: [{}]", id);

        Optional<ReservationModel> optionalReservation = reservationService.getReservation(id);

        return optionalReservation.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<ReservationModel> addReservation(ReservationRequestModel reservationRequest) {
        log.debug("Adding reservation with request: [{}]", reservationRequest);

        // TODO: If null then return specific code

        Optional<ReservationModel> reservationOptional = reservationService.addReservation(reservationRequest);
        return reservationOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Override
    public ResponseEntity<Void> deleteReservation(Long id) {
        log.debug("Deleting reservation with id: [{}]", id);

        reservationService.deleteReservation(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<ReservationModel> updateReservation(Long id, ReservationRequestModel reservationRequest) {
        log.debug("Updating reservation with id: [{}], request: [{}]", id, reservationRequest);

        Optional<ReservationModel> optionalReservation = reservationService.updateReservation(id, reservationRequest);

        return optionalReservation.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }
}
