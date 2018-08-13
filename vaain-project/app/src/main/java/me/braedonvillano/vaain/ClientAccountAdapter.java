package me.braedonvillano.vaain;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
        final Request request = mrequests.get(i);

        if(request.getProduct().getName() != null) {
            String prodName = request.getProduct().getName();
            viewHolder.service.setText(prodName);
        }

        if (request.getBeaut().getString("Name") != null) {
            String beautName = request.getBeaut().getString("Name");
            viewHolder.beautName.setText(beautName);
        }

        String price = request.getProduct().getPrice().toString();
        viewHolder.price.setText("$ " + price);

        String date = request.getDateTime().toString().substring(0,10);
        viewHolder.date.setText(date);

        String time = request.getDateTime().toString().substring(11,16);
        viewHolder.time.setText(time);

            Glide.with(context)
                    .load(request.getBeaut().getParseFile("profileImage")
                            .getUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(viewHolder.profilePic);

        viewHolder.phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNum = request.getBeaut().get("phone").toString();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                context.startActivity(intent);
                // get it to return back to the app after the call is over
            }
        });


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
        private ImageButton phone;
        private TextView beautName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.ivAProPic);
            service = itemView.findViewById(R.id.tvAService);
            date = itemView.findViewById(R.id.tvADate);
            time = itemView.findViewById(R.id.tvATime);
            price = itemView.findViewById(R.id.tvAPrice);
            phone = itemView.findViewById(R.id.ivbtnPhone2);
            beautName = itemView.findViewById(R.id.tvABeautName);

        }
    }
}
