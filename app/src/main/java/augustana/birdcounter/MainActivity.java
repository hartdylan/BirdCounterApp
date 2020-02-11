package augustana.birdcounter;

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
import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    String[] birds = {"Blue Jay", "Kiwi"};
    HashMap<String, Integer> mp;
    String curBird;
    int totalFound;
    Spinner sp;
    Adapter ad;
    TextView name,count;
    Button fBtn, uBtn;
    ImageView bImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mp = initializeMap(birds);

        bImg = findViewById(R.id.birdImg);
        name = findViewById(R.id.birdName);
        count = findViewById(R.id.foundText);
        fBtn = findViewById(R.id.foundBtn);
        uBtn = findViewById(R.id.undoBtn);
        fBtn.setOnClickListener(this);
        uBtn.setOnClickListener(this);
        sp = findViewById(R.id.spinner);
        ad = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, birds );
        sp.setAdapter((SpinnerAdapter) ad);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                curBird = birds[position];
                name.setText(curBird);
                updateDisplayCount();
                if(curBird.equals("Blue Jay")) {
                    bImg.setImageResource(R.drawable.bluejay);
                } else if (curBird.equals("Kiwi")) {
                    bImg.setImageResource(R.drawable.kiwi);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void onClick(View view, TextView textView) {

    }

    @Override
    public void onClick(View v) {
        if(v == fBtn) {
            mp.put(curBird, mp.get(curBird)+1);
        } else if (v == uBtn && mp.get(curBird) > 0) {
            mp.put(curBird, mp.get(curBird)-1);
        }
         updateDisplayCount();
    }

    public HashMap<String, Integer> initializeMap(String[] birdTypes) {
        HashMap<String, Integer> map = new HashMap<>();
        for(String name: birdTypes) {
            map.put(name, 0);
        }
        System.out.println(map.get("Blue Jay"));
        System.out.println(map.get("Kiwi"));
        return map;
    }

    public void updateDisplayCount() {
        count.setText("Found : " + mp.get(curBird));
    }
}