package pro.sky.lesson29skypro.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
public class Ingredients {
    private int id;
    @NotBlank(message = "Обязательно введите имя ингредиента")
    private String nameIngredient;
    @Positive
    private int quantity;
    @NotBlank
    private String measureUnit;
}
