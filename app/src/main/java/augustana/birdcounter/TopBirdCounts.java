package augustana.birdcounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class that is used to handle the second activity called top bird counts.
 */
public class TopBirdCounts extends AppCompatActivity {

    // fields
    ListView birdCountList;
    DatabaseReference birdDB;
    List<Bird> birdList;

    /**
     * Method used to initialize the view and fields
     * @param savedInstanceState - Not too sure
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_bird_counts);
        birdCountList = findViewById(R.id.birdCountList);
        birdDB = FirebaseDatabase.getInstance().getReference("birds");
        birdList = new ArrayList<>();
    }

    /**
     * Method used to update the current list of values of birds that will be used in the list view item.
     */
    @Override
    protected void onStart() {
        super.onStart();
        birdDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                birdList.clear();
                for (DataSnapshot birdSnapshot : dataSnapshot.getChildren()) {
                    Bird bird = birdSnapshot.getValue(Bird.class);
                    birdList.add(bird);
                }
                Collections.sort(birdList, Collections.<Bird>reverseOrder());
                BirdListAdapter adapter = new BirdListAdapter(TopBirdCounts.this, birdList);
                birdCountList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}

