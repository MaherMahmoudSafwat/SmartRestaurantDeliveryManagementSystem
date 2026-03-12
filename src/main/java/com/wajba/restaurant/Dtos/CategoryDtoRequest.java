package com.wajba.restaurant.Dtos;

import com.wajba.restaurant.Interfaces.CreateValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;
import lombok.Builder;

import java.io.Serializable;

@Builder
@Schema(description = "Category creation request data transfer object")
public record CategoryDtoRequest
        (
                @NotBlank(message = "Category name is required.")
                @Size(groups = {Default.class, CreateValidationGroup.class},
                        min = 2, max = 50,
                        message = "Category name must be between 2 and 50 characters.")
                @Pattern(groups = {Default.class, CreateValidationGroup.class},
                        regexp = "^[a-zA-Z0-9\\s\\-\\'\\&]+$",
                        message = "Category name can only contain letters, numbers, spaces, hyphens, apostrophes and &.")
                @Schema(description = "Category name", example = "Fast Food", minLength = 2, maxLength = 50)
                String name
        ) implements Serializable
{
}