package com.example.final_project.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.final_project.R;
import com.example.final_project.databinding.ItemCartBinding;
import com.example.final_project.databinding.QuantityDialogBinding;
import com.example.final_project.model.Product;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    Context context;
    ArrayList<Product> products;
    CartListener cartListener;
    Cart cart;

    // Интерфейс для обработки изменений количества товаров в корзине
    public interface CartListener {
        public void onQuantityChanged();
    }

    // Конструктор адаптера
    public CartAdapter(Context context, ArrayList<Product> products, CartListener cartListener) {
        this.context = context;
        this.products = products;
        this.cartListener = cartListener;
        cart = TinyCartHelper.getCart(); // Получаем экземпляр корзины
    }

    // Создание нового ViewHolder
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Инфлейт макета для каждого элемента корзины
        return new CartViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false));
    }

    // Привязка данных к ViewHolder
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = products.get(position);

        // Используем Glide для загрузки изображения товара
        Glide.with(context)
                .load(product.getImage())
                .into(holder.binding.image);

        // Устанавливаем имя товара, цену и количество
        holder.binding.name.setText(product.getName());
        holder.binding.price.setText("$ " + String.format("%.0f",product.getPrice())+ " CAD");
        holder.binding.quantity.setText(product.getQuantity() + " item(s)");

        // Обработчик клика по элементу корзины для открытия диалогового окна с количеством товаров
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                // Инициализация и настройка диалогового окна
                QuantityDialogBinding quantityDialogBinding = QuantityDialogBinding.inflate(LayoutInflater.from(context));
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setView(quantityDialogBinding.getRoot())
                        .create();

                // Установка прозрачного фона для диалогового окна
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));

                // Установка данных о товаре в диалоговом окне
                quantityDialogBinding.productName.setText(product.getName());
                quantityDialogBinding.productStock.setText("Stock: " + product.getStock());
                quantityDialogBinding.quantity.setText(String.valueOf(product.getQuantity()));
                int stock = product.getStock();

                // Обработчик кнопки "+" для увеличения количества товаров
                quantityDialogBinding.plusBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int quantity = product.getQuantity();
                        quantity++;

                        // Проверка наличия товара на складе
                        if (quantity > product.getStock()) {
                            Toast.makeText(context, "Max stock available: " + product.getStock(), Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            product.setQuantity(quantity);
                            quantityDialogBinding.quantity.setText(String.valueOf(quantity));
                        }

                        notifyDataSetChanged(); // Обновление списка
                        cart.updateItem(product, product.getQuantity()); // Обновление корзины
                        cartListener.onQuantityChanged(); // Уведомление слушателя об изменении количества
                    }
                });

                // Обработчик кнопки "-" для уменьшения количества товаров
                quantityDialogBinding.minusBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int quantity = product.getQuantity();
                        if (quantity > 1)
                            quantity--;
                        product.setQuantity(quantity);
                        quantityDialogBinding.quantity.setText(String.valueOf(quantity));

                        notifyDataSetChanged(); // Обновление списка
                        cart.updateItem(product, product.getQuantity()); // Обновление корзины
                        cartListener.onQuantityChanged(); // Уведомление слушателя об изменении количества
                    }
                });

                // Обработчик кнопки "Сохранить" в диалоговом окне
                quantityDialogBinding.saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss(); // Закрытие диалогового окна
                    }
                });

                dialog.show(); // Отображение диалогового окна
            }
        });
    }

    // Возвращает количество элементов в списке
    @Override
    public int getItemCount() {
        return products.size();
    }

    // ViewHolder для элемента корзины
    public class CartViewHolder extends RecyclerView.ViewHolder {

        ItemCartBinding binding;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCartBinding.bind(itemView); // Привязка ViewBinding для элемента
        }
    }
}
