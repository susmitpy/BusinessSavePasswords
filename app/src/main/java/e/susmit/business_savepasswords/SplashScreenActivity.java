package e.susmit.business_savepasswords;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SplashScreenActivity extends AppCompatActivity {
    DbAdapter db;
    Common common;
    String secKey, registeredpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        db = new DbAdapter(this);
        db.open();
        common = new Common();
        FirebaseFirestore.getInstance().collection("Conf").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override

            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        registeredpass = documentSnapshot.getString("pass");
                        secKey = documentSnapshot.getString("seckey");
                        db.insert(registeredpass, secKey);
                        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }
}
