CREATE TABLE gateway (
  id bigint NOT NULL AUTO_INCREMENT,
  ip_address varchar(255) NOT NULL,
  name varchar(255) DEFAULT NULL,
  serial_number varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;