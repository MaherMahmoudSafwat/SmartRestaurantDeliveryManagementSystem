package com.wajba.restaurant.Dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.io.Serializable;

@Builder
@Schema(description = "Image details data transfer object")
public record ImagesDto
        (
                @Schema(description = "Image file name", example = "grilled-chicken.jpg")
                String name,
                @Schema(description = "Image content/data (base64 or raw)")
                String content,
                @Schema(description = "Image URL or path", example = "/uploads/images/grilled-chicken.jpg")
                String url
        ) implements Serializable
{
}
