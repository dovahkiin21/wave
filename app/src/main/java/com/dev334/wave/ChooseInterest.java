package com.dev334.wave;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dev334.wave.Database.TinyDB;

import java.util.ArrayList;
import java.util.Arrays;

public class ChooseInterest extends AppCompatActivity implements organisationAdapter.SelectedItem {

    private organisationAdapter adapter;
    private ArrayList<String> Interests;
    private ArrayList<String> userInterest;
    private String uInterest="";
    private RecyclerView recyclerView;
    private TextView EditInt, Choosen;
    private TinyDB tinyDB;
    private Button Done;
    String[] interest= {"Dance", "Music", "Photography", "Designing", "Videography","Dramatics","Reading"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_interest);
        recyclerView=findViewById(R.id.intRecycler);
        EditInt=findViewById(R.id.editInterest);
        Choosen=findViewById(R.id.textView5);
        Done=findViewById(R.id.btnDone);

        Interests=new ArrayList<>();
        userInterest=new ArrayList<>();
        Interests.addAll(Arrays.asList(interest));
        adapter=new organisationAdapter(Interests,this);

        tinyDB=new TinyDB(getApplicationContext());
        userInterest=tinyDB.getListString("UserInterest");
        setupText();

        recyclerView.setAdapter(adapter);

        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Done.setOnClickListener(v->{
            tinyDB.putListString("UserInterest",userInterest);
            finish();
        });

        EditInt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    void filter(String text){
        ArrayList<String> temp = new ArrayList();
        for(String d: Interests){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.contains(text)){
                temp.add(d);
            }
        }
        //update recyclerview
        adapter.updateList(temp);
    }

    @Override
    public void selectedItem(String organisation) {
        if (userInterest.contains(organisation)) {
            userInterest.remove(organisation);
            setupText();
        } else {
            if(userInterest.size()==3){
                Toast.makeText(getApplicationContext(),"You can only select at max 3 interest", Toast.LENGTH_SHORT).show();
            }else {
                userInterest.add(organisation);
                setupText();
            }
        }
    }

    private void setupText() {

        uInterest="";
        for(int i=0;i<userInterest.size();i++){
            uInterest=uInterest+userInterest.get(i)+" | ";
        }
        Choosen.setText(uInterest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        tinyDB.putListString("UserInterest",userInterest);
    }
}