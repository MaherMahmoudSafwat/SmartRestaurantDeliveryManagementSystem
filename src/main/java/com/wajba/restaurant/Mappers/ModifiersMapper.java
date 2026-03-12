package com.wajba.restaurant.Mappers;

import com.wajba.restaurant.Dtos.ImagesDto;
import com.wajba.restaurant.Dtos.ModifierDtoRequest;
import com.wajba.restaurant.Dtos.ModifierDtoResponse;
import com.wajba.restaurant.Models.Image;
import com.wajba.restaurant.Models.Modifier;
import org.springframework.stereotype.Service;


@Service
public class ModifiersMapper
{

    public static Modifier ToEntityRequest(ModifierDtoRequest modifierDtoRequest)
    {
        return Modifier.builder()
                .name(modifierDtoRequest.name())
                .price(modifierDtoRequest.price())
                .build();
    }

    private static ImagesDto GetOnlyImagesInformation(Image image)
    {
        if(image == null)
        {
            return null;
        }
        return ImagesDto.builder()
                .name(image.getName())
                .content(image.getType())
                .url(image.getUrl())
                .build();
    }

    public static ModifierDtoResponse ToDtoResponse(Modifier Modifiers)
    {

        return ModifierDtoResponse.builder()
                .name(Modifiers.getName())
                .createdAt(Modifiers.getCreatedAt())
                .updatedAt(Modifiers.getUpdatedAt())
                .price(Modifiers.getPrice())
                .imagesDto(GetOnlyImagesInformation(Modifiers.getImage()))
                .build();
    }
}
