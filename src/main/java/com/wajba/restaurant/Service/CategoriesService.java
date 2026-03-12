package com.wajba.restaurant.Service;

import com.wajba.restaurant.Dtos.AllCategoryDtoResponses;
import com.wajba.restaurant.Dtos.CategoryDtoRequest;
import com.wajba.restaurant.Dtos.CategoryDtoResponse;
import com.wajba.restaurant.Exceptions.CategoryAlreadyExistsException;
import com.wajba.restaurant.Exceptions.CategoryNotFoundException;
import com.wajba.restaurant.Interfaces.ICategory;
import com.wajba.restaurant.Mappers.CategoryMappers;
import com.wajba.restaurant.Models.Category;
import com.wajba.restaurant.Models.Image;
import com.wajba.restaurant.Repository.CategoriesRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class CategoriesService implements ICategory
{

    private final CategoriesRepository categoriesRepository;

    @Value("${app.web.url}")
    private String WEB_URL;

    @Value("${app.upload.path}")
    private String FILE_SYSTEM;

    public CategoriesService (CategoriesRepository categoriesRepository)
    {
        this.categoriesRepository = categoriesRepository;
    }

    @Transactional
    @Override
    @CacheEvict(value = "Category",allEntries = true)
    public Category AddCategory(CategoryDtoRequest categoryDtoRequest,MultipartFile Image) throws IOException {
        Category category = new Category();
        if(categoriesRepository.existsByName(categoryDtoRequest.name()))
        {
            throw new CategoryAlreadyExistsException("This category name " + categoryDtoRequest.name() + " is already exists.");
        }
        category = CategoryMappers.ToEntityRequest(categoryDtoRequest);
        category.setCreatedAt(LocalDateTime.now());
        AddNewCategoryImage(category,Image);
        return categoriesRepository.save(category);
    }

    private void AddNewCategoryImage(Category category, MultipartFile Image) throws IOException {
        if(Image != null)
        {
            Image image = new Image();
            image = ImageService.CreateNewImage(FILE_SYSTEM,WEB_URL,Image);
            category.setImage(image);
        }
    }

    @Override
    @Cacheable(value = "Category", key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort.toString()")
    public Page<AllCategoryDtoResponses> ShowAllCategory(Pageable pageable)
    {
        return categoriesRepository.findAll(pageable)
                .map(CategoryMappers::ToDtoAllResponses);
    }

    @Override
    @Cacheable(value = "Category", key = "#Name")
    public CategoryDtoResponse ShowCategoryByName(String Name)
    {
        return CategoryMappers.ToDtoResponse
                (
                        categoriesRepository.findByName(Name)
                                .orElseThrow(() -> new CategoryNotFoundException("The category name " + Name + " doesn't exist."))
                );
    }

    @Override
    @CacheEvict(value = {"Category","MenuItems"}, allEntries = true)
    public void DeleteCategories(String Name) throws IOException {
        Category category = new Category();
        category = categoriesRepository.findByName(Name)
                .orElseThrow(() -> new CategoryNotFoundException("The category name " + Name + " doesn't exist."));
        ImageService.DeleteFileImage(FILE_SYSTEM,category);
        categoriesRepository.delete(category);
    }

    @Override
    @CacheEvict(value = {"Category","MenuItems"}, allEntries = true)
    public void UpdateCategories(String OldName,CategoryDtoRequest categoryDtoRequest,MultipartFile Image) throws IOException {
        Category category = new Category();
        category = categoriesRepository.findByName(OldName)
                .orElseThrow(() -> new CategoryNotFoundException("The category name " + OldName + " doesn't exist."));
        if (categoryDtoRequest.name() != null && !categoryDtoRequest.name().isEmpty())
        {
            category.setName(categoryDtoRequest.name());
        }
        category.setUpdatedAt(LocalDateTime.now());
        UpdateCategoryImage(category,Image);
        categoriesRepository.save(category);
    }

    private void UpdateCategoryImage(Category category,MultipartFile Image) throws IOException {
        if(Image != null)
        {
            Image image = new Image();
            ImageService.DeleteFileImage(FILE_SYSTEM,category);
            image = ImageService.CreateNewImage(FILE_SYSTEM,WEB_URL,Image);
            category.setImage(image);
        }
    }
}
