package augustana.birdcounter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewPropertyAnimatorListenerAdapter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    String[] birds = {"Blue Jay", "Kiwi", "Bird3", "Bird4", "Bird5", "Bird6", "Bird7", "Bird8", "Bird9", "Bird10", "Bird11", "Bird12", "Bird13", "Bird14", "Bird15", "Bird16", "Bird17", "Bird18", "Bird19", "Bird20"};
    String curBird;
    Spinner sp;
    Adapter ad;
    TextView name, count;
    Button fBtn, uBtn;
    ImageView bImg;
    DatabaseReference ref;
    DatabaseReference ref2;
    Long curVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        curBird = birds[0];
        ref = FirebaseDatabase.getInstance().getReference(curBird);
        ref2 = ref.child("count");
        bImg = findViewById(R.id.birdImg);
        name = findViewById(R.id.birdName);
        count = findViewById(R.id.foundText);
        fBtn = findViewById(R.id.foundBtn);
        uBtn = findViewById(R.id.undoBtn);
        fBtn.setOnClickListener(this);
        uBtn.setOnClickListener(this);
        sp = findViewById(R.id.spinner);
        ad = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, birds);
        sp.setAdapter((SpinnerAdapter) ad);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                curBird = birds[position];
                curVal = updateBirdRef();
                name.setText(curBird);
                if (curBird.equals("Blue Jay")) {
                    bImg.setImageResource(R.drawable.bluejay);
                } else if (curBird.equals("Kiwi")) {
                    bImg.setImageResource(R.drawable.kiwi);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ref = FirebaseDatabase.getInstance().getReference(curBird.toLowerCase().replaceAll(" ", ""));
        ref2 = ref.child("count");
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                curVal = dataSnapshot.getValue(Long.class);
                count.setText("Found : " + dataSnapshot.getValue(Long.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        if (v == fBtn) {
            ref2.setValue(curVal+1);
        } else if (v == uBtn && curVal > 0) {
            ref2.setValue(curVal-1);
        }
    }

    public Long updateBirdRef() {
        ref = FirebaseDatabase.getInstance().getReference(curBird.toLowerCase().replaceAll(" ", ""));
        ref2 = ref.child("count");
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                curVal = dataSnapshot.getValue(Long.class);
                count.setText("Found : " + dataSnapshot.getValue(Long.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return curVal;
    }
}


