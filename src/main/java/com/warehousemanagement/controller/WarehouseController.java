package com.warehousemanagement.controller;

import com.warehousemanagement.BayRepository;
import com.warehousemanagement.WarehouseRepository;
import com.warehousemanagement.assembler.BayModelAssembler;
import com.warehousemanagement.assembler.WarehouseModelAssembler;
import com.warehousemanagement.exception.BayNotEmptyException;
import com.warehousemanagement.exception.BayNotFoundException;
import com.warehousemanagement.exception.BayPointNumberExceededException;
import com.warehousemanagement.exception.WarehouseNotFoundException;
import com.warehousemanagement.model.Bay;
import com.warehousemanagement.model.Warehouse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@AllArgsConstructor
@Validated
public class WarehouseController {
    private final WarehouseRepository warehouseRepository;
    private final BayRepository bayRepository;

    private final WarehouseModelAssembler warehouseAssembler;
    private final BayModelAssembler bayAssembler;

    @GetMapping("/warehouses")
    public CollectionModel<EntityModel<Warehouse>> all() {

        List<EntityModel<Warehouse>> warehouses = warehouseRepository.findAll().stream() //
                .map(warehouseAssembler::toModel) //
                .collect(Collectors.toList());

        return CollectionModel.of(warehouses, linkTo(methodOn(WarehouseController.class).all()).withSelfRel());
    }

    @GetMapping("/warehouses/{code}")
    public ResponseEntity<EntityModel<Warehouse>> findWarehouseByCode(@PathVariable @Pattern(regexp = "\\d{3}") String code) {
        Warehouse warehouse = warehouseRepository.findByCode(code);

        if (warehouse != null) {
            EntityModel<Warehouse> model = warehouseAssembler.toModel(warehouse);
            return ResponseEntity.ok(model);
        } else {
            throw new WarehouseNotFoundException(code);
        }
    }

    @PostMapping("/warehouses")
    ResponseEntity<EntityModel<Warehouse>> newWarehouse(@Valid @RequestBody Warehouse newWarehouse) {

        EntityModel<Warehouse> entityModel = warehouseAssembler.toModel(warehouseRepository.save(newWarehouse));

        return ResponseEntity //
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
    }

    @PutMapping("/warehouses/{code}")
    ResponseEntity<EntityModel<Warehouse>> replaceWarehouse(@Valid @RequestBody Warehouse newWarehouse, @PathVariable @Pattern(regexp = "\\d{3}") String code) {

        Warehouse updatedWarehouse = warehouseRepository.findByCode(code);
        if (updatedWarehouse != null) {
            updatedWarehouse.setName(newWarehouse.getName());
            updatedWarehouse.setAddress(newWarehouse.getAddress());
            warehouseRepository.save(updatedWarehouse);

            EntityModel<Warehouse> entityModel = warehouseAssembler.toModel(updatedWarehouse);
            return ResponseEntity.ok(entityModel);
        } else {
            throw new WarehouseNotFoundException(code);
        }
    }

    @DeleteMapping("/warehouses/{code}")
    public ResponseEntity<?> deleteWarehouse(@PathVariable @Pattern(regexp = "\\d{3}") String code) {
        Warehouse warehouse = warehouseRepository.findByCode(code);
        boolean isOccupiedPointsExisting = false;
        Bay bayWithOccupiedPoints = new Bay();
        if (warehouse != null) {
            List<Bay> bays = warehouse.getBays();
            for (Bay bay : bays
            ) {
                int occupiedPoints = bay.getOccupiedPoints();
                if (occupiedPoints != 0) {
                    bayWithOccupiedPoints = bay;
                    isOccupiedPointsExisting = true;
                    break;
                }
            }

            if (!isOccupiedPointsExisting) {
                warehouseRepository.delete(warehouse);
                return ResponseEntity.noContent().build();
            } else {
                throw new BayNotEmptyException(code, bayWithOccupiedPoints.getRowNumber(), bayWithOccupiedPoints.getShelfNumber(), bayWithOccupiedPoints.getLevelNumber());
            }

        } else {
            throw new WarehouseNotFoundException(code);
        }
    }

