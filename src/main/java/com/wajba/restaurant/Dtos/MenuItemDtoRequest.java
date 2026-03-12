package com.wajba.restaurant.Dtos;

import com.wajba.restaurant.Interfaces.CreateValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import jakarta.validation.groups.Default;
import lombok.Builder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Builder
@Schema(description = "Menu item creation request data transfer object")
public record MenuItemDtoRequest
        (
                @NotBlank(message = "Item name is required.")
                @Size(groups = {Default.class, CreateValidationGroup.class},min = 3, max = 100, message = "Item name must be between 3 and 100 characters.")
                @Pattern(groups = {Default.class,CreateValidationGroup.class},regexp = "^[a-zA-Z0-9\\s\\-\\'\\&]+$",
                        message = "Item name can only contain letters, numbers, spaces, hyphens, apostrophes and &.")
                @Schema(description = "Menu item name", example = "Grilled Chicken", minLength = 3, maxLength = 100)
                String name,

                @NotNull(message = "Price is required")
                @DecimalMin(groups = {Default.class,CreateValidationGroup.class},value = "0.01", message = "Price must be greater than 0.")
                @Digits(groups={Default.class, CreateValidationGroup.class},integer = 4, fraction = 2, message = "Price can have max 4 digits and 2 decimal places.")
                @Schema(description = "Menu item price", example = "25.99")
                BigDecimal price,

                @NotNull(message = "Category is required.")
                @Schema(description = "Category name", example = "Main Courses")
                String category,

                @Size(max = 20, message = "Maximum 20 modifiers allowed per item.")
                @Schema(description = "List of modifier names")
                List<String> modifiers,
                @NotNull(message = "Quantity is required.")
                @Min(groups={Default.class, CreateValidationGroup.class},value = 0, message = "Quantity cannot be negative.")
                @Schema(description = "Available quantity", example = "50")
                Integer quantity,

                @Size(max = 500, message = "Description cannot exceed 500 characters.")
                @Schema(description = "Item description", example = "Fresh grilled chicken with special sauce", maxLength = 500)
                String description
        )
        implements Serializable
{
}