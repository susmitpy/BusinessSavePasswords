package e.susmit.business_savepasswords;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.security.GeneralSecurityException;
import java.util.HashMap;

public class New_Entry extends AppCompatActivity {

    EditText etnewfor, etnewuser, etnewpass;
    String newforwhat, newun, newpw, secKey;
    DbAdapter db;
    Common common;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__entry__layout);
        DoNew();
    }

    private void DoNew() {
        Button Save = findViewById(R.id.btnsave);
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    Log.e("New_Entry", "Hide Keyboard");
                }
                try {
                    SaveAction();
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void SaveAction() throws GeneralSecurityException {
        etnewfor = findViewById(R.id.NewFor);
        etnewuser = findViewById(R.id.NewUser);
        etnewpass = findViewById(R.id.NewPass);
        newforwhat = etnewfor.getText().toString().toLowerCase().trim();
        newun = etnewuser.getText().toString().trim();
        newpw = etnewpass.getText().toString().trim();

        if (validate()) Nmain();

    }

    private void Nmain() throws GeneralSecurityException {
        db = new DbAdapter(this);
        db.open();
        Cursor cursor;
        cursor = db.GetConf("seckey");
        cursor.moveToPosition(0);
        secKey = cursor.getString(cursor.getColumnIndex("SECKEY"));
        cursor.close();
        common = new Common();
        Firestore firestore = new Firestore();
        firestore.addData(newforwhat,
                          common.encrypt(common.encode(newun), secKey),
                          common.encrypt(common.encrypt(common.encode(newpw), secKey), secKey));
        clear();

        //common.done(getApplicationContext());
        // Toast.makeText(getBaseContext(), R.string.done, Toast.LENGTH_SHORT).show();
        //etnewfor.requestFocus();

    }

    private void clear() {
        etnewfor.getText().clear();
        etnewuser.getText().clear();
        etnewpass.getText().clear();
    }

    private boolean validate() {
        /*List<EditText> inputs = new ArrayList<>(Arrays.asList(etnewfor, etnewuser, etnewpass));
        List<String> inputsstr = new ArrayList<>(Arrays.asList(newforwhat, newun, newpw));
        for (String x : inputsstr) {
            if (TextUtils.isEmpty(x)) {
                inputs.get(inputsstr.indexOf(x)).setError("Cannot Be Empty!");
            }
        }*/

        HashMap<String, EditText> input_str = new HashMap<String, EditText>() {
            {
                put(newforwhat, etnewfor);
                put(newun, etnewuser);
                put(newpw, etnewpass);
            }
        };
        for (Object x : input_str.keySet())
            if (TextUtils.isEmpty(x.toString())) {
                input_str.get(x).setError(getString(R.string.empty));
                return false;
            }
        return true;
    }
}
