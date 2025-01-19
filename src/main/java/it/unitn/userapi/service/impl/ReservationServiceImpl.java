package it.unitn.userapi.service.impl;

import it.unitn.userapi.api.error.AlternativeHttpStatusCodeResponse;
import it.unitn.userapi.carrentalapi.model.CustomerRequestModel;
import it.unitn.userapi.carrentalapi.model.ReservationsPaginationResponseModel;
import it.unitn.userapi.entity.UserEntity;
import it.unitn.userapi.facade.CarRentalApiFacade;
import it.unitn.userapi.facade.HttpClientErrorsAwareResponse;
import it.unitn.userapi.mapper.Mappers;
import it.unitn.userapi.openapi.model.ReservationModel;
import it.unitn.userapi.openapi.model.ReservationRequestModel;
import it.unitn.userapi.openapi.model.ReservationsSortColumn;
import it.unitn.userapi.openapi.model.SortDirection;
import it.unitn.userapi.repository.UserRepository;
import it.unitn.userapi.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final UserRepository userRepository;
    private final Mappers mappers;
    private final CarRentalApiFacade carRentalApiFacade;

    @Override
    public Page<ReservationModel> searchReservations(Long carId,
                                                      LocalDate startDate,
                                                      LocalDate endDate,
                                                      ReservationsSortColumn sortBy,
                                                      SortDirection sortDirection,
                                                      Integer page,
                                                      Integer size) {


        if (startDate == null) {
            startDate = LocalDate.of(1900, 1, 1);
        }
        if (endDate == null) {
            endDate = LocalDate.of(2500, 1, 1);
        }

        if (sortBy == null) {
            sortBy = ReservationsSortColumn.ID;
        }
        if (sortDirection == null) {
            sortDirection = SortDirection.ASC;
        }

        String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Long userId = userRepository.findByUsername(username).map(UserEntity::getId)
                        .orElseThrow(() -> new RuntimeException("User not found"));

        HttpClientErrorsAwareResponse<ReservationsPaginationResponseModel> response = carRentalApiFacade.getReservationsByUserId(
                userId, carId, startDate, endDate,
                mappers.toCarRentalApiReservationsSortColumn(sortBy),
                mappers.toCarRentalApiSortDirection(sortDirection),
                page, size);

        if (response.isSuccess()) {
            List<ReservationModel> reservations = response.getBody().getReservations().stream()
                    .map(mappers::toReservationModel)
                    .toList();

            // Return a Page object constructed with retrieved data
            return new PageImpl<>(
                    reservations,
                    PageRequest.of(response.getBody().getPageNumber(), response.getBody().getPageSize(),
                            Sort.by(sortDirection == SortDirection.ASC ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy.name())),
                    response.getBody().getTotalRecords()
            );
        } else {
            return Page.empty();
        }
    }

    @Override
    public Optional<ReservationModel> addReservation(ReservationRequestModel reservationRequest) {

        it.unitn.userapi.carrentalapi.model.ReservationRequestModel reservationRequestModel = mappers.toCarRentalApiReservationRequestModel(reservationRequest);

        String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        userRepository.findByUsername(username).ifPresent(user -> reservationRequestModel.setCustomer(toCustomerModel(user)));

        HttpClientErrorsAwareResponse<it.unitn.userapi.carrentalapi.model.ReservationModel> response =
                carRentalApiFacade.makeReservation(reservationRequestModel);

        if (response.isSuccess()) {
            return Optional.of(mappers.toReservationModel(response.getBody()));
        }

        return Optional.empty();
    }

    @Override
    public Optional<ReservationModel> getReservation(Long id) {
        HttpClientErrorsAwareResponse<it.unitn.userapi.carrentalapi.model.ReservationModel> response =
                carRentalApiFacade.getReservation(id);

        if (response.isSuccess()) {
            return Optional.of(mappers.toReservationModel(response.getBody()));
        }

        return Optional.empty();
    }

    @Override
    public void deleteReservation(Long id) {
        HttpClientErrorsAwareResponse<Void> response = carRentalApiFacade.deleteReservation(id);

        if (!response.isSuccess()) {
            log.error("Failed to delete reservation with id: [{}]", id);
            throw new AlternativeHttpStatusCodeResponse(HttpStatus.I_AM_A_TEAPOT, null);
        }
    }

    @Override
    public Optional<ReservationModel> updateReservation(Long id, ReservationRequestModel reservationRequest) {

        it.unitn.userapi.carrentalapi.model.ReservationRequestModel reservationRequestModel = mappers.toCarRentalApiReservationRequestModel(reservationRequest);

        String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        userRepository.findByUsername(username).ifPresent(user -> reservationRequestModel.setCustomer(toCustomerModel(user)));

        HttpClientErrorsAwareResponse<it.unitn.userapi.carrentalapi.model.ReservationModel> response =
                carRentalApiFacade.editReservation(id, reservationRequestModel);

        if (response.isSuccess()) {
            return Optional.of(mappers.toReservationModel(response.getBody()));
        }

        return Optional.empty();

    }

    private CustomerRequestModel toCustomerModel(UserEntity user) {
        return new CustomerRequestModel()
            .externalId(user.getId())
            .name(user.getName())
            .surname(user.getSurname());
    }
}
