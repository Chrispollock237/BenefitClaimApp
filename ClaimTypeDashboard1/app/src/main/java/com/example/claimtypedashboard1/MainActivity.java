package com.example.claimtypedashboard1;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    // Member variables.
    private RecyclerView mRecyclerView;
    private ArrayList<ClaimType> mClaimData;
    private ClaimTypeAdapter mAdapter;

    // Timer variable to hold the timers based on User activity
    Timer exitTimer = new Timer();
    Timer sessionTimer = new Timer();

    // Handler to handle when the user interacts(or doesn't) with the activity
    private Handler pauseHandler;
    private Runnable pauseRunner;

    // Client object to hold the current clients information
    public static Client currentClient = new Client();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get extra from intent(Client object) from LoginActivity
        Intent clientIntent = getIntent();
        currentClient = (Client) clientIntent.getSerializableExtra("Client");

        // Creates a toolbar object to handle toolbar event listeners(i.e. back button)
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);

        // Icon onClick listener
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent(MainActivity.this,
                        LoginActivity.class);
                startActivity(intentLogin);
            }
        });

        // Number of columns to be laid out based on the orientation of the phone
        int gridColumnCount = getResources().getInteger(R.integer.grid_column_count);

        // Initialize the RecyclerView.
        mRecyclerView = findViewById(R.id.recyclerView);

        // Set the Layout Manager based on portrait or landscape. Resource values
        // (integers folder) set to the number of columns wanted.
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridColumnCount));

        // Initialize the ArrayList that will contain the data.
        mClaimData = new ArrayList<>();

        // Initialize the adapter and set it to the RecyclerView.
        mAdapter = new ClaimTypeAdapter(this, mClaimData);
        mRecyclerView.setAdapter(mAdapter);

        // Get the data.
        initializeData();

        /*// Initialize the Handler and Runnable variables
        pauseHandler = new Handler();
        pauseRunner = new Runnable() {

            @Override
            public void run() {
                // Build Alert dialog box to display if the user is inactive
                AlertDialog.Builder userAlert =
                        new AlertDialog.Builder(MainActivity.this);

                // Set the dialog title and message.
                userAlert.setTitle("Inactivity Alert!");
                userAlert.setMessage("Are you still active in this session?");

                // Add dialog button
                userAlert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onUserInteraction();
                    }
                });

                userAlert.show();
            }
        };

        // Start the timer when the user is inactive on the current activity.
        // If the user is inactive for more than 3 minutes, prompt the user to
        // make sure they are still there.
        startPauseHandler();


    }

    /*public void onUserInteraction() {
        super.onUserInteraction();

        // Stop the handler when the user interacts with the activity
        stopPauseHandler();

        // Restart the timer after the last user interaction
        startPauseHandler();
    }

    public void stopPauseHandler() {
        pauseHandler.removeCallbacks(pauseRunner);
    }

    public void startPauseHandler() {

        // 5 minute pause time after last user interaction
        pauseHandler.postDelayed(pauseRunner, 30*1000); *//**** SET BACK TO 5 MINS ****/
    }


    /**
     * Initialize the sports data from resources.
     */
    private void initializeData() {

        // Get the resources from the XML file.
        String[] claimList = getResources().getStringArray(R.array.claimtype_titles);
        String[] claimInfo = getResources().getStringArray(R.array.claimtype_info);
        TypedArray claimImageResources = getResources().obtainTypedArray(R.array.type_images);

        // Clear the existing data (to avoid duplication).
        mClaimData.clear();

        // Create the ArrayList of Claim objects with titles and
        // information about each claim.
        for(int i=0;i<claimList.length;i++){
            mClaimData.add(new ClaimType(claimList[i],claimInfo[i],
                    claimImageResources.getResourceId(i,0)));
        }

        claimImageResources.recycle();

        // Notify the adapter of the change.
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Inflates the menu, and adds items to the action bar if it is present.
     *
     * @param menu Menu to inflate.
     * @return Returns true if the menu inflated.
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Handles app bar item clicks.
     *
     * @param item Item clicked.
     * @return True if one of the defined items was clicked.
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        // This comment suppresses the Android Studio warning about simplifying
        // the return statements.
        // noinspection SimplifiableIfStatement
        // Get the menu item id that is clicked on
        switch (item.getItemId()) {
            case R.id.action_contact:
                Intent intentContact = new Intent(MainActivity.this, ContactActivity.class);
                intentContact.putExtra("Client",currentClient);
                startActivity(intentContact);
                return true;
            case R.id.action_help:
                Intent intentHelp = new Intent(MainActivity.this, HelpActivity.class);
                intentHelp.putExtra("Client",currentClient);
                startActivity(intentHelp);
                return true;
            case R.id.action_logout:

                if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAndRemoveTask();
                    finishAffinity();
                }
                else
                    finish();

                Intent intentLogout = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intentLogout);
                return true;
            default:
                // Do nothing
        }
        return super.onOptionsItemSelected(item);
    }

    // Get the currentClient information to be passed as an Extra on an Intent
    public static Client getClient() { return currentClient;}

    // Timer for current session time out if user has been in app for 10 minutes or more.
    // User will be auto logged out and will have to log back in.
    @Override
    public void onStart() {

        super.onStart();

        sessionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Build Alert dialog box to display if the user is inactive
                AlertDialog.Builder userAlert =
                        new AlertDialog.Builder(MainActivity.this);

                // Set the dialog title and message.
                userAlert.setTitle("Session Timeout!");
                userAlert.setMessage("Your session time has expired. " +
                        "You will have to login back in to complete your task.");

                // Add dialog button
                userAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            finishAndRemoveTask();
                            finishAffinity();
                        }
                        else
                            finish();

                        Intent intentLogout = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intentLogout);
                    }
                });
            }
        }, 10*60*1000);

    }

    // Timer for user accidentally hitting the "Home" button, then getting back into app.
    // User has 10 seconds to continue current session after exiting.
    @Override
    public void onStop() {

        super.onStop();

        // Start the timer when the app is exited by the user pressing the home button
        exitTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                // Check the version of the API running the app. If the version is
                // equal to or greater than API 21 then exit the app and remove all
                // the activities form the stack for the default back button
                if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAndRemoveTask();
                    finishAffinity();

                }
                else
                    finish();
            }
        }, 10000);
    }

    // Timer for onPause when there is inactivity on the app/ activity for more than 10 mins
    /*@Override
    public void onPause() {

        super.onPause();

    }*/
}

