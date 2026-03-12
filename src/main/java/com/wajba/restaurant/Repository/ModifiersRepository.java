package com.wajba.restaurant.Repository;

import com.wajba.restaurant.Models.Modifier;
import jakarta.transaction.Transactional;
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
public interface ModifiersRepository extends JpaRepository<Modifier,Integer>
{
    public Optional<Modifier> findByNameIgnoreCase(String Name);
    @EntityGraph(attributePaths = {"image"})
    public List<Modifier> findByNameIgnoreCaseIn(List<String> Names);
    public Boolean existsByName(String Name);
    @EntityGraph(attributePaths = {"image"})
    public Page<Modifier> findByPriceGreaterThanEqual(BigDecimal Price, Pageable pageable);
    @EntityGraph(attributePaths = {"image"})
    public Page<Modifier> findByPriceLessThanEqual(BigDecimal Price, Pageable pageable);
}

