package com.codev.scan_eat_api.entities.ingredient;

import com.codev.scan_eat_api.entities.Unit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.util.Pair;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Ingredient {
    @Id
    @Column(name = "barcode")
    private long barcode;

    @Column(name = "name")
    private String name;

    @JsonIgnore
    @Column(name = "last_refresh")
    private long lastRefresh;

    /*@Column(name = "quantity")
    private String quantity;*/

    @ManyToOne(cascade= CascadeType.ALL)
    @JoinColumn(name="id_unit")
    private Unit unit;

    @Column(name = "nutriscore")
    private Character nutriscore;

    @Column(name = "kcal_100g")
    private double kcal100g;

    @Column(name = "proteins_100g")
    private double proteins100g;

    @Column(name = "sugars_100g")
    private double sugars100g;

    @Column(name = "fat_100g")
    private double fat100g;

    @Column(name = "salt_100g")
    private double salt100g;

    @Column(name = "fiber_100g")
    private double fiber100g;

    @Column(name = "image_url")
    private String imageUrl;

    @JsonIgnore
    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name = "barcode_ingredient")
    private List<IngredientAdditive> additives;

    @Transient
    private List<String> additiveTags;

    @Transient
    private List<Pair<Integer, String>> possibleUnits;

    public Ingredient() {
    }

    public Ingredient(long barcode) {
        this.barcode = barcode;
    }

    @PostLoad
    public void initIngredient() {
        this.additiveTags = this.additives.stream().map(a -> a.getAdditive().getId()).collect(Collectors.toList());
    }

    public long getBarcode() {
        return barcode;
    }

    public void setBarcode(long barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLastRefresh() {
        return lastRefresh;
    }

    public void setLastRefresh(long lastRefresh) {
        this.lastRefresh = lastRefresh;
    }

    /*public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }*/

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Character getNutriscore() {
        return nutriscore;
    }

    public void setNutriscore(Character nutriscore) {
        this.nutriscore = nutriscore;
    }

    public Integer getNutriscoreInt() {
        if(nutriscore == null) {return null;}
        List<Character> nutriScores = Arrays.asList('A', 'B', 'C', 'D', 'E');
        return nutriScores.indexOf(nutriscore)+1;
    }

    public double getKcal100g() {
        return kcal100g;
    }

    public void setKcal100g(double kcal100g) {
        this.kcal100g = kcal100g;
    }

    public double getProteins100g() {
        return proteins100g;
    }

    public void setProteins100g(double proteins100g) {
        this.proteins100g = proteins100g;
    }

    public double getSugars100g() {
        return sugars100g;
    }

    public void setSugars100g(double sugars100g) {
        this.sugars100g = sugars100g;
    }

    public double getFat100g() {
        return fat100g;
    }

    public void setFat100g(double fat100g) {
        this.fat100g = fat100g;
    }

    public double getSalt100g() {
        return salt100g;
    }

    public void setSalt100g(double salt100g) {
        this.salt100g = salt100g;
    }

    public double getFiber100g() {
        return fiber100g;
    }

    public void setFiber100g(double fiber100g) {
        this.fiber100g = fiber100g;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<IngredientAdditive> getAdditives() {
        return additives;
    }

    public void setAdditives(List<IngredientAdditive> additives) {
        this.additives = additives;
        this.additiveTags = this.additives.stream().map(a -> a.getAdditive().getId()).collect(Collectors.toList());
    }

    public List<String> getAdditiveTags() {
        return additiveTags;
    }

    public void setAdditiveTags(List<String> additiveTags) {
        this.additiveTags = additiveTags;
    }

    public List<Pair<Integer, String>> getPossibleUnits() {
        return possibleUnits;
    }

    public void setPossibleUnits(List<Pair<Integer, String>> possibleUnits) {
        this.possibleUnits = possibleUnits;
    }
}
