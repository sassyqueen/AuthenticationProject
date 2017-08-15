package sg.edu.rp.webservices.authenticationproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SetDisplayName extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    Button btnDisplayName;
    EditText etDisplayName;
    String uid, displayName;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userListRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_display_name);

        btnDisplayName = (Button)findViewById(R.id.btnDisplayName);
        etDisplayName = (EditText)findViewById(R.id.etDisplayName);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        uid = firebaseUser.getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();
        userListRef = firebaseDatabase.getReference("/profiles/" + uid);

        btnDisplayName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                userListRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String displayName = dataSnapshot.getValue(String.class);
                        etDisplayName.setText(displayName);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                String userDisplayName = etDisplayName.getText().toString();
                userListRef.setValue(userDisplayName);
                Toast.makeText(getBaseContext(), "Successful!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getBaseContext(), ChatActivity.class);
                startActivity(i);

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
