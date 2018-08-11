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
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ClientFollowAdapter extends RecyclerView.Adapter<ClientFollowAdapter.ViewHolder> {
    private List<ParseUser> beauts;
    private Context context;

    static Callback callback;

    public interface Callback{
        void onClickBeaut(ParseUser beaut, int code);
    }


    public ClientFollowAdapter(List<ParseUser> beautArray, Callback callback1){
        beauts = new ArrayList<>();
        beauts.addAll(beautArray);
        callback = callback1;
    }

    public ClientFollowAdapter(List<ParseUser> beautArray){
        beauts = new ArrayList<>();
        beauts.addAll(beautArray);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View requestView = inflater.inflate(R.layout.item_beaut, viewGroup, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(requestView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            ParseUser beaut = beauts.get(i);
            viewHolder.tvBeautName.setText(beaut.getString("Name"));
            viewHolder.tvEmail.setText(beaut.getUsername());

        if (beaut.get("profileImage") != null){
            ParseFile file = beaut.getParseFile("profileImage");
            Glide.with(context).load(file.getUrl()).apply(RequestOptions.circleCropTransform()).into(viewHolder.ivBeautImage);
            viewHolder.ivBeautImage.loadInBackground();
        }

    }

    void clearAll(){
        beauts.clear();
    }

    void addAll(List<ParseUser> newBeauts){
        beauts.addAll(newBeauts);
    }

    @Override
    public int getItemCount() {
        return beauts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView tvBeautName;
        public TextView tvEmail;

        public ParseImageView ivBeautImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBeautName = itemView.findViewById(R.id.tvBeautName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            ivBeautImage = itemView.findViewById(R.id.ivBeautImage);

            itemView.setOnClickListener(this);


        }

        // Handles the row being being clicked
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                ParseUser beaut = beauts.get(position);
                callback.onClickBeaut(beaut,SearchProductsAdapter.PROFILE_CODE);
            }
        }
    }
}
