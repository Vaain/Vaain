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
import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
            ParseFile productImage = request.getProduct().getImage();
            Date apptDateTime = request.getDateTime();
            DateFormat dateFormat = new SimpleDateFormat("M/d/yyyy h:m");
            String strDateTime = dateFormat.format(apptDateTime);
            viewHolder.tvDate.setText(strDateTime);
            viewHolder.tvProName.setText(request.getProduct().getName());
            if(productImage != null)Glide.with(viewHolder.itemView).load(productImage.getUrl()).into(viewHolder.ivProImage);

    }

    void clear(){
        requests.clear();
    }

    void addAll(List<Request> newRequests){
        requests = newRequests;
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvClientName;
        public TextView tvProName;
        public TextView tvDate;

        public ImageView ivProImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvClientName = itemView.findViewById(R.id.tvClientName);
            tvProName = itemView.findViewById(R.id.tvProName);
            tvDate = itemView.findViewById(R.id.tvDate);
            ivProImage = itemView.findViewById(R.id.ivProImage);
        }
    }
}
