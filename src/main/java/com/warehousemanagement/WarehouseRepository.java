package com.warehousemanagement;

import com.warehousemanagement.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    Warehouse findByCode(String code);

}
