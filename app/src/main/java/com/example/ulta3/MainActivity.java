package com.example.ulta3;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.opencsv.CSVReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
    private FusedLocationProviderClient fusedLocationClient;
    LocationManager mLocationManager;

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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getClosestStore();
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

    private String destinations = "";

    private void getClosestStore(){
        StringBuilder buildURL = new StringBuilder();
        buildURL.append("https://maps.googleapis.com/maps/api/distancematrix/json?origins=");

        try {
            Task locationResult = fusedLocationClient.getLastLocation();

            locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Location loc = (Location)task.getResult();
                        buildURL.append(loc.getLatitude()+","+loc.getLongitude());
                        buildURL.append("&destinations=");
                        buildURL.append(destinations + "&key=");
                        Log.d("LAT", buildURL.toString());

                    }
                }
            });


        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }


//        try{
            InputStream is = getResources().openRawResource(R.raw.store_details);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is, Charset.forName("UTF-8")));
            String line;

            try {
                reader.readLine();
                while ((line = reader.readLine()) != null) {
                    String[] tokens = line.split(",");

                    if (!tokens[4].equals("Chicago")) continue;

                    int storeID;
                    try {
                        storeID = Integer.parseInt(tokens[0]);
                    } catch (NumberFormatException e){
                        continue;
                    }

                    String address = tokens[2];
                    String[] addressArr = address.split(" ");

                    for (int i=0;i<addressArr.length;i++){
                        destinations += addressArr[i] + "+";
                    }

                    String city = tokens[4];
                    String[] cityArr = city.split(" ");

                    for (int i=0;i<cityArr.length;i++){
                        destinations += cityArr[i] + "+";
                    }

                    String state = tokens[5];
                    destinations += state + "|";
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            destinations = destinations.substring(0, destinations.length()-1);
//            URL urlStations = new URL(buildURL.toString());
//
//            Thread thread = new Thread() {
//                public void run() {
//                    try {
//                        Scanner scan = new Scanner(urlStations.openStream());
//                        while (scan.hasNext()) sb.append(scan.nextLine());
//                        scan.close();
//                    } catch (IOException e){
//                        e.printStackTrace();
//                    }
//                }
//            };
//            thread.start();
//            try{
//                thread.join();
//            } catch (InterruptedException e){
//                e.printStackTrace();
//            }
//
//        } catch (IOException e) {
//            sb.delete(0, sb.length());
//            sb.append("NO INTERNET");
//        }
//        String JSONString = sb.toString();
//
//        Log.d("JSONString", JSONString);

//        //Set internet connection status
//        connectionStatusLD.postValue(!JSONString.equals("NO INTERNET"));
//        if (JSONString.equals("NO INTERNET")) return;
//
//        ArrayList<String> currentAlerts = new ArrayList<>(); //For multiple alerts
//        ArrayList<String> beforeStationsOut = new ArrayList<>(mStationDao.getAllAlertStationIDs());
//
//        try {
//            JSONObject outer = new JSONObject(JSONString);
//            JSONObject inner = outer.getJSONObject("CTAAlerts");
//            JSONArray arrAlerts = inner.getJSONArray("Alert");
//
//            for (int i=0;i<arrAlerts.length();i++){
//                JSONObject alert = (JSONObject) arrAlerts.get(i);
//                String impact = alert.getString("Impact");
//                if (!impact.equals("Elevator Status")) continue;
//
//                JSONArray service;
//                try {
//                    JSONObject impactedService = alert.getJSONObject("ImpactedService");
//                    service = impactedService.getJSONArray("Service");
//                } catch (JSONException e){
//                    e.printStackTrace();
//                    continue;
//                }
//
//                for (int j=0;j<service.length();j++) {
//                    JSONObject serviceInstance = (JSONObject) service.get(j);
//                    if (serviceInstance.getString("ServiceType").equals("T")) {
//                        String id = serviceInstance.getString("ServiceId");
//                        String headline = alert.getString("Headline");
//
//                        if (headline.contains("Back in Service")) break;
//
//                        //End up with beforeStationsOut only containing alerts that no longer exist
//                        beforeStationsOut.remove(id);
//
//                        //Looking for multiple alerts for the same station.
//                        String shortDesc = alert.getString("ShortDescription");
//                        if (currentAlerts.contains(id)) {
//                            shortDesc += "\n\n";
//                            shortDesc += mStationDao.getShortDescription(id);
//                        } else {
//                            currentAlerts.add(id);
//                        }
//
//                        mStationDao.setAlert(id, shortDesc);
//                        break;
//                    }
//                }
//            }
//        }
//        catch (JSONException | NullPointerException e) {
//            e.printStackTrace();
//        }
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
