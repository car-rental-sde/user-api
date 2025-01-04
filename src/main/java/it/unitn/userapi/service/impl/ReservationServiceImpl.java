package it.unitn.userapi.service.impl;

import it.unitn.userapi.carrentalapi.model.CustomerRequestModel;
import it.unitn.userapi.carrentalapi.model.ReservationModel;
import it.unitn.userapi.carrentalapi.model.ReservationsPaginationResponseModel;
import it.unitn.userapi.entity.ReservationEntity;
import it.unitn.userapi.entity.UserEntity;
import it.unitn.userapi.facade.CarRentalApiFacade;
import it.unitn.userapi.facade.HttpClientErrorsAwareResponse;
import it.unitn.userapi.mapper.Mappers;
import it.unitn.userapi.openapi.model.ReservationRequestModel;
import it.unitn.userapi.openapi.model.ReservationsSortColumn;
import it.unitn.userapi.openapi.model.SortDirection;
import it.unitn.userapi.repository.UserRepository;
import it.unitn.userapi.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public Page<ReservationEntity> searchReservations(Long carId,
                                                      LocalDate startDate,
                                                      LocalDate endDate,
                                                      String startPlace,
                                                      String endPlace,
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
        startPlace = addSqlWildcards(startPlace);
        endPlace = addSqlWildcards(endPlace);

        if (sortBy == null) {
            sortBy = ReservationsSortColumn.ID;
        }
        if (sortDirection == null) {
            sortDirection = SortDirection.ASC;
        }

        String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Long userId = userRepository.findByUsername(username).map(UserEntity::getId)
                        .orElseThrow(() -> new RuntimeException("User not found")); // TODO: catch?

        HttpClientErrorsAwareResponse<ReservationsPaginationResponseModel> response = carRentalApiFacade.getReservationsByUserId(
                userId, carId, startDate, endDate, startPlace, endPlace,
                mappers.toCarRentalApiReservationsSortColumn(sortBy),
                mappers.toCarRentalApiSortDirection(sortDirection),
                page, size);

        if (response.isSuccess()) {
            List<ReservationEntity> reservations = response.getBody().getReservations().stream()
                    .map(mappers::toReservationEntity)
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
    public Optional<ReservationEntity> addReservation(ReservationRequestModel reservationRequest) {

        it.unitn.userapi.carrentalapi.model.ReservationRequestModel reservationRequestModel = mappers.toCarRentalApiReservationRequestModel(reservationRequest);

        String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        userRepository.findByUsername(username).ifPresent(user -> reservationRequestModel.setCustomer(toCustomerModel(user)));

        HttpClientErrorsAwareResponse<ReservationModel> response = carRentalApiFacade.makeReservation(reservationRequestModel);

        if (response.isSuccess()) {
            return Optional.of(mappers.toReservationEntity(response.getBody()));
        }

        return Optional.empty();
    }

    @Override
    public Optional<ReservationEntity> getReservation(Long id) {
        HttpClientErrorsAwareResponse<ReservationModel> response = carRentalApiFacade.getReservation(id);

        if (response.isSuccess()) {
            return Optional.of(mappers.toReservationEntity(response.getBody()));
        }

        return Optional.empty();
    }

    @Override
    public void deleteReservation(Long id) {
        HttpClientErrorsAwareResponse<Void> response = carRentalApiFacade.deleteReservation(id);

        if (!response.isSuccess()) {
            log.error("Failed to delete reservation with id: [{}]", id);
            // TODO: throw exception?
        }
    }

    @Override
    public Optional<ReservationEntity> updateReservation(Long id, ReservationRequestModel reservationRequest) {

        it.unitn.userapi.carrentalapi.model.ReservationRequestModel reservationRequestModel = mappers.toCarRentalApiReservationRequestModel(reservationRequest);

        String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        userRepository.findByUsername(username).ifPresent(user -> reservationRequestModel.setCustomer(toCustomerModel(user)));

        HttpClientErrorsAwareResponse<ReservationModel> response = carRentalApiFacade.editReservation(id, reservationRequestModel);

        if (response.isSuccess()) {
            return Optional.of(mappers.toReservationEntity(response.getBody()));
        }

        return Optional.empty();

    }

    private String addSqlWildcards(String arg) {
        return StringUtils.defaultIfBlank(arg, "") + "%";
    }

    private CustomerRequestModel toCustomerModel(UserEntity user) {
        return new CustomerRequestModel()
            .externalId(user.getId())
            .name(user.getName())
            .surname(user.getSurname());
    }
}
