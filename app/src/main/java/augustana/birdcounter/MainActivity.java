package augustana.birdcounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * Name: Dylan Hart
 * Date: 13 Feb 2020
 * Class: CSC490
 * Program name: Bird Counter
 * Description:
 * A simple application that allows the user to track 20 different birds from their smart phone and will
 * save the latest birdCount of each bird to a real time database.
 * Three features:
 * 1. 'Spinner' or select box with the birds available to observe within it.
 * 2. Button above 'Spinner' that allows to sort alphabetically (or in reverse alphabetically).
 * 3. Found, undo, and reset button for actually tracking the observed birds.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /*
    Global variables for the project
     */
    public static String[] birds = {"Blue Jay", "Kiwi", "Golden Pheasant", "Philippine Eagle",
            "Peregrine Falcon", "Frigatebird", "Loon", "Merlin", "Nighthawk", "Oriole",
            "Puffin", "Quail", "Cassowary", "Emperor Penguin", "Andean Cock of the Rock", "Hoatzin", "Shoebill",
            "California Condor", "Arctic Tern", "Marabou Stork"};
    static String currentBirdStr;
    Spinner spinner;
    Adapter adapter;
    TextView birdName, birdCount;
    Button foundButton, undoButton, resetButton, sortButton, birdCountViewBtn;
    ImageView birdImage;
    public static DatabaseReference birdDB = FirebaseDatabase.getInstance().getReference("birds");
    public static DatabaseReference currentBirdDBRef;
    public static DatabaseReference currentBirdCountDBRef;
    public static DatabaseReference currentBirdNameDBRef;
    Long currentFoundValue;
    boolean alphabeticalSort;
    int drawableId;

    /**
     * References to objects in the xml file are stored and created here as well as the setup of the
     * screen elements such as the current bird and the stored value from the database.
     * @param savedInstanceState - Bundle storing data to pass between activities.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        birdImage = findViewById(R.id.birdImg);
        birdName = findViewById(R.id.birdName);
        birdCount = findViewById(R.id.foundText);
        alphabeticalSort = true;
        foundButton = findViewById(R.id.foundBtn);
        undoButton = findViewById(R.id.undoBtn);
        resetButton = findViewById(R.id.resetBtn);
        sortButton = findViewById(R.id.sortBtn);
        birdCountViewBtn = findViewById(R.id.birdCountViewBtn);
        foundButton.setOnClickListener(this);
        undoButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);
        sortButton.setOnClickListener(this);
        birdCountViewBtn.setOnClickListener(this);
        Arrays.sort(birds);
        updateSpinner();
        updateBirdValFromDB();
    }

    /**
     * Method used to handle the various buttons that are displayed in the app to the user.
     * @param v - Used to handle event based interactions from the user and handled in the
     *          if/else block.
     */
    @Override
    public void onClick(View v) {
        if (v == foundButton) {
            currentBirdCountDBRef.setValue(currentFoundValue +1);
        } else if (v == undoButton) {
            if(currentFoundValue > 0) {
                currentBirdCountDBRef.setValue(currentFoundValue - 1);
            }
        } else if (v == resetButton){
            resetCurBird();
        } else if (v == sortButton) {
            setSortingMethod();
        } else if (v == birdCountViewBtn) {
            openTopBirdCountView();
        }
    }

    /**
     * Method used to update bird found birdCount when the DB observes a change in data (Firebase).
     * @return Long - Returns the currently displayed bird's DB value for birdCount
     */
    public Long updateBirdCount() {
        /*
        How to learned how to make a reference to the DB and get values.

        https://stackoverflow.com/questions/43293935/how-to-get-child-of-child-value-from-firebase-in-android
         */
        currentBirdDBRef = birdDB.child(currentBirdStr);
        currentBirdCountDBRef = currentBirdDBRef.child("count");
        currentBirdCountDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentFoundValue = dataSnapshot.getValue(Long.class);
                birdCount.setText("Found : " + dataSnapshot.getValue(Long.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return currentFoundValue;
    }

    /**
     * Method to change all object references in xml to use the currently selected bird.
     * @param pos - Passed in as the bird in pos x of the bird array to be a way to setup
     *            the new bird values / assets in the app.
     */
    public void setNewBird(int pos) {
        /*
        At first had a 40 line if/else block of code but now with the help of the
        below source I was able to dynamically change the bird image.

        https://blog.danlew.net/2009/12/27/dynamically_retrieving_resources_in_android/
         */
        currentBirdStr = birds[pos];
        Resources r = getResources();
        drawableId = r.getIdentifier(currentBirdStr.toLowerCase().replaceAll(" ", ""), "drawable", "augustana.birdcounter");
        birdImage.setImageResource(drawableId);
        currentFoundValue = updateBirdCount();
        birdName.setText(currentBirdStr);

    }

    /**
     * Method used to reset the count of the currently selected bird and update the DB.
     */
    public void resetCurBird() {
        currentBirdDBRef = birdDB.child(currentBirdStr);
        currentBirdCountDBRef = currentBirdDBRef.child("count");
        currentBirdCountDBRef.setValue(0);
        currentFoundValue = (long) 0;
        currentBirdNameDBRef = currentBirdDBRef.child("name");
        currentBirdNameDBRef.setValue(currentBirdStr);

    }

    /**
     * Method used to determine which sorting method is selected by the user and update it
     * accordingly in the spinner object.
     */
    public void setSortingMethod() {

        if(alphabeticalSort) { // if alphabeticalSort is true then flip it to sorting by Z-A.
            sortButton.setText("Z-A");
            Arrays.sort(birds, Collections.reverseOrder(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareTo(o2);
                }
            }));
            alphabeticalSort = false;

        } else { // if alphabeticalSort is false then flip it sort by AZ
            sortButton.setText("A-Z");
            Arrays.sort(birds);
            alphabeticalSort = true;
        }
        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, birds);
        spinner.setAdapter((SpinnerAdapter) adapter);
    }

    /**
     * Method that handles state changes of the spinner object to update the bird that is currently
     * desired to be viewew by the user.
     */
    public void updateSpinner() {
        spinner = findViewById(R.id.spinner);
        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, birds);
        spinner.setAdapter((SpinnerAdapter) adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setNewBird(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    /**
     * Method used to get the most current value of currentBird.count from the Firebase DB and
     * set the birdCount ViewText to be that value.
     */
    public void updateBirdValFromDB() {
        currentBirdStr = "";
        currentBirdDBRef = FirebaseDatabase.getInstance().getReference(currentBirdStr);
        currentBirdCountDBRef = currentBirdDBRef.child("count");
        currentBirdCountDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentFoundValue = dataSnapshot.getValue(Long.class);
                birdCount.setText("Found : " + dataSnapshot.getValue(Long.class));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void openTopBirdCountView() {
        Intent intent = new Intent(this, TopBirdCounts.class);
        startActivity(intent);
    }
}


