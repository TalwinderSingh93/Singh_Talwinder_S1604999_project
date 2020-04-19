//\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/
//\ NAME        =                 Talwinder
//\ SURNAME     =                   Singh
//\ ID NUMBER   =                 S1604999
//\ DATE        =                 17/04/2020
//\ MODULE      =       Mobile Platform Development(MHI322959-19-B)
//\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/

package org.me.gcu.mpd_coursework;


//import android.support.v7.app.AppCompatActivity;
import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class RoadWorksActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener
{
    //the global variables
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Date pStartDate;
    private Date pEndDate;
    private Date pSearchDate;
    private TextView dateSelection;
    private SearchView searchView;
    private String szSearchDate;
    private String resTask;
    private String resMain;
    private String szSearchFilter;
    private String urlSource;
    private String szStrAux;
    private Button pResetDate;
    private float fDaysCount;

    GoogleMap map;

    LinkedList <RoadWorksClass> List = null;

    //Variables for the expandable list
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road_works);

        //Disable the home button option at top of the page
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            //Do some stuff
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }



        //Initialize variables
        szSearchFilter = "";
        szSearchDate = "";
        urlSource = "https://trafficscotland.org/rss/feeds/roadworks.aspx";

        List  = new LinkedList<RoadWorksClass>();

        //Get different objects ids
        searchView = (SearchView)findViewById(R.id.filterResearch);
        dateSelection = (TextView)findViewById(R.id.DateSelection);
        expListView = (ExpandableListView) findViewById(R.id.ExpandableList);
        pResetDate = (Button) findViewById(R.id.clearDateButton);

        pResetDate.setOnClickListener(this);

        //Initialize variables for expandable list
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);




        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            String value = extras.getString("KEY");

            //Log.e("Passing Data","lenght:"+value.length());
        }

        //Set the methods for search view object
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                //Update the data depending on the research query
                szSearchFilter = query;
                startProgress();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                //if the search string is empty than present all the data
                if(newText.length() == 0)
                {
                    szSearchFilter = newText;
                    startProgress();
                }

                return false;
            }
        });

        //Set the methods for the date selection
        dateSelection.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Calendar calender = Calendar.getInstance();
                int year = calender.get(Calendar.YEAR);
                int month = calender.get(Calendar.MONTH);
                int day = calender.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(RoadWorksActivity.this,
                        android.R.style.Theme_Holo,
                        mDateSetListener,
                        year,
                        month,
                        day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }

        });

        //Once the date is selected by the user, it appears on the screen as text.
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                //as the month is saved as 0 to 11, therfore increase by 1
                month = month + 1;

                //Display the date on the screen
                dateSelection.setText(String.format("%02d", dayOfMonth) + " " + GetMonthByNumber(month, true) + " " + year);

                //Create the serch string
                szSearchDate = String.format("%02d", dayOfMonth) + " " + GetMonthByNumber(month, true) + " " + year;

                //Temporary string in order to create the search date object
                String szTempDate = String.format("%02d", dayOfMonth) + "/" + String.format("%02d", month) + "/" + year;
                try {
                    pSearchDate=new SimpleDateFormat("dd/MM/yyyy").parse(szTempDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                startProgress();

            }
        };


        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

                    map.clear();

                    double dLat;
                    double dLng;
                    String szLat;
                    String szLng;
                    String szGeoLocation;

                    //Get the geolocation from the selected element as string
                    szGeoLocation = List.get(groupPosition).getGeoLocation();

                    //split the geolocation string in latitude and longitude
                    szLat = szGeoLocation.substring(0, szGeoLocation.indexOf(' '));
                    szLng = szGeoLocation.substring(szGeoLocation.indexOf(' '));

                    //Cast string to double
                    dLat = Double.parseDouble(szLat);
                    dLng = Double.parseDouble(szLng);

                    //Show on the map the geolocation position on the map
                    LatLng pGlasgow = new LatLng(dLat, dLng);
                    map.addMarker(new MarkerOptions().position(pGlasgow).title("Selected Location"));
                    map.moveCamera(CameraUpdateFactory.newLatLng(pGlasgow));
                    map.animateCamera(CameraUpdateFactory.zoomTo(12));
                }

            }
        });

        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

                    onMapReady(map);
                }

            }
        });

        //Load the data
        startProgress();
    }



    //Method to get the month in string
    private String GetMonthByNumber(int iMonth, boolean bAbbrev)
    {
        String szMonth;

        if (iMonth == 1)
        {
            if(bAbbrev)
                return "Jan";
            else
                return "January";
        }
        else if (iMonth == 2)
        {
            if(bAbbrev)
                return "Feb";
            else
                return "February";
        }
        else if (iMonth == 3)
        {
            if(bAbbrev)
                return "Mar";
            else
                return "March";
        }
        else if (iMonth == 4)
        {
            if(bAbbrev)
                return "Apr";
            else
                return "April";
        }
        else if (iMonth == 5)
        {
            return "May";
        }
        else if (iMonth == 6)
        {
            if(bAbbrev)
                return "Jun";
            else
                return "June";
        }
        else if (iMonth == 7)
        {
            if(bAbbrev)
                return "Jul";
            else
                return "July";
        }
        else if (iMonth == 8)
        {
            if(bAbbrev)
                return "Aug";
            else
                return "August";
        }
        else if (iMonth == 9)
        {
            if(bAbbrev)
                return "Sep";
            else
                return "September";
        }
        else if (iMonth == 10)
        {
            if(bAbbrev)
                return "Oct";
            else
                return "October";
        }
        else if (iMonth == 11)
        {
            if(bAbbrev)
                return "Nov";
            else
                return "November";
        }
        else if (iMonth == 12)
        {
            if(bAbbrev)
                return "Dec";
            else
                return "December";
        }

        return "Error";
    }

    private void prepareListData() {


        if(List != null)
        {
            //Clear all the lists at the start
            listDataHeader.clear();
            listDataChild.clear();

            //Run the for loop in order to insert all the items in the expandable list
            for (int i = 0; i < List.size(); i++)
            {
                //Get the title name
                listDataHeader.add(List.get(i).getTitle());

                List<String> listItems = new ArrayList<String>();

                //Set the information of the element
                szStrAux = List.get(i).getDescription();
                szStrAux.replaceAll("<br/>", " ");
                Log.e("Descriptionnnn", szStrAux);
                listItems.add(List.get(i).getDescription());
                listItems.add(List.get(i).getPubDate());
                listItems.add("How long will last: " + Math.round(List.get(i).getDuration()) + " Days");

                //Insert the title and all the elements
                listDataChild.put(listDataHeader.get(i), listItems);
                expListView.setAdapter(listAdapter);
            }
        }

        expListView.setAdapter(listAdapter);
    }



    private boolean CheckDateInside(String szDescription)
    {
        if(szDescription.contains("Start Date"))
        {
            //Get the start date in string
            String szStartDate = szDescription.substring(szDescription.indexOf(","), szDescription.indexOf(">"));
            String temp1= szDescription.substring(szDescription.indexOf(">"));

            //Get the end date in string
            String szEndDate = temp1.substring(temp1.indexOf(","));


            String szStartDateTemp = "";
            String szEndDateTemp = "";

            //szDate1 = szStartDate.substring(2, 7);


            for(int iCount = 8; iCount < szStartDate.length(); iCount++)
            {
                if(szStartDate.charAt(iCount) == ' ')
                {
                    szStartDateTemp = szStartDate.substring(2, 8) + " " + szStartDate.charAt(iCount + 1) + szStartDate.charAt(iCount + 2) + szStartDate.charAt(iCount + 3) + szStartDate.charAt(iCount + 4);
                    break;
                }
            }

            for(int iCount = 8; iCount < szEndDate.length(); iCount++)
            {
                if(szEndDate.charAt(iCount) == ' ')
                {
                    szEndDateTemp = szEndDate.substring(2, 8) + " " + szEndDate.charAt(iCount + 1) + szEndDate.charAt(iCount + 2) + szEndDate.charAt(iCount + 3) + szEndDate.charAt(iCount + 4);
                    break;
                }
            }

            try {
                //Get the start date from string
                SimpleDateFormat formatter=new SimpleDateFormat("dd MMM yyyy");
                pStartDate = formatter.parse(szStartDateTemp);
                pEndDate = formatter.parse(szEndDateTemp);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long diff = pEndDate.getTime() - pStartDate.getTime();

            fDaysCount = (float) diff / (24 * 60 * 60 * 1000);

            //Log.e("Dates", fDaysCount + "Days" + "Diff" + diff);

            if(pSearchDate != null)
            {
                //Check if the selected date by the user is contained in the range
                if ((pSearchDate.compareTo(pStartDate) >= 0) && (pSearchDate.compareTo(pEndDate) <= 0)) {
                    Log.e("Dates", "TRUE");
                    return true;
                } else {
                    Log.e("Dates", "FALSE");
                    return false;
                }
            }

        }

        return false;
    }

    public void onClick(View aview)
    {
        if(aview == pResetDate)
        {
            szSearchDate = "";
            dateSelection.setText("Select date");

            startProgress();
        }
    }

    public void startProgress()
    {
        // Run network access on a separate thread;
        resTask = "";
        new Thread(new Task(urlSource)).start();
    } //

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            map = googleMap;

            map.clear();

            map.getUiSettings().setMapToolbarEnabled(false);

            LatLng pGlasgow = new LatLng(55.860916, -4.251433);
            //map.addMarker(new MarkerOptions().position(pGlasgow).title("Glasgow"));
            map.moveCamera(CameraUpdateFactory.newLatLng(pGlasgow));
            map.animateCamera(CameraUpdateFactory.zoomTo(8));
        }
    }

    // Need separate thread to access the internet resource over network
    private class Task implements Runnable
    {
        private String url;

        public Task(String aurl)
        {
            url = aurl;
        }
        @Override
        public void run()
        {

            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";

            try
            {
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));


                while ((inputLine = in.readLine()) != null)
                {
                    resTask = resTask + inputLine;
                }
                in.close();
            }
            catch (IOException ae)
            {
                Log.e("MyTag", "ioexception");
            }

            // Now update the expandable list to display
            RoadWorksActivity.this.runOnUiThread(new Runnable()
            {
                public void run() {

                    resMain = resTask;

                    if(List != null)
                        List.clear();

                    List = parseDataRoadWorks(resMain);

                    prepareListData();

                    onMapReady(map);

                }
            });
        }
    }

    //Method to parse the road works data
    private LinkedList<RoadWorksClass> parseDataRoadWorks(String dataToParse)
    {
        RoadWorksClass Widget = null;
        boolean bStart = false;
        boolean bSkip = false;


        try
        {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput( new StringReader( dataToParse ) );
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT)
            {

                switch(eventType)
                {
                    case XmlPullParser.START_TAG:

                        // Check which Tag we have
                        if (xpp.getName().equalsIgnoreCase("channel"))
                        {
                            List  = new LinkedList<RoadWorksClass>();
                        }
                        else if (xpp.getName().equalsIgnoreCase("item"))
                        {
                            Widget = new RoadWorksClass();
                            bStart = true;
                        }
                        else if(bStart)
                        {
                            //Log.e("MyTag","TagName:" + xpp.getName());
                            if (xpp.getName().equalsIgnoreCase("title"))
                            {
                                // Now just get the associated text
                                String temp = xpp.nextText();

                                //Check if the name filter is on
                                if(szSearchFilter.length() > 0)
                                {
                                    if(!temp.contains(szSearchFilter))
                                    {
                                        bSkip = true;
                                    }
                                }

                                Widget.setTitle(temp);

                            }
                            else if (xpp.getName().equalsIgnoreCase("description"))
                            {
                                // Now just get the associated text
                                String temp = xpp.nextText();

                                // Do something with text
                                Widget.setDescription(temp);

                                CheckDateInside(temp);

                                Widget.setDuration(fDaysCount);
                            }
                            else if (xpp.getName().equalsIgnoreCase("link"))
                            {
                                // Now just get the associated text
                                String temp = xpp.nextText();
                                // Do something with text
                                Widget.setLink(temp);
                            }
                            else if (xpp.getName().equalsIgnoreCase("georss:point"))
                            {
                                // Now just get the associated text
                                String temp = xpp.nextText();
                                // Do something with text
                                Widget.setGeoLocationV2(temp);
                            }
                            else if (xpp.getName().equalsIgnoreCase("pubDate"))
                            {
                                // Now just get the associated text
                                String temp = xpp.nextText();

                                //Check if the date filter is on
                                if(szSearchDate.length() > 0)
                                {
                                    if(!temp.contains(szSearchDate) && !CheckDateInside(Widget.getDescription()))
                                    {
                                        bSkip = true;
                                    }
                                }


                                Widget.setPubDateV2(temp);
                            }
                        }




                        break;

                    case XmlPullParser.END_TAG:

                        if (xpp.getName().equalsIgnoreCase("item"))
                        {
                            if(bSkip == false)
                            {
                                //Log.e("MyTag","incidentWidget is " + roadWorkWidget.toString());
                                List.add(Widget);

                            }

                            bSkip = false;
                        }
                        else
                        if (xpp.getName().equalsIgnoreCase("channel"))
                        {
                            int size;
                            size = List.size();
                        }

                        break;


                }

                // Get the next event
                eventType = xpp.next();
            }
        }
        catch (XmlPullParserException ae1)
        {
            Log.e("MyTag","Parsing error" + ae1.toString());
        }
        catch (IOException ae1)
        {
            Log.e("MyTag","IO error during parsing");
        }

        Log.e("MyTag","End document");

        return List;
    }





} // End of MainActivity
