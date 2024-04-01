package kr.co.himedia.ecommerce.mainproject.main.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kr.co.himedia.ecommerce.mainproject.R;
import kr.co.himedia.ecommerce.mainproject.buy.Activity.BuyActivity;
import kr.co.himedia.ecommerce.mainproject.buy.dto.BuyDtlDto;
import kr.co.himedia.ecommerce.mainproject.common.AppDatabase;
import kr.co.himedia.ecommerce.mainproject.common.SharedPreferencesManager;
import kr.co.himedia.ecommerce.mainproject.login.activity.LoginActivity;
import kr.co.himedia.ecommerce.mainproject.main.activity.MainActivity;
import kr.co.himedia.ecommerce.mainproject.main.adapter.CartAdapter;
import kr.co.himedia.ecommerce.mainproject.main.interfaces.OnCartItemClickListener;

public class CartFragment extends Fragment {

    RecyclerView recyclerCartView;
    TextView txtTotalCount;
    CartAdapter cartAdapter;
    AppDatabase db;
    Button btnCartBuy;
    ArrayList<BuyDtlDto> arrBuyDtlDto = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        txtTotalCount = view.findViewById(R.id.txtTotalCount);

        recyclerCartView = view.findViewById(R.id.recyclerCartView);
        recyclerCartView.setHasFixedSize(true);;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerCartView.setLayoutManager(layoutManager);

        cartAdapter = new CartAdapter();
        recyclerCartView.setAdapter(cartAdapter);

        cartAdapter.setOnClickListener(new OnCartItemClickListener() {
            @Override
            public void onCancelButtonClick(CartAdapter.ViewHolder holder, View view, int position) {

                BuyDtlDto buyDtlDto = cartAdapter.getItem(position);
                println("장바구니 취소");
                class InsertRunnable implements Runnable {
                    @Override
                    public void run() {
                        db.buyDtlDao().deleteBySeqSle(buyDtlDto.getSeq_sle());
                    }
                }
                InsertRunnable insertRunnable = new InsertRunnable();
                new Thread(insertRunnable).start();

                new SelectAsyncTask().execute();

            }

            @Override
            public void onMinusButtonClick(CartAdapter.ViewHolder holder, View view, int position) {

                BuyDtlDto buyDtlDto = cartAdapter.getItem(position);
                if(buyDtlDto.getCount() > 1) {
                    class InsertRunnable implements Runnable {
                        @Override
                        public void run() {
                            db.buyDtlDao().updateCount(buyDtlDto.getCount() - 1, buyDtlDto.getSeq_sle());
                        }
                    }
                    InsertRunnable insertRunnable = new InsertRunnable();
                    new Thread(insertRunnable).start();

                    new SelectAsyncTask().execute();
                }
            }

            @Override
            public void onPlusButtonClick(CartAdapter.ViewHolder holder, View view, int position) {

                BuyDtlDto buyDtlDto = cartAdapter.getItem(position);

                class InsertRunnable implements Runnable {
                    @Override
                    public void run() {
                        db.buyDtlDao().updateCount(buyDtlDto.getCount() + 1, buyDtlDto.getSeq_sle());

                    }
                }
                InsertRunnable insertRunnable = new InsertRunnable();
                new Thread(insertRunnable).start();

                new SelectAsyncTask().execute();
            }
        });

        db = AppDatabase.getInstance(getActivity().getApplicationContext());

        new SelectAsyncTask().execute();

        btnCartBuy = view.findViewById(R.id.btnCartBuy);
        btnCartBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SharedPreferencesManager.getSeq_cst(view.getContext()) == 0){
                    Toast.makeText(view.getContext(), "로그인이 필요한 서비스입니다!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(view.getContext(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                } else{
                    arrBuyDtlDto.clear();
                    for(int i=0; i<cartAdapter.getItemCount(); i++){
                        BuyDtlDto buyDtlDto = new BuyDtlDto();
                        buyDtlDto = cartAdapter.getItem(i);
                        arrBuyDtlDto.add(buyDtlDto);
                    }
                    Intent intent = new Intent(view.getContext(), BuyActivity.class);
                    intent.putExtra("listBuyDtlDto", arrBuyDtlDto);
                    startActivity(intent);
                }
            }
        });

        return view;
    }

    private class SelectAsyncTask extends AsyncTask<Void, Void, List<BuyDtlDto>> {
        @Override
        protected List<BuyDtlDto> doInBackground(Void... voids) {
            // 백그라운드에서 데이터베이스 쿼리 수행
            return db.buyDtlDao().getAll();
        }

        @Override
        protected void onPostExecute(List<BuyDtlDto> listBuyDtlDto) {
            // 결과를 받아와서 UI 스레드에서 UI 업데이트
            totalCount(listBuyDtlDto);
        }
    }

    public void totalCount(List<BuyDtlDto> listBuyDtlDto){
        int totalCount = 0;

        if(listBuyDtlDto.size() !=0) {
            for (int i = 0; i < listBuyDtlDto.size(); i++) {
                totalCount += listBuyDtlDto.get(i).getPrice() * listBuyDtlDto.get(i).getCount();
            }

            txtTotalCount.setText("합계 : " + totalCount + " 원");
            btnCartBuy.setVisibility(View.VISIBLE);

        } else {
            txtTotalCount.setText("장바구니에 든 상품이 없습니다!");
            btnCartBuy.setVisibility(View.GONE);
        }

        cartAdapter.setItems(listBuyDtlDto);

        cartAdapter.notifyDataSetChanged();
    }

    public void println(String data) {
        Log.d("CartFragment", data);
    }
}
