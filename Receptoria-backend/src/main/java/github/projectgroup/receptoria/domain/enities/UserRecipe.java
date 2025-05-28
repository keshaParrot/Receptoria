package github.projectgroup.receptoria.domain.enities;

import github.projectgroup.receptoria.domain.enums.CookingMethod;
import github.projectgroup.receptoria.domain.enums.MealCategory;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRecipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "recipe_ingredients", joinColumns = @JoinColumn(name = "recipe_id"))
    private List<RecipeIngredients> ingredients;

    private String description;
    private String instructions;
    private int portionSize;

    @Enumerated(EnumType.STRING)
    private CookingMethod method;
    @Enumerated(EnumType.STRING)
    private MealCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "co_owner_id")
    private User coOwner;

    @OneToMany(mappedBy = "ratedRecipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reaction> reactions;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipePhoto> photos;

    private boolean isPublic;
}

