package proteus.acmeair;

import com.netifi.reactor.pool.NonBlockingPoolFactory;
import com.netifi.reactor.pool.PartitionedThreadPool;
import com.netifi.reactor.pool.Pool;
import com.netifi.reactor.pool.r2dbc.R2DbcPoolManager;
import io.r2dbc.postgresql.PostgresqlConnection;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import proteus.acmeair.loader.CsvFlightsLoader;
import proteus.acmeair.loader.FlightsLoader;

import java.time.Duration;

@SpringBootApplication
public class FlightsMain {
  static final Logger logger = LogManager.getLogger(FlightsMain.class);

  @Value("${netifi.proteus.postgres.host}")
  private String host;

  @Value("${netifi.proteus.postgres.database}")
  private String database;

  @Value("${netifi.proteus.postgres.username}")
  private String username;

  @Value("${netifi.proteus.postgres.password}")
  private String password;

  @Value("${netifi.proteus.postgres.poolSize}")
  private int poolSize;

  @Value("${netifi.proteus.acme.cacheflights}")
  private boolean flightsCacheEnabled;

  public static void main(String... args) {
    SpringApplication.run(FlightsMain.class, args);
  }

  @Bean
  PostgresqlConnectionFactory connectionFactory() {
    return new PostgresqlConnectionFactory(
        PostgresqlConnectionConfiguration.builder()
            .host(host)
            .port(5432)
            .applicationName("flight")
            .database(database)
            .username(username)
            .password(password)
            .build());
  }

  @Bean
  Pool<PostgresqlConnection> connectionPool(PostgresqlConnectionFactory connectionFactory) {
    R2DbcPoolManager poolManager = new R2DbcPoolManager(connectionFactory);
    return new PartitionedThreadPool<>(
        new NonBlockingPoolFactory<PostgresqlConnection>(
            poolSize, 1024, Duration.ofSeconds(60), Duration.ofSeconds(120), poolManager));
  }

  @Bean
  FlightsLoader flightsLoader() {
    return new CsvFlightsLoader();
  }

  @Qualifier("flightCache")
  @Bean
  boolean flightsCacheEnabled() {
    return flightsCacheEnabled;
  }
}
