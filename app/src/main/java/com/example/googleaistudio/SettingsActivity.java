package com.example.googleaistudio; // Make sure this matches your package name

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    // Constants for saving user preferences. Think of it as a small file to store settings.
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SELECTED_MODEL = "selectedModel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // 1. Find the RecyclerView in our layout
        RecyclerView modelsRecyclerView = findViewById(R.id.models_recycler_view);
        modelsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

// 2. Create an accurate list of available models
        List<AIModel> modelList = new ArrayList<>();
        modelList.add(new AIModel(
                "Gemini 2.5 Pro",
                "Our most powerful reasoning model, which excels at coding and complex reasoning tasks.",
                "gemini-2.5-pro" // Use the official model ID
        ));
        modelList.add(new AIModel(
                "Gemini 2.5 Flash",
                "Our hybrid reasoning model, with a 1M token context window.",
                "gemini-2.5-flash"
        ));
        modelList.add(new AIModel(
                "Gemini 2.5 Flash-Lite",
                "Our smallest and most cost effective model, built for at scale usage.",
                "gemini-2.5-flash-lite"
        ));
        modelList.add(new AIModel(
                "Nano Banana (Image Preview)",
                "State-of-the-art image generation and editing model.",
                "gemini-2.5-flash-image-preview"
        ));

        // 3. Create our adapter and define what happens when an item is clicked
        ModelAdapter adapter = new ModelAdapter(modelList, new ModelAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AIModel model) {
                // When a model is clicked, save its ID
                saveModelSelection(model.getModelId());
                Toast.makeText(SettingsActivity.this, model.getName() + " selected", Toast.LENGTH_SHORT).show();

                // Close the settings screen and return to MainActivity
                finish();
            }
        });

        // 4. Connect the RecyclerView to our adapter
        modelsRecyclerView.setAdapter(adapter);
    }

    // This method saves the chosen model ID to the device's storage
    private void saveModelSelection(String modelId) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SELECTED_MODEL, modelId);
        editor.apply(); // apply() saves the changes
    }
}