package me.braedonvillano.vaain;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Parcel;
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

import org.parceler.Parcels;

import java.util.List;

import me.braedonvillano.vaain.models.Appointment;
import me.braedonvillano.vaain.models.Appointments2;


public class ClientAccountAdapterAppoint extends RecyclerView.Adapter<ClientAccountAdapterAppoint.ViewHolder> {

    static List<Appointment> mappointments;
    public Context context;
    static Callback mcallback;
    public static final int PHONE_CODE = 800;

    public ClientAccountAdapterAppoint(List<Appointment> appointments) {
        mappointments = appointments;
//        mcallback = callback;
    }

    public ClientAccountAdapterAppoint(Context con) {
        context = con;
    }

    public interface Callback {
        void onRequestAppointment(Appointment appointment, int code);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_client_appoint, viewGroup, false);

        final ViewHolder viewHolder = new ViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Appointment appointment = mappointments.get(i);

        String beautName = appointment.getBeaut().getString("Name");
        viewHolder.beautApp.setText(beautName);

        String prodName = appointment.getProduct().getName();
        viewHolder.serviceApp.setText(prodName);

        String price = appointment.getProduct().getPrice().toString();
        viewHolder.priceApp.setText("$ "+ price);

        String date = appointment.getDateTime().toString().substring(0, 10);
        viewHolder.dateApp.setText(date);


        //String time = appointment.getDateTime().toString();
        if (appointment.getDateTime().toString().substring(11,12).compareTo("0") == 0) {
            String time1 = appointment.getDateTime().toString().substring(12,16);
            viewHolder.timeApp.setText(time1);
        } else {
            String time = appointment.getDateTime().toString().substring(11,16);
            viewHolder.timeApp.setText(time);
        }

        Glide.with(context)
                .load(appointment.getBeaut().getParseFile("profileImage")
                        .getUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(viewHolder.profilePicApp);

        viewHolder.phoneApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNum = appointment.getBeaut().get("phone").toString();
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
            }
        });


        }


    @Override
    public int getItemCount() { return mappointments.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView serviceApp;
        private TextView dateApp;
        private TextView timeApp;
        private TextView priceApp;
        private ImageView profilePicApp;
        private ImageButton phoneApp;
        private TextView beautApp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePicApp = itemView.findViewById(R.id.ivAppProPic);
            serviceApp = itemView.findViewById(R.id.tvSer);
            dateApp = itemView.findViewById(R.id.tvDate);
            timeApp = itemView.findViewById(R.id.tvTime);
            priceApp = itemView.findViewById(R.id.tvPrice);
            phoneApp = itemView.findViewById(R.id.ibtnPhone);
            beautApp = itemView.findViewById(R.id.tvBeautName);

            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Appointment appt = mappointments.get(position);
            Appointments2 appt2 = new Appointments2(appt);
            Intent intent = new Intent(context,ReqApptDetailActivity.class);
            intent.putExtra("appt",Parcels.wrap(appt2));
            context.startActivity(intent);

        }
    }

}
