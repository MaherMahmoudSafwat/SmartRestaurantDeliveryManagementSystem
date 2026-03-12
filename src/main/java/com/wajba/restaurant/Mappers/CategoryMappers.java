package com.wajba.restaurant.Mappers;

import com.wajba.restaurant.Dtos.AllCategoryDtoResponses;
import com.wajba.restaurant.Dtos.CategoryDtoRequest;
import com.wajba.restaurant.Dtos.CategoryDtoResponse;
import com.wajba.restaurant.Dtos.ImagesDto;
import com.wajba.restaurant.Models.Category;
import com.wajba.restaurant.Models.Image;
import org.springframework.stereotype.Service;

@Service
public class CategoryMappers
{
    public static Category ToEntityRequest(CategoryDtoRequest categoryDtoRequest)
    {
        return Category.builder()
                .name(categoryDtoRequest.name())
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
    public static AllCategoryDtoResponses ToDtoAllResponses(Category category)
    {
        return AllCategoryDtoResponses.builder()
                .Name(category.getName())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .imagesDto(GetOnlyImagesInformation(category.getImage()))
                .build();
    }

    public static CategoryDtoResponse ToDtoResponse(Category category)
    {
        return CategoryDtoResponse.builder()
                .Name(category.getName())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .menuItems(category.getMenuItems().stream().map(item -> item.getName()).toList())
                .imagesDto(GetOnlyImagesInformation(category.getImage()))
                .build();
    }
}
