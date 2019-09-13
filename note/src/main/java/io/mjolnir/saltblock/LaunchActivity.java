package io.mjolnir.saltblock;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LaunchActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LaunchActivity.class.getSimpleName(), "onCreate");
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LaunchActivity.class.getSimpleName(), "onStart");
        FirebaseUser user = mAuth.getCurrentUser();

        signIn(user);
    }

    private void signIn(FirebaseUser user) {
        if (user == null) {
            mAuth.signInAnonymously()
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startMain();
                            } else {
                                Log.e(LaunchActivity.class.getSimpleName(), "Sign in failed");
                            }

                        }
                    });

        } else {
            startMain();
        }
    }

    private void startMain() {
        Intent intent = new Intent(LaunchActivity.this,
                MainActivity.class);
        startActivity(intent);
        finish();
    }
}
