package com.codev.scan_eat_api.controller.securedcontrollers;

import com.codev.scan_eat_api.entities.recipe.Recipe;
import com.codev.scan_eat_api.entities.User;
import com.codev.scan_eat_api.entities.recipe.RecipeContent;
import com.codev.scan_eat_api.entities.recipe.Serving;
import com.codev.scan_eat_api.exceptions.ExceptionGenerator;
import com.codev.scan_eat_api.exceptions.ScanEatException;
import com.codev.scan_eat_api.repository.*;
import com.codev.scan_eat_api.repository.RecipeIngredientRepository;
import com.codev.scan_eat_api.security.UserAuthenticationService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping(value={"public/recipes", "recipes"}) //public/ingredient is only for testing
@FieldDefaults(level = PRIVATE, makeFinal = true)
@AllArgsConstructor(access = PACKAGE)
public class SecuredRecipeController {
    @NonNull
    private UserAuthenticationService authentication;

    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final UnitRepository unitRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;

    @GetMapping("/all")
    ResponseEntity<Object> all(@AuthenticationPrincipal final User user) {
        if(user == null) {
            return ResponseEntity.ok().body(recipeRepository.findAll());
        }
        return ResponseEntity.ok().body(recipeRepository.findAllByOwnerAndDeleted(user.getUsername(), false));
    }

    @GetMapping("/find")
    ResponseEntity<Object> find(@AuthenticationPrincipal final User user, @RequestParam("recipeId") final int recipeId) throws ScanEatException {
        Optional<Recipe> recipeOpt = recipeRepository.findById(recipeId);
        if(!recipeOpt.isPresent() || recipeOpt.get().isDeleted()) {
            ExceptionGenerator.recipeNotFound(recipeId);
        }
        return ResponseEntity.ok(recipeOpt.get());
    }

    @Transactional
    @DeleteMapping("/delete")
    ResponseEntity<Object> delete(@AuthenticationPrincipal final User user, @RequestParam("recipeId") final int recipeId) throws ScanEatException {
        Optional<Recipe> recipeOpt = recipeRepository.findById(recipeId);
        if(!recipeOpt.isPresent() || recipeOpt.get().isDeleted()) {
            ExceptionGenerator.recipeNotFound(recipeId);
        }
        recipeOpt.get().setDeleted(true);
        recipeRepository.save(recipeOpt.get());
        return ResponseEntity.ok().build();
    }

    @Transactional
    @PutMapping("/create")
    ResponseEntity<Object> create(@AuthenticationPrincipal User user, @RequestBody Recipe recipe) throws ScanEatException {
        if(user == null) {
            user = userRepository.findAll().get(0);
        }
        recipe.setId(null);
        return createOrUpdate(user, recipe);
    }

    private void verifyRecipeContent(Recipe recipe) throws ScanEatException {
        for(RecipeContent content : recipe.getIngredients())
        {
            if(!ingredientRepository.existsByBarcode(content.getIngredientBarcode())) {
                ExceptionGenerator.ingredientNotFound(content.getIngredientBarcode());
            }
            if(!unitRepository.existsById(content.getIdUnit())) {
                ExceptionGenerator.unitNotFound(content.getIdUnit());
            }
        }
    }

    @Transactional
    @PutMapping("/update")
    ResponseEntity<Object> update(@AuthenticationPrincipal User user, @RequestBody Recipe recipe) throws ScanEatException {
        if(user == null) {
            user = userRepository.findAll().get(0);
        }
        Optional<Recipe> recipeOpt = recipeRepository.findById(recipe.getId());
        if (!recipeOpt.isPresent()) {
            ExceptionGenerator.recipeNotFound(recipe.getId());
        }

        Recipe dbRecipe = recipeRepository.findById(recipe.getId()).get();
        dbRecipe.setDeleted(true);
        recipeRepository.save(dbRecipe);

        return createOrUpdate(user, recipe);
    }

    @Transactional
    protected ResponseEntity<Object> createOrUpdate(User user, Recipe recipe) throws ScanEatException {
        verifyRecipeContent(recipe);

        List<RecipeContent> ingredients = recipe.getIngredients();
        recipe.setIngredients(new ArrayList<>());
        recipe.setOwner(user.getUsername());
        recipe.setId(null);
        recipeRepository.saveAndFlush(recipe);
        recipeIngredientRepository.deleteByRecipe(recipe);
        for(RecipeContent content : ingredients)
        {
            recipeIngredientRepository.save(
                    new RecipeContent(
                            recipe.getId(),
                            content.getIngredientBarcode(),
                            content.getQuantity(),
                            content.getIdUnit()
                    )
            );
        }

        recipeRepository.flush();
        recipeIngredientRepository.flush();

        return ResponseEntity.ok("{id: " + recipe.getId() + "}");
    }

    @GetMapping("/serving")
    ResponseEntity<Object> serving(@AuthenticationPrincipal User user, @RequestParam("recipeId") final int recipeId, @RequestParam("people") final int people) throws ScanEatException {
        Optional<Recipe> recipeOpt = recipeRepository.findById(recipeId);
        if (!recipeOpt.isPresent()) {
            ExceptionGenerator.recipeNotFound(recipeId);
        }
        Recipe recipe = recipeOpt.get();

        return ResponseEntity.ok(new Serving(recipe, people));
    }

    @ExceptionHandler({ScanEatException.class})
    public ResponseEntity<Object> onScanEatException(HttpServletRequest req, ScanEatException ex) {
        return ex.getResponseEntity();
    }
}
