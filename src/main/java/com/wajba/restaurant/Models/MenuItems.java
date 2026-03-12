package com.wajba.restaurant.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
        (
                indexes =
                        {
                                @Index(name = "FoodName_Idx",columnList = "name")
                        }
        )
@Builder
@Schema(description = "Menu item entity representing items available for order")
public class MenuItems
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "MenuItem_Id")
    @SequenceGenerator(name = "MenuItem_Id", sequenceName = "MenuItem_Id",allocationSize = 50)
    @Schema(description = "Unique menu item identifier")
    Integer id;
    @Column(unique = true, nullable = false)
    @Schema(description = "Menu item name", example = "Grilled Chicken")
    String name;
    @Column(nullable = false)
    @Schema(description = "Menu item price", example = "25.99")
    BigDecimal price;
    @Column(nullable = false)
    @Schema(description = "Available quantity", example = "50")
    Integer quantity;
    @Schema(description = "Item description", example = "Fresh grilled chicken with special sauce")
    String description;
    @Column(updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss:a")
    @Schema(description = "Creation timestamp", example = "2024-01-15 10:30:45:AM")
    LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss:a")
    @Schema(description = "Last update timestamp", example = "2024-01-15 10:30:45:AM")
    LocalDateTime updatedAt;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
            (
                    name = "Category_Id"
            )
    @JsonBackReference
    Category category;
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn
            (
                    name = "Image_Id"
            )
    @JsonManagedReference
    Image image;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable
            (
                    name = "MenuItems_Modifiers",
                    joinColumns = @JoinColumn(name = "Menu_Items_Id"),
                    inverseJoinColumns = @JoinColumn(name = "Modifiers_Id")
            )
    @Builder.Default
    @JsonManagedReference
    List<Modifier> modifiers = new ArrayList<>();
}

