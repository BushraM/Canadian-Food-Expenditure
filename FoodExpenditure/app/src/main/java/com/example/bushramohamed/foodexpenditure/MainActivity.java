package com.example.bushramohamed.foodexpenditure;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;


public class MainActivity extends ActionBarActivity {

    private Spinner dropdown;
    private String[] data;
    private TextView regionNotSelectedMsg;
    private Button searchButton;
    private String geo_Location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Household Food Expenditure");
        setContentView(R.layout.activity_main);

        InputStream input = getResources().openRawResource(R.raw.provinces);

        //Reading provinces from XML
        DomParser dp = new DomParser();

        //Inserting provinces from the xml to the Spinner selector
        data =  dp.parseProvince(input);

        //populating dropdown spinner menu
        populateProvinceToSpinner();

        //Listens for Search button be pressed
         regionDetailRequest();

        //Listens for exit request from the user
        exitRequest();

    }

    public void populateProvinceToSpinner(){
        dropdown = (Spinner)findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        dropdown.setAdapter(adapter);
    }


    public void regionDetailRequest(){
        searchButton = (Button) findViewById(R.id.searchButton);
        regionNotSelectedMsg = (TextView)findViewById(R.id.error);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String region = dropdown.getSelectedItem().toString();
                displayRegionContent(region);
            }
        });
    }

    public void displayRegionContent(String region){

        //get the GEO location in order to be used to parse specific region's info in the Content class
        geo_Location = String.valueOf(dropdown.getSelectedItemPosition() + 1);

        Intent intent = new Intent(this, Content.class);
        intent.putExtra("currentRegion",region);
        intent.putExtra("Geo_Location",geo_Location);
        startActivity(intent);
    }

    public void exitRequest(){

        //Exit button pressed
        Button exitButton = (Button)findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent intent = new Intent(this, About.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.action_help){
            Intent intent = new Intent(this, Help.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
