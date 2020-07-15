package com.openshop.gazapp.view.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openshop.gazapp.R;
import com.openshop.gazapp.adapter.ProductAdapter;
import com.openshop.gazapp.data.helper.OnEndLess;
import com.openshop.gazapp.data.model.category.CategoryData;
import com.openshop.gazapp.data.model.products.ProductData;
import com.openshop.gazapp.data.model.products.Products;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.openshop.gazapp.data.api.RetrofitClient.getClient;
import static com.openshop.gazapp.data.helper.HelperMethod.dismissProgressDialog;
import static com.openshop.gazapp.data.helper.HelperMethod.showProgressDialog;
import static com.openshop.gazapp.data.local.GrawanConstants.API_TOKEN;
import static com.openshop.gazapp.data.local.SharedPreferencesManger.LoadData;

public class ProductsActivity extends AppCompatActivity {

    @BindView(R.id.activity_products_btn_back)
    ImageView activityProductsBtnBack;
    @BindView(R.id.activity_products_tv_category_name)
    TextView activityProductsTvCategoryName;
    @BindView(R.id.activity_products_rel_title)
    RelativeLayout activityProductsRelTitle;
    @BindView(R.id.activity_products_rv_products)
    RecyclerView activityProductsRvProducts;
    String department_name;
    ArrayList<ProductData> products_list;
    GridLayoutManager gridLayoutManager;
    ProductAdapter adapter;
    @BindView(R.id.activity_products_iv_cart)
    ImageView activityProductsIvCart;
    @BindView(R.id.activity_products_iv_search)
    ImageView activityProductsIvSearch;

    private Integer Maxpage;
    private String token;
    private OnEndLess onEndLess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        ButterKnife.bind(this);
        //TODO get department name
        CategoryData category = (CategoryData) getIntent().getSerializableExtra("category");
        department_name = category.getName();
        activityProductsTvCategoryName.setText(department_name);

        //TODO add products
        gridLayoutManager = new GridLayoutManager(this, 2);
        activityProductsRvProducts.setLayoutManager(gridLayoutManager);
        token = LoadData(ProductsActivity.this, API_TOKEN);
        addProducts(token, category.getId(), 1);
        adapter = new ProductAdapter(products_list, this, ProductsActivity.this);
        activityProductsRvProducts.setAdapter(adapter);
        onEndLess = new OnEndLess(gridLayoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {

                if (current_page <= Maxpage) {
                    if (Maxpage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;
                        addProducts(token, category.getId(), current_page);
                        adapter.notifyDataSetChanged();
                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;

                }


            }
        };
    }

    private void addProducts(String token, int category_id, int page) {

        products_list = new ArrayList<>();

        showProgressDialog(this, this.getString(R.string.wait));
        getClient().getProducts(token, category_id, page).enqueue(new Callback<Products>() {
            @Override
            public void onResponse(Call<Products> call, Response<Products> response) {
                dismissProgressDialog();
                Toast.makeText(ProductsActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();

                try {
                    if (response.body().getStatus() == 1) {
                        products_list.addAll(response.body().getData().getData());
                        adapter.notifyDataSetChanged();
                        Maxpage = response.body().getData().getLastPage();


                    }

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<Products> call, Throwable t) {

            }
        });


    }

    @OnClick(R.id.activity_products_btn_back)
    public void onViewClicked() {
        super.onBackPressed();
    }

    @OnClick({R.id.activity_products_iv_cart, R.id.activity_products_iv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_products_iv_cart:
                startActivity(new Intent(ProductsActivity.this, CartActivity.class));
                break;
            case R.id.activity_products_iv_search:
                startActivity(new Intent(ProductsActivity.this, SaarchActivity.class));

                break;
        }
    }
}
