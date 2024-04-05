package kr.co.himedia.ecommerce.mainproject.customer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import kr.co.himedia.ecommerce.mainproject.R;
import kr.co.himedia.ecommerce.mainproject.buy.dto.BuyDtlDto;
import kr.co.himedia.ecommerce.mainproject.customer.adapter.BuyDetailAdapter;
import kr.co.himedia.ecommerce.mainproject.customer.adapter.ReviewListAdapter;
import kr.co.himedia.ecommerce.mainproject.customer.interfaces.OnReviewItemClickListener;
import kr.co.himedia.ecommerce.mainproject.sale.activity.ViewSaleActivity;
import kr.co.himedia.ecommerce.mainproject.sale.dto.SaleDto;

public class ReviewListActivity extends AppCompatActivity {

    RecyclerView recyclerReviewView;
    ReviewListAdapter reviewListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);

        recyclerReviewView = findViewById(R.id.recyclerReviewView);
        recyclerReviewView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerReviewView.setLayoutManager(layoutManager);

        reviewListAdapter = new ReviewListAdapter();
        recyclerReviewView.setAdapter(reviewListAdapter);

        Intent intent = getIntent();
        ArrayList<SaleDto> listSaleDto = (ArrayList<SaleDto>) intent.getSerializableExtra("listSaleDto");

        reviewListAdapter.setItems(listSaleDto);

        reviewListAdapter.notifyDataSetChanged();



        reviewListAdapter.setOnClickListener(new OnReviewItemClickListener() {
            @Override
            public void onItemClick(ReviewListAdapter.ViewHolder holder, View view, int position) {
                SaleDto saleDto = reviewListAdapter.getItem(position);

                Intent intent = new Intent(getApplicationContext(), ViewSaleActivity.class);
                intent.putExtra("saleDto", saleDto);
                startActivity(intent);
            }
        });
    }
}
