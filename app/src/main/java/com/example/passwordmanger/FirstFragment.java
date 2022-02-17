package com.example.passwordmanger;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.example.passwordmanger.encryption.Decryption;

import com.example.passwordmanger.databinding.FragmentFirstBinding;
import com.example.passwordmanger.encryption.Decryption;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.login.setOnClickListener(this::login);
        binding.createAccount.setOnClickListener(this::createAccount);
    }

    public void login(View view) {
        String user = binding.email.getEditText().getText().toString().split("@")[0];
        Toast.makeText(getContext(), user, Toast.LENGTH_SHORT).show();
        try {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference("users/" + user + "/");
            databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    } else {
                        DataSnapshot result = task.getResult();
                        if (result.getValue() != null) {
                            boolean flag = true;
                            String email = binding.email.getEditText().getText().toString();
                            String pass = binding.pass.getEditText().getText().toString();
                            if (isEmpty(email) != true) {
                                if (isValidEmail(email) != true) {
                                    binding.email.setError("not valid email");
                                    flag = false;
                                } else {
                                    binding.email.setError("");
                                }
                            } else {
                                binding.email.setError("fill field!");
                                flag = false;
                            }

                            if (isEmpty(pass) != true) {
                                if (pass.length() < 6) {
                                    binding.pass.setError("password must be at least 6 digits!");
                                    flag = false;
                                }
                            } else {
                                binding.pass.setError("fill field!");
                                flag = false;
                            }
                            if (flag) {
                                String passValue = result.child("pass").getValue().toString();
                                Decryption decryption=new Decryption(passValue,"preorder");
                                passValue=decryption.getPlaintext();
                                if (pass.equals(passValue))
                                    displayHomeFragment(email, pass);
                                else
                                    Toast.makeText(getContext(), "email or pass is wrong", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(getContext(), "email or pass is wrong", Toast.LENGTH_SHORT).show();
                        }

                    }
                }


            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createAccount(View view) {
        NavController navController = Navigation.findNavController(view);
        navController.navigate(R.id.action_FirstFragment_to_SecondFragment);

    }

    public boolean isEmpty(String input) {
        if (TextUtils.isEmpty(input))
            return true;

        return false;
    }

    public boolean isValidEmail(String email) {

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return false;
        }

        return true;
    }

    public void displayHomeFragment(String email, String pass) {
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        bundle.putString("pass", pass);
        NavController navController = Navigation.findNavController(binding.getRoot());
        navController.navigate(R.id.action_FirstFragment_to_homeFragment,bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}