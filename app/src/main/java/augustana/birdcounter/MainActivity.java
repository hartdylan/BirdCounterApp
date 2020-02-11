package augustana.birdcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button bjButton;
    Button kiwiButton;
    TextView bjCount;
    TextView kiwiCount;
    int bjNum;
    int kNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bjButton = (Button) findViewById(R.id.blueJayBtn);
        bjButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doBjCount();
            }
        });
        kiwiButton = (Button) findViewById(R.id.KiwiBtn);
        kiwiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doKCount();
            }
        });
        bjCount = (TextView) findViewById(R.id.blueJayCountText);
        kiwiCount = (TextView) findViewById(R.id.KiwiCountText);

    }

    private void doBjCount() {
        bjNum++;
        bjCount.setText("Blue Jays seen: " + bjNum);
    }

    private void doKCount() {
        kNum++;
        kiwiCount.setText("Kiwis seen: " + kNum);
    }
}
