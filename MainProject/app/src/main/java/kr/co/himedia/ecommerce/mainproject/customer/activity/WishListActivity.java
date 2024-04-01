package kr.co.himedia.ecommerce.mainproject.customer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import kr.co.himedia.ecommerce.mainproject.R;
import kr.co.himedia.ecommerce.mainproject.customer.adapter.WishAdapter;
import kr.co.himedia.ecommerce.mainproject.customer.interfaces.OnWishItemClickListener;
import kr.co.himedia.ecommerce.mainproject.sale.activity.ViewSaleActivity;
import kr.co.himedia.ecommerce.mainproject.sale.adapter.SearchSaleAdapter;
import kr.co.himedia.ecommerce.mainproject.sale.dto.SaleDto;

public class WishListActivity extends AppCompatActivity {

    RecyclerView recyclerWishView;
    WishAdapter wishAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        recyclerWishView = findViewById(R.id.recyclerWishView);
        recyclerWishView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerWishView.setLayoutManager(layoutManager);

        wishAdapter = new WishAdapter();
        recyclerWishView.setAdapter(wishAdapter);

        Intent intent = getIntent();
        ArrayList<SaleDto> listSaleDto = (ArrayList<SaleDto>) intent.getSerializableExtra("listSaleDto");

        wishAdapter.setItems(listSaleDto);

        wishAdapter.notifyDataSetChanged();

        wishAdapter.setOnClickListener(new OnWishItemClickListener() {
            @Override
            public void onItemClick(WishAdapter.ViewHolder holder, View view, int position) {
                SaleDto saleDto = wishAdapter.getItem(position);

                Intent intent = new Intent(getApplicationContext(), ViewSaleActivity.class);
                intent.putExtra("saleDto", saleDto);
                startActivity(intent);
            }
        });

    }
}