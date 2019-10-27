package com.example.ulta3;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.constraint.Constraints;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.ulta3.model.Product;
import com.opencsv.CSVReader;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

//    public ArrayList<HashMap<String, String>> maps = new ArrayList<>();
//    public int prob1, prob2, prob3;

    ViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vm = ViewModelProviders.of(this).get(ViewModel.class);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        buildDatabase();

        Log.d("2544852 Argan Oil", vm.getNameBySKU(2544852));
        ArrayList<Integer> al = (ArrayList)vm.getManProducts();
        HashMap<String, String> newmap = new HashMap<>();
        for (int i : al){
            newmap.put(vm.getNameBySKU(i), "");
            Log.d("Men Products", vm.getNameBySKU(i) + ": " + vm.getShortDescBySKU(i));
        }
        Log.d("size", Integer.toString(newmap.keySet().size()));
    }

    private static final int SPEECH_REQUEST_CODE = 0;

    public void displaySpeechRecognizer(View v) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    private void buildDatabase(){
        if (vm.getCount() > 0) return;

        InputStream is = getResources().openRawResource(R.raw.product_catalog);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8")));
        String line;

        try {
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");

                int sku;
                try {
                    sku = Integer.parseInt(tokens[0]);
                } catch (NumberFormatException e){
                    continue;
                }

                String prodID = "";
                String name = "";
                String brand = "";
                double price = 0;
                String category = "";
                String shortDesc = "";
                String longDesc = "";

                try {
                    prodID = tokens[1];
                    name = tokens[2];
                    brand = tokens[3];
                    price = Double.parseDouble(tokens[4]);
                    category = tokens[5];
                    shortDesc = tokens[8];
                    longDesc = tokens[9];
                } catch (NumberFormatException | IndexOutOfBoundsException e) {}
                Product p = new Product(sku, prodID, name, brand, price, category, shortDesc, longDesc);
                vm.insert(p);
                Log.d("MainActivity", "Just Created " + p.sku + " " + p.name);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);




        }
        super.onActivityResult(requestCode, resultCode, data);
    }






//
//
//    private void createHashMaps(){
//        HashMap<String, String> p1 = new HashMap<>();
//        HashMap<String, String> p2 = new HashMap<>();
//        HashMap<String, String> p3 = new HashMap<>();
//
//        p1.put("SKU_ID", "2021212");
//        p1.put("DISPLAY_NAME", "Fahrenheit Eau de Toilette");
//        p1.put("BRAND_NAME", "Dior");
//        p1.put("CATEGORY", "Cologne");
//        p1.put("DESCRIPTION", "Dior Fahrenheit Eau de Toilette is a sensual and masculine leather woods fragrance. A unique and contrasting signature");
//
//        p2.put("SKU_ID", "2021224");
//        p2.put("DISPLAY_NAME", "Obsession For Men Eau de Toilette");
//        p2.put("BRAND_NAME", "Calvin Klein");
//        p2.put("CATEGORY", "Cologne");
//        p2.put("DESCRIPTION", "Obsession For Men Eau de Toilette by Calvin Klein. Intense. Unforgettable. Provocative. Between love and madness lies Obsession. This spicy oriental is a provocative and compelling blend of herbs and rare woods.");
//
//        p3.put("SKU_ID", "2049501");
//        p3.put("DISPLAY_NAME", "Chrome Eau de Toilette");
//        p3.put("BRAND_NAME", "Azzaro");
//        p3.put("CATEGORY", "Cologne");
//        p3.put("DESCRIPTION", "Experience the polished citrus essences of Azzaro's Chrome Eau de Toilette.");
//
//        maps.add(p1);
//        maps.add(p2);
//        maps.add(p3);
//    }
//
//    private void doTextStuff(String spokenText){
//        prob1 = 0;
//        prob2 = 0;
//        prob3 = 0;
//
//        String[] words = spokenText.split(" ");
//        for (String word : words){
//            Log.d("word", word);
//            for (int i=0;i<maps.size();i++){
//                Collection c = maps.get(i).values();
//
//                for (Object o : c){
//                    String s = (String)o;
//
//                    String[] newwords = s.split(" ");
//                    for (String wd : newwords) {
//                        word = word.toLowerCase();
//                        wd = wd.toLowerCase();
//                        wd = wd.replaceAll("[^a-zA-Z0-9]", "");
//                        if (word.equals(wd)) {
//                            Log.d("newword", wd);
//                            Log.d("newwordi", Integer.toString(i));
//                            if (i == 0) prob1++;
//                            if (i == 1) prob2++;
//                            if (i == 2) prob3++;
//                        }
//                    }
//                }
//            }
//        }
//
//        String s = "Probability = " + prob1 + "\nName= " + maps.get(0).get("DISPLAY_NAME");
//        s += "\n by " + maps.get(0).get("BRAND_NAME") + "\n\n";
//
//        String s1 = "Probability = " + prob2 + "\nName= " + maps.get(1).get("DISPLAY_NAME");
//        s1 += "\n by " + maps.get(1).get("BRAND_NAME") + "\n\n";
//
//        String s2 = "Probability = " + prob3 + "\nName= " + maps.get(2).get("DISPLAY_NAME");
//        s2 += "\n by " + maps.get(2).get("BRAND_NAME") + "\n\n";
//
//        TextView tv = findViewById(R.id.text_home);
//        tv.setText(s + s1 + s2);
//
//        // Do something with spokenText
//    }
}
