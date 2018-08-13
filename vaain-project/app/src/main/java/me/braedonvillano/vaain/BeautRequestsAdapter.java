package me.braedonvillano.vaain;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.ParseFile;
import com.parse.ParseImageView;

import java.util.List;

import me.braedonvillano.vaain.models.Request;

public class BeautRequestsAdapter extends RecyclerView.Adapter<BeautRequestsAdapter.ViewHolder> {

    List<Request> requests;


    public static final int REQUEST_CODE = 100;

    static Callback callback;

    public interface Callback{
        void onDetail(Request request, int code);
    }

    public BeautRequestsAdapter(List<Request> requestArray, final Callback callback){
        requests = requestArray;
        this.callback = callback;
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
        // TODO: really need to do form validation all over
        Request request = requests.get(i);
        String clientName = request.getClient().getString("Name");
        viewHolder.tvClientName.setText(clientName);
        ParseFile clientImage = request.getClient().getParseFile("profileImage");
        viewHolder.tvDate.setText(request.getStrDateTime());
        viewHolder.tvProName.setText(request.getProduct().getName());
        viewHolder.tvPrice.setText("$ " + request.getProduct().getPrice().toString());
        if (clientImage != null) {
            Glide.with(viewHolder.itemView).load(clientImage.getUrl()).apply(RequestOptions.circleCropTransform()).into(viewHolder.ivProImage);
            viewHolder.ivProImage.loadInBackground();
        }

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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

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
            itemView.setOnClickListener(this);


        }

        // Handles the row being being clicked
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                Request request = requests.get(position);
                callback.onDetail(request,REQUEST_CODE);
            }
        }
    }
}
