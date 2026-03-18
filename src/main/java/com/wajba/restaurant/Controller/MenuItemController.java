package com.wajba.restaurant.Controller;

import com.wajba.restaurant.Dtos.MenuItemDtoRequest;
import com.wajba.restaurant.Dtos.MenuItemDtoResponse;
import com.wajba.restaurant.Interfaces.CreateValidationGroup;
import com.wajba.restaurant.Interfaces.IMenuItem;
import com.wajba.restaurant.Service.MenuItemService;
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
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/menu-items")
@Tag(name = "Menu Items", description = "APIs for managing restaurant menu items with pricing, modifiers, and availability")
public class MenuItemController
{
    private final IMenuItem menuItemService;

    @Value("${app.pagination.default-page}")
    private int defaultPage;

    @Value("${app.pagination.default-size}")
    private int defaultSize;

    @Value("${app.pagination.max-size}")
    private int maxSize;

    public MenuItemController(IMenuItem menuItemService)
    {
        this.menuItemService = menuItemService;
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

    @PostMapping("/AddMenuItem")
    @Operation(summary = "Create a new menu item", description = "Creates a new menu item with name, price, category, quantity and optional modifiers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Menu item created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
            @ApiResponse(responseCode = "404", description = "Category or modifiers not found"),
            @ApiResponse(responseCode = "409", description = "Menu item name already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> addMenuItem(
            @Validated({Default.class, CreateValidationGroup.class})
            @RequestPart MenuItemDtoRequest menuItemDtoRequest,
            @RequestPart(required = true) MultipartFile image) throws IOException
    {
        menuItemService.AddMenuItem(menuItemDtoRequest, image);
        return new ResponseEntity<>("Menu item added successfully.", HttpStatus.CREATED);
    }

    @GetMapping("/ShowAllMenuItems")
    @Operation(summary = "Retrieve all menu items", description = "Fetches all menu items with pagination and sorting support")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Menu items retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<MenuItemDtoResponse>> showAllMenuItems(
            @Parameter(description = "Page number (0-indexed)") @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page") @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by") @RequestParam(required = false, defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction (ASC/DESC)") @RequestParam(required = false, defaultValue = "ASC") String sortDirection)
    {
        Pageable pageable = adjustPaginationAndSorting(page, size, sortBy, sortDirection);
        Page<MenuItemDtoResponse> menuItems = menuItemService.ShowAllMenuItems(pageable);
        return new ResponseEntity<>(menuItems, HttpStatus.OK);
    }

    @GetMapping("/SearchByName/{name}")
    @Operation(summary = "Search menu item by name", description = "Retrieves a specific menu item by its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Menu item found"),
            @ApiResponse(responseCode = "404", description = "Menu item not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<MenuItemDtoResponse> showMenuItemByName(
            @Parameter(description = "Menu item name") @PathVariable String name)
    {
        MenuItemDtoResponse menuItem = menuItemService.ShowMenuItem(name);
        return new ResponseEntity<>(menuItem, HttpStatus.OK);
    }

    @GetMapping("/SearchByPriceGreaterThanOrEqual")
    @Operation(summary = "Search menu items by minimum price", description = "Retrieves menu items with price greater than or equal to the specified amount")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Menu items found"),
            @ApiResponse(responseCode = "400", description = "Invalid price parameter"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<MenuItemDtoResponse>> showMenuItemsByPriceGreaterThanOrEqual(
            @Parameter(description = "Minimum price") @RequestParam BigDecimal price,
            @Parameter(description = "Page number (0-indexed)") @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page") @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by") @RequestParam(required = false, defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction (ASC/DESC)") @RequestParam(required = false, defaultValue = "ASC") String sortDirection)
    {
        Pageable pageable = adjustPaginationAndSorting(page, size, sortBy, sortDirection);
        Page<MenuItemDtoResponse> menuItems = menuItemService.ShowMenuItemsByPriceGreaterThanOrEqual(price, pageable);
        return new ResponseEntity<>(menuItems, HttpStatus.OK);
    }

    @GetMapping("/SearchByPriceLessThanOrEqual")
    @Operation(summary = "Search menu items by maximum price", description = "Retrieves menu items with price less than or equal to the specified amount")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Menu items found"),
            @ApiResponse(responseCode = "400", description = "Invalid price parameter"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<MenuItemDtoResponse>> showMenuItemsByPriceLessThanOrEqual(
            @Parameter(description = "Maximum price") @RequestParam BigDecimal price,
            @Parameter(description = "Page number (0-indexed)") @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page") @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by") @RequestParam(required = false, defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction (ASC/DESC)") @RequestParam(required = false, defaultValue = "ASC") String sortDirection)
    {
        Pageable pageable = adjustPaginationAndSorting(page, size, sortBy, sortDirection);
        Page<MenuItemDtoResponse> menuItems = menuItemService.ShowMenuItemsByPriceLessThanOrEqual(price, pageable);
        return new ResponseEntity<>(menuItems, HttpStatus.OK);
    }

    @GetMapping("/SearchByQuantity")
    @Operation(summary = "Search menu items by quantity", description = "Retrieves menu items with specified quantity available")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Menu items found"),
            @ApiResponse(responseCode = "400", description = "Invalid quantity parameter"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<MenuItemDtoResponse>> showMenuItemsByQuantity(
            @Parameter(description = "Quantity to search for") @RequestParam Integer quantity,
            @Parameter(description = "Page number (0-indexed)") @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page") @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by") @RequestParam(required = false, defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction (ASC/DESC)") @RequestParam(required = false, defaultValue = "ASC") String sortDirection)
    {
        Pageable pageable = adjustPaginationAndSorting(page, size, sortBy, sortDirection);
        Page<MenuItemDtoResponse> menuItems = menuItemService.ShowMenuItemsByQuantity(quantity, pageable);
        return new ResponseEntity<>(menuItems, HttpStatus.OK);
    }

    @PutMapping("/UpdateMenuItem/{name}")
    @Operation(summary = "Update an existing menu item", description = "Updates menu item details, price, quantity and/or image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Menu item updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
            @ApiResponse(responseCode = "404", description = "Menu item not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> updateMenuItem(
            @Parameter(description = "Current menu item name") @PathVariable String name,
            @Validated({CreateValidationGroup.class})
            @RequestPart(required = false) MenuItemDtoRequest menuItemDtoRequest,
            @RequestPart(required = false) MultipartFile image) throws IOException
    {
        menuItemService.UpdateMenuItem(name, menuItemDtoRequest, image);
        return new ResponseEntity<>("Menu item updated successfully.", HttpStatus.OK);
    }

    @DeleteMapping("/DeleteMenuItem/{name}")
    @Operation(summary = "Delete a menu item", description = "Deletes a menu item and its associated data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Menu item deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Menu item not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> deleteMenuItem(
            @Parameter(description = "Menu item name to delete") @PathVariable String name) throws IOException
    {
        menuItemService.DeleteMenuItem(name);
        return new ResponseEntity<>("Menu item " + name + " has been deleted successfully.", HttpStatus.OK);
    }

    @DeleteMapping("/DeleteModifierFromMenuItem/{name}")
    @Operation(summary = "Remove modifiers from menu item", description = "Removes one or more modifiers from a menu item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Modifiers removed successfully"),
            @ApiResponse(responseCode = "404", description = "Menu item or modifiers not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> deleteModifierFromMenuItem(
            @Parameter(description = "Menu item name") @PathVariable String name,
            @Parameter(description = "List of modifier names to remove") @RequestBody List<String> modifierNames)
    {
        Boolean isDeleted = menuItemService.DeleteMenuItemModifiers(name, modifierNames);

        if(isDeleted)
        {
            return new ResponseEntity<>("Modifiers removed from '" + name + "' successfully.", HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>("There is no modifiers were removed from '" + name + "'.", HttpStatus.OK);
        }
    }
}
