package com.wajba.restaurant.Repository;

import com.wajba.restaurant.Models.Category;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriesRepository extends JpaRepository<Category,Integer>
{
    public Boolean existsByName(String Name);
    @EntityGraph(attributePaths = {"image","menuItems"})
    @NonNull
    public Page<Category> findAll(@NonNull Pageable pageable);
    public Optional<Category> findByName(String Category);
}
