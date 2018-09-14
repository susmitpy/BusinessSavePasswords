package e.susmit.business_savepasswords;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;



public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        DoWhat();

    }

    private void DoWhat() {
        Button NewEntry = (Button)findViewById(R.id.btnnew);
        NewEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                New_Entry();
            }
        });

        Button Search = (Button)findViewById(R.id.btnsea);
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Search_Entry();
            }
        });

        Button Change = (Button)findViewById(R.id.btnchng);
        Change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Change_Entry();
            }
        });
    }

    private void Search_Entry() {
        Intent search = new Intent(Home.this, Search_Entry.class);
        startActivity(search);
    }

    private void New_Entry() {
        Intent newe = new Intent(Home.this, New_Entry.class);
        startActivity(newe);
    }

    private void Change_Entry(){
        Intent change = new Intent(Home.this, Change_Entry.class);
        startActivity(change);

    }
}
