package it.unitn.userapi.api;

import it.unitn.userapi.carrentalapi.model.CarsPaginationResponseModel;
import it.unitn.userapi.carrentalapi.model.ReservationModel;
import it.unitn.userapi.facade.CarRentalApiFacade;
import it.unitn.userapi.facade.HttpClientErrorsAwareResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final CarRentalApiFacade carRentalApiFacade;

    @GetMapping("/test")
    public ResponseEntity<String> test() {

//        HttpClientErrorsAwareResponse<CarsPaginationResponseModel> car = carRentalApiFacade.searchCars(1L);
//        HttpClientErrorsAwareResponse<ReservationModel> reservation = carRentalApiFacade.getReservation(1L);

        return ResponseEntity.ok("Test");
    }
}
