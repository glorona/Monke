package com.nasaapps.monke;

import androidx.fragment.app.FragmentActivity;

import android.icu.util.Output;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;
import com.nasaapps.monke.bestapi.BestApiService;
import com.nasaapps.monke.databinding.ActivityMapsBinding;
import com.nasaapps.monke.modelo.Locacion;
import com.nasaapps.monke.modelo.LocacionRespuesta;

import org.json.JSONObject;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {



    private GoogleMap mMap;
    private Retrofit retrofit;
    private ActivityMapsBinding binding;
    private static final String TAG = "Locacion";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://besttime.app/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        String col = consumoWebCol("mcdonalds");

        if (!col.equals("")){

        }

    }


    private String consumoWebCol(String q){
        String collection="";
        try{
            StrictMode.ThreadPolicy p=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(p);
            HttpRequest request=new HttpRequest("https://besttime.app/api/v1/venues/search?api_key_private=pri_78cc3773778b4cfe866e2d8595a2c64e&q=" + q,false,HttpRequest.POST_METHOD);
            //request.appendFormData("api_key_private","pri_78cc3773778b4cfe866e2d8595a2c64e");
            //request.appendFormData("q","McDonaldq");
            HttpResponse response=request.execute();
            int status=response.getStatusCode();
            String body=response.getBody();
            JSONObject json=new JSONObject(body);
            collection=json.getString("collection_id");
            System.out.println(collection);
            System.out.println(status);
            System.out.println(body);

        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        return collection;

    }


    private String consumoWebVen(String q){
        String collection="";
        try{
            StrictMode.ThreadPolicy p=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(p);
            HttpRequest request=new HttpRequest("https://besttime.app/api/v1/collection?api_key_private=pri_78cc3773778b4cfe866e2d8595a2c64e&q=" + q,false,HttpRequest.POST_METHOD);
            //request.appendFormData("api_key_private","pri_78cc3773778b4cfe866e2d8595a2c64e");
            //request.appendFormData("q","McDonaldq");
            HttpResponse response=request.execute();
            int status=response.getStatusCode();
            String body=response.getBody();
            JSONObject json=new JSONObject(body);
            collection=json.getString("collection_id");
            System.out.println(collection);
            System.out.println(status);
            System.out.println(body);

        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        return collection;

    }





    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}