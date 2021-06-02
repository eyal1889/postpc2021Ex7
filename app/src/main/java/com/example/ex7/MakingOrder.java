package com.example.ex7;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

public class MakingOrder extends AppCompatActivity {
    ListenerRegistration listener=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_making_order);
        FirebaseFirestore firestore = OrderApp.getInstance().getFireBase();
        String id = OrderApp.getInstance().loadFromSp();
        listener = firestore.collection("orders").document(id)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error!=null){
                            Toast.makeText(MakingOrder.this, "something went wrong, try again later",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else if (!value.exists() || value==null){
                            Toast.makeText(MakingOrder.this, "order does not exists",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            FirestoreOrder order = value.toObject(FirestoreOrder.class);
                            if (order.getStatus().equals("ready")){
                                createReadyOrderActivity();
                            }
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (listener!=null){
            listener.remove();
        }
    }
    private void createReadyOrderActivity(){
        Intent ready_order = new Intent(MakingOrder.this,ReadyOrder.class);
        MakingOrder.this.startActivity(ready_order);
        finish();
    }
}

