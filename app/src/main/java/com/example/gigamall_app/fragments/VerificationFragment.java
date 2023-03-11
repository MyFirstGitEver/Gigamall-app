package com.example.gigamall_app.fragments;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.gigamall_app.R;
import com.example.gigamall_app.tools.Tools;
import com.example.gigamall_app.services.UserService;

import java.io.IOException;
import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificationFragment extends DialogFragment
{
    private int numberIndex = 0;

    private TextView introTxt, timerTxt;
    private EditText firstEditTxt, secondEditTxt, thirdEditTxt, fourthEditTxt;
    private Button confirmBtn;

    private final InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned dest, int i2, int i3) {
            if(dest.length() == 0){
                controlEditText(numberIndex, false);

                numberIndex = Math.min(numberIndex + 1, 3);
                controlEditText(numberIndex, true);
            }
            else if(dest.length() == 1 && charSequence.length() == 0){ // is erasing!
                controlEditText(numberIndex, false);
                numberIndex = Math.max(0, numberIndex - 1);
                controlEditText(numberIndex, true);
            }

            if(numberIndex != 3){
                confirmBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
            }
            else{
                confirmBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
            }

            return dest.length() == 0 ? charSequence.subSequence(0, 1) : "";
        }
    };

    private final View.OnClickListener confirmClickListener = (v) ->{
        if(numberIndex != 3){
            return;
        }

        UserService.service.verify(getArguments().getInt("id"), getCode()).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.code() == HttpURLConnection.HTTP_NOT_FOUND){
                    int code = -3;
                    try {
                        code = Integer.parseInt(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(code == -3){
                        Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                    else if(code == -2){
                        Toast.makeText(getContext(), "Mã hết hiệu lực!", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                    else{
                        Toast.makeText(getContext(), "Mã sai. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    }
                    
                    return;
                }

                Bundle bundle = new Bundle();

                bundle.putInt("userId", response.body());
                getParentFragmentManager().setFragmentResult("verified", bundle);

                dismiss();
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    };

    private void controlEditText(int id, boolean focusable){
        EditText editText;

        switch (id){
            case 0:
                editText = firstEditTxt;
                break;
            case 1:
                editText = secondEditTxt;
                break;
            case 2:
                editText = thirdEditTxt;
                break;
            default:
                editText = fourthEditTxt;
                break;
        }

        if(focusable){
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
        }
        else{
            editText.setFocusable(false);
        }
    }

    private String getCode(){
        return firstEditTxt.getText().toString()
                + secondEditTxt.getText().toString()
                + thirdEditTxt.getText().toString()
                + fourthEditTxt.getText().toString();
    }

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
        View view = inflater.inflate(R.layout.verification_fragment, container, false);

        introTxt = view.findViewById(R.id.introTxt);
        firstEditTxt = view.findViewById(R.id.firstEditTxt);
        secondEditTxt = view.findViewById(R.id.secondEditTxt);
        thirdEditTxt = view.findViewById(R.id.thirdEditTxt);
        fourthEditTxt = view.findViewById(R.id.fourthEditTxt);

        timerTxt = view.findViewById(R.id.timerTxt);

        confirmBtn = view.findViewById(R.id.confirmBtn);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        if(savedInstanceState != null){
            numberIndex = savedInstanceState.getInt("numberIndex");
        }
        setIntroText();
        customiseEditText();
        if(getArguments().getBoolean("setCountDown", true)){
            setCountDown();
        }
        else{
            timerTxt.setText("Mã có hiệu lực trong vòng 5 phút tính từ lúc hệ thống gửi đi mã");
        }

        confirmBtn.setOnClickListener(confirmClickListener);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("numberIndex", numberIndex);
        super.onSaveInstanceState(outState);
    }

    private void customiseEditText(){
        firstEditTxt.setCursorVisible(false);
        secondEditTxt.setCursorVisible(false);
        thirdEditTxt.setCursorVisible(false);
        fourthEditTxt.setCursorVisible(false);

        firstEditTxt.setFilters(new InputFilter[] {filter});
        secondEditTxt.setFilters(new InputFilter[] {filter});
        thirdEditTxt.setFilters(new InputFilter[] {filter});
        fourthEditTxt.setFilters(new InputFilter[] {filter});

        firstEditTxt.setFocusable(true);
        secondEditTxt.setFocusable(false);
        thirdEditTxt.setFocusable(false);
        fourthEditTxt.setFocusable(false);
    }

    private void setIntroText(){
        String info = getArguments().getString("user");

        if(info != null){
            introTxt.setText("Xin chào, " + info);
        }
    }

    private void setCountDown(){
        new CountDownTimer(300000, 1000) {
            public void onTick(long millisUntilFinished) {
                timerTxt.setText("Mã chỉ có hiệu lực trong " + Tools.toStamp((millisUntilFinished) / 1000));
            }

            public void onFinish() {
            }
        }.start();
    }
}