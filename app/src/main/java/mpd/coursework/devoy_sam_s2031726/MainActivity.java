package mpd.coursework.devoy_sam_s2031726;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity implements OnClickListener
{

    //Xml items in view
    private LinearLayout searchTab;
    private ListView planListView;
    private TextView listTitle;
    private Toolbar toolBar;
    private DatePicker datePicker;
    private Button searchDropButton;
    private LinearLayout buttonBar;
    private Button searchBackButton;
    private TextView loadingText;


    //global variable
    private String listName= "";
    private String result = "";
    private EditText roadSearch;
    private ArrayList<Roadwork> rList;
    private int visible;
    private String incidentURL = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    public static final String[] MONTHS = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};



    //method to execute on creating page
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //creating replacing the old action bar
        toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        //button to go to Planned Roadwork Activity
        Button plannedButton = findViewById(R.id.plannedButton);
        plannedButton.setOnClickListener(this);

        //button to go to current incidents
        Button incidentButton = findViewById(R.id.incidentButton);
        incidentButton.setOnClickListener(this);

        //button to go to Current Roadworks
        Button roadworkButton = findViewById(R.id.roadworkButton);
        roadworkButton.setOnClickListener(this);

        //button to search for roads
        Button searchRoadButton = findViewById(R.id.searchRoadButton);
        searchRoadButton.setOnClickListener(this);

        //back button
        searchBackButton = findViewById(R.id.searchBackButton);
        searchBackButton.setOnClickListener(this);

        //search bar dorpdown button
        searchDropButton = findViewById(R.id.searchDropButton);
        searchDropButton.setOnClickListener(this);

        //search date button
        Button searchDateButton = findViewById(R.id.searchDateButton);
        searchDateButton.setOnClickListener(this);

        //different xml resources required for application
        buttonBar = findViewById(R.id.buttonBar);
        datePicker = findViewById(R.id.datePicker);
        roadSearch = findViewById(R.id.roadSearch);
        listTitle = findViewById(R.id.listTitle);
        loadingText = findViewById(R.id.loadingText);
        planListView = findViewById(R.id.planListView);
        searchTab = findViewById(R.id.searchTab);
        searchTab.setVisibility(View.GONE);

        //to tell if search bar is already open or not
        visible = 0;

        //load current incidents upon opening the app
        listName = "Current Incidents";
        listTitle.setText(listName);
        fetchFeed(incidentURL);








    }

    //creating the menu in action bar
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.my_menu, menu);
        searchBackButton.setVisibility(View.GONE);
        return super.onCreateOptionsMenu(menu);
    }

    // actions on clicking one of the icon buttons in the action bar
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        //options to open and close the search tab
        if(id == R.id.searchDropButton){
            if(visible == 0) {
                searchTab.setVisibility(View.VISIBLE);
                buttonBar.setVisibility(View.GONE);
                visible =1;
            }
            else{
                searchTab.setVisibility(View.GONE);
                buttonBar.setVisibility(View.VISIBLE);
                visible =0;
            }
        }
        //back button functionality
        else if(id == R.id.searchBackButton){
            roadSearch.setText("");
            listTitle.setText(listName);
            planListView.setAdapter(null);
            RoadworkAdapter arrayAdapter = new RoadworkAdapter(MainActivity.this,rList);
            planListView.setAdapter(arrayAdapter);
        }

        return super.onOptionsItemSelected(item);
    }

    //on button click method
    @Override
    public void onClick(View v) {

        //different feed urls to be selected for loading
        String roadworkUrl = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
        String plannedUrl = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
        String searchName;


        //switch statemnt for each of the buttons in the main body of the app
        switch(v.getId()){
            //loads incidents feed
            case R.id.incidentButton:
                listName = "Current Incidents";
                listTitle.setText(listName);
                loadingText.setText("Loading " +  listName + "...");
                loadingText.setVisibility(View.VISIBLE);
                
                fetchFeed(incidentURL);

                break;

            //Loads Planned roadworks feed
            case R.id.plannedButton:
                listName = "Planned Roadworks";
                listTitle.setText(listName);
                loadingText.setText("Loading " +  listName + "...");
                loadingText.setVisibility(View.VISIBLE);

                fetchFeed(plannedUrl);
                break;

            //loads the current roadworks feed
            case R.id.roadworkButton:
                listName = "Current Roadworks";
                listTitle.setText(listName);
                loadingText.setText("Loading " +  listName + "...");
                loadingText.setVisibility(View.VISIBLE);
                fetchFeed(roadworkUrl);
                break;



            //takes in search name and ands to the road search function
            case R.id.searchRoadButton:
                String roadName;
                roadName = roadSearch.getText().toString();
                searchName = "Searching for: \n " +roadName;
                listTitle.setText(searchName);
                Log.e("MyTag", roadName);
                searchRoad(roadName);
                searchBackButton.setVisibility(View.VISIBLE);
                break;

            //takes in search date and sends to date search function
            case R.id.searchDateButton:
                String date;
                int monthNumber = datePicker.getMonth();
                String month = MONTHS[monthNumber];
                date = datePicker.getDayOfMonth() + " " + month + " " + datePicker.getYear();
                searchName = "Searching: \n" + date;
                listTitle.setText(searchName);
                searchDate(date);
                searchBackButton.setVisibility(View.VISIBLE);
                break;

            default:
                break;
        }
    }

    public void fetchFeed(String url)
    {
        //Create a timer thread with the pull parser task inside
        Task task = new Task(url);
        Timer timer = new Timer();

        //schedule the timer to refresh the current feed ever 10 mins
        timer.schedule(task, 0, 600000) ;

        //new Thread(new Task(url)).start();
    }

    //search road method
    public void searchRoad(String roadName){
        Log.e("MyTag", "In searchRoad " + roadName);
        //if the search value is empty ask for user to enter road
        if(roadName.equals("")){
            Toast.makeText(this, "Please Enter a Road", Toast.LENGTH_LONG).show();

        }
        //else search the current loaded field for instances of the search term
        else{
            int size = rList.size();
            Log.e("MyTag", "rList: " + size );
            ArrayList<Roadwork> sList = new ArrayList<>(rList);

            int size2 = sList.size();
            Log.e("MyTag", "sList: " + size2 );
            int i = 0;
            while(i < sList.size())
            {
                int list = sList.get(i).getTitle().indexOf(roadName);
                if(list == -1){
                    sList.remove(i);
                }
                else {
                    i++;
                }
            }

            //for testing feed sizes and ensure full feed comes through and is searched
            size = rList.size();
            size2 = sList.size();
            Log.e("MyTag", "rList: " + size );
            Log.e("MyTag", "sList: " + size2 );

            //put data in custom array adapter sepcifically made for these feeds
            planListView.setAdapter(null);
            RoadworkAdapter arrayAdapter = new RoadworkAdapter(MainActivity.this,sList);
            planListView.setAdapter(arrayAdapter);

        }

    }

    //date search method
    public void searchDate(String searchDate)
    {
        Log.e("MyTag", "searchDate: " + searchDate );
        ArrayList<Roadwork> sList = new ArrayList<>(rList);

        //loops through roadworks looking for the search date in the description
        int i = 0;
        while(i < sList.size())
        {
            boolean inList = sList.get(i).getDescription().contains(searchDate);
            if(!inList){
                sList.remove(i);
            }
            else {
                i++;
            }
        }

        //add the new list to the custom array addapter
        planListView.setAdapter(null);
        RoadworkAdapter arrayAdapter = new RoadworkAdapter(MainActivity.this,sList);
        planListView.setAdapter(arrayAdapter);

    }

    //task which runs on the timer thread
    private class Task extends TimerTask {
        private String url;
        public Task(String aurl)
        {
            url = aurl;
        }

        //run method inside taks
        @Override
        public void run()
        {
            //different variables for pull parser

            URL aurl;
            URLConnection yc;
            BufferedReader in;
            String inputLine ;

            Log.e("MyTag","in run");

            try
            {
                Log.e("MyTag","in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                Log.e("MyTag","after ready");
                result = "";

                //add the whole feed to a string
                while ((inputLine = in.readLine()) != null)
                {
                    //full feed in results by the end of loop
                    result = result + inputLine;
                }
                in.close();
            }
            catch (IOException ae)
            {
                Log.e("MyTag", "ioexception in run");
            }

            //runs method back on mainActivity outside of task class
            MainActivity.this.runOnUiThread(() -> {
                Log.d("UI thread", "I am the UI thread");

                rList = roadworksPlanned(result);

                //put the list in the new array addapter
                loadingText.setVisibility(View.GONE);
                RoadworkAdapter arrayAdapter = new RoadworkAdapter(MainActivity.this,rList);
                planListView.setAdapter(arrayAdapter);


            });
        }


        // putting the feed results into roadworks class using pull parser
        private ArrayList<Roadwork> roadworksPlanned(String result){
            //variables used in the class
            Roadwork plan = null;
            ArrayList<Roadwork> rList = null;
            int inItem = 0;

            //pull parser code
            try{
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(new StringReader(result));
                int eventType;
                eventType = xpp.getEventType();

                while(eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        //checck tag for channel and is channel is found creat a new list
                        if (xpp.getName().equalsIgnoreCase("channel")) {


                            rList = new ArrayList<>();
                        }
                        //if parser finds an item then creat a new instsance of the roadowork class
                        else if (xpp.getName().equalsIgnoreCase("item")) {
                            inItem = 1;

                            plan = new Roadwork();
                        }
                        //if a roadwork class has been mad add the variables found inside item to class
                        if(inItem == 1) {
                            if (xpp.getName().equalsIgnoreCase("title")) {
                                String title = xpp.nextText();
                                plan.setTitle(title);
                            } else if (xpp.getName().equalsIgnoreCase("description")) {
                                String description = xpp.nextText();
                                String regex = "<br />";
                                String subst = "\\\n \\\n";

                                Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
                                Matcher matcher = pattern.matcher(description);

                                // The substituted value will be contained in the result variable
                                String results = matcher.replaceAll(subst);

                                plan.setDescription(results);
                            }
//                          else if(xpp.getName().equalsIgnoreCase("pubdate")){
//                              String pubDate = xpp.nextText();
//                              Log.e("MyTag", "publish date is" + pubDate);
//                              plan.setPubDate(pubDate);
//                          }
                        }
                        //when end tag is found end parsing and add object created to array list
                    } else if (eventType == XmlPullParser.END_TAG) {
                        if (xpp.getName().equalsIgnoreCase("item")) {
                            rList.add(plan);
                        } else if (xpp.getName().equalsIgnoreCase("channel")) {
                            int size;
                            size = rList.size();
                            Log.e("MyTag", "The size is " + size);
                        }
                    }
                    eventType = xpp.next();
                }
            }
            catch (XmlPullParserException ae1) {
                Log.e("MyTag", "Parsing Error", ae1);
            }
            catch(IOException ae1){
                Log.e("MyTag","IO error during parsing");
            }

            Log.e("MyTag", "End Document");
            return rList;
        }
    }
}