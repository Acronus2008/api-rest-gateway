package com.ex.gateways.manage.restgateways.repository;

import com.ex.gateways.manage.restgateways.model.Gateway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GatewayRepository extends JpaRepository<Gateway, Long> {
    Optional<Gateway> findGatewayByIpAddress(String ip);
    Optional<Gateway> findGatewayBySerialNumber(String serialNumber);
}
