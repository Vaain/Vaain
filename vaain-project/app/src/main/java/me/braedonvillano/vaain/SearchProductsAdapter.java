package me.braedonvillano.vaain;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import me.braedonvillano.vaain.models.Product;

public class SearchProductsAdapter extends RecyclerView.Adapter<SearchProductsAdapter.ViewHolder>  {

    List<Product> mProducts;
    List<Product> mCopyProducts;
    public Context context;
    private Dialog myDialog;
    private CardView cardView;
    static Callback callback;
    public static final int REQUEST_CODE = 300;
    public static final int PROFILE_CODE = 400;

    public interface Callback {
        void onRequestProduct(Product product, int code);
    }


    public SearchProductsAdapter(List<Product> products, @NonNull final Callback callback) {
        mProducts = products;
        mCopyProducts = products;
        this.callback = callback;

    }

    public void clear() {
        mProducts.clear();
        notifyDataSetChanged();
    }

    // add a list of items -- change to type used
    public void addAll(List<Product> list) {
        mProducts.addAll(list);
        notifyDataSetChanged();
    }

    public void add(List<Product> product) {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int b) {
        context = viewGroup.getContext();
        LayoutInflater i = LayoutInflater.from(context);
        View view = i.inflate(R.layout.item_home_grid, viewGroup, false);
        //view.setOnClickListener(mClickListener);

        final ViewHolder vHolder = new ViewHolder(view);

        myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.item_home);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        vHolder.rlHomeGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Product curProd = mProducts.get(vHolder.getAdapterPosition());

                ImageView dlgProfilePic = myDialog.findViewById(R.id.ivDProfilePic);
                TextView dlgBeautName = myDialog.findViewById(R.id.tvDiaBeautName);
                TextView dlgPrice = myDialog.findViewById(R.id.tvDPrice);
                TextView dlgDescription = myDialog.findViewById(R.id.tvDDescript);
                ImageView dlgProductPic = myDialog.findViewById(R.id.ivDProduct);
                Button dlgRequest = myDialog.findViewById(R.id.btnDRequest);
                TextView dlgProductName = myDialog.findViewById(R.id.tvDProName);

                dlgBeautName.setText(curProd.getBeaut().getString("Name"));
                dlgPrice.setText(curProd.getPrice().toString());
                dlgDescription.setText(curProd.getDescription());
                dlgProductName.setText(curProd.getName());

                Glide.with(context)
                        .load(curProd.getImage()
                        .getUrl())
                        .into(dlgProductPic);
                Glide.with(context)
                        .load(curProd.getBeaut().getParseFile("profileImage")
                        .getUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(dlgProfilePic);

                dlgRequest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            Product curProd = mProducts.get(vHolder.getAdapterPosition());
                            callback.onRequestProduct(curProd,REQUEST_CODE);
                            myDialog.dismiss();
                        }
                    });
                    myDialog.show();
                    dlgProfilePic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Product curProd = mProducts.get(vHolder.getAdapterPosition());
                            callback.onRequestProduct(curProd,PROFILE_CODE);
                            myDialog.dismiss();
                            Log.d("request","beaut clicked");
                        }
                    });
                }
        });

        vHolder.rlHomeGrid.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Product likeProd = mProducts.get(vHolder.getAdapterPosition());
                int current = vHolder.cv.getCardBackgroundColor().getDefaultColor();
                String currentStr = Integer.toHexString(current);
                if (currentStr.compareTo("ff484848") == 0) {
                    vHolder.cv.setCardBackgroundColor(Color.parseColor("#FF4081"));
                    ParseUser user = ParseUser.getCurrentUser();
                    likeProd.add("usherLike", user.getObjectId());
                    likeProd.saveInBackground();

                } else {
                    vHolder.cv.setCardBackgroundColor(Color.parseColor("#484848"));
                    ParseUser user = ParseUser.getCurrentUser();
                    ArrayList list = new ArrayList();
                    list.add(user.getObjectId());
                    likeProd.removeAll("usherLike", list);
                    likeProd.saveInBackground();
                }
                return true;
            }
        });
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        final Product product = mProducts.get(position);
        ParseUser parseUser = ParseUser.getCurrentUser();
        List<String> likees = (List<String>) product.get("usherLike");

        if (likees != null) {
            if (likees.contains(parseUser.getObjectId())) {
                viewHolder.cv.setCardBackgroundColor(Color.parseColor("#FF4081"));
            }
        }

        viewHolder.tvBeautName.setText(product.getBeaut().getString("Name"));

        viewHolder.tvProductName.setText(product.getName());
        if (product.getImage() != null) {
            Glide.with(context)
                    .load(product.getImage().getUrl())
                    .into(viewHolder.ivProductImage);
        }
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cv;
        public RelativeLayout rlHomeGrid;
        public TextView tvProductName;
        public TextView tvBeautName;
        public ImageView ivProductImage;


        public ViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            rlHomeGrid = itemView.findViewById(R.id.item_home_grid);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvBeautName = itemView.findViewById(R.id.tvBeautName);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
        }
    }
}
