package e.susmit.business_savepasswords;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.GeneralSecurityException;
import java.util.HashMap;


public class Change_Entry extends AppCompatActivity {

    EditText etchngfor, etchnguser, etchngpass;
    String chngforwhat, chngun, chngpw, secKey;
    DbAdapter db;
    Common common;
    Firestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__entry);
        DoChng();
    }

    private void DoChng() {
        Button btnchng = (Button) findViewById(R.id.btnchng);
        btnchng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ChngAction();
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void ChngAction() throws GeneralSecurityException {
        etchngfor = (EditText) findViewById(R.id.ChngFor);
        etchnguser = (EditText) findViewById(R.id.ChngUser);
        etchngpass = (EditText) findViewById(R.id.ChngPass);
        chngforwhat = etchngfor.getText().toString().toLowerCase().trim();
        chngun = etchnguser.getText().toString().trim();
        chngpw = etchngpass.getText().toString().trim();
        if (validate()) Cmain();

    }

    private void Cmain() throws GeneralSecurityException {
        //AtomicBoolean exist = firestore.Exists(chngforwhat);

        firestore = new Firestore();
        Cursor cursor;
        db = new DbAdapter(this);
        db.open();
        cursor = db.GetConf("seckey");
        cursor.moveToPosition(0);
        secKey = cursor.getString(cursor.getColumnIndex("SECKEY"));
        cursor.close();
        common = new Common();
        firestore.changeData(chngforwhat,
                             common.encrypt(common.encode(chngun), secKey),
                             common.encrypt(common.encrypt(common.encode(chngpw), secKey), secKey));
        clear();

    }


    private void clear() {
        etchngfor.getText().clear();
        etchnguser.getText().clear();
        etchngpass.getText().clear();
    }

    private boolean validate() {
        /*List<EditText> inputs = new ArrayList<>(Arrays.asList(etchngfor, etchnguser, etchngpass));
        List<String> inputsstr = new ArrayList<>(Arrays.asList(chngforwhat, chngun, chngpw));
        for (String x : inputsstr) {
            if (TextUtils.isEmpty(x)) {
                inputs.get(inputsstr.indexOf(x)).setError("Cannot Be Empty!");
            }
        }*/

        HashMap<String, EditText> input_str = new HashMap<String, EditText>() {
            {
                put(chngforwhat, etchngfor);
                put(chngun, etchnguser);
                put(chngpw, etchngpass);
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
