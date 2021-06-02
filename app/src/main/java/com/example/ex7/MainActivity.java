package com.example.ex7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseFirestore firestore = OrderApp.getInstance().getFireBase();
        String id = OrderApp.getInstance().loadFromSp();

        if (id.equals("nope") || id==null){
            createNewOrderActivity();
        }
        else{
            firestore.collection("orders").document(id).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (!documentSnapshot.exists() || documentSnapshot==null){
                                createNewOrderActivity();
                            }
                            FirestoreOrder order = documentSnapshot.toObject(FirestoreOrder.class);
                            if (order.getStatus().equals("waiting")){
                                createEditOrderActivity();
                            }
                            if (order.getStatus().equals("in-progress")){
                                createMakingOrderActivity();
                            }
                            if (order.getStatus().equals("ready")){
                                createReadyOrderActivity();
                            }
                        }
                    })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "something went wrong, try again later",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void createNewOrderActivity(){
        Intent new_order = new Intent(MainActivity.this,NewOrderActivity.class);
        MainActivity.this.startActivity(new_order);
        finish();
    }
    private void createEditOrderActivity(){
        Intent edit_order = new Intent(MainActivity.this,EditOrder.class);
        MainActivity.this.startActivity(edit_order);
        finish();
    }
    private void createMakingOrderActivity(){
        Intent making_order = new Intent(MainActivity.this,MakingOrder.class);
        MainActivity.this.startActivity(making_order);
        finish();
    }
    private void createReadyOrderActivity(){
        Intent ready_order = new Intent(MainActivity.this,ReadyOrder.class);
        MainActivity.this.startActivity(ready_order);
        finish();
    }
}