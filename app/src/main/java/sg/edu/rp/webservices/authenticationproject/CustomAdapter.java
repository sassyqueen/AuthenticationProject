package sg.edu.rp.webservices.authenticationproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 15004557 on 15/8/2017.
 */

public class CustomAdapter extends ArrayAdapter {

    private ArrayList<ChatDetails> cd;
    // private String[] strObjects;
    private Context context;
    private TextView tvName, tvMessage, tvDate;


    public CustomAdapter(Context context, int resource, ArrayList<ChatDetails> objects){
        super(context, resource, objects);
        // Store the food that is passed to this adapter
        cd = objects;
        // Store Context object as we would need to use it later
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // The usual way to get the LayoutInflater object to
        //  "inflate" the XML file into a View object
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // "Inflate" the row.xml as the layout for the View object
        View rowView = inflater.inflate(R.layout.row, parent, false);

        // Get the TextView object
        tvName = (TextView) rowView.findViewById(R.id.tvName);
        // Get the ImageView object
        tvDate = (TextView) rowView.findViewById(R.id.tvTime);
        tvMessage = (TextView) rowView.findViewById(R.id.tvMessage);


        // The parameter "position" is the index of the
        //  row ListView is requesting.
        ChatDetails currentItem = cd.get(position);
        // Set the TextView to show the food

        tvName.setText(currentItem.getMessageUser());
        tvDate.setText(currentItem.getMessageTime());
        tvMessage.setText(currentItem.getMessageText());
        return rowView;
    }



}
