package augustana.birdcounter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

    class ViewHolder {
        TextView holdName;
        TextView holdCount;
    }


public class BirdListAdapter extends ArrayAdapter<Bird> {

    private Activity context;
    private List<Bird> birdList;
    private int lastPosition = -1;


    public BirdListAdapter(Activity context, List<Bird> birdList) {
        super(context, R.layout.layout_adapter, birdList);
        this.context = context;
        this.birdList = birdList;
    }


    /**
     * Tutorial for this here; https://www.youtube.com/watch?v=SApBLHIpH8A
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final View result;
        ViewHolder holder = new ViewHolder();

        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_adapter, null, true);
        result = listViewItem;
        holder.holdName = listViewItem.findViewById(R.id.birdNameTextView);
        holder.holdCount = listViewItem.findViewById(R.id.birdCountTextView);

        listViewItem.setTag(holder);

        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
        result.startAnimation(animation);
        lastPosition = position;

        holder.holdName.setText(birdList.get(position).getName());
        holder.holdCount.setText("Found: "+birdList.get(position).getCount());


        return listViewItem;

    }
}
