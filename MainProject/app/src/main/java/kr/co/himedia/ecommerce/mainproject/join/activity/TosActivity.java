package kr.co.himedia.ecommerce.mainproject.join.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


import kr.co.himedia.ecommerce.mainproject.R;


public class TosActivity extends AppCompatActivity {

    static RequestQueue requestQueue;

    CheckBox checkToS;
    CheckBox checkFinancial;
    CheckBox checkPersonal;
    CheckBox checkEmail;
    CheckBox checkSms;
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tos);


        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        checkToS = findViewById(R.id.checkToS);
        checkFinancial = findViewById(R.id.checkFinancial);
        checkPersonal = findViewById(R.id.checkPersonal);
        checkSms = findViewById(R.id.checkSms);
        checkEmail = findViewById(R.id.checkEmail);

        btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkToS.isChecked() && checkFinancial.isChecked() && checkPersonal.isChecked()) {
                    Intent intent = new Intent(TosActivity.this, JoinActivity.class);
                    intent.putExtra("flg_sms", checkSms.isChecked());
                    intent.putExtra("flg_email", checkEmail.isChecked());

                    startActivity(intent);
                } else {
                    Toast.makeText(TosActivity.this, "필수 약관 동의에 모두 체크해 주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}