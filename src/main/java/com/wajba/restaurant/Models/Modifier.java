package com.wajba.restaurant.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@Builder
@Table
        (
                indexes =
                        {
                                @Index(name = "Name_Idx",columnList="name")
                        }
        )
@Schema(description = "Modifier entity representing optional extras for menu items")
public class Modifier
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "Modifier_id")
    @SequenceGenerator(name = "Modifier_Id",sequenceName = "Modifier_Id",allocationSize = 50)
    @Schema(description = "Unique modifier identifier")
    Integer id;
    @Column(nullable = false,unique = true)
    @Schema(description = "Modifier name", example = "Extra Cheese")
    String name;
    @Column(updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss:a")
    @Schema(description = "Creation timestamp", example = "2024-01-15 10:30:45:AM")
    LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss:a")
    @Schema(description = "Last update timestamp", example = "2024-01-15 10:30:45:AM")
    LocalDateTime updatedAt;
    @Column(nullable = false)
    @Schema(description = "Modifier price", example = "2.50")
    BigDecimal price;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn
            (
                    name = "Image_Id"
            )
    Image image;
    @ManyToMany(mappedBy="modifiers")
    @JsonBackReference
    @Builder.Default
    List<MenuItems> menuItemsList = new ArrayList<>();
}
