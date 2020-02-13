package com.ex.gateways.manage.restgateways.repository;

import com.ex.gateways.manage.restgateways.model.PeripheralDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeripheralDeviceRepository extends JpaRepository<PeripheralDevice, Long> {

    @Override
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "DELETE FROM peripheral WHERE id = ?1", nativeQuery = true)
    void deleteById(Long id);

    Optional<PeripheralDevice> findPeripheralDeviceByUid(Long uid);
}
