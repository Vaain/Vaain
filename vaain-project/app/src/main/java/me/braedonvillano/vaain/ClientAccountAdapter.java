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

import java.util.List;

import me.braedonvillano.vaain.models.Request;

public class ClientAccountAdapter extends RecyclerView.Adapter<ClientAccountAdapter.ViewHolder> {

    static List<Request> mrequests;
    public Context context;
    public ClientAcctReqFragment mclientAcctReqFragment;

    public ClientAccountAdapter(List<Request> requests) {
        mrequests = requests;
//        mclientAcctReqFragment = clientAcctReqFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_client_request, viewGroup,false);

        final ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Request request = mrequests.get(i);

        if(request.getProduct().getName() != null) {
            String prodName = request.getProduct().getName();
            viewHolder.service.setText(prodName);
        }


        String price = request.getProduct().getPrice().toString();
        viewHolder.price.setText(price);

        String date = request.getDateTime().toString().substring(0,10);
        viewHolder.date.setText(date);

        String time = request.getDateTime().toString().substring(11,16);
        viewHolder.time.setText(time);

            Glide.with(context)
                    .load(request.getBeaut().getParseFile("profileImage")
                            .getUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(viewHolder.profilePic);


    }

    @Override
    public int getItemCount() {
        return mrequests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView service;
        private TextView date;
        private TextView time;
        private TextView price;
        private ImageView profilePic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.ivAProPic);
            service = itemView.findViewById(R.id.tvAService);
            date = itemView.findViewById(R.id.tvADate);
            time = itemView.findViewById(R.id.tvATime);
            price = itemView.findViewById(R.id.tvAPrice);

        }
    }
}
