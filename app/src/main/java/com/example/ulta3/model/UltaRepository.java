package com.example.ulta3.model;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UltaRepository {
    private final RoomDao roomDao;
    private final ExecutorService executor;

    private static volatile UltaRepository INSTANCE;

    public static UltaRepository getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (UltaRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UltaRepository(application);
                }
            }
        }
        return INSTANCE;
    }

    private UltaRepository(Application application) {
        UltaDatabase db = UltaDatabase.getDatabase(application);
        roomDao = db.roomDao();
        executor = Executors.newFixedThreadPool(4);
    }

    int i = 0;
    public int getInventoryCount(){
        try{
            i = executor.submit(() -> roomDao.getInventoryCount()).get();
        } catch (ExecutionException | InterruptedException e){}
        return i;
    }

    int i2 = 0;
    public int getProductCount(){
        try{
            i2 = executor.submit(() -> roomDao.getProductCount()).get();
        } catch (ExecutionException | InterruptedException e){}
        return i2;
    }

    public void insertProduct(Product p){
        Thread thread = new Thread() {
            public void run() {
                roomDao.insertProduct(p);
            }
        };
        thread.start();
        try{
            thread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void insertInventory(Inventory p){
        Thread thread = new Thread() {
            public void run() {
                roomDao.insertInventory(p);            }
        };
        thread.start();
        try{
            thread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    private List<Integer> al = new ArrayList<>();
    public List<Integer> getProductsByCategory(String category) {
        try{
            al = executor.submit(() -> roomDao.getProductsByCategory(category)).get();
        } catch (ExecutionException | InterruptedException e){}
        return al;
    }

    private List<Integer> a2 = new ArrayList<>();
    public List<Integer> getProductsByDescription(String category) {
        String newcategory = "%"+category+"%";
        Product[] p = null;
        try{
            a2 = executor.submit(() -> roomDao.getProductsByDescription(newcategory)).get();
        } catch (ExecutionException | InterruptedException e){}
        return a2;
    }

    public void setNonManProductsToZero() {
        String s = "% man %";
        String s2 = "% men %";
        String s3 = "% male %";
        executor.execute(()-> roomDao.setNonManProductsToZero(s, s2, s3));
    }

    private List<Integer> a4 = new ArrayList<>();
    public List<Integer> getSearchName(String s) {
        try{
            String str = "%" + s + "%";
            a4 = executor.submit(() -> roomDao.getSearchName(str)).get();
        } catch (ExecutionException | InterruptedException e){}
        return a4;
    }

    private List<Integer> a5 = new ArrayList<>();
    public List<Integer> getSearchCategory(String s) {
        try{
            a5 = executor.submit(() -> roomDao.getSearchCategory(s)).get();
        } catch (ExecutionException | InterruptedException e){}
        return a5;
    }

    private List<Integer> a6 = new ArrayList<>();
    public List<Integer> getSearchDesc(String s) {
        try{
            String str = "%" + s + "%";
            a6 = executor.submit(() -> roomDao.getSearchDesc(str)).get();
        } catch (ExecutionException | InterruptedException e){}
        return a6;
    }

    private List<Integer> a7 = new ArrayList<>();
    public List<Integer> getSearchBrand(String s) {
        try{
            String str = "%" + s + "%";
            a7 = executor.submit(() -> roomDao.getSearchBrand(str)).get();
        } catch (ExecutionException | InterruptedException e){}
        return a7;
    }

    private List<Integer> a8 = new ArrayList<>();
    public List<Integer> getProbNonzeroSorted() {
        try{
            a8 = executor.submit(() -> roomDao.getProbNonzeroSorted()).get();
        } catch (ExecutionException | InterruptedException e){}
        return a8;
    }

    private List<Integer> a9 = new ArrayList<>();
    public List<Integer> getStoresInStock(int sku) {
        try{
            a9 = executor.submit(() -> roomDao.getStoresInStock(sku)).get();
        } catch (ExecutionException | InterruptedException e){}
        return a9;
    }

    public String[] getInfoBySKU(int sku){
        String[] info = new String[5];
        try{
            info[0] = executor.submit(() -> roomDao.getName(sku)).get();
            info[1] = executor.submit(() -> roomDao.getBrand(sku)).get();
            info[2] = executor.submit(() -> roomDao.getCategory(sku)).get();
            info[3] = executor.submit(() -> roomDao.getShortDesc(sku)).get();
            info[4] = executor.submit(() -> roomDao.getLongDesc(sku)).get();

        } catch (ExecutionException | InterruptedException e){}
        return info;
    }

    private double price = 0;
    public double getPriceBySKU(int sku){
        try{
            price = executor.submit(() -> roomDao.getPrice(sku)).get();
        } catch (ExecutionException | InterruptedException e){}
        return price;
    }

    private int prob = 0;
    public int getProb(int sku){
        try{
            prob = executor.submit(() -> roomDao.getProb(sku)).get();
        } catch (ExecutionException | InterruptedException e){}
        return prob;
    }

    private String name = "";
    public String getNameBySKU(int sku){
        Thread thread = new Thread() {
            public void run() {
                name = roomDao.getName(sku);
            }
        };
        thread.start();
        try{
            thread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        return name;
    }

    private String category = "";
    public String getCategoryBySKU(int sku){
        Thread thread = new Thread() {
            public void run() {
                category = roomDao.getCategory(sku);
            }
        };
        thread.start();
        try{
            thread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        return category;
    }

    private String desc = "";
    public String getShortDescBySKU(int sku){
        Thread thread = new Thread() {
            public void run() {
                desc = roomDao.getShortDesc(sku);
            }
        };
        thread.start();
        try{
            thread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        return desc;
    }

    public void incrementProbBy(int id, int i) {

        Thread thread = new Thread() {
            public void run() {
                roomDao.incrementProb(id, i);
            }
        };
        thread.start();
        try{
            thread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void resetProb() {
        Thread thread = new Thread() {
            public void run() {
                roomDao.resetProb();
            }
        };
        thread.start();
        try{
            thread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
