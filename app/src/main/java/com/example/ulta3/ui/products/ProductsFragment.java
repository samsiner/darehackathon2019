package com.example.ulta3.ui.products;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.ulta3.R;
import com.example.ulta3.adapter.ProductAdapter;
import java.util.ArrayList;
import java.util.Arrays;


public class ProductsFragment extends Fragment {
    RecyclerView mRecyclerView;
    ProductAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Integer> productResults;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            productResults = getArguments().getIntegerArrayList("ProductResults");
        }
        productResults = new ArrayList<Integer>(Arrays.asList(1131244, 1951000, 2023633));
//        homeViewModel =
//                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_products, container, false);

        mRecyclerView = root.findViewById(R.id.rv_products);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ProductAdapter(getContext(), productResults);
        mRecyclerView.setAdapter(mAdapter);

        return root;
    }
}
