//\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/
//\ NAME        =                 Talwinder
//\ SURNAME     =                   Singh
//\ ID NUMBER   =                 S1604999
//\ DATE        =                 17/04/2020
//\ MODULE      =       Mobile Platform Development(MHI322959-19-B)
//\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/

package org.me.gcu.mpd_coursework;

//import android.support.v7.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.service.autofill.FillEventHistory;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Locale;
import java.util.logging.Handler;

public class MainActivity extends FragmentActivity implements View.OnClickListener
{
    private Button currentIncidentsButton;
    private Button roadWorkstButton;
    private Button plannedWorksButton;
    private Button planJourneyButton;
    private Intent pIncidentsActivity;
    private Intent pPlannedWorksActivity;
    private Intent pRoadWorksActivity;
    private Intent pPlanJourneyActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get the id of the buttons
        currentIncidentsButton = (Button)findViewById(R.id.currentIncidentsButton);
        currentIncidentsButton.setOnClickListener(this);

        roadWorkstButton = (Button)findViewById(R.id.roadWorkstButton);
        roadWorkstButton.setOnClickListener(this);

        plannedWorksButton = (Button)findViewById(R.id.plannedWorksButton);
        plannedWorksButton.setOnClickListener(this);

        planJourneyButton = (Button)findViewById(R.id.planJourneyButton);
        planJourneyButton.setOnClickListener(this);

    }

    public void onClick(View aview)
    {
        //Switch to the selected page
        if(aview == currentIncidentsButton)
        {
            pIncidentsActivity = new Intent(this, IncidentsActivity.class);
            startActivity(pIncidentsActivity);
        }
        else if(aview == plannedWorksButton)
        {
            pPlannedWorksActivity = new Intent(this, PlannedWorksActivity.class);
            startActivity(pPlannedWorksActivity);
        }
        else if(aview == roadWorkstButton)
        {
            pRoadWorksActivity = new Intent(this, RoadWorksActivity.class);
            startActivity(pRoadWorksActivity);
        }
        else if(aview == planJourneyButton)
        {
            pPlanJourneyActivity = new Intent(this, MapActivity.class);
            startActivity(pPlanJourneyActivity);
        }

    }







} // End of MainActivity
