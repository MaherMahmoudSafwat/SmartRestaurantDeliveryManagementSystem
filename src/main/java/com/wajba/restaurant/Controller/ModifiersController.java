package com.wajba.restaurant.Controller;

import com.wajba.restaurant.Dtos.ModifierDtoRequest;
import com.wajba.restaurant.Dtos.ModifierDtoResponse;
import com.wajba.restaurant.Interfaces.CreateValidationGroup;
import com.wajba.restaurant.Interfaces.IModifiers;
import com.wajba.restaurant.Service.ModifiersService;
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

@RestController
@RequestMapping("/api/v1/modifiers")
@Tag(name = "Modifiers", description = "APIs for managing menu item modifiers with pricing and availability")
public class ModifiersController
{
    private final IModifiers modifiersService;

    @Value("${app.pagination.default-page}")
    private int defaultPage;

    @Value("${app.pagination.default-size}")
    private int defaultSize;

    @Value("${app.pagination.max-size}")
    private int maxSize;

    public ModifiersController(IModifiers modifiersService)
    {
        this.modifiersService = modifiersService;
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

    @PostMapping("/AddModifiers")
    @Operation(summary = "Create a new modifier", description = "Creates a new menu item modifier with name, price and image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Modifier created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
            @ApiResponse(responseCode = "409", description = "Modifier name already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> addModifier(
            @Validated({Default.class, CreateValidationGroup.class})
            @RequestPart ModifierDtoRequest modifierDtoRequest,
            @RequestPart(required = true) MultipartFile image) throws IOException
    {
        modifiersService.AddNewModifiers(modifierDtoRequest, image);
        return new ResponseEntity<>("Modifier added successfully.", HttpStatus.CREATED);
    }

    @GetMapping("/ShowAllModifiers")
    @Operation(summary = "Retrieve all modifiers", description = "Fetches all modifiers with pagination and sorting support")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Modifiers retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<ModifierDtoResponse>> showAllModifiers(
            @Parameter(description = "Page number (0-indexed)") @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page") @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by") @RequestParam(required = false, defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction (ASC/DESC)") @RequestParam(required = false, defaultValue = "ASC") String sortDirection)
    {
        Pageable pageable = adjustPaginationAndSorting(page, size, sortBy, sortDirection);
        Page<ModifierDtoResponse> modifiers = modifiersService.ShowAllModifiers(pageable);
        return new ResponseEntity<>(modifiers, HttpStatus.OK);
    }

    @GetMapping("/SearchByName/{name}")
    @Operation(summary = "Search modifier by name", description = "Retrieves a specific modifier by its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Modifier found"),
            @ApiResponse(responseCode = "404", description = "Modifier not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ModifierDtoResponse> showModifierByName(
            @Parameter(description = "Modifier name") @PathVariable String name)
    {
        ModifierDtoResponse modifier = modifiersService.ShowModifierByName(name);
        return new ResponseEntity<>(modifier, HttpStatus.OK);
    }

    @GetMapping("/SearchByPriceGreaterThanOrEqual")
    @Operation(summary = "Search modifiers by minimum price", description = "Retrieves modifiers with price greater than or equal to the specified amount")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Modifiers found"),
            @ApiResponse(responseCode = "400", description = "Invalid price parameter"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<ModifierDtoResponse>> showModifiersByPriceGreaterThanOrEqual(
            @Parameter(description = "Minimum price") @RequestParam BigDecimal price,
            @Parameter(description = "Page number (0-indexed)") @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page") @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by") @RequestParam(required = false, defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction (ASC/DESC)") @RequestParam(required = false, defaultValue = "ASC") String sortDirection)
    {
        Pageable pageable = adjustPaginationAndSorting(page, size, sortBy, sortDirection);
        Page<ModifierDtoResponse> modifiers = modifiersService.ShowAllModifiersByPriceGreaterThanOrEqual(price, pageable);
        return new ResponseEntity<>(modifiers, HttpStatus.OK);
    }

    @GetMapping("/SearchByPriceLessThanOrEqual")
    @Operation(summary = "Search modifiers by maximum price", description = "Retrieves modifiers with price less than or equal to the specified amount")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Modifiers found"),
            @ApiResponse(responseCode = "400", description = "Invalid price parameter"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<ModifierDtoResponse>> showModifiersByPriceLessThanOrEqual(
            @Parameter(description = "Maximum price") @RequestParam BigDecimal price,
            @Parameter(description = "Page number (0-indexed)") @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page") @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by") @RequestParam(required = false, defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction (ASC/DESC)") @RequestParam(required = false, defaultValue = "ASC") String sortDirection)
    {
        Pageable pageable = adjustPaginationAndSorting(page, size, sortBy, sortDirection);
        Page<ModifierDtoResponse> modifiers = modifiersService.ShowAllModifiersByPriceLessThanOrEqual(price, pageable);
        return new ResponseEntity<>(modifiers, HttpStatus.OK);
    }

    @PutMapping("/UpdateModifier/{name}")
    @Operation(summary = "Update an existing modifier", description = "Updates modifier details, price and/or image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Modifier updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
            @ApiResponse(responseCode = "404", description = "Modifier not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> updateModifier(
            @Parameter(description = "Current modifier name") @PathVariable String name,
            @Validated({CreateValidationGroup.class})
            @RequestPart ModifierDtoRequest modifierDtoRequest,
            @RequestPart(required = false) MultipartFile image) throws IOException
    {
        modifiersService.UpdateModifier(name, modifierDtoRequest, image);
        return new ResponseEntity<>("Modifier updated successfully.", HttpStatus.OK);
    }

    @DeleteMapping("/DeleteModifier/{name}")
    @Operation(summary = "Delete a modifier", description = "Deletes a modifier from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Modifier deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Modifier not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> deleteModifier(
            @Parameter(description = "Modifier name to delete") @PathVariable String name) throws IOException
    {
        modifiersService.DeleteModifier(name);
        return new ResponseEntity<>("Modifier " + name + " has been deleted successfully.", HttpStatus.OK);
    }
}