package com.wajba.restaurant.Controller;

import com.wajba.restaurant.Dtos.AllCategoryDtoResponses;
import com.wajba.restaurant.Dtos.CategoryDtoRequest;
import com.wajba.restaurant.Dtos.CategoryDtoResponse;
import com.wajba.restaurant.Interfaces.CreateValidationGroup;
import com.wajba.restaurant.Interfaces.ICategory;
import com.wajba.restaurant.Service.CategoriesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.groups.Default;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "Categories", description = "APIs for managing restaurant categories and their menu items")
public class CategoryController
{
    private final ICategory categoryService;

    @Value("${app.pagination.default-page}")
    private int defaultPage;

    @Value("${app.pagination.default-size}")
    private int defaultSize;

    @Value("${app.pagination.max-size}")
    private int maxSize;

    public CategoryController(ICategory categoryService)
    {
        this.categoryService = categoryService;
    }

    private Pageable adjustPaginationAndSorting(Integer page, Integer size, String sortBy, String sortDirection)
    {
        int pageNumber = (page != null && page >= 0) ? page : defaultPage-1;
        int pageSize = (size != null && size > 0) ? size : defaultSize;

        if (pageSize > maxSize) {
            pageSize = maxSize;
        }

        Sort sort = sortDirection.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        return PageRequest.of(pageNumber, pageSize, sort);
    }

    @PostMapping("/AddCategory")
    @Operation(summary = "Create a new category", description = "Creates a new restaurant category with name and image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
            @ApiResponse(responseCode = "409", description = "Category name already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> addCategory(
            @Validated({Default.class, CreateValidationGroup.class})
            @RequestPart CategoryDtoRequest categoryDtoRequest,
            @RequestPart(required = true) MultipartFile image) throws IOException
    {
        categoryService.AddCategory(categoryDtoRequest, image);
        return new ResponseEntity<>("Category added successfully.", HttpStatus.CREATED);
    }

    @GetMapping("/ShowAllCategories")
    @Operation(summary = "Retrieve all categories", description = "Fetches all categories with pagination and sorting support")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<AllCategoryDtoResponses>> showAllCategories(
            @Parameter(description = "Page number (0-indexed)") @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page") @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by") @RequestParam(required = false, defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction (ASC/DESC)") @RequestParam(required = false, defaultValue = "ASC") String sortDirection)
    {
        Pageable pageable = adjustPaginationAndSorting(page, size, sortBy, sortDirection);
        Page<AllCategoryDtoResponses> categories = categoryService.ShowAllCategory(pageable);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/SearchByName/{name}")
    @Operation(summary = "Search category by name", description = "Retrieves a specific category by its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category found"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CategoryDtoResponse> showCategoryByName(
            @Parameter(description = "Category name") @PathVariable String name)
    {
        CategoryDtoResponse category = categoryService.ShowCategoryByName(name);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PutMapping("/UpdateCategory/{name}")
    @Operation(summary = "Update an existing category", description = "Updates category details and/or image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> updateCategory(
            @Parameter(description = "Current category name") @PathVariable String name,
            @Validated({CreateValidationGroup.class})
            @RequestPart CategoryDtoRequest categoryDtoRequest,
            @RequestPart(required = false) MultipartFile image) throws IOException
    {
        categoryService.UpdateCategories(name, categoryDtoRequest, image);
        return new ResponseEntity<>("Category updated successfully.", HttpStatus.OK);
    }

    @DeleteMapping("/DeleteCategory/{name}")
    @Operation(summary = "Delete a category", description = "Deletes a category and its associated menu items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> deleteCategory(
            @Parameter(description = "Category name to delete") @PathVariable String name) throws IOException
    {
        categoryService.DeleteCategories(name);
        return new ResponseEntity<>("Category '" + name + "' deleted successfully.", HttpStatus.OK);
    }
}
