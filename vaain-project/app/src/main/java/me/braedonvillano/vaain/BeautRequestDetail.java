package me.braedonvillano.vaain;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import me.braedonvillano.vaain.models.Appointment;
import me.braedonvillano.vaain.models.Product;
import me.braedonvillano.vaain.models.Request;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BeautRequestDetail.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BeautRequestDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BeautRequestDetail extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static  int LOCATION_CODE = 200;

    TextView tvProName;
    TextView tvDateTime;
    ImageView ivProImage;
    ParseImageView ivClientImage;
    TextView tvComments;
    TextView tvClientName;

    Button btnConfirm;
    Button btnCancel;

    Request request;


    private OnFragmentInteractionListener mListener;

    public BeautRequestDetail() {
    }


    public BeautRequestDetail newInstance(OnFragmentInteractionListener fragListener) {
        BeautRequestDetail fragment = new BeautRequestDetail();
        return fragment;
    }

    public void setRequest(Request request){
        this.request = request;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_beaut_request_detail, container, false);

        tvProName = view.findViewById(R.id.tvProName);
        tvDateTime = view.findViewById(R.id.tvDateTime);
        tvClientName = view.findViewById(R.id.tvClientName);
        tvComments = view.findViewById(R.id.tvComments);

        ivProImage = view.findViewById(R.id.ivProImage);
        ivClientImage = view.findViewById(R.id.ivClientImage);

        btnConfirm = view.findViewById(R.id.btnConfirm);
        btnCancel = view.findViewById(R.id.btnCancel);

        tvProName.setText(request.getProduct().getName());
        tvDateTime.setText(request.getStrDateTime());
        tvClientName.setText(request.getClient().getUsername());
        tvComments.setText(request.getDescription());

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    //
                try {
                    createNewAppt();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    request.delete();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mListener.onFragmentInteraction(request,LOCATION_CODE);


            }
        });

        if(request.getClient().get("profileImage") != null){
            ParseFile file = request.getClient().getParseFile("profileImage");
            Glide.with(this).load(file.getUrl()).apply(RequestOptions.circleCropTransform()).into(ivClientImage);
            ivClientImage.loadInBackground();
        }

        if(request.getProduct().getImage() != null) Glide.with(this).load(request.getProduct().getImage().getUrl()).into(ivProImage);

        return view;
    }

    private void createNewAppt() throws ParseException {
        Appointment newAppt = new Appointment();
        newAppt.setClient(request.getClient());
        newAppt.setBeaut(ParseUser.getCurrentUser());
        newAppt.setDateTime(request.getDateTime());
        newAppt.setDescription(request.getDescription());
        newAppt.setProduct(request.getProduct());
        newAppt.setStatus(false);

        saveAppt(newAppt);

        request.delete();
        mListener.onFragmentInteraction(request,LOCATION_CODE);



    }


    public void saveAppt(Appointment appt) {
        appt.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("BusinessFragment", "Created Appointment");
                    Toast.makeText(getContext(), "Appointment Confirmed", Toast.LENGTH_LONG).show();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Request request, int code);
    }
}
