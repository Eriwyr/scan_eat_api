package com.codev.scan_eat_api.repository;


import com.codev.scan_eat_api.enties.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeDao extends JpaRepository<Recipe, Integer> {
}