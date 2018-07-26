package me.braedonvillano.vaain;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

import me.braedonvillano.vaain.models.Request;

public class BeautRequestsAdapter extends RecyclerView.Adapter<BeautRequestsAdapter.ViewHolder> {

    List<Request> requests;


    public BeautRequestsAdapter(List<Request> requestArray){
        requests = requestArray;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View requestView = inflater.inflate(R.layout.item_beaut_request, viewGroup, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(requestView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            Request request = requests.get(i);

            String clientName = request.getClient().getUsername();
            viewHolder.tvClientName.setText(clientName);

    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvClientName;
        public TextView tvProName;
        public TextView tvDate;
        public TextView tvTime;

        public ImageView ivProImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvClientName = itemView.findViewById(R.id.tvClientName);
            tvProName = itemView.findViewById(R.id.tvProName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            ivProImage = itemView.findViewById(R.id.ivProImage);
        }
    }
}
