package augustana.birdcounter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;


public class BirdListAdapter extends ArrayAdapter<Bird> {

    private Activity context;
    private List<Bird> birdList;

    public BirdListAdapter(Activity context, List<Bird> birdList) {
        super(context, R.layout.layout_adapter, birdList);
        this.context = context;
        this.birdList = birdList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_adapter, null, true);

        TextView name = listViewItem.findViewById(R.id.birdNameTextView);
        TextView count = listViewItem.findViewById(R.id.birdCountTextView);

        Bird bird = birdList.get(position);

        name.setText(bird.getName());
        count.setText("Found: " + bird.getCount());

        return listViewItem;

    }
}
