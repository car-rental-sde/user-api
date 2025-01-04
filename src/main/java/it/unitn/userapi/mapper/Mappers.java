package it.unitn.userapi.mapper;

import it.unitn.userapi.entity.ReservationEntity;
import it.unitn.userapi.openapi.model.ReservationModel;
import it.unitn.userapi.openapi.model.ReservationRequestModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface Mappers {
    it.unitn.userapi.carrentalapi.model.ReservationRequestModel toCarRentalApiReservationRequestModel(ReservationRequestModel entity);
    ReservationEntity toReservationEntity(it.unitn.userapi.carrentalapi.model.ReservationModel entity);
    ReservationModel toReservationModel(ReservationEntity entity);
}
