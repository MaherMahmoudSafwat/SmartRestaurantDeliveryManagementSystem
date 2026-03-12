package com.wajba.restaurant.Dtos;

import com.wajba.restaurant.Interfaces.CreateValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;
import lombok.Builder;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Schema(description = "Modifier creation request data transfer object")
public record ModifierDtoRequest
        (
                @NotBlank(message = "Modifier name is required.")
                @Size(groups = {Default.class,CreateValidationGroup.class}, min = 2, max = 50, message = "Modifier name must be between 2 and 50 characters.")
                @Schema(description = "Modifier name", example = "Extra Cheese", minLength = 2, maxLength = 50)
                String name,

                @NotNull(message = "Price is required.")
                @DecimalMin(groups = {Default.class, CreateValidationGroup.class},value = "0.00", message = "Price must be greater than or equal to 0.")
                @Schema(description = "Modifier price", example = "2.50")
                BigDecimal price
        )
        implements Serializable
{
}
