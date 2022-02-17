package com.example.passwordmanger;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.passwordmanger.databinding.FragmentFirstBinding;
import com.example.passwordmanger.databinding.FragmentHomeBinding;
import com.example.passwordmanger.encryption.Encryption;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "email";
    private static final String ARG_PARAM2 = "pass";

    // TODO: Rename and change types of parameters
    private String email;
    private String pass;
    private String user;
    private LinkedList<Password> passwords;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            email = getArguments().getString(ARG_PARAM1);
            pass = getArguments().getString(ARG_PARAM2);
            user = email.split("@")[0];
            passwords=new LinkedList<Password>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        binding.user.setText(email.split("@")[0]);
        binding.floatingActionButton.setOnClickListener(this::addNewPassword);
        readPasswords();
        return binding.getRoot();
    }

    private void readPasswords() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference("users/"+user+"/passwords/");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot list) {
                passwords.clear();

                if (list.getValue() != null) {

                    for (DataSnapshot p : list.getChildren()) {
                        passwords.add(new Password(p.getKey(),p.child("desc").getValue().toString(),p.child("pass").getValue().toString()));

                    }

                }
                RecyclerView recyclerView = (RecyclerView) binding.driverQueue;
                PassAdapter adapter = new PassAdapter(user,passwords, getContext());
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapter);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Toast.makeText(getContext(), databaseError.toException().toString(), Toast.LENGTH_SHORT).show();

            }
        };

        databaseReference.addValueEventListener(postListener);
    }

    public void addNewPassword(View view) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.add_password, null);
        final EditText txt_inputText = (EditText) mView.findViewById(R.id.description);
        final EditText pass_inputText = (EditText) mView.findViewById(R.id.new_pass);
        Button btn_cancel = (Button) mView.findViewById(R.id.btn_cancel);
        Button btn_okay = (Button) mView.findViewById(R.id.btn_okay);
        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String desc = txt_inputText.getText().toString().trim();
                String pass = pass_inputText.getText().toString();
                if (desc.length() != 0 && pass.length() != 0) {
                    addPassword(desc, pass);
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(getActivity(), "check fields!!", Toast.LENGTH_SHORT).show();

                }


            }
        });
        alertDialog.show();
    }

    public void addPassword(String desc, String pass) {
        Encryption encryption = new Encryption(pass, "preorder");
        pass = encryption.getCipherText();
        Map<String, Object> password = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        data.put("desc", desc);
        data.put("pass", pass);
        password.put(getKey(),data);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("users/"+user+"/passwords/" );
        databaseReference.updateChildren(password);
        Toast.makeText(getActivity(), "success!!", Toast.LENGTH_SHORT).show();


    }

    public String getKey() {
        DateFormat dateFormat = new SimpleDateFormat("kk:mm:ss");
        String dateString = dateFormat.format(new Date()).toString();

        Calendar cal = Calendar.getInstance();
        // displaying date
        Format f = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = f.format(new Date());
        return strDate + dateString;
    }
}