// src/main/java/github/projectgroup/receptoria/domain/enities/RecipePhoto.java
package github.projectgroup.receptoria.domain.enities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recipe_photo")  // ← явно вказуємо ім’я таблиці
@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecipePhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "recipe_id",
            referencedColumnName = "id"  // ← вказуємо, що recipe_id → user_recipe.id
    )
    private UserRecipe recipe;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "content_type")
    private String contentType;
}
