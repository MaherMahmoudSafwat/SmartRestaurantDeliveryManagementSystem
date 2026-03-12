package com.wajba.restaurant.Repository;

import com.wajba.restaurant.Models.MenuItems;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItems,Integer>
{
    @EntityGraph(attributePaths = {"modifiers", "category", "image"})
    public Optional<MenuItems> findByName(String Name);

    @EntityGraph(attributePaths = {"modifiers", "category", "image"})
    @NonNull
    public Page<MenuItems> findAll(@NonNull Pageable pageable);

    public Boolean existsByName(String Name);

    @EntityGraph(attributePaths = {"modifiers", "category", "image"})
    public Page<MenuItems> findByQuantity(Integer Quantity, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM menu_items_modifiers " +
            "WHERE menu_item_id = (SELECT id FROM menu_items WHERE name = :menuItemName) " +
            "AND modifier_id IN (SELECT id FROM modifiers WHERE name IN (:modifierNames))",
            nativeQuery = true)
    Integer deleteMenuItemModifiersByName(@Param("menuItemName") String menuItemName,
                                    @Param("modifierNames") List<String> modifierNames);
    
    @EntityGraph(attributePaths = {"modifiers", "category", "image"})
    public Page<MenuItems> findByPriceLessThanEqual(@Param("Price") BigDecimal Price,Pageable pageable);

    @EntityGraph(attributePaths = {"modifiers", "category", "image"})
    public Page<MenuItems> findByPriceGreaterThanEqual(@Param("Price") BigDecimal Price,Pageable pageable);
}
