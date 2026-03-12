package com.wajba.restaurant.Dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Schema(description = "Menu item response data transfer object")
public record MenuItemDtoResponse
        (
                @Schema(description = "Menu item name", example = "Grilled Chicken")
                String name,

                @Schema(description = "Menu item price", example = "25.99")
                BigDecimal price,

                @Schema(description = "Category name", example = "Main Courses")
                String category,

                @Schema(description = "List of associated modifiers")
                List<ModifiersForMenuItemDto> modifiers,

                @Schema(description = "Menu item image details")
                ImagesDto imagesDto,

                @Schema(description = "Available quantity", example = "50")
                Integer quantity,

                @Schema(description = "Creation timestamp", example = "2024-01-15 10:30:45:AM")
                LocalDateTime createdAt,

                @Schema(description = "Last update timestamp", example = "2024-01-15 10:30:45:AM")
                LocalDateTime updatedAt,

                @Schema(description = "Item description", example = "Fresh grilled chicken with special sauce")
                String description
        )
        implements Serializable
{
}
