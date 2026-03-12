package com.wajba.restaurant.Service;

import com.wajba.restaurant.Dtos.ModifierDtoRequest;
import com.wajba.restaurant.Dtos.ModifierDtoResponse;
import com.wajba.restaurant.Exceptions.ModifierAlreadyExistsException;
import com.wajba.restaurant.Exceptions.ModifierNotFoundException;
import com.wajba.restaurant.Interfaces.IModifiers;
import com.wajba.restaurant.Mappers.ModifiersMapper;
import com.wajba.restaurant.Models.Image;
import com.wajba.restaurant.Models.Modifier;
import com.wajba.restaurant.Repository.ModifiersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class ModifiersService implements IModifiers
{

    private final ModifiersRepository modifiersRepository;

    @Value("${app.web.url}")
    private String WEB_URL;
    @Value("${app.upload.path}")
    private String FILE_SYSTEM;

    public ModifiersService(ModifiersRepository modifiersRepository)
    {
        this.modifiersRepository = modifiersRepository;
    }

    @CacheEvict(value = "Modifiers" , allEntries = true)
    @Override
    @Transactional
    public Modifier AddNewModifiers(ModifierDtoRequest modifierDtoRequest, MultipartFile Image) throws IOException {
        if(modifiersRepository.existsByName(modifierDtoRequest.name()))
        {
            throw new ModifierAlreadyExistsException("The modifier name " + modifierDtoRequest.name() + " already exists.");
        }
        Modifier modifier = new Modifier();
        modifier = ModifiersMapper.ToEntityRequest(modifierDtoRequest);
        modifier.setCreatedAt(LocalDateTime.now());
        AddNewModifierImages(modifier,Image);
        return modifiersRepository.save(modifier);
    }

    private void AddNewModifierImages(Modifier modifier,MultipartFile Image) throws IOException {
        if(Image != null)
        {
            Image image = new Image();
            image = ImageService.CreateNewImage(FILE_SYSTEM,WEB_URL,Image);
            modifier.setImage(image);
        }
    }
    @Cacheable(value = "Modifiers", key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort.toString()")
    @Override
    public Page<ModifierDtoResponse> ShowAllModifiers(Pageable pageable)
    {
        return modifiersRepository.findAll(pageable)
                .map(ModifiersMapper::ToDtoResponse);
    }
    @Cacheable(value = "Modifiers", key = "#Name")
    @Override
    public ModifierDtoResponse ShowModifierByName(String Name)
    {
        return ModifiersMapper.ToDtoResponse
                (
                        modifiersRepository.findByNameIgnoreCase(Name)
                                .orElseThrow(() -> new ModifierNotFoundException("The modifier name " + Name + " doesn't exist."))
                );
    }

    @Cacheable(value = "Modifiers", key = "'Gte' + '-' + #Price + '-' + #pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort.toString()")
    @Override
    public Page<ModifierDtoResponse> ShowAllModifiersByPriceGreaterThanOrEqual(BigDecimal Price, Pageable pageable)
    {
        return modifiersRepository.findByPriceGreaterThanEqual(Price,pageable)
                .map(ModifiersMapper::ToDtoResponse);
    }

    @Cacheable(value = "Modifiers", key = "'Lte' + '-' + #Price + '-' + #pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort.toString()")
    @Override
    public Page<ModifierDtoResponse> ShowAllModifiersByPriceLessThanOrEqual(BigDecimal Price, Pageable pageable)
    {
        return modifiersRepository.findByPriceLessThanEqual(Price,pageable)
                .map(ModifiersMapper::ToDtoResponse);
    }

    @CacheEvict(value = {"Modifiers","MenuItems"}, allEntries = true)
    @Override
    public void DeleteModifier(String Name) throws IOException {
        Modifier modifier = new Modifier();
        modifier = modifiersRepository.findByNameIgnoreCase(Name)
                .orElseThrow(() -> new ModifierNotFoundException("The modifier name " + Name + " doesn't exist."));
        ImageService.DeleteFileImage(FILE_SYSTEM,modifier);
        modifiersRepository.delete(modifier);
    }

    @CacheEvict(value = {"Modifiers","MenuItems"}, allEntries = true)
    @Override
    public Modifier UpdateModifier(String OldName, ModifierDtoRequest modifierDtoRequest, MultipartFile Image) throws IOException
    {
        Modifier modifier = new Modifier();
        modifier = modifiersRepository.findByNameIgnoreCase(OldName)
                .orElseThrow(() -> new ModifierNotFoundException("The modifier name " + OldName + " doesn't exist."));
        SaveNewDataAfterBeingUpdated(modifier, modifierDtoRequest);
        modifier.setUpdatedAt(LocalDateTime.now());
        UpdateModifierImage(modifier,Image);
        return modifiersRepository.save(modifier);
    }

    private void UpdateModifierImage(Modifier modifier,MultipartFile Image) throws IOException {
        if(Image != null)
        {
            Image image = new Image();
            ImageService.DeleteFileImage(FILE_SYSTEM,modifier);
            image = ImageService.CreateNewImage(FILE_SYSTEM,WEB_URL,Image);
            modifier.setImage(image);
        }
    }

    private void SaveNewDataAfterBeingUpdated(Modifier modifier,ModifierDtoRequest modifierDtoRequest)
    {
        if(modifierDtoRequest.name()!=null && !modifierDtoRequest.name().isEmpty())
        {
            modifier.setName(modifierDtoRequest.name());
        }
        if(modifierDtoRequest.price()!=null)
        {
            modifier.setPrice(modifierDtoRequest.price());
        }
    }
}
