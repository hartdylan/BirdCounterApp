package augustana.birdcounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
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
 * Date: 18 Feb 2020
 * Class: CSC490
 * Program name: Bird Counter
 * Description:
 * A simple application that will allow a user to track up to 20 different birds and store the data
 * in a realtime Firebase database. There is also a secondary screen that presents the end user with
 * the top bird count data directly from the database in highest to lowest order.
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
        updateBirdCount();
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
     * Method that us used to display the currently stored value in the DB for the current
     * bird and assign that value to currentFoundValue.
     * @return Long - Returns the currently displayed bird's DB value for birdCount
     */
    public Long updateBirdCount() {
        currentBirdStr = "Andean Cock of the Rock"; // Start with first bird after onCreate is started (user opens app).
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
     * Method to change all object references in xml to use the currently selected bird and update
     * the value of currentFoundValue so that the display can be updated.
     * @param pos - Parameter that is passed in from the updateSpinner method as the user may
     *            select a new bird to 'find'.
     */
    public void setNewBird(int pos) {
        currentBirdStr = birds[pos];
        Resources r = getResources();
        drawableId = r.getIdentifier(currentBirdStr.toLowerCase().replaceAll(" ", ""), "drawable", "augustana.birdcounter");
        birdImage.setImageResource(drawableId);
        currentFoundValue = updateBirdCount();
        birdName.setText(currentBirdStr);

    }

    /**
     * Method used to reset the count of the currently selected bird and update the DB as well as
     * the currentFoundValue.
     */
    public void resetCurBird() {
        currentBirdDBRef = birdDB.child(currentBirdStr);
        currentBirdCountDBRef = currentBirdDBRef.child("count");
        currentBirdCountDBRef.setValue(0);
        currentFoundValue = (long) 0;
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
     * desired to be viewed by the user.
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

    public void openTopBirdCountView() {
        Intent intent = new Intent(this, TopBirdCounts.class);
        startActivity(intent);
    }
}


