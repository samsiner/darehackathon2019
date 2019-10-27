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
import com.example.ulta3.BuildConfig;
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
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
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

        ArrayList<Integer> al = (ArrayList)vm.getManProducts();
        HashMap<String, String> newmap = new HashMap<>();
        for (int i : al){
            newmap.put(vm.getNameBySKU(i), "");
        }
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
            String[] textarr = spokenText.split(" ");

            vm.resetProb();

            for (String s : textarr){
                ArrayList<String> commonWords = new ArrayList<>(Arrays.asList("all", "any", "how", "mad", "new", "now", "old", "well", "and", "but", "nor", "yet", "the", "our", "you", "who", "are", "has", "have", "get", "was", "show", "let", "can"));
                if (commonWords.contains(s) || s.length() < 3) continue;
                List<Integer> skusName = vm.getSearchName(s);
                List<Integer> skusCategory = vm.getSearchCategory(s);
                List<Integer> skusDesc = vm.getSearchDesc(s);
                List<Integer> skusBrand = vm.getSearchBrand(s);

                for (int sku : skusDesc) vm.incrementProbBy(sku, 1);
                for (int sku : skusName) vm.incrementProbBy(sku, 3);
                for (int sku : skusBrand){
                    vm.incrementProbBy(sku, 5);
                }
                for (int sku : skusCategory) vm.incrementProbBy(sku, 5);
            }

            List<Integer> nonzeroSortedSKUs = vm.getProbNonzeroSorted();

            for (int i=0; i<nonzeroSortedSKUs.size();i++){
                Log.d(vm.getCategoryBySKU(nonzeroSortedSKUs.get(i)), vm.getProb(nonzeroSortedSKUs.get(i)) + ": " + vm.getNameBySKU(nonzeroSortedSKUs.get(i)) + ", " + vm.getShortDescBySKU(nonzeroSortedSKUs.get(i)));
                if (i == 10) break;
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
