package kr.co.himedia.ecommerce.mainproject.login.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import kr.co.himedia.ecommerce.mainproject.R;

public class ResultIdActivity extends AppCompatActivity {

    TextView txtResultId;
    Button btnIdPwd;
    Button btnIdLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_id);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        txtResultId = findViewById(R.id.txtResultId);
        txtResultId.setText(id);

        btnIdPwd = findViewById(R.id.btnIdPwd);
        btnIdPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchPwdActivity.class);
                startActivity(intent);
            }
        });

        btnIdLogin = findViewById(R.id.btnIdLogin);
        btnIdLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}