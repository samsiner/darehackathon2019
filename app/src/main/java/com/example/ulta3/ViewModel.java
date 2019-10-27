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
    public void resetProb(){ mRepository.resetProb();}
    public int getProb(int sku){ return mRepository.getProb(sku);}
    public List<Integer> getProbNonzeroSorted(){ return mRepository.getProbNonzeroSorted();}
    public double getPriceBySKU(int sku){ return mRepository.getPriceBySKU(sku);}
    public String getNameBySKU(int sku){ return mRepository.getNameBySKU(sku);}
    public String getCategoryBySKU(int sku){ return mRepository.getCategoryBySKU(sku);}
    public String getShortDescBySKU(int sku){ return mRepository.getShortDescBySKU(sku);}
    public void insert(Product p){ mRepository.insert(p);}
    public int getCount(){ return mRepository.getCount();}
    public List<Integer> getManProducts(){ return mRepository.getManProducts();}
    public List<Integer> getSearchName(String s){ return mRepository.getSearchName(s);}
    public List<Integer> getSearchCategory(String s){ return mRepository.getSearchCategory(s);}
    public List<Integer> getSearchDesc(String s){ return mRepository.getSearchDesc(s);}
    public List<Integer> getSearchBrand(String s){ return mRepository.getSearchBrand(s);}
}
