package proteus.acmeair;

import com.google.protobuf.Empty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import proteus.acmeair.contract.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Calendar;
import java.util.Date;

@Component
public class ClientApplication implements CommandLineRunner {
  private static final Empty EMPTY = Empty.getDefaultInstance();
  private static Logger logger = LoggerFactory.getLogger(ClientApplication.class);


  private final BookingServiceClient bookingServiceClient;
  private final BookingLoaderServiceClient bookingLoaderServiceClient;
  private final FlightServiceClient flightServiceClient;
  private final FlightLoaderServiceClient flightLoaderServiceClient;
  private final LoginServiceClient loginServiceClient;
  private final CustomerLoaderServiceClient customerLoaderClient;
  private final CustomerServiceClient customerServiceClient;
  
  private static final boolean load = true;

  @Autowired
  public ClientApplication(
      BookingServiceClient bookingServiceClient,
      BookingLoaderServiceClient bookingLoaderServiceClient,
      FlightServiceClient flightServiceClient,
      FlightLoaderServiceClient flightLoaderServiceClient,
      LoginServiceClient loginServiceClient,
      CustomerLoaderServiceClient customerLoaderClient,
      CustomerServiceClient customerServiceClient) {
    this.bookingServiceClient = bookingServiceClient;
    this.bookingLoaderServiceClient = bookingLoaderServiceClient;
    this.flightServiceClient = flightServiceClient;
    this.flightLoaderServiceClient = flightLoaderServiceClient;
    this.loginServiceClient = loginServiceClient;
    this.customerLoaderClient = customerLoaderClient;
    this.customerServiceClient = customerServiceClient;
  }

  private static GetBookingByNumberRequest knownBookingRequest(String bookingId) {
    return GetBookingByNumberRequest.newBuilder()
        .setBookingId(bookingId)
        .setUsername("uid42@email.com")
        .build();
  }

  private static LoginRequest knownLogin() {
    return LoginRequest.newBuilder().setUsername("uid42@email.com").setPassword("password").build();
  }

  private static LogoutRequest knownLogout(String sessionId) {
    return LogoutRequest.newBuilder()
        .setUsername("uid42@email.com")
        .setSessionId(sessionId)
        .build();
  }

  private static BookOnewayFlightRequest knownFlightRequest() {
    return BookOnewayFlightRequest.newBuilder()
        .setUsername("uid42@email.com")
        .setToFlightId("AA7")
        .build();
  }

  @Override
  public void run(String... args) {
    setupApplication();
/*
    System.out.println("logging on");

    LoginResponse loginResponse = loginServiceClient.login(knownLogin()).block();

    System.out.println("received session id " + loginResponse.getSessionId());

    ValidateCustomerResponse valid =
        customerServiceClient
            .validateCustomer(
                ValidateCustomerRequest.newBuilder()
                    .setUsername("uid42@email.com")
                    .setPassword("password")
                    .build())
            .block();

    System.out.println("valid login " + valid.getValid());

    ValidateCustomerResponse invalid =
        customerServiceClient
            .validateCustomer(
                ValidateCustomerRequest.newBuilder()
                    .setUsername("uid42@email.com")
                    .setPassword("notgoingtwork")
                    .build())
            .block();

    System.out.println("invalid login " + invalid.getValid());

    BookOnewayFlightResponse bookOnewayFlightResponse =
        bookingServiceClient.bookOnewayFlight(knownFlightRequest()).block();

    System.out.println("booked flight " + bookOnewayFlightResponse.getToBookingId());

    Booking fromId =
        bookingServiceClient
            .getBookingByNumber(
                GetBookingByNumberRequest.newBuilder()
                    .setBookingId(bookOnewayFlightResponse.getToBookingId())
                    .build())
            .block();

    System.out.println("found by id " + fromId);

    bookingServiceClient
        .getBookingsByUser(
            GetBookingsByUserRequest.newBuilder().setUsername("uid42@email.com").build())
        .toIterable()
        .forEach(booking -> System.out.println("found by user name -> " + booking));

    System.out.println("canceling booking");
    bookingServiceClient
        .cancelBooking(
            CancelBookingRequest.newBuilder().setBookingId(fromId.getBookingId()).build())
        .block();

    System.out.println("booking canceled");

    Calendar c = Calendar.getInstance();
    c.setTime(new Date());
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    c.set(Calendar.MILLISECOND, 0);
    c.set(Calendar.DAY_OF_MONTH, 11);
    c.set(Calendar.MONTH, 2);
    c.set(Calendar.YEAR, 2019);

    GetTripFlightsResponse block =
        flightServiceClient
            .getTripsFlight(
                GetTripFlightsRequest.newBuilder()
                    .setFromAirport("AMS")
                    .setToAirport("BOM")
                    .setFromDate(c.getTime().getTime())
                    .setOneWay(true)
                    .build())
            .block();

    System.out.println("got response -> " + block);

    System.out.println("logging out");
    loginServiceClient.logout(knownLogout(loginResponse.getSessionId())).block();
    System.out.println("logged out");

    // Booking block = booking.block();
    // System.out.println("Found -> " + block);

    /*setup.then(
        loginThenLogout.then(
            booking.doOnNext(b -> logger.info("Found booked flight: {}", b.getBookingId()))
                .then(
                    validatedCustomer.doOnNext(valid -> logger.info("Known customer is valid: {}", valid)))
                .then()))
    .block(Duration.ofSeconds(60));*/
  }

  private ValidateCustomerRequest knownCustomer() {
    return ValidateCustomerRequest.newBuilder()
        .setUsername("uid42@email.com")
        .setPassword("password")
        .build();
  }

  private CreateCustomersRequest customersRequest() {
    return CreateCustomersRequest.newBuilder().setCount(10_000).build();
  }

  private void setupApplication() {
    if (load) {
      System.out.println("starting to load database");
      Mono<CreateCustomersResponse> createCustomers =
          customerLoaderClient.createCustomers(customersRequest());
      Mono<LoadFlightsResponse> createFlights =
          flightLoaderServiceClient.loadDefaultFlights(Empty.getDefaultInstance());
  
      clearDatabase();
  
      Flux.merge(createFlights, createCustomers)
          .doOnError(throwable -> logger.error("error setting up application", throwable))
          .then()
          .block();
  
      System.out.println("database loaded");
    }
  }

  private void clearDatabase() {
   if (load) {
     System.out.println("starting to clean database");
     Mono<DropFlightsResponse> dropFlights = flightLoaderServiceClient.dropFlights(EMPTY);
  
     Mono<DropCustomersResponse> dropCustomers = customerLoaderClient.dropCustomers(EMPTY);
     Mono<DroppedBookings> dropBookings = bookingLoaderServiceClient.dropBookings(EMPTY);
  
     Flux.merge(dropCustomers, dropFlights, dropBookings)
         .then()
         .doOnError(throwable -> logger.error("error tearing down application", throwable))
         .block();
  
     System.out.println("database cleared");
   }
  }
}
