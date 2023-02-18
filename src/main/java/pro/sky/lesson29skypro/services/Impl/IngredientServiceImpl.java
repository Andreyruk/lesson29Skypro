package pro.sky.lesson29skypro.services.Impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import pro.sky.lesson29skypro.model.Ingredients;
import pro.sky.lesson29skypro.common.CommonUtils;
import pro.sky.lesson29skypro.services.IngredientService;

import java.util.Collection;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {
    private TreeMap<Integer, Ingredients> ingredientsMap = new TreeMap<>();

    private static int id = 0;

    @Value("${sources.ingredients:}")
    private String directory;

    private String sourceDir;

    @PostConstruct
    public void checkDirectoryIngredient() {
        sourceDir = CommonUtils.createDirectory(directory);
        ingredientsMap = CommonUtils.readObjects(sourceDir, Ingredients.class);
        if (!ingredientsMap.isEmpty()) {
            id = ingredientsMap.lastKey();
        }
    }

    @Override
    public int addIngredient(Ingredients ingredient) {
        ingredient.setId(++id);
        writeIngredient(ingredient.getId(),ingredient);

        return id;
    }

    @Override
    public Ingredients getIngredient(int id) {
        if (!ingredientsMap.containsKey(id)) {
            throw new NotFoundException("Ингредиент с заданным id не найден");
        }
        return ingredientsMap.get(id);
    }

    @Override
    public Collection<Ingredients> getAllIngredient() {
        return ingredientsMap.values();
    }

    @Override
    public Ingredients editIngredients(int id, Ingredients ingredient) {
        if (!ingredientsMap.containsKey(id)) {
            throw new NotFoundException("Ингредиент с заданным id не найден");
        }
        writeIngredient(id, ingredient);
        return ingredient;
    }

    @Override
    public void removeIngredients(int id) {
        if (!ingredientsMap.containsKey(id)) {
            throw new NotFoundException("Ингредиент с заданным id отсутствует");
        }
        if (CommonUtils.deleteFile(String.format("%s/%s.json",sourceDir, id))) {
           ingredientsMap.remove(id);
        } else {
            throw new RuntimeException(String.format("не удалось удалить ингредиент с id %s", id));
        }
    }

    private void writeIngredient(int id, Ingredients ingredient) {
        if (CommonUtils.writeFile(sourceDir, id, ingredient)) {
            ingredientsMap.put(id, ingredient);
        } else {
            throw new RuntimeException("Не удалось записать ингредиент в файл");
        }
    }
}