    @GetMapping("/bays")
    public CollectionModel<EntityModel<Bay>> allBays() {
        List<EntityModel<Bay>> bays = bayRepository.findAll().stream()
                .map(bayAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(bays, linkTo(methodOn(WarehouseController.class).allBays()).withSelfRel());
    }

    @GetMapping("/warehouses/{code}/bays")
    public CollectionModel<EntityModel<Bay>> findBaysByWarehouse(@PathVariable @Pattern(regexp = "\\d{3}") String code) {
        Warehouse warehouse = warehouseRepository.findByCode(code);
        if (warehouse != null) {

            List<EntityModel<Bay>> bays = bayRepository.findAllByWarehouse(warehouse).stream()
                    .map(bayAssembler::toModel)
                    .collect(Collectors.toList());

            return CollectionModel.of(bays, linkTo(methodOn(WarehouseController.class).findBaysByWarehouse(warehouse.getCode())).withSelfRel());
        } else {
            throw new WarehouseNotFoundException(code);
        }
    }

    @GetMapping("/warehouses/{code}/bays/{rowNumber}/{shelfNumber}/{levelNumber}")
    public ResponseEntity<EntityModel<Bay>> findByWarehouseAndRowNumberAndShelfNumberAndLevelNumber(
            @PathVariable @Pattern(regexp = "\\d{3}") String code,
            @PathVariable int rowNumber,
            @PathVariable int shelfNumber,
            @PathVariable int levelNumber) {

        Warehouse warehouse = warehouseRepository.findByCode(code);
        if (warehouse != null) {
            Bay bay = bayRepository.findByWarehouseAndRowNumberAndShelfNumberAndLevelNumber(
                    warehouse, rowNumber, shelfNumber, levelNumber);
            if (bay != null) {
                EntityModel<Bay> model = bayAssembler.toModel(bay);
                return ResponseEntity.ok(model);
            } else {
                throw new BayNotFoundException(code, rowNumber, shelfNumber, levelNumber);
            }
        } else {
            throw new WarehouseNotFoundException(code);
        }
    }

    @PostMapping("/warehouses/{code}/bays")
    public ResponseEntity<EntityModel<Bay>> newBay(@PathVariable @Pattern(regexp = "\\d{3}") String code, @Valid @RequestBody Bay newBay) {
        Warehouse warehouse = warehouseRepository.findByCode(code);
        if (warehouse != null) {
            newBay.setWarehouse(warehouse);
            Bay savedBay = bayRepository.save(newBay);
            EntityModel<Bay> entityModel = bayAssembler.toModel(savedBay);
            return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        } else {
            throw new WarehouseNotFoundException(code);
        }
    }

    @PutMapping("/warehouses/{code}/bays")
    public ResponseEntity<EntityModel<Bay>> replaceBay(@PathVariable @Pattern(regexp = "\\d{3}") String code, @Valid @RequestBody Bay newBay) {
        Warehouse warehouse = warehouseRepository.findByCode(code);
        int rowNumber = newBay.getRowNumber();
        int shelfNumber = newBay.getShelfNumber();
        int levelNumber = newBay.getLevelNumber();

        int holdingPoints = newBay.getHoldingPoints();
        int occupiedPoints = newBay.getOccupiedPoints();
        if (warehouse != null) {
            Bay updatedBay = bayRepository.findByWarehouseAndRowNumberAndShelfNumberAndLevelNumber(
                    warehouse, rowNumber, shelfNumber, levelNumber);
            if (updatedBay != null) {
                if (occupiedPoints <= holdingPoints) {


                    updatedBay.setRowNumber(rowNumber);
                    updatedBay.setShelfNumber(shelfNumber);
                    updatedBay.setLevelNumber(levelNumber);
                    updatedBay.setType(newBay.getType());
                    updatedBay.setHoldingPoints(newBay.getHoldingPoints());
                    updatedBay.setOccupiedPoints(newBay.getOccupiedPoints());
                    updatedBay.setTags(newBay.getTags());
                    bayRepository.save(updatedBay);

                    EntityModel<Bay> entityModel = bayAssembler.toModel(updatedBay);
                    return ResponseEntity.ok(entityModel);
                } else {
                    throw new BayPointNumberExceededException(holdingPoints, occupiedPoints);
                }
            } else {
                throw new BayNotFoundException(code, rowNumber, shelfNumber, levelNumber);
            }

        } else {
            throw new WarehouseNotFoundException(code);
        }


    }

    @DeleteMapping("/warehouses/{code}/bays/{rowNumber}/{shelfNumber}/{levelNumber}")
    public ResponseEntity<?> deleteBay(@PathVariable @Pattern(regexp = "\\d{3}") String code,
                                       @PathVariable int rowNumber,
                                       @PathVariable int shelfNumber,
                                       @PathVariable int levelNumber) {
        Warehouse warehouse = warehouseRepository.findByCode(code);

        if (warehouse != null) {
            Bay bay = bayRepository.findByWarehouseAndRowNumberAndShelfNumberAndLevelNumber(
                    warehouse, rowNumber, shelfNumber, levelNumber);
            if (bay != null) {
                int occupiedPoints = bay.getOccupiedPoints();
                if (occupiedPoints == 0) {
                    bayRepository.delete(bay);
                    return ResponseEntity.noContent().build();
                } else {
                    throw new BayNotEmptyException(code, rowNumber, shelfNumber, levelNumber);
                }
            } else {
                throw new BayNotFoundException(code, rowNumber, shelfNumber, levelNumber);
            }
        } else {
            throw new WarehouseNotFoundException(code);
        }
    }
}
