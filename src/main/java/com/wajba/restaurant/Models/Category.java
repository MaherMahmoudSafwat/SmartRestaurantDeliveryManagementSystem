package com.wajba.restaurant.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
                                @Index(name = "Idx_Name",columnList = "name")
                        }
        )
@Schema(description = "Restaurant category entity representing a category of menu items")
public class Category
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Category_Id")
    @SequenceGenerator(name = "Category_Id",sequenceName = "Category_Id",allocationSize = 50)
    @Schema(description = "Unique category identifier")
    Integer id;
    @Column(nullable = false, unique = true)
    @Schema(description = "Category name", example = "Appetizers")
    String name;
    @Column(updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss:a")
    @Schema(description = "Category creation timestamp", example = "2024-01-15 10:30:45:AM")
    LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss:a")
    @Schema(description = "Category last update timestamp", example = "2024-01-15 10:30:45:AM")
    LocalDateTime updatedAt;
    @OneToMany(mappedBy = "category",cascade = CascadeType.REMOVE)
    @JsonManagedReference
    @Builder.Default
    List<MenuItems> menuItems = new ArrayList<>();
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn
            (
                    name = "Image_id"
            )
    Image image;
}
