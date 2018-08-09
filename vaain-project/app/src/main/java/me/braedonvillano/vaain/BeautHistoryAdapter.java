package me.braedonvillano.vaain;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import me.braedonvillano.vaain.models.Appointment;

public class BeautHistoryAdapter extends RecyclerView.Adapter<BeautHistoryAdapter.ViewHolder> {

    List<Appointment> pastAppts;

    public BeautHistoryAdapter(List<Appointment> newPastAppts) {
        pastAppts = new ArrayList<>();
        pastAppts.addAll(newPastAppts);
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View requestView = inflater.inflate(R.layout.item_beaut_request, viewGroup, false);

        // Return a new holder
        ViewHolder viewHolder = new ViewHolder(requestView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder beautHistoryAdapter, int i) {
        Appointment appt = pastAppts.get(i);
        if (ParseUser.getCurrentUser().getBoolean("isClient")) {
            String beautName = appt.getBeaut().getString("Name");
            beautHistoryAdapter.tvClientName.setText(beautName);
        } else {
            String clientName = appt.getClient().getString("Name");
            beautHistoryAdapter.tvClientName.setText(clientName);
        }
        ParseFile clientImage = appt.getClient().getParseFile("profileImage");
        beautHistoryAdapter.tvDate.setText(appt.getStrDateTime());
        beautHistoryAdapter.tvProName.setText(appt.getProduct().getName());
        beautHistoryAdapter.tvPrice.setText("$ " + appt.getProduct().getPrice().toString());
        if (clientImage != null) {
            Glide.with(beautHistoryAdapter.itemView).load(clientImage.getUrl()).apply(RequestOptions.circleCropTransform()).into(beautHistoryAdapter.ivProImage);
            beautHistoryAdapter.ivProImage.loadInBackground();
        }
    }

    @Override
    public int getItemCount() {
        return pastAppts.size();
    }

    void clear(){
        pastAppts.clear();
    }

    void addAll(List<Appointment> newRequests){
        pastAppts.addAll(newRequests);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvClientName;
        public TextView tvProName;
        public TextView tvDate;
        public TextView tvPrice;

        public ParseImageView ivProImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvClientName = itemView.findViewById(R.id.tvClientName);
            tvProName = itemView.findViewById(R.id.tvProName);
            tvDate = itemView.findViewById(R.id.tvDate);
            ivProImage = itemView.findViewById(R.id.ivProImage);
            tvPrice = itemView.findViewById(R.id.tvPrice);


        }

    }
}
