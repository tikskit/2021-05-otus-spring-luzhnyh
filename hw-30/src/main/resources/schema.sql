CREATE TABLE holidays(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    monthno INT NOT NULL,
    dayno INT NOT NULL,
    description VARCHAR(MAX) NOT NULL
);

ALTER TABLE holidays
ADD CONSTRAINT ck_holidays_monthno CHECK (monthno >= 1) AND (monthno <= 12);

ALTER TABLE holidays
    ADD CONSTRAINT ck_holidays_dayno CHECK (dayno >= 1) AND (dayno <= 31);

CREATE INDEX ix_holidays_monthno_dayno on holidays(monthno, dayno);