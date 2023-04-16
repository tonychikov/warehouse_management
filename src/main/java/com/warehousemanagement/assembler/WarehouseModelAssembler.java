package com.warehousemanagement.assembler;

import com.warehousemanagement.controller.WarehouseController;
import com.warehousemanagement.model.Warehouse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class WarehouseModelAssembler implements RepresentationModelAssembler<Warehouse, EntityModel<Warehouse>> {

    @Override
    public EntityModel<Warehouse> toModel(Warehouse warehouse) {

        return EntityModel.of(warehouse, //
                WebMvcLinkBuilder.linkTo(methodOn(WarehouseController.class).findWarehouseByCode(warehouse.getCode())).withSelfRel(),
                linkTo(methodOn(WarehouseController.class).all()).withRel("warehouses")
        );
    }
}
