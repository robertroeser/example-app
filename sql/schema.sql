DROP INDEX  IF EXISTS username_idx;

DROP TABLE BOOKING;
DROP TABLE CUSTOMER;
DROP TABLE AIRPORT;
DROP TABLE FLIGHT;
DROP TABLE FLIGHT_SEGMENT;
DROP TABLE LOGIN;

CREATE TABLE BOOKING(
   bookingId CHAR(36) PRIMARY KEY NOT NULL,
   username CHAR(36) NOT NULL,
   flightId  VARCHAR(50)  NOT NULL,
   dateOfBooking TIMESTAMP NOT NULL
);

CREATE INDEX username_idx ON BOOKING (username);

CREATE TABLE CUSTOMER (
    username VARCHAR(50) PRIMARY KEY NOT NULL,
    password VARCHAR(50) NOT NULL,
    status VARCHAR(10) NOT NULL,
    totalMiles INTEGER NOT NULL,
    mileYtd INTEGER NOT NULL,
    address VARCHAR(50) NOT NULL,
    phoneNumber VARCHAR(50) NOT NULL,
    phoneNumberType VARCHAR(10) NOT NULL
);

CREATE TABLE AIRPORT (
    airportCode CHAR(3) PRIMARY KEY NOT NULL,
    airportName VARCHAR(25) NOT NULL
);

CREATE TABLE FLIGHT (
    flightId CHAR(36) PRIMARY KEY NOT NULL,
    firstClassBaseCost INTEGER NOT NULL,
    numFirstClassSeats INTEGER NOT NULL,
    economyClassBaseCost INTEGER NOT NULL,
    numEconomyClassSeats INTEGER NOT NULL,
    airplaneTypeId INTEGER NOT NULL,
    flightSegmentId CHAR(36) NOT NULL,
    scheduledDepartureTime TIMESTAMP NOT NULL,
    scheduledArrivalTime TIMESTAMP NOT NULL
);

CREATE TABLE FLIGHT_SEGMENT (
   flightSegmentId CHAR(36) PRIMARY KEY NOT NULL,
   originPort CHAR(3) NOT NULL,
   destPort CHAR(3) NOT NULL,
   miles INTEGER NOT NULL
);

CREATE TABLE LOGIN (
    sessionId CHAR(36) PRIMARY KEY NOT NULL,
    userId UUID NOT NULL,
    lastAccessedTime TIMESTAMP NOT NULL,
    timeoutTime TIMESTAMP NOT NULL
);