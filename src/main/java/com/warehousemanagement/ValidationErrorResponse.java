package com.warehousemanagement;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ValidationErrorResponse {

    private List<Violation> violations = new ArrayList<>();

}
