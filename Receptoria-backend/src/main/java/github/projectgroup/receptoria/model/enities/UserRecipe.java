package github.projectgroup.receptoria.model.enities;

import github.projectgroup.receptoria.model.enums.CookingMethod;
import github.projectgroup.receptoria.model.enums.MealCategory;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRecipe implements Ownable {
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

    @ElementCollection
    @CollectionTable(
            name = "recipe_photos",
            joinColumns = @JoinColumn(name = "recipe_id")
    )
    @Column(name = "photo_path", nullable = false)
    private List<String> photoPaths = new ArrayList<>();

    private boolean isPublic;

    @Override
    public Long getOwnerIdentifier() {
        return owner.getId();
    }
}

