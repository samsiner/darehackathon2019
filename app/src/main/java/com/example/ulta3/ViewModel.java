package com.example.ulta3;

import com.example.ulta3.model.Product;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.example.ulta3.model.UltaRepository;

import java.util.List;

public class ViewModel extends AndroidViewModel {

    private final UltaRepository mRepository;

    public ViewModel(Application application){
        super(application);
        mRepository = UltaRepository.getInstance(application);
    }

    public List<Integer> getProductsByDescription(String category){ return mRepository.getProductsByDescription(category);}
    public List<Integer> getProductsByCategory(String category){ return mRepository.getProductsByCategory(category);}
    public String[] getInfoBySKU(int sku){ return mRepository.getInfoBySKU(sku);}
    public void incrementProbBy(int sku, int i){ mRepository.incrementProbBy(sku, i);}
    public void resetProb(int sku){ mRepository.resetProb(sku);}
    public double getPriceBySKU(int sku){ return mRepository.getPriceBySKU(sku);}
    public String getNameBySKU(int sku){ return mRepository.getNameBySKU(sku);}
    public void insert(Product p){ mRepository.insert(p);}
}
