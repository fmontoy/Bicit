package com.bicit.bicit;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.kml.KmlLayer;
import com.google.maps.android.kml.KmlPlacemark;

import org.apache.http.conn.scheme.HostNameResolver;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.logging.Handler;

public class Maproute extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maproute);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Este metodo se ejecuta cuando el mapa esta listo para ser mostrado
     * Ubica la posicion del usuario y centra el mapa en ese punto
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Se activa la capa para que el usuario se ubique en el mapa,
        //Es decir aparecera la opcion para ubicarse y aparecera un punto
        //Azul que indica su posicion geografica
        mMap.setMyLocationEnabled(true);

        //Se busca el mejor proveedor de localizacion que tenga el celular para luego
        //Obtener ultima posici�n que se conoce del usuario
        //Y centrar el mapa en ese lugar
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);

        //Se mueve el mapa al lugar y se hace un zoom de 15
        LatLng posicion = new LatLng(location.getLatitude(),location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(posicion));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        //Se accede al archivo kml que tiene la descripcion de las lineas que se deben dibujar y su posicion
        //El archivo se encuentra en la ruta /Bicit/Bicit/app/src/main/res/raw
        //Para modificar acceder a mymaps con la cuenta de bicit, una vez modificado descargar el archivo
        //Dando a la opcion export to kml y darle a la opcion de exportar en kml en vez de kmz.
        //Reemplazar el archivo descargado por el antiguo en la carpeta de resources
        try {
            KmlLayer layer = new KmlLayer(mMap, R.raw.ciclorutasmap, getApplicationContext());
            layer.addLayerToMap();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
