package com.wajba.restaurant.Dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Schema(description = "Category response data transfer object with menu items")
public record AllCategoryDtoResponses
        (
                @Schema(description = "Category name", example = "Appetizers")
                String Name,
                @Schema(description = "Creation timestamp", example = "2024-01-15 10:30:45:AM")
                LocalDateTime createdAt,
                @Schema(description = "Last update timestamp", example = "2024-01-15 10:30:45:AM")
                LocalDateTime updatedAt,
                @Schema(description = "Category image details")
                ImagesDto imagesDto
        ) implements Serializable
{
}

