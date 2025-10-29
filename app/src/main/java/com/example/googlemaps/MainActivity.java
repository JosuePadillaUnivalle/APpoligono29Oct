package com.example.googlemaps;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private GoogleMap myMap;
    private List<LatLng> puntosPoligono = new ArrayList<>();
    private List<Marker> marcadoresPoligono = new ArrayList<>();
    private Polygon poligonoActual;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment)  getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        LatLng scz = new LatLng(-17.778895255637625,-63.1606723916897);

        // Habilitar controles de zoom
        myMap.getUiSettings().setZoomControlsEnabled(true);
        myMap.getUiSettings().setZoomGesturesEnabled(true);
        myMap.getUiSettings().setScrollGesturesEnabled(true);

        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(scz, 20));

        // Agregar marcador inicial
        anadirMarker(scz, "SCZ", "Santa Cruz de la Sierra", BitmapDescriptorFactory.HUE_GREEN);

        // Configurar listener para clics en el mapa
        myMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        // Agregar punto al polígono
        puntosPoligono.add(latLng);
        
        // Agregar marcador en el punto
        Marker marker = myMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Punto " + puntosPoligono.size())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        marcadoresPoligono.add(marker);

        // Actualizar polígono si hay al menos 3 puntos
        if (puntosPoligono.size() >= 3) {
            actualizarPoligono();
        }
    }

    private void actualizarPoligono() {
        // Eliminar polígono anterior si existe
        if (poligonoActual != null) {
            poligonoActual.remove();
        }

        // Crear nuevo polígono con todos los puntos
        PolygonOptions polygonOptions = new PolygonOptions()
                .addAll(puntosPoligono)
                .strokeColor(0xFFFF0000)  // Rojo
                .fillColor(0x44FF0000)    // Rojo semi-transparente
                .strokeWidth(5);           // Corregido: strokeWidth en lugar de strokeColor

        poligonoActual = myMap.addPolygon(polygonOptions);
    }
    public void anadirMarker(LatLng latLng, String titulo, String snippet, float hue) {
        myMap.addMarker (new MarkerOptions()
                .position(latLng)
                .title(titulo)
                .snippet(snippet)
                .icon(BitmapDescriptorFactory.defaultMarker(hue)));
    }
}