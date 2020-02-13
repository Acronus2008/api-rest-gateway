CREATE TABLE peripheral (
  id bigint NOT NULL AUTO_INCREMENT,
  date_created datetime DEFAULT NULL,
  status varchar(255) DEFAULT NULL,
  uid bigint DEFAULT NULL,
  vendor varchar(255) DEFAULT NULL,
  gateway_id bigint NOT NULL,
  PRIMARY KEY (id),
  KEY `gateway_k` (gateway_id),
  CONSTRAINT `peripheral_gateway_fk` FOREIGN KEY (gateway_id) REFERENCES gateway (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;