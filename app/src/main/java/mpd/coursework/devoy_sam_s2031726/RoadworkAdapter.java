package mpd.coursework.devoy_sam_s2031726;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import java.util.ArrayList;

public class RoadworkAdapter extends ArrayAdapter<Roadwork> {

    private Context rContext;
    private ArrayList<Roadwork> rList = new ArrayList<>();

    public RoadworkAdapter(@NonNull Context context, ArrayList<Roadwork> list){
        super(context,0, list);
        rContext = context;
        rList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View listItem = convertView;
        if(listItem == null)
        {
            listItem = LayoutInflater.from(rContext).inflate(R.layout.my_text_view,parent,false);
        }
        Roadwork currentRoadwork = rList.get(position);

        TextView title = listItem.findViewById(R.id.roadworkTitle);
        title.setText(currentRoadwork.getTitle());

        TextView description = listItem.findViewById(R.id.roadworkDescription);
        description.setText(currentRoadwork.getDescription());

        return listItem;
    }

}
