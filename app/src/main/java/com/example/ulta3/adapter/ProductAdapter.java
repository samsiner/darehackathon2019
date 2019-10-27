package com.example.ulta3.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ulta3.MainActivity;
import com.example.ulta3.R;
import com.example.ulta3.ViewModel;
import com.example.ulta3.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductAdapterViewHolder> {
    class ProductAdapterViewHolder extends RecyclerView.ViewHolder{
        private final TextView productName;
        private final TextView productPrice;
        private ProductAdapterViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
        }
    }

    private final LayoutInflater mInflater;
    private final Context context;
    private final List<Integer> products;
    public ProductAdapter(Context context, List<Integer> products){
        this.context = context;
        this.products = products;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    @NonNull
    public ProductAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView = mInflater.inflate(R.layout.cardview_products, parent, false);
        return new ProductAdapter.ProductAdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductAdapterViewHolder holder, int position){
        Integer productID = products.get(position);
        ViewModel vm = ((MainActivity)context).getViewModel();
        String productName = vm.getNameBySKU(productID);
        double price = vm.getPriceBySKU(productID);

        holder.productName.setText(productName);
        holder.productPrice.setText("$"+Double.toString(price));

    }

    @Override
    public int getItemCount(){
        if (products != null) return products.size();
        else return 0;
    }
}
