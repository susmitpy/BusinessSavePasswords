package e.susmit.business_savepasswords;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.security.NoSuchAlgorithmException;


public class MainActivity extends AppCompatActivity {
    EditText pas;
    Button log, forget;
    String pass;
    Common common;
    DbAdapter db;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        db = new DbAdapter(this);
        get_input();
    }

    private void get_input() {
        pas = findViewById(R.id.etpass);
        log =  findViewById(R.id.button);
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    validate();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void validate() throws NoSuchAlgorithmException {
        db.open();
        common = new Common();
        String password = pas.getText().toString().trim();
        //get pass from db
        cursor = db.GetConf("pass");
        cursor.moveToPosition(0);
        pass = cursor.getString(cursor.getColumnIndex("PASS"));
        if (TextUtils.isEmpty(password)) {
            pas.setError(getString(R.string.pasinfo2));
        } else if (!common.enpass(password).equals(pass)) {
            pas.setError("Wrong Password!");
        } else if (common.enpass(password).equals(pass)){
            login();
        }
    }

    private void login() {
        Intent home = new Intent(MainActivity.this, Home.class);
        startActivity(home);
        finish();
    }

}
