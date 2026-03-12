package com.wajba.restaurant.Interfaces;

import com.wajba.restaurant.Dtos.MenuItemDtoRequest;
import com.wajba.restaurant.Dtos.MenuItemDtoResponse;
import com.wajba.restaurant.Models.Category;
import com.wajba.restaurant.Models.MenuItems;
import com.wajba.restaurant.Models.Modifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface IMenuItem
{
    public MenuItems AddMenuItem(MenuItemDtoRequest menuItemDtoRequest,MultipartFile Image) throws IOException;
    public Page<MenuItemDtoResponse> ShowAllMenuItems(Pageable pageable);
    public MenuItemDtoResponse ShowMenuItem(String Name);
    public Page<MenuItemDtoResponse> ShowMenuItemsByPriceGreaterThanOrEqual(BigDecimal Price, Pageable pageable);
    public Page<MenuItemDtoResponse> ShowMenuItemsByPriceLessThanOrEqual(BigDecimal Price, Pageable pageable);
    public Page<MenuItemDtoResponse> ShowMenuItemsByQuantity(Integer Quantity, Pageable pageable);
    public void DeleteMenuItem(String Name) throws IOException;
    public Boolean DeleteMenuItemModifiers(String Name,List<String> Modifiers);
    public MenuItems UpdateMenuItem(String OldName,MenuItemDtoRequest menuItemDto,MultipartFile multipartFile) throws IOException;
}
