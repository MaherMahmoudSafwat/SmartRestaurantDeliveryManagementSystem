package com.wajba.restaurant.Interfaces;

import com.wajba.restaurant.Dtos.AllCategoryDtoResponses;
import com.wajba.restaurant.Dtos.CategoryDtoRequest;
import com.wajba.restaurant.Dtos.CategoryDtoResponse;
import com.wajba.restaurant.Models.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ICategory
{
    public Category AddCategory(CategoryDtoRequest categoryDtoRequest, MultipartFile Image) throws IOException;
    public Page<AllCategoryDtoResponses> ShowAllCategory(Pageable pageable);
    public CategoryDtoResponse ShowCategoryByName(String name);
    public void DeleteCategories(String Name) throws IOException;
    public void UpdateCategories(String OldName,CategoryDtoRequest categoryDtoRequest, MultipartFile Image) throws IOException;
}
