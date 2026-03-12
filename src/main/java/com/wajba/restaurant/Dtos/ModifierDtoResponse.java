package com.wajba.restaurant.Dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Schema(description = "Modifier response data transfer object")
public record ModifierDtoResponse
        (
                @Schema(description = "Modifier name", example = "Extra Cheese")
                String name,

                @Schema(description = "Creation timestamp", example = "2024-01-15 10:30:45:AM")
                LocalDateTime createdAt,

                @Schema(description = "Last update timestamp", example = "2024-01-15 10:30:45:AM")
                LocalDateTime updatedAt,

                @Schema(description = "Modifier image details")
                ImagesDto imagesDto,

                @Schema(description = "Modifier price", example = "2.50")
                BigDecimal price

        )
    implements Serializable
{
}
