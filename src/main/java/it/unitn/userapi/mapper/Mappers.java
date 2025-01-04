package it.unitn.userapi.mapper;

import it.unitn.userapi.entity.ReservationEntity;
import it.unitn.userapi.openapi.model.ReservationModel;
import it.unitn.userapi.openapi.model.ReservationRequestModel;
import it.unitn.userapi.openapi.model.ReservationsSortColumn;
import it.unitn.userapi.openapi.model.SortDirection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface Mappers {
    it.unitn.userapi.carrentalapi.model.ReservationRequestModel toCarRentalApiReservationRequestModel(ReservationRequestModel entity);
    ReservationEntity toReservationEntity(it.unitn.userapi.carrentalapi.model.ReservationModel entity);
    ReservationModel toReservationModel(ReservationEntity entity);
    it.unitn.userapi.carrentalapi.model.ReservationsSortColumn toCarRentalApiReservationsSortColumn(ReservationsSortColumn entity);
    it.unitn.userapi.carrentalapi.model.SortDirection toCarRentalApiSortDirection(SortDirection entity);
}
