CREATE TABLE trains (
    train_name VARCHAR(255) PRIMARY KEY,
    total_seats INT,
    available_seats INT
);

CREATE TABLE bookings (
    train_name VARCHAR(255),
    passenger_name VARCHAR(255),
    seat_number INT,
    PRIMARY KEY (train_name, passenger_name),
    FOREIGN KEY (train_name) REFERENCES trains(train_name)
);
