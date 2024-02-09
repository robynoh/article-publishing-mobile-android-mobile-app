package com.test.dessertationone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class payment_success extends AppCompatActivity {

    Button okay;
    TextView articletitle,articleauthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);

        String firstname = getIntent().getStringExtra("firstname");
        String lastname = getIntent().getStringExtra("lastname");
        String title = getIntent().getStringExtra("title");
        String articletopic = getIntent().getStringExtra("articletopic");



        okay=findViewById(R.id.okay);
        articletitle=findViewById(R.id.articletitle);
        articleauthor=findViewById(R.id.articleauthor);

        articletitle.setText(articletopic);
        articleauthor.setText(title+""+firstname+""+lastname);



        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(payment_success.this,MainActivity.class);
               startActivity(intent);
               finish();
            }
        });
    }
}