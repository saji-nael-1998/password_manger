package com.example.passwordmanger;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;


import com.example.passwordmanger.databinding.FragmentCreateAccountBinding;
import com.example.passwordmanger.encryption.Encryption;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class SecondFragment extends Fragment {

    private FragmentCreateAccountBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentCreateAccountBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.createAccount.setOnClickListener(this::createAccount);

    }

    public void createAccount(View view) {
        boolean flag = true;
        String email = binding.email.getEditText().getText().toString();
        String pass = binding.pass.getEditText().getText().toString();
        String cPass = binding.confirmPass.getEditText().getText().toString();
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
        if (isEmpty(cPass)) {
            binding.confirmPass.setError("fill field");
            flag = false;
        } else {
            binding.confirmPass.setError("");
        }
        if (isEmpty(pass) != true) {
            if (pass.length() < 6) {
                binding.pass.setError("password must be at least 6 digits!");
                flag = false;

            } else {
                if (pass.equals(cPass) != true) {
                    binding.confirmPass.setError("password is not matching!");
                    flag = false;
                }
                binding.pass.setError("");
            }

        } else {
            binding.pass.setError("fill field!");
            flag = false;
        }
        if (flag) {
            createUser(email, pass);
        }

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

    public void createUser(String email, String pass) {
        Encryption encryption = new Encryption(pass, "preorder");
        pass = encryption.getCipherText();
        Map<String, Object> user = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        data.put("email", email);
        data.put("pass", pass);
        user.put(email.split("@")[0],data);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("users/" );
        databaseReference.updateChildren(user);
        Toast.makeText(getActivity(), "welcome!!", Toast.LENGTH_SHORT).show();
        NavController navController = Navigation.findNavController(binding.getRoot());
        navController.navigate(R.id.action_SecondFragment_to_FirstFragment);

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}