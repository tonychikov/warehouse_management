package com.warehousemanagement;

import com.warehousemanagement.model.Bay;
import com.warehousemanagement.model.BayType;
import com.warehousemanagement.model.Warehouse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(WarehouseRepository warehouseRepository, BayRepository bayRepository) {

        return args -> {

            Warehouse berlinWarehouse = new Warehouse("000", "Berlin", "Adenauerplatz 1, 13454 Berlin");
            Warehouse potsdamWarehouse = new Warehouse("123", "Potsdam", "Branderburgerstr. 23, 42342 Potsdam");
            Warehouse berlinSchoenefeldWarehouse = new Warehouse("001", "Berlin-Schönefeld", "Sachsendamm 20, 10829 Berlin");

            warehouseRepository.save(berlinWarehouse);
            warehouseRepository.save(potsdamWarehouse);
            warehouseRepository.save(berlinSchoenefeldWarehouse);

            warehouseRepository.findAll().forEach(warehouse -> log.info("Preloaded " + warehouse));


            for (int row = 1; row <= 2; row++) {
                for (int shelf = 1; shelf <= 3; shelf++) {
                    for (int level = 1; level <= 10; level++) {
                        Bay bay = new Bay(berlinWarehouse, row, shelf, level, BayType.CART, 8, 0,
                                Arrays.asList("row_" + row, "shelf_" + shelf, "level" + level));
                        bayRepository.save(bay);
                        log.info("Preloaded " + bay);
                    }
                }
            }

            for (int row = 1; row <= 3; row++) {
                for (int shelf = 1; shelf <= 4; shelf++) {
                    for (int level = 1; level <= 5; level++) {
                        Bay bay = new Bay(potsdamWarehouse, row, shelf, level, BayType.PALLET, 4, 0,
                                Arrays.asList("row_" + row, "shelf_" + shelf, "level" + level));
                        bayRepository.save(bay);
                        log.info("Preloaded " + bay);
                    }
                }
            }

            for (int row = 1; row <= 2; row++) {
                for (int shelf = 1; shelf <= 4; shelf++) {
                    for (int level = 1; level <= 5; level++) {
                        Bay bay = new Bay(berlinSchoenefeldWarehouse, row, shelf, level, BayType.CART, 9, 0,
                                Arrays.asList("Row_" + row, "Shelf_" + shelf, "Level_" + level));
                        bayRepository.save(bay);
                        log.info("Preloaded " + bay);
                    }
                }
            }

            //bayRepository.findAll().forEach(bayEntity -> log.info("Preloaded " + bayEntity));

        };
    }


}
