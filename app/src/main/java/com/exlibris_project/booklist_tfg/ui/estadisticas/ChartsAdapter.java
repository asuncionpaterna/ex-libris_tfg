package com.exlibris_project.booklist_tfg.ui.estadisticas;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.booklist_tfg.R;

import java.util.List;

public class ChartsAdapter extends RecyclerView.Adapter<ChartsAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName;
        private final Context context;

        ViewHolder(Context context, View itemView) {
            super(itemView);

            this.context = context;
            tvName = itemView.findViewById(R.id.tv_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Graficos chart = chartList.get(position);

                Intent intent = new Intent(context, chart.getActivityClass());
                context.startActivity(intent);
            }
        }
    }

    private final List<Graficos> chartList;

    ChartsAdapter(List<Graficos> chartList) {
        this.chartList = chartList;
    }

    @NonNull
    @Override
    public ChartsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        return new ViewHolder(context, inflater.inflate(R.layout.card_view_chart, parent, false));
    }

    @Override
    public void onBindViewHolder(ChartsAdapter.ViewHolder viewHolder, int position) {
        Graficos chart = chartList.get(position);

        TextView textView = viewHolder.tvName;
        textView.setText(chart.getName());
    }

    @Override
    public int getItemCount() {
        return chartList.size();
    }


}