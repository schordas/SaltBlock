package io.mjolnir.saltblock.ui;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;


import io.mjolnir.saltblock.EncryptionAlgorithm;
import io.mjolnir.saltblock.R;
import io.mjolnir.saltblock.SaltBlock;
import io.mjolnir.saltblock.data.Note;
import io.mjolnir.saltblock.adapter.NoteAdapter;
import io.mjolnir.saltblock.models.ListViewModel;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String uId;

    private ListViewModel viewModel;

    private NoteAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        List<String> RSATest = new ArrayList<>();

        RSATest.add("Hello");
        RSATest.add("World");
        RSATest.add("This is a test string");

        SaltBlock saltBlock = new SaltBlock(EncryptionAlgorithm.RSA);

        List<String> RSAEnc = saltBlock.encrypt("myRSAKey", RSATest);


        for (String enc : RSAEnc) {
            Log.i(MainActivity.class.getSimpleName(), "Enc: " + enc);
        }

        List<String> RSAde = saltBlock.decrypt("myRSAKey", RSAEnc);

        for (String dec : RSAde) {
            Log.i(MainActivity.class.getSimpleName(), "Dec: " + dec);
        }

        viewModel = ViewModelProviders.of(this).get(ListViewModel.class);
        mAuth = FirebaseAuth.getInstance();

        uId = mAuth.getCurrentUser().getUid();

        initAdapter();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initAdapter() {

        adapter = new NoteAdapter();
        viewModel.getNotes(uId).observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    List<Note> notes = viewModel.parseSnapshot(dataSnapshot);
                    adapter.updateList(notes);
                }
            }
        });
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
