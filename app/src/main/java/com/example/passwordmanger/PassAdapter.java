package com.example.passwordmanger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.passwordmanger.encryption.Decryption;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;

public class PassAdapter extends RecyclerView.Adapter<PassAdapter.ViewHolder> {
    private LinkedList<Password> listdata;
    private Context context;
    private  String user;

    // RecyclerView recyclerView;
    public PassAdapter(String user,LinkedList<Password> listdata, Context context) {
        this.listdata = listdata;
        this.context = context;
        this.user=user;
    }

    @Override
    public PassAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.password_item, parent, false);
        PassAdapter.ViewHolder viewHolder = new PassAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PassAdapter.ViewHolder holder, int position) {
        final Password myListData = listdata.get(position);
        holder.descTV.setText(myListData.getDesc());
        holder.passTI.getEditText().setText(myListData.getPass());
        holder.showBTN.setOnClickListener(view -> {
            Decryption decryption=new Decryption(myListData.getPass(),"preorder");
            holder.passTI.getEditText().setText(decryption.getPlaintext());

        });
        holder.removeBTN.setOnClickListener(view -> {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference("users/"+user+"/passwords/"+myListData.getId()+"/" );
            databaseReference.removeValue();

        });
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView descTV;
        private TextInputLayout passTI;
        private Button showBTN;
        private Button removeBTN;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.descTV = (TextView) itemView.findViewById(R.id.desc);

           this.passTI=(TextInputLayout) itemView.findViewById(R.id.password);
            this.showBTN=(Button) itemView.findViewById(R.id.show);
            this.removeBTN=(Button) itemView.findViewById(R.id.remove);



        }
    }
}
