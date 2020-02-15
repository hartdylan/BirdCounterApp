package augustana.birdcounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import static augustana.birdcounter.MainActivity.currentBirdDBRef;

public class TopBirdCounts extends AppCompatActivity implements View.OnClickListener {

    Button returnToMainBtn;
    ListView birdCountList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_bird_counts);
        returnToMainBtn = findViewById(R.id.mainBtn);
        birdCountList = findViewById(R.id.birdCountList);
        populateBirdCountValues();

     }

    @Override
    public void onClick(View v) {
        returnToMain();
    }

    public void returnToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void populateBirdCountValues() {

        for(String bird: MainActivity.birds) {
            MainActivity.currentBirdDBRef = FirebaseDatabase.getInstance().getReference(bird);
            MainActivity.currentBirdCountDBRef = currentBirdDBRef.child("count");
            MainActivity.currentBirdCountDBRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }
}
