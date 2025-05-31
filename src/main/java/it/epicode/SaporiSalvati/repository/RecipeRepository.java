package it.epicode.SaporiSalvati.repository;

import it.epicode.SaporiSalvati.model.Recipe;
import it.epicode.SaporiSalvati.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByUser(User user);
}