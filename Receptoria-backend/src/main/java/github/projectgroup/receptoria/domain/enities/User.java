package github.projectgroup.receptoria.domain.enities;

import github.projectgroup.receptoria.domain.enums.MealCategory;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.autoconfigure.security.saml2.Saml2RelyingPartyProperties;

import java.util.List;

@Entity
@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;

    @ElementCollection(targetClass = MealCategory.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "recipe_meal_categories", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "meal_category")
    private List<MealCategory> mainMealCategories;

    @OneToMany(mappedBy = "followed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followers;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> follows;

    @OneToMany(mappedBy = "generatedFor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VerificationCode> verificationCodes;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRecipe> recipes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications;

    private boolean isVerified = false;
}
