package com.warehousemanagement;

import com.warehousemanagement.model.Bay;
import com.warehousemanagement.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BayRepository extends JpaRepository<Bay, Long> {
    List<Bay> findAllByWarehouse(Warehouse warehouse);

    Bay findByWarehouseAndRowNumberAndShelfNumberAndLevelNumber(Warehouse warehouse, int rowNumber, int shelfNumber, int levelNumber);
}
