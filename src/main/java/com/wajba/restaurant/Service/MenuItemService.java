package com.wajba.restaurant.Service;

import com.wajba.restaurant.Dtos.MenuItemDtoRequest;
import com.wajba.restaurant.Dtos.MenuItemDtoResponse;
import com.wajba.restaurant.Exceptions.CategoryNotFoundException;
import com.wajba.restaurant.Exceptions.FoodAlreadyExistsException;
import com.wajba.restaurant.Exceptions.FoodNotFoundException;
import com.wajba.restaurant.Interfaces.IMenuItem;
import com.wajba.restaurant.Mappers.MenuItemMapper;
import com.wajba.restaurant.Models.Category;
import com.wajba.restaurant.Models.Image;
import com.wajba.restaurant.Models.MenuItems;
import com.wajba.restaurant.Models.Modifier;
import com.wajba.restaurant.Repository.CategoriesRepository;
import com.wajba.restaurant.Repository.MenuItemRepository;
import com.wajba.restaurant.Repository.ModifiersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MenuItemService implements IMenuItem
{
    private final ModifiersRepository modifiersRepository;
    private final MenuItemRepository menuItemRepository;
    private final CategoriesRepository categoriesRepository;
    @Value("${app.web.url}")
    private String WEB_URL;
    @Value("${app.upload.path}")
    private String FILE_SYSTEM;
    public MenuItemService
            (
                    ModifiersRepository modifiersRepository,
                    MenuItemRepository menuItemRepository,
                    CategoriesRepository categoriesRepository
            )
    {
        this.modifiersRepository = modifiersRepository;
        this.menuItemRepository = menuItemRepository;
        this.categoriesRepository = categoriesRepository;
    }

    private List<Modifier> RemoveDuplicateModifiers(List<String> Modifiers,MenuItems menuItems)
    {
        List<Modifier> NewModifiers = modifiersRepository.findByNameIgnoreCaseIn(Modifiers);
        List<Modifier> CurrentModifiers = menuItems.getModifiers();
        List<Modifier> ModifierToAdd = NewModifiers.stream()
                .filter(modifiers -> !CurrentModifiers.contains(modifiers))
                .toList();
        return ModifierToAdd;
    }

    @Transactional
    @Override
    @CacheEvict(value = "MenuItems", allEntries = true)
    public MenuItems AddMenuItem(MenuItemDtoRequest menuItemDtoRequest,MultipartFile Image) throws IOException {
        if(menuItemRepository.existsByName(menuItemDtoRequest.name()))
        {
            throw new FoodAlreadyExistsException("This food name " + menuItemDtoRequest.name() + " is already exists in menu items.");
        }
        MenuItems menuItems = new MenuItems();
        menuItems = MenuItemMapper.ToEntityRequest(menuItemDtoRequest);
        menuItems.setCreatedAt(LocalDateTime.now());
        AddMenuItemModifiers(menuItems,menuItemDtoRequest.modifiers());
        AddMenuItemsImage(menuItems,Image);
        AddMenuItemsCategory(menuItems,menuItemDtoRequest.category());
        return menuItemRepository.save(menuItems);
    }

    private void AddMenuItemModifiers(MenuItems menuItems, List<String> Modifiers)
    {
        List<Modifier> ModifiersList = new ArrayList<>();
        if(Modifiers != null)
        {
            ModifiersList = modifiersRepository.findByNameIgnoreCaseIn(Modifiers.stream().distinct().toList());
            menuItems.setModifiers(ModifiersList);
        }
    }


    private void AddMenuItemsImage(MenuItems menuItems, MultipartFile Image) throws IOException
    {
        if(Image != null)
        {
            Image image = new Image();
            image = ImageService.CreateNewImage(FILE_SYSTEM,WEB_URL,Image);
            menuItems.setImage(image);
        }
    }


    private void AddMenuItemsCategory(MenuItems menuItems, String CategoryName)
    {
        Category category = new Category();
        category = categoriesRepository.findByName(CategoryName)
                .orElseThrow(() -> new CategoryNotFoundException("This category name " + CategoryName + " doesn't exist in categories."));
        menuItems.setCategory(category);
    }

    @Override
    @Cacheable(value = "MenuItems",key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort.toString()")
    public Page<MenuItemDtoResponse> ShowAllMenuItems(Pageable pageable)
    {
        return menuItemRepository.findAll(pageable)
                .map(MenuItemMapper::ToDtoResponse);
    }

    @Override
    @Cacheable(value = "MenuItems", key = "#Name")
    public MenuItemDtoResponse ShowMenuItem(String Name)
    {
        return MenuItemMapper.ToDtoResponse
                (
                        menuItemRepository.findByName(Name)
                                .orElseThrow(() -> new FoodNotFoundException("The food name " + Name + " is not found."))
                );
    }

    @Override
    @Cacheable(value = "MenuItems", key = "'Gte' + '-' + #Price + '-' + #pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort.toString()")
    public Page<MenuItemDtoResponse> ShowMenuItemsByPriceGreaterThanOrEqual(BigDecimal Price, Pageable pageable)
    {
        return menuItemRepository.findByPriceGreaterThanEqual(Price,pageable)
                .map(MenuItemMapper::ToDtoResponse);
    }

    @Override
    @Cacheable(value = "MenuItems", key = "'Lte' + '-' + #Price + '-' + #pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort.toString()")
    public Page<MenuItemDtoResponse> ShowMenuItemsByPriceLessThanOrEqual(BigDecimal Price, Pageable pageable)
    {
        return menuItemRepository.findByPriceLessThanEqual(Price,pageable)
                .map(MenuItemMapper::ToDtoResponse);
    }

    @Override
    @Cacheable(value = "MenuItems", key = "#Quantity + '-' + #pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort.toString()")
    public Page<MenuItemDtoResponse> ShowMenuItemsByQuantity(Integer Quantity, Pageable pageable)
    {
        return menuItemRepository.findByQuantity(Quantity,pageable)
                .map(MenuItemMapper::ToDtoResponse);
    }

    @Override
    @CacheEvict(value = {"MenuItems","Category"}, allEntries = true)
    public void DeleteMenuItem(String Name) throws IOException
    {
        MenuItems menuItems = new MenuItems();
        menuItems = menuItemRepository.findByName(Name)
                .orElseThrow(() -> new FoodNotFoundException("The menu item name " + Name + " doesn't exist."));
        ImageService.DeleteFileImage(FILE_SYSTEM,menuItems);
        menuItemRepository.delete(menuItems);
    }

    @Override
    @CacheEvict(value = "MenuItems", key = "#Name")
    public Boolean DeleteMenuItemModifiers(String Name, List<String> Modifiers) {
        return menuItemRepository.deleteMenuItemModifiersByName(Name,Modifiers)!=0;
    }

    @Transactional
    @Override
    @CacheEvict(value = {"MenuItems","Category"}, allEntries = true)
    public MenuItems UpdateMenuItem(String OldName, MenuItemDtoRequest menuItemDtoRequest,MultipartFile Image) throws IOException {
        MenuItems menuItems = menuItemRepository.findByName(OldName)
                .orElseThrow(() -> new FoodNotFoundException("This food name " + OldName + " is doesn't exist in menu items."));
        UpdateMenuItemModifier(menuItems,menuItemDtoRequest.modifiers());
        UpdateMenuItemsImage(menuItems,Image);
        UpdateMenuItemsCategory(menuItems,menuItemDtoRequest.category());
        menuItems.setUpdatedAt(LocalDateTime.now());
        SaveNewDataAfterBeingUpdated(menuItems,menuItemDtoRequest);
        return menuItemRepository.save(menuItems);
    }

    private void UpdateMenuItemModifier(MenuItems menuItems, List<String> Modifiers)
    {
        if(Modifiers != null)
        {
            List<Modifier> NewModifiers = RemoveDuplicateModifiers(Modifiers.stream().distinct().toList(),menuItems);
            menuItems.getModifiers().addAll(NewModifiers);
        }
    }

    private void UpdateMenuItemsImage(MenuItems menuItems, MultipartFile Image) throws IOException
    {
        if(Image != null)
        {
            Image image = new Image();
            ImageService.DeleteFileImage(FILE_SYSTEM,menuItems);
            image = ImageService.CreateNewImage(FILE_SYSTEM,WEB_URL,Image);
            menuItems.setImage(image);
        }
    }

    private void UpdateMenuItemsCategory(MenuItems menuItems,String Name)
    {
        Category category = new Category();
        if(Name!=null && !Name.trim().isBlank())
        {
            category = categoriesRepository.findByName(Name)
                    .orElseThrow(() -> new CategoryNotFoundException("This category name " + Name + " doesn't exist."));
            menuItems.setCategory(category);
        }
    }

    private void SaveNewDataAfterBeingUpdated(MenuItems menuItems,MenuItemDtoRequest menuItemDtoRequest)
    {
        if(menuItemDtoRequest.name()!=null && !menuItemDtoRequest.name().isEmpty())
        {
            menuItems.setName(menuItemDtoRequest.name());
        }
        if(menuItemDtoRequest.price()!=null)
        {
            menuItems.setPrice(menuItemDtoRequest.price());
        }
        if(menuItemDtoRequest.quantity()!=null)
        {
            menuItems.setQuantity(menuItemDtoRequest.quantity());
        }
        if(menuItemDtoRequest.description()!=null && !menuItemDtoRequest.description().isEmpty())
        {
            menuItems.setDescription(menuItemDtoRequest.description());
        }
    }
}
