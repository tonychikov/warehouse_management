package com.warehousemanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnTransformer;

import java.util.List;

@Entity
@Table(name = "bay", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"warehouse_id", "row_number", "shelves_number", "level_number"})
})
@Data
@NoArgsConstructor
public class Bay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @Column(name = "row_number", nullable = false)
    private int rowNumber;

    @Column(name = "shelves_number", nullable = false)
    private int shelfNumber;

    @Column(name = "level_number", nullable = false)
    private int levelNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BayType type;

    @Column(nullable = false)
    @Min(1)
    @Max(9)
    private int holdingPoints;

    @Column(nullable = false)
    @Min(0)
    @Max(9)
    private int occupiedPoints;

    @ElementCollection
    @CollectionTable(name = "bay_tags", uniqueConstraints = @UniqueConstraint(columnNames = {"bay_id", "tag"}))
    @Column(name = "tag", nullable = false)
    @ColumnTransformer(read = "LOWER(tag)", write = "LOWER(?)")
    @NotNull
    private List<String> tags;

    public Bay(Warehouse warehouse, int rowNumber, int shelfNumber, int levelNumber, BayType type, int holdingPoints, int occupiedPoints, List<String> tags) {
        this.warehouse = warehouse;
        this.rowNumber = rowNumber;
        this.shelfNumber = shelfNumber;
        this.levelNumber = levelNumber;
        this.type = type;
        this.holdingPoints = holdingPoints;
        this.occupiedPoints = occupiedPoints;
        this.tags = tags;
    }
}
