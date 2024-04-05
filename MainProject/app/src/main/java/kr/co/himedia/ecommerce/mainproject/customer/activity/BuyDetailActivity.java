package kr.co.himedia.ecommerce.mainproject.customer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import kr.co.himedia.ecommerce.mainproject.R;
import kr.co.himedia.ecommerce.mainproject.buy.dto.BuyDtlDto;
import kr.co.himedia.ecommerce.mainproject.buy.dto.BuyMstDto;
import kr.co.himedia.ecommerce.mainproject.customer.adapter.BuyDetailAdapter;
import kr.co.himedia.ecommerce.mainproject.customer.adapter.BuyListAdapter;
import kr.co.himedia.ecommerce.mainproject.customer.interfaces.OnBuyDetailItemClickListener;
import kr.co.himedia.ecommerce.mainproject.customer.interfaces.OnBuyListItemClickListener;

public class BuyDetailActivity extends AppCompatActivity {

    RecyclerView recyclerBuyDetailView;
    BuyDetailAdapter buyDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_detail);

        recyclerBuyDetailView = findViewById(R.id.recyclerBuyDetailView);
        recyclerBuyDetailView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerBuyDetailView.setLayoutManager(layoutManager);

        buyDetailAdapter = new BuyDetailAdapter();
        recyclerBuyDetailView.setAdapter(buyDetailAdapter);

        Intent intent = getIntent();
        ArrayList<BuyDtlDto> listBuyDtlDto = (ArrayList<BuyDtlDto>) intent.getSerializableExtra("listBuyDtlDto");

        buyDetailAdapter.setItems(listBuyDtlDto);

        buyDetailAdapter.notifyDataSetChanged();


        buyDetailAdapter.setOnClickListener(new OnBuyDetailItemClickListener() {
            @Override
            public void onImageClick(BuyDetailAdapter.ViewHolder holder, View view, int position) {
                Toast.makeText(getApplicationContext(), "이미지 클릭", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onBtnReviewlick(BuyDetailAdapter.ViewHolder holder, View view, int position) {

                Intent intent = new Intent(BuyDetailActivity.this, ReviewActivity.class);

                BuyDtlDto item = buyDetailAdapter.getItem(position);

                intent.putExtra("seq_buy_dtl", Integer.toString(item.getSeq_buy_dtl()));
                intent.putExtra("seq_sle", Integer.toString(item.getSeq_sle()));
                intent.putExtra("sle_nm", item.getSle_nm());
                intent.putExtra("img", item.getImg());

                startActivity(intent);

                //Toast.makeText(getApplicationContext(), "버튼 클릭", Toast.LENGTH_LONG).show();
            }
        });
    }
}