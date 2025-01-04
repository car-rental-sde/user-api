package it.unitn.userapi.service.impl;

import it.unitn.userapi.carrentalapi.model.CustomerRequestModel;
import it.unitn.userapi.carrentalapi.model.ReservationModel;
import it.unitn.userapi.entity.ReservationEntity;
import it.unitn.userapi.entity.UserEntity;
import it.unitn.userapi.facade.CarRentalApiFacade;
import it.unitn.userapi.facade.HttpClientErrorsAwareResponse;
import it.unitn.userapi.mapper.Mappers;
import it.unitn.userapi.openapi.model.ReservationRequestModel;
import it.unitn.userapi.openapi.model.ReservationsSortColumn;
import it.unitn.userapi.openapi.model.SortDirection;
import it.unitn.userapi.repository.ReservationRepository;
import it.unitn.userapi.repository.UserRepository;
import it.unitn.userapi.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository; // TODO: Somehow i have to get my user, here or in the controller
//    private final EntityToModelMappers mappers;
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

        Pageable pageRequest;
        Sort.Direction direction = SortDirection.ASC.equals(sortDirection) ?
                Sort.Direction.ASC : Sort.Direction.DESC;
        pageRequest = switch (sortBy) {
            case BRAND -> PageRequest.of(page - 1, size, direction, "car.model.brand.name");
            case MODEL -> PageRequest.of(page - 1, size, direction, "car.model.name");
            case CAR_TYPE -> PageRequest.of(page - 1, size, direction, "car.model.carType.name");
            default -> PageRequest.of(page - 1, size, direction, sortBy.getValue());
        };

        // NO, get from facade
        return reservationRepository.searchReservations(carId, startDate, endDate, startPlace, endPlace, pageRequest);
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
        return reservationRepository.findById(id);
    }

    @Override
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    @Override
    public ReservationEntity updateReservation(Long id, ReservationRequestModel reservationRequest) {

//        ReservationEntity reservation = reservationRepository.getById(id);
//        reservation.setCar(carRepository.getById(reservationRequest.getCarId()));
//
//        reservation.setDetails(EntityToModelMappers.jsonNullableToString(reservationRequest.getDetails()));
//        reservation.setBeginDate(reservationRequest.getBeginDate());
//        reservation.setEndDate(reservationRequest.getEndDate());
//        reservation.setBeginPlace(reservationRequest.getBeginPlace());
//        reservation.setEndPlace(reservationRequest.getEndPlace());
//        reservation.setBeginPosition(reservationRequest.getBeginPosition());
//        reservation.setEndPosition(reservationRequest.getEndPosition());
//        reservation.setIsMaintenance(reservationRequest.getIsMaintenance());
//
//        // add or update customer
//        if (reservationRequest.getCustomer() != null) {
//            Optional<CustomerEntity> customerOptional = customerRepository.findByBooklyId(reservationRequest.getCustomer().getBooklyId());
//
//            if (customerOptional.isPresent()) {
//                CustomerEntity customer = customerOptional.get();
//                customer.setBooklyId(reservationRequest.getCustomer().getBooklyId());
//                customer.setName(reservationRequest.getCustomer().getName());
//                customer.setSurname(reservationRequest.getCustomer().getSurname());
//
//                customerRepository.save(customer);
//                reservation.setCustomer(customerRepository.getById(reservationRequest.getCustomer().getBooklyId()));
//            } else {
//                CustomerEntity newCustomer = new CustomerEntity();
//                newCustomer.setName(reservationRequest.getCustomer().getName());
//                newCustomer.setSurname(reservationRequest.getCustomer().getSurname());
//                newCustomer.setBooklyId(reservationRequest.getCustomer().getBooklyId());
//                newCustomer.setIsBlocked(false);
//                customerRepository.save(newCustomer);
//
//                reservation.setCustomer(customerRepository.getByBooklyId(newCustomer.getBooklyId()));
//            }
//
//
//        }
//        return reservationRepository.save(reservation);
        return null;
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
