package proteus.acmeair;

import io.netifi.proteus.Proteus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import proteus.acmeair.contract.*;

@Configuration
public class ProteusConfig {
  @Value("${netifi.proteus.broker.hostname}")
  String hostName;

  @Value("${netifi.proteus.broker.port}")
  int brokerPort;

  @Value("${netifi.proteus.ssl.disabled}")
  boolean sslDisabled;

  @Value("${netifi.proteus.access.key}")
  long accessKey;

  @Value("${netifi.proteus.access.token}")
  String accessToken;

  @Value("${netifi.proteus.group}")
  String proteusGroup;

  @Bean
  Proteus proteus() {
    return Proteus.builder()
        .host(hostName)
        .port(brokerPort)
        .group(proteusGroup)
        .sslDisabled(sslDisabled)
        .accessKey(accessKey)
        .accessToken(accessToken)
        .build();
  }

  @Bean
  BookingServiceClient bookingServiceClient(Proteus proteus) {
    return new BookingServiceClient(proteus.group("acmeair.proteus.booking"));
  }

  @Bean
  BookingLoaderServiceClient bookingLoaderServiceClient(Proteus proteus) {
    return new BookingLoaderServiceClient(proteus.group("acmeair.proteus.booking"));
  }

  @Bean
  FlightServiceClient flightServiceClient(Proteus proteus) {
    return new FlightServiceClient(proteus.group("acmeair.proteus.flight"));
  }

  @Bean
  FlightLoaderServiceClient flightLoaderServiceClient(Proteus proteus) {
    return new FlightLoaderServiceClient(proteus.group("acmeair.proteus.flight"));
  }
  @Bean
  LoginServiceClient loginServiceClient(Proteus proteus) {

    return new LoginServiceClient(proteus.group("acmeair.proteus.login"));
  }

  @Bean
  CustomerLoaderServiceClient customerLoaderServiceClient(Proteus proteus) {
    return new CustomerLoaderServiceClient(proteus.group("acmeair.proteus.customer"));
  }

  @Bean
  CustomerServiceClient customerServiceClient(Proteus proteus) {
    return new CustomerServiceClient(proteus.group("acmeair.proteus.customer"));
  }
}
