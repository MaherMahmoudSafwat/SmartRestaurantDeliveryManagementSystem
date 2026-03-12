package com.wajba.restaurant.Dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Schema(description = "Category detailed response data transfer object")
public record CategoryDtoResponse
        (
                @Schema(description = "Category name", example = "Appetizers")
                String Name,
                @Schema(description = "Creation timestamp", example = "2024-01-15 10:30:45:AM")
                LocalDateTime createdAt,
                @Schema(description = "Last update timestamp", example = "2024-01-15 10:30:45:AM")
                LocalDateTime updatedAt,
                @Schema(description = "Associated menu items")
                List<String> menuItems,
                @Schema(description = "Category image details")
                ImagesDto imagesDto
        ) implements Serializable
{
}
