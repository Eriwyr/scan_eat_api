package com.codev.scan_eat_api.entities.recipe;

import com.codev.scan_eat_api.entities.Ingredient;
import com.codev.scan_eat_api.entities.Unit;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "recipe_ingredient")
public class RecipeContent implements Serializable{

    @JsonIgnore
    @EmbeddedId
    private RecipeContentIdentity recipeContentIdentity;

    @Column(name = "quantity")
    private float quantity;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_recipe", insertable=false, updatable=false)
    private Recipe recipe;

    @JsonIgnore
    @ManyToOne(cascade= CascadeType.ALL)
    @JoinColumn(name="barcode_ingredient",  insertable=false, updatable=false)
    private Ingredient ingredient;

    @Column(name = "id_unit")
    private int idUnit;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_unit", insertable=false, updatable=false)
    private Unit unit;

    @Transient
    private long ingredientBarcode;

    @Transient
    private String ingredientName;

    @Transient
    private String unitName;

    public RecipeContent(int idRecipe, long ingredientBarcode, float quantity, int idUnit) {
        this.setRecipeContentIdentity(new RecipeContentIdentity(idRecipe, ingredientBarcode));
        this.quantity = quantity;
        this.idUnit = idUnit;
    }

    public RecipeContent() {
    }

    public RecipeContentIdentity getRecipeContentIdentity() {
        return recipeContentIdentity;
    }

    @PostLoad
    public void init() {
        this.ingredientBarcode = ingredient.getBarcode();
        this.ingredientName = ingredient.getName();
        this.unitName = unit.getName();
    }

    public void setRecipeContentIdentity(RecipeContentIdentity recipeContentIdentity) {
        this.recipeContentIdentity = recipeContentIdentity;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public int getIdUnit() {
        return idUnit;
    }

    public void setIdUnit(int idUnit) {
        this.idUnit = idUnit;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public long getIngredientBarcode() {
        return ingredientBarcode;
    }

    public void setIngredientBarcode(long ingredientBarcode) {
        this.ingredientBarcode = ingredientBarcode;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    @Override
    public String toString() {
        return "RecipeContent{" +"\n"+
                "recipeContentIdentity=" + recipeContentIdentity +"\n"+
                //", actor=" + actor +"\n"+
                //", name='" + name + '\'' +
                '}';
    }

}

