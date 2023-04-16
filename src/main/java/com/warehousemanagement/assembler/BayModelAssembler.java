package com.warehousemanagement.assembler;

import com.warehousemanagement.controller.WarehouseController;
import com.warehousemanagement.model.Bay;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BayModelAssembler implements RepresentationModelAssembler<Bay, EntityModel<Bay>> {

    @Override
    public EntityModel<Bay> toModel(Bay bay) {

        return EntityModel.of(bay, //
                WebMvcLinkBuilder.linkTo(methodOn(WarehouseController.class).findByWarehouseAndRowNumberAndShelfNumberAndLevelNumber(bay.getWarehouse().getCode(),
                        bay.getRowNumber(), bay.getShelfNumber(), bay.getLevelNumber())).withSelfRel(),
                linkTo(methodOn(WarehouseController.class).allBays()).withRel("bays")
                , linkTo(methodOn(WarehouseController.class).findBaysByWarehouse(bay.getWarehouse().getCode())).withRel("baysByWarehouse")
        );
    }
}
