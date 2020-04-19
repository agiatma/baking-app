package com.example.bakingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.R;
import com.example.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    Context context;
    private List<Step> stepList = new ArrayList<>();

    private final ItemClickListener<Step> mOnclickListener;


    public interface ItemClickListener<T>{
        void onListItemClicked(Step t);
    }

    public StepAdapter(Context context, ItemClickListener onClickListener){
        this.context = context;
        this.mOnclickListener = onClickListener;
    }
    public void setStepList(List<Step> stepData){
        stepList = new ArrayList<>();
        stepList = stepData;
    }

    public class StepViewHolder extends RecyclerView.ViewHolder{
        private View itemView;
        TextView tvSteps;
        TextView tvId;
        public StepViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvSteps = itemView.findViewById(R.id.tv_steps);
            tvId = itemView.findViewById(R.id.tv_idStep);
        }
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_step_layout,parent,false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder,final int position) {
        final String stepName = stepList.get(position).getShortDescription();
        final int id = stepList.get(position).getId();

        holder.tvSteps.setText(stepName);
        holder.tvId.setText(position+".");

        if ( position != 0){
            holder.tvId.setVisibility(View.VISIBLE);
        }
        else{
            holder.tvId.setVisibility(View.INVISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnclickListener.onListItemClicked(stepList.get(position));

            }
        });

    }

    @Override
    public int getItemCount() {
        return (stepList != null)? stepList.size():0;
    }






}
