package sg.edu.rp.webservices.authenticationproject;

import android.content.Intent;
import android.icu.util.Calendar;
import android.text.format.DateFormat;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private Button btnAddMessage;
    private TextView tvForecast;
    private ListView lvChat;
    private EditText etAddMessage;

    CustomAdapter ca;
    ArrayList<ChatDetails> alChatDetails;

    private static String TAG = "ChatActivity";
    String uid, user;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference messageListRef, profileListRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        uid = firebaseUser.getUid();

        alChatDetails = new ArrayList<ChatDetails>();

        btnAddMessage = (Button)findViewById(R.id.addMessage);
        lvChat = (ListView)findViewById(R.id.lv);
        etAddMessage = (EditText)findViewById(R.id.etMessage);
        tvForecast = (TextView)findViewById(R.id.tvForecast);


        firebaseDatabase = FirebaseDatabase.getInstance();
        messageListRef = firebaseDatabase.getReference("/messages");
        profileListRef = firebaseDatabase.getReference("/profiles/"+uid);
        HttpRequest request= new HttpRequest("https://api.data.gov.sg/v1/environment/2-hour-weather-forecast");
        request.setMethod("GET");
        request.setAPIKey("api-key", "VAwL0ma3x1pJJAVDWPN0GES51UVvsVli");
        request.execute();

        try{
            String jsonString = request.getResponse();
            Log.d(TAG, "jsonString: " + jsonString);

            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArrayItems = jsonObject.getJSONArray("items");

            JSONObject jsonObjectItems = jsonArrayItems.getJSONObject(0);
            JSONArray forecasts = jsonObjectItems.getJSONArray("forecasts");

            for (int i = 0; i < forecasts.length(); i++){
                JSONObject forecast = forecasts.getJSONObject(i);
                String area = forecast.getString("area");

                if (area.equalsIgnoreCase("Woodlands")){
                    String f = forecast.getString("forecast");
                    tvForecast.setText("Weather Forecast @ " + area + ": " + f);
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        profileListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userDisplayName = dataSnapshot.getValue(String.class);
                user = userDisplayName;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        messageListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                alChatDetails.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    ChatDetails post = postSnapshot.getValue(ChatDetails.class);
                    alChatDetails.add(post);
                }

                for (int i=0; i< alChatDetails.size(); i++) {
                    Log.d("Database content", i + ". " + alChatDetails.get(i));
                    ca = new CustomAdapter(ChatActivity.this, R.layout.row, alChatDetails);

                }
                lvChat.setAdapter(ca);
                ca.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        btnAddMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date();
                String dateCurrent = String.valueOf(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", date));

                ChatDetails chatDetails = new ChatDetails(etAddMessage.getText().toString(), dateCurrent, user);
                messageListRef.push().setValue(chatDetails);


            }
        });





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {

            firebaseAuth.signOut();

            Intent i = new Intent(getBaseContext(), MainActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

}
