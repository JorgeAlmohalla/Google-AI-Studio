package com.example.googleaistudio;

// A simple data class to hold information about an AI model.
public class AIModel {
    private String name;
    private String description;
    private String modelId; // The ID used for the API call, e.g., "gemini-1.5-pro-latest"

    public AIModel(String name, String description, String modelId) {
        this.name = name;
        this.description = description;
        this.modelId = modelId;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getModelId() {
        return modelId;
    }
}