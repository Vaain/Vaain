package me.braedonvillano.vaain;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import me.braedonvillano.vaain.models.Product;

public class SearchProductsAdapter extends RecyclerView.Adapter<SearchProductsAdapter.ViewHolder>  {

    static List<Product> mProducts;
    public Context context;
    private Dialog myDialog;
    static Callback callback;

    public interface Callback {
        void onRequestProduct(Product product);
    }


    public SearchProductsAdapter(List<Product> products, @NonNull final Callback callback) {
        mProducts = products;
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

    public void add(Product product) {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int b) {
        context = viewGroup.getContext();
        LayoutInflater i = LayoutInflater.from(context);
        View view = i.inflate(R.layout.item_home_grid, viewGroup, false);
        //view.setOnClickListener(mClickListener);

        final ViewHolder vHolder = new ViewHolder(view);

        // Dialog ini
        myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.item_home);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



        vHolder.item_home_grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Product curProd = mProducts.get(vHolder.getAdapterPosition());

                ImageView Dialog_profilePic = myDialog.findViewById(R.id.ivDProfilePic);
                TextView Dialog_nameBeaut = myDialog.findViewById(R.id.tvDiaBeautName);
                TextView Dialog_price = myDialog.findViewById(R.id.tvDPrice);
                TextView Dialog_descript = myDialog.findViewById(R.id.tvDDescript);
                //TextView Dialog_location = (TextView) myDialog.findViewById(R.id.tvDLoc);
                ImageView Dialog_productpic = myDialog.findViewById(R.id.ivDProduct);
                Button Dialog_request = myDialog.findViewById(R.id.btnDRequest);
                TextView Dialog_nameProduct = myDialog.findViewById(R.id.tvDProName);


                Dialog_nameBeaut.setText(curProd.getBeaut().getUsername());
                Dialog_price.setText(curProd.getPrice().toString());
                Dialog_descript.setText(curProd.getDescription());
                //Dialog_location.setText(product.get(vHolder.getAdapterPosition()).getLocation());
                Dialog_nameProduct.setText(curProd.getName());

                // add profile and product pic to modal
                Glide.with(context)
                        .load(curProd.getImage()
                        .getUrl())
                        .into(Dialog_productpic);
                Glide.with(context)
                        .load(curProd.getBeaut().getParseFile("profileImage")
                        .getUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(Dialog_profilePic);

                Dialog_request.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Product curProd = mProducts.get(vHolder.getAdapterPosition());

                        callback.onRequestProduct(curProd);
                        // Create new fragment and transaction

                        myDialog.dismiss();
                    }

                });

                myDialog.show();
            }
        });

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        final Product product = mProducts.get(position);

        viewHolder.BeautName.setText(product.getBeaut().getUsername());
        viewHolder.ProductName.setText(product.getName());
        if (product.getImage() != null) {
            Glide.with(context)
                    .load(product.getImage().getUrl())
                    .into(viewHolder.productImage);
        }
    }


    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout item_home_grid;
        public TextView ProductName;
        public TextView BeautName;
        public ImageView productImage;


        public ViewHolder(View itemView) {
            super(itemView);

            item_home_grid = itemView.findViewById(R.id.item_home_grid);
            ProductName = itemView.findViewById(R.id.tvProductName);
            BeautName = itemView.findViewById(R.id.tvBeautName);
            productImage = itemView.findViewById(R.id.ivProductImage);


        }
    }


}
