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

    public void insert(Product p){
        Thread thread = new Thread() {
            public void run() {
                roomDao.insert(p);            }
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

    public void incrementProbBy(int id, int i) {
        executor.execute(() -> roomDao.incrementProb(id, i));
    }

    public void resetProb(int id) {
        executor.execute(() -> roomDao.resetProb(id));
    }
}
