package com.wajba.restaurant.Mappers;

import com.wajba.restaurant.Dtos.ImagesDto;
import com.wajba.restaurant.Dtos.MenuItemDtoRequest;
import com.wajba.restaurant.Dtos.MenuItemDtoResponse;
import com.wajba.restaurant.Dtos.ModifiersForMenuItemDto;
import com.wajba.restaurant.Models.Image;
import com.wajba.restaurant.Models.MenuItems;
import com.wajba.restaurant.Models.Modifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuItemMapper
{

    private static List<ModifiersForMenuItemDto> ToModifiersForMenuItemDtoListResponse(List<Modifier> Modifiers)
    {
        if(Modifiers == null)
        {
            return null;
        }
        return Modifiers.stream()
                .map
                        (
                                modifier ->
                                        ModifiersForMenuItemDto.builder()
                                                .name(modifier.getName())
                                                .price(modifier.getPrice())
                                                .build()
                        )
                .toList();
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
    public static MenuItems ToEntityRequest(MenuItemDtoRequest menuItemDto)
    {
        return MenuItems.builder()
                .name(menuItemDto.name())
                .price(menuItemDto.price())
                .quantity(menuItemDto.quantity())
                .description(menuItemDto.description())
                .build();
    }

    public static MenuItemDtoResponse ToDtoResponse(MenuItems menuItems)
    {
        return MenuItemDtoResponse.builder()
                .name(menuItems.getName())
                .price(menuItems.getPrice())
                .category(menuItems.getCategory().getName())
                .createdAt(menuItems.getCreatedAt())
                .updatedAt(menuItems.getUpdatedAt())
                .modifiers(ToModifiersForMenuItemDtoListResponse((menuItems.getModifiers())))
                .imagesDto(GetOnlyImagesInformation(menuItems.getImage()))
                .quantity(menuItems.getQuantity())
                .description(menuItems.getDescription())
                .build();
    }
}

