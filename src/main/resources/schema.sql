 CREATE TABLE IF NOT EXISTS CLIENTS(
   ID UUID PRIMARY KEY,
   CLIENT_NAME VARCHAR(50),
   PHONE_NUMBER VARCHAR(20),
   CODE_CARD VARCHAR(13),
   BONUS NUMERIC(20,2)
 );

CREATE TABLE IF NOT EXISTS DEVICES(
    CLIENT_ID UUID REFERENCES CLIENTS(ID),
    DEVICE_ID VARCHAR(50)
);

create table if not exists promotions(
    id uuid primary key,
    name varchar(50),
    description varchar(100),
    start_date date,
    end_date date,
    up_to_discount numeric(20,2),
    image varchar(max),
    image_name varchar(100)
);