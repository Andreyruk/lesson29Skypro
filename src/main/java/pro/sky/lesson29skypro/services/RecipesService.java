package pro.sky.lesson29skypro.services;

import pro.sky.lesson29skypro.model.Recipes;

import java.util.Collection;
import java.util.List;

public interface RecipesService {
    int addRecipe(Recipes recipe);

    Recipes getRecipe(int id);

    Collection<Recipes> getAllRecipe();

    Recipes editRecipe(int id, Recipes recipes);

    Recipes removeRecipe(int id);

//    List<Recipes> findRecipesByIngredient(int id);
//
    byte[] downloadRecipes();
}
