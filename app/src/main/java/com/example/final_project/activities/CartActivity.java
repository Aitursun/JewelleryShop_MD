package com.example.final_project.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.final_project.adapters.CartAdapter;
import com.example.final_project.databinding.ActivityCartBinding;
import com.example.final_project.model.Product;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.model.Item;
import com.hishd.tinycart.util.TinyCartHelper;

import java.util.ArrayList;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    ActivityCartBinding binding;
    CartAdapter adapter;
    ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        products = new ArrayList<>();

        // Получаем экземпляр корзины с помощью TinyCartHelper
        Cart cart = TinyCartHelper.getCart();

        // Перебираем все элементы в корзине и заполняем список продуктов
        for (Map.Entry<Item, Integer> item : cart.getAllItemsWithQty().entrySet()) {
            Product product = (Product) item.getKey();
            int quantity = item.getValue();
            product.setQuantity(quantity);

            products.add(product);
        }

        // Инициализируем адаптер с продуктами и устанавливаем слушатель для изменений количества
        adapter = new CartAdapter(this, products, new CartAdapter.CartListener() {
            @Override
            public void onQuantityChanged() {
                // Обновляем подитог при изменении количества товаров
                binding.subtotal.setText(String.format("$ %.0f CAD", (cart.getTotalPrice())));
            }
        });

        // Настраиваем RecyclerView с LinearLayoutManager и DividerItemDecoration
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        binding.cartList.setLayoutManager(layoutManager);
        binding.cartList.addItemDecoration(itemDecoration);
        binding.cartList.setAdapter(adapter);

        // Отображаем начальное значение подитога
        binding.subtotal.setText(String.format("$ %.0f CAD", cart.getTotalPrice()));

        // Обработчик клика кнопки "Продолжить" для перехода к CheckoutActivity
        binding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartActivity.this, CheckoutActivity.class));
                finish();// Завершаем текущую активность после запуска CheckoutActivity
            }
        });

        // Включаем кнопку "назад" в ActionBar для навигации
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}