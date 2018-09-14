package e.susmit.business_savepasswords;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class Firestore {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference;
    String[] data = new String[]{"Please", "Wait"};
    private Common common;


    public void addData(String forname, String username, String password) throws GeneralSecurityException {
        common = new Common();
        Map<String, String> data = new HashMap<>();
        data.put("forName", common.enCodePass(forname));
        data.put("userName", username);
        data.put("passWord", password);
        collectionReference = db.collection("Storage");
        collectionReference.add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.e("fsAddData", "Saved To FS");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("fsAddData", e.toString());
            }
        });
    }

    public void readData(String forname) throws GeneralSecurityException {
        common = new Common();
        collectionReference = db.collection("Storage");
        final Query query = collectionReference.whereEqualTo("forName", common.enCodePass(forname));

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String docId = document.getId();
                        DocumentReference documentReference = db.document("Storage/" + docId);
                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                data[0] = documentSnapshot.getString("userName");
                                data[1] = documentSnapshot.getString("passWord");
                            }
                        });
                    }
                }

            }
        });

    }

    public String[] getData()
    {
        return data;
    }

    public void changeData(String forname, String username, String password) throws GeneralSecurityException {
        common = new Common();
        collectionReference = db.collection("Storage");
        final Query query = collectionReference.whereEqualTo("forName", common.enCodePass(forname));
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document: task.getResult()){
                        String docId = document.getId();
                        DocumentReference documentReference = db.document("Storage/" + docId);
                        Map<String, Object> update = new HashMap<String, Object>() {{
                            put("userName", username);
                            put("passWord", password);
                        }
                        };
                        documentReference.update(update);
                    }
                }
            }
        });

    }

    public void delData(String forname) throws GeneralSecurityException {
        common = new Common();
        collectionReference = db.collection("Storage");
        final Query query = collectionReference.whereEqualTo("forName", common.enCodePass(forname));
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    for (QueryDocumentSnapshot documentSnapshot: task.getResult())
                    {
                        String docId = documentSnapshot.getId();
                        DocumentReference documentReference = FirebaseFirestore.getInstance()
                                                              .collection("Storage")
                                                              .document(docId);
                        documentReference.delete();
                    }
                }
            }
        });
    }

}


