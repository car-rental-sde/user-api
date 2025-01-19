package it.unitn.userapi.mapper;

import it.unitn.userapi.entity.UserEntity;
import it.unitn.userapi.entity.UserRole;
import it.unitn.userapi.openapi.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface Mappers {
    @Named("roleToUserRoleModel")
    static UserRoleModel roleToUserRoleModel(UserRole userType) {

        if (UserRoleModel.ADMIN.getValue().equals(userType.name())) {
            return UserRoleModel.ADMIN;
        }
        if (UserRoleModel.USER.getValue().equals(userType.name())) {
            return UserRoleModel.USER;
        }
        if (UserRoleModel.API_CLIENT.getValue().equals(userType.name())) {
            return UserRoleModel.API_CLIENT;
        }

        return UserRoleModel.USER;
    }

    it.unitn.userapi.carrentalapi.model.ReservationRequestModel toCarRentalApiReservationRequestModel(ReservationRequestModel entity);
    ReservationModel toReservationModel(it.unitn.userapi.carrentalapi.model.ReservationModel entity);
    it.unitn.userapi.carrentalapi.model.ReservationsSortColumn toCarRentalApiReservationsSortColumn(ReservationsSortColumn entity);
    it.unitn.userapi.carrentalapi.model.SortDirection toCarRentalApiSortDirection(SortDirection entity);

    @Mapping(source = "role", target = "userRole",  qualifiedByName = "roleToUserRoleModel")
    UserModel userToUserModel(UserEntity user);
}
