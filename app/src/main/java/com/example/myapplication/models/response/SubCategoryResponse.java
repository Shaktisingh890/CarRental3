package com.example.myapplication.models.response;

import java.util.List;

public class SubCategoryResponse {
    private String category;
    private List<String> subcategories;

    // Getters and Setters
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<String> subcategories) {
        this.subcategories = subcategories;
    }
}

