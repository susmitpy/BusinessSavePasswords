package e.susmit.business_savepasswords;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.security.GeneralSecurityException;

public class Search_Entry extends AppCompatActivity {
    EditText srhetfor;
    String srhforwhat, showuser, showpass, secKey, u, v;
    DbAdapter db;
    TextView usertv, passtv;
    Common common;
    Button sear, del;
    Firestore firestore = new Firestore();
    String[] ans;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__entry);
        DoSear();
    }

    private void DoSear() {
        del = findViewById(R.id.delbtn);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    delValidate();
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }
            }
        });
        sear = findViewById(R.id.btnsearch);
        sear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //Toast.makeText(getApplicationContext(), "Click", Toast.LENGTH_SHORT).show();
                    try {
                        SearchAction();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void delValidate() throws GeneralSecurityException {
        boolean a, b;
        a = TextUtils.isEmpty(srhetfor.getText());
        b = usertv.getText().toString().equals("Please");
        if (!a && !b) {
            Confirmation();
        }

    }

    private void Confirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(String.format(getString(R.string.delconf), srhforwhat)).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    delAction();
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }

            }
        }).setNegativeButton("NO", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void delAction() throws GeneralSecurityException {
        firestore.delData(srhforwhat);
        common.done(getApplicationContext());
        usertv.setText(getString(R.string.username));
        passtv.setText(getString(R.string.password));
        srhetfor.getText().clear();
        srhetfor.requestFocus();
    }

    private void SearchAction() throws GeneralSecurityException, InterruptedException {
        srhetfor = findViewById(R.id.SearchFor);
        srhforwhat = srhetfor.getText().toString().toLowerCase().trim();
        if (validate()) {
            // Toast.makeText(getApplicationContext(), "Search Action", Toast.LENGTH_SHORT).show();
            Smain();
        }

    }

    private boolean validate() {
        //  Toast.makeText(getApplicationContext(), "validate", Toast.LENGTH_SHORT).show();
        if (srhforwhat.isEmpty()) {
            srhetfor.setError(getString(R.string.empty));
            return false;
        }
        return true;
    }

    private void Smain() throws GeneralSecurityException, InterruptedException {

        Cursor cursor;
        db = new DbAdapter(this);
        db.open();
        cursor = db.GetConf("seckey");
        cursor.moveToPosition(0);
        secKey = cursor.getString(cursor.getColumnIndex("SECKEY"));
        cursor.close();
        common = new Common();
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            Log.e("Search_Entry", "Hide Keyboard");
        }
        firestore.readData(srhforwhat);
        show("Please", "Wait");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ans = firestore.getData();
                u = ans[0];
                v = ans[1];
                try {
                    u = common.decode(common.decrypt(u, secKey));
                    v = common.decode(common.decrypt(common.decrypt(v, secKey), secKey));
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }
                show(u, v);
            }
        }, 4000);
        firestore.data[0] = "Please";
        firestore.data[1] = "Wait";

    }


    private void show(String user, String pass) {
        usertv = findViewById(R.id.tv2);
        passtv = findViewById(R.id.tv3);
        usertv.setText(user);
        passtv.setText(pass);
    }


}