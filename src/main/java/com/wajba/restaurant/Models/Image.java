package com.wajba.restaurant.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Image entity for storing images of categories, menu items, and modifiers")
public class Image
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Image_Id")
    @SequenceGenerator(name = "Image_Id",sequenceName = "Image_Id",allocationSize = 50)
    @Schema(description = "Unique image identifier")
    Integer id;
    @Schema(description = "Image file name", example = "grilled-chicken.jpg")
    String name;
    @Schema(description = "Image MIME type", example = "image/jpeg")
    String type;
    @Schema(description = "Image URL or path", example = "/uploads/images/grilled-chicken.jpg")
    String url;
    @OneToOne(mappedBy = "image",orphanRemoval = true)
    @JsonBackReference
    MenuItems menuItems;
    @OneToOne(mappedBy = "image")
    @JsonIgnore
    Category category;
    @OneToOne(mappedBy = "image")
    @JsonIgnore
    Modifier modifier;
}
