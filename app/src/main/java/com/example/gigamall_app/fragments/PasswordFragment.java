package com.example.gigamall_app.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;

import com.example.gigamall_app.R;

public class PasswordFragment extends DialogFragment {
    private EditText passwordEditTxt, passwordEditTxt2;
    private AppCompatButton doneBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.FullScreenDialogStyle);
    }

    @Override
    public void onStart()
    {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null)
        {
            dialog.getWindow().getAttributes().windowAnimations = R.style.FullScreenDialogStyle;
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.password_fragment, container, false);

        passwordEditTxt = view.findViewById(R.id.passwordEditTxt);
        passwordEditTxt2 = view.findViewById(R.id.passwordEditTxt2);
        doneBtn = view.findViewById(R.id.doneBtn);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        doneBtn.setOnClickListener(v ->{
            if(!passwordEditTxt.getText().toString().equals(passwordEditTxt2.getText().toString())){
                Toast.makeText(getContext(), "Your retyped password does not match!", Toast.LENGTH_SHORT).show();
            }
            else{
                Bundle bundle = new Bundle();
                bundle.putString("pass", passwordEditTxt.getText().toString());
                getParentFragmentManager().setFragmentResult("last step", bundle);

                dismiss();
            }
        });
    }
}