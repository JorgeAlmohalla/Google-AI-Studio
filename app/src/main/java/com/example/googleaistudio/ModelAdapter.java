package com.example.googleaistudio;

// Import statements needed for the adapter
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;


public class ModelAdapter extends RecyclerView.Adapter<ModelAdapter.ModelViewHolder> {

    private List<AIModel> modelList;
    private OnItemClickListener listener;

    // Interface for handling clicks
    public interface OnItemClickListener {
        void onItemClick(AIModel model);
    }

    public ModelAdapter(List<AIModel> modelList, OnItemClickListener listener) {
        this.modelList = modelList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_model, parent, false);
        return new ModelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModelViewHolder holder, int position) {
        AIModel model = modelList.get(position);
        holder.bind(model, listener);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    // The ViewHolder class
    static class ModelViewHolder extends RecyclerView.ViewHolder {
        TextView modelName;
        TextView modelDescription;

        public ModelViewHolder(@NonNull View itemView) {
            super(itemView);
            modelName = itemView.findViewById(R.id.model_name);
            modelDescription = itemView.findViewById(R.id.model_description);
        }

        public void bind(final AIModel model, final OnItemClickListener listener) {
            modelName.setText(model.getName());
            modelDescription.setText(model.getDescription());
            itemView.setOnClickListener(v -> listener.onItemClick(model));
        }
    }
}