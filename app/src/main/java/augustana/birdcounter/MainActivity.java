package augustana.birdcounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    String[] birds = {"Blue Jay", "Kiwi", "Golden Pheasant", "Philippine Eagle",
            "Peregrine Falcon", "Frigatebird", "Loon", "Merlin", "Nighthawk", "Oriole",
            "Puffin", "Quail", "Cassowary", "Emperor Penguin", "Andean Cock-of-the-Rock", "Hoatzin", "Shoebill",
            "California Condor", "Arctic Tern", "Marabou Stork"};
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
        Arrays.sort(birds);
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
                setNewBird(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ref = FirebaseDatabase.getInstance().getReference(curBird);
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
        } else {
            resetAll();
        }
    }

    public Long updateBirdRef() {
        ref = FirebaseDatabase.getInstance().getReference(curBird);
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

    public void setNewBird(int pos) {
        curBird = birds[pos];
        curVal = updateBirdRef();
        name.setText(curBird);
        if (curBird.equals("Andean Cock-of-the-Rock")) {
            bImg.setImageResource(R.drawable.andean);;
        } else if (curBird.equals("Arctic Tern")) {
            bImg.setImageResource(R.drawable.tern);
        } else if (curBird.equals("Blue Jay")) {
            bImg.setImageResource(R.drawable.bluejay);
        } else if (curBird.equals("California Condor")) {
            bImg.setImageResource(R.drawable.condor);
        } else if (curBird.equals("Cassowary")) {
            bImg.setImageResource(R.drawable.cassowary);
        } else if (curBird.equals("Emperor Penguin")) {
            bImg.setImageResource(R.drawable.emperorpenguin);
        } else if (curBird.equals("Frigatebird")) {
            bImg.setImageResource(R.drawable.frigatebird);
        } else if (curBird.equals("Golden Pheasant")) {
            bImg.setImageResource(R.drawable.goldenpheasant);
        } else if (curBird.equals("Hoatzin")) {
            bImg.setImageResource(R.drawable.hoatzin);
        } else if (curBird.equals("Kiwi")) {
            bImg.setImageResource(R.drawable.kiwi);
        } else if (curBird.equals("Loon")) {
            bImg.setImageResource(R.drawable.loon);
        } else if (curBird.equals("Marabou Stork")) {
            bImg.setImageResource(R.drawable.stork);
        } else if (curBird.equals("Merlin")) {
            bImg.setImageResource(R.drawable.merlin);
        } else if (curBird.equals("Nighthawk")) {
            bImg.setImageResource(R.drawable.nighthawk);
        } else if (curBird.equals("Oriole")) {
            bImg.setImageResource(R.drawable.oriole);
        } else if (curBird.equals("Peregrine Falcon")) {
            bImg.setImageResource(R.drawable.peregrinefalcon);
        } else if (curBird.equals("Philippine Eagle")) {
            bImg.setImageResource(R.drawable.philippineeagle);
        } else if (curBird.equals("Puffin")) {
            bImg.setImageResource(R.drawable.puffin);
        } else if (curBird.equals("Quail")) {
            bImg.setImageResource(R.drawable.quail);
        } else if (curBird.equals("Shoebill")) {
            bImg.setImageResource(R.drawable.shoebill);
        }
    }

    public void resetAll() {
        for(String bird: birds) {
            ref = FirebaseDatabase.getInstance().getReference(curBird);
            ref2 = ref.child("count");
            ref2.setValue(0);
            curVal = (long) 0;
        }

    }
}


