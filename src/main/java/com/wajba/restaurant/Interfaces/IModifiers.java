package com.wajba.restaurant.Interfaces;

import com.wajba.restaurant.Dtos.ModifierDtoRequest;
import com.wajba.restaurant.Dtos.ModifierDtoResponse;
import com.wajba.restaurant.Models.Modifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;

public interface IModifiers
{
    public Modifier AddNewModifiers(ModifierDtoRequest modifierDtoRequest,MultipartFile Image) throws IOException;
    public Page<ModifierDtoResponse> ShowAllModifiers(Pageable pageable);
    public ModifierDtoResponse ShowModifierByName(String Name);
    public Page<ModifierDtoResponse> ShowAllModifiersByPriceGreaterThanOrEqual(BigDecimal Price,Pageable pageable);
    public Page<ModifierDtoResponse> ShowAllModifiersByPriceLessThanOrEqual(BigDecimal Price,Pageable pageable);
    public void DeleteModifier(String Name) throws IOException;
    public Modifier UpdateModifier(String OldName, ModifierDtoRequest modifierDtoRequest, MultipartFile Image) throws IOException;
}
