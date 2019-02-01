package com.example.claimtypedashboard1;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class ContactActivity extends AppCompatActivity {

    // Member variable to hold the client object
    Client currentClient = new Client();

    // Timer variable to hold the timers based on User activity
    Timer exitTimer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        // Get extra from intent(Client object) from previous Activity
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
                Intent intentMain = new Intent(ContactActivity.this,
                        MainActivity.class);
                intentMain.putExtra("Client", currentClient);
                startActivity(intentMain);
            }
        });
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
        switch (item.getItemId()) {
            case R.id.action_contact:
                Intent intent = new Intent(ContactActivity.this, ContactActivity.class);
                intent.putExtra("Client", currentClient);
                startActivity(intent);
                return true;
            case R.id.action_help:
                Intent intentHelp = new Intent(ContactActivity.this, HelpActivity.class);
                intentHelp.putExtra("Client", currentClient);
                startActivity(intentHelp);
                return true;
            case R.id.action_logout:

                if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAndRemoveTask();
                    finishAffinity();
                }
                else
                    finish();

                Intent intentLogout = new Intent(ContactActivity.this, LoginActivity.class);
                startActivity(intentLogout);
                return true;
            default:
                // Do nothing
        }
        return super.onOptionsItemSelected(item);
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
}
