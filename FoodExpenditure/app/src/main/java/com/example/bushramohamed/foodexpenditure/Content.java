package com.example.bushramohamed.foodexpenditure;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class Content extends ActionBarActivity {

    private List<Product> foodCategory;
    private Product currentProduct;

    //UI
    private TextView foodTitle;
    private TextView year2012;
    private TextView year2013;
    private TextView avg2012;
    private TextView avg2013;

    private ListView listview;
    private String provTitle;

    //THE LOCATION INDEX
    private String Geo_Location;

    private Bundle bundle;
    private TextView regionTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Household Food Expenditure");
        setContentView(R.layout.activity_content);

        //
        //sets the region title from the passed in string from an intent
        setRegionTitle();

        InputStream input = getResources().openRawResource(R.raw.provinces);

        //Reading provinces from XML
        DomParser dp = new DomParser();

        //Inserting provinces from the xml to the Spinner selector
        foodCategory =  dp.parseFoodCategory(input);

        //populating the list with food categories
        populateToListView();

        //Listens for food category item to be selected
        foodDetailRequest();

        setInitialData();

    }

    public void setInitialData(){

        foodTitle.setText(listview.getItemAtPosition(0).toString());

        //Gets the detail for the first food category on start of the app
        getFoodDetail(foodCategory.get(0),0 );

        calculatePercentage();
    }

    public void setRegionTitle(){
        Intent intent = getIntent();
        bundle = intent.getExtras();

        if(bundle!=null)
        {
            provTitle =(String) bundle.get("currentRegion");
            setTitle(provTitle);

            Geo_Location = (String) bundle.get("Geo_Location");
        }

    }

    public void populateToListView(){
        listview = (ListView) findViewById(R.id.provinceListView);

       //Sorts the food category. This will make it easier to identify and trigger the right
       //food detail data when the food item is selected (using the index)
       Collections.sort(foodCategory, new Comparator<Product>() {
           @Override
           public int compare(Product lhs, Product rhs) {
               return lhs.getValue() - rhs.getValue(); //Ascending
           }
       });

        //Populates the listView with food categories
        List<String> foods = new ArrayList<String>();

        for(int i = 0; i < foodCategory.size(); i++){
            String food = foodCategory.get(i).getProductType();
            foods.add(food);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                foods );

        listview.setAdapter(arrayAdapter);
    }

    public void foodDetailRequest(){
        foodTitle = (TextView)findViewById(R.id.foodTitle);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                foodTitle.setText(listview.getItemAtPosition(position).toString());

                //Gets the detail for the selected food category
                getFoodDetail(foodCategory.get(position), position);

                calculatePercentage();

            }
        });
    }

    //Gets food Detail by calling a method that parses data from xml
    public void getFoodDetail(Product product, int categoryIndex){
        DomParser dp = new DomParser();

        currentProduct = dp.parseFoodDetail(product,Geo_Location,getResources().openRawResource(R.raw.data));

        year2012 = (TextView)findViewById(R.id.spending2012);
        year2013 = (TextView)findViewById(R.id.spending2013);

        NumberFormat nm1 = NumberFormat.getNumberInstance();
        try{
            year2012.setText("$"+nm1.format(currentProduct.getSpending_12()) );

            year2013.setText("$"+nm1.format(currentProduct.getSpending_13()) );

            //the first food category selected (which is the total cost of food) we need this for calculation reasons
            if(categoryIndex == 0){
                Product.totalSpent2012 = product.getSpending_12();
                Product.totalSpent2013 = product.getSpending_13();
            }

        }catch(Exception e){
             year2012.setText("Not Data");
             year2013.setText("No Data");
        }




    }

    public void calculatePercentage(){

        double avgSpent;
        int avg;

        avg2012 = (TextView)findViewById(R.id.pctSpending12);
        avg2013 = (TextView)findViewById(R.id.pctSpending13);

        try {
            avgSpent = (currentProduct.getSpending_12() / Product.totalSpent2012) * 100;
            avg = (int) avgSpent;

            DecimalFormat df = new DecimalFormat("#.##");

            if (avg > 0) {
                avg2012.setText("%" + avg);
            } else {  //the percentage value is less than 0
                avg2012.setText("%" + df.format(avgSpent));
            }

            avgSpent = (currentProduct.getSpending_13() / Product.totalSpent2013) * 100;
            avg = (int) avgSpent;

            if (avg > 0) {
                avg2013.setText("%" + avg);
            } else {//the percentage value is less than 0

                avg2013.setText("%" + df.format(avgSpent));
            }
        }catch(Exception e){
            avg2012.setText("No Data");
            avg2013.setText("No Data");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_content, menu);
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
