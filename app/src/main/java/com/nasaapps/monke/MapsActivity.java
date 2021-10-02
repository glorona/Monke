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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nasaapps.monke.bestapi.BestApiService;
import com.nasaapps.monke.databinding.ActivityMapsBinding;
import com.nasaapps.monke.modelo.Locacion;
import com.nasaapps.monke.modelo.LocacionRespuesta;

import org.json.JSONArray;
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
    private static ArrayList<Locacion> locSelectBoxData;




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

        String col = consumoWebCol("kfc");

        if (!col.equals("")){
            ArrayList<String> ven = consumoWebVen(col);
            for(Locacion l: locSelectBoxData){
                System.out.println(l.getName());
            }

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


    private ArrayList<String> consumoWebVen(String col){
        ArrayList<String> final_lis=null;
        JSONArray venues =null;
        try{
            StrictMode.ThreadPolicy p=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(p);
            HttpRequest request=new HttpRequest("https://besttime.app/api/v1/collection/"+ col + "?api_key_private=pri_78cc3773778b4cfe866e2d8595a2c64e",false,HttpRequest.GET_METHOD);
            //request.appendFormData("api_key_private","pri_78cc3773778b4cfe866e2d8595a2c64e");
            //request.appendFormData("q","McDonaldq");
            HttpResponse response=request.execute();
            locSelectBoxData = new ArrayList<>();
            int status=response.getStatusCode();
            String body=response.getBody();
            JSONObject json=new JSONObject(body);
            final_lis = new ArrayList<>();
            venues=json.getJSONArray("venue_ids");
            for(int x=0; x<venues.length();x++){
                System.out.println(venues.get(x));
                final_lis.add((String) venues.get(x));
                locSelectBoxData.add(consumoWebLoc((String) venues.get(x)));
            }

            System.out.println(final_lis);
            System.out.println(venues);
            System.out.println(status);
            System.out.println(body);

        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        return final_lis;

    }

    private Locacion consumoWebLoc(String ven){
        JSONObject venues =null;
        Locacion ubicacion = null;
        try{
            StrictMode.ThreadPolicy p=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(p);
            HttpRequest request=new HttpRequest("https://besttime.app/api/v1/venues/"+ ven + "?api_key_public=pub_596c3dfff0ff40ae829737ea9c5e7ff1",false,HttpRequest.GET_METHOD);
            //request.appendFormData("api_key_private","pri_78cc3773778b4cfe866e2d8595a2c64e");
            //request.appendFormData("q","McDonaldq");
            HttpResponse response=request.execute();
            int status=response.getStatusCode();
            String body=response.getBody();
            JSONObject json=new JSONObject(body);
            venues=json.getJSONObject("venue_info");

            String name = venues.getString("venue_name");
            String latitud = venues.getString("venue_lng");
            String longitud = venues.getString("venue_lat");
            String id = venues.getString("venue_id");

            ubicacion = new Locacion(id,name,latitud,longitud);

        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        return ubicacion;

    }

    public void agregarPunto(Locacion loc){
        LatLng punto = new LatLng(Double.parseDouble(loc.getLon()),Double.parseDouble(loc.getLat()));
        mMap.addMarker(new MarkerOptions().position(punto).title(loc.getName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(punto));
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
        for(Locacion l: locSelectBoxData){
            agregarPunto(l);
        }

    }
}