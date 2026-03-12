package com.wajba.restaurant.Dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Schema(description = "Modifier details for menu item data transfer object")
public record ModifiersForMenuItemDto
        (
                @Schema(description = "Modifier name", example = "Extra Cheese")
                String name,
                @Schema(description = "Modifier price", example = "2.50")
                BigDecimal price
        ) implements Serializable
{
}
