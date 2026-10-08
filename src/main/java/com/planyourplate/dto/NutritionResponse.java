package com.planyourplate.dto;

public class NutritionResponse {

    private double calories;
    private double protein;
    private double carbohydrates;
    private double fat;
    private double fiber;

    public NutritionResponse() {
    }

    public NutritionResponse(
            double calories,
            double protein,
            double carbohydrates,
            double fat,
            double fiber) {

        this.calories = calories;
        this.protein = protein;
        this.carbohydrates = carbohydrates;
        this.fat = fat;
        this.fiber = fiber;
    }

    public double getCalories() {
        return calories;
    }

    public double getProtein() {
        return protein;
    }

    public double getCarbohydrates() {
        return carbohydrates;
    }

    public double getFat() {
        return fat;
    }

    public double getFiber() {
        return fiber;
    }
}
