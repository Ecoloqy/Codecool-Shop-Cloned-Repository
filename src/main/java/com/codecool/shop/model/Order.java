package com.codecool.shop.model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Order extends BaseModel {

    @SerializedName(value = "paid")
    private boolean paid;

    @SerializedName(value = "cart")
    private Cart cart;

    private int cart_id;
    private String date;
    private double totalPrice;

    public Order(Cart cart) {
        this.cart = cart;
        this.cart_id = cart.getId();
        totalPrice = countTotalPrice();
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean status) {
        this.paid = status;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public int getCart_id() {
        if (cart_id == 0) {
            return cart.getId();
        }
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    private double countTotalPrice() {
        double totalPrice = 0;
        for (ProductInCart productInCart : cart.getProducts()) {
            totalPrice += productInCart.getQuantity() * productInCart.getProduct().getDefaultPrice();
        }
        return totalPrice;
    }

    @Override
    public String toString() {
        return String.format("id: %1$d, " +
                        "paid status: %2$b, " +
                        "date: %3$s, " +
                        "cart id: %4$d",
                this.id,
                this.paid,
                this.date,
                this.cart_id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return paid == order.paid &&
                cart_id == order.cart_id &&
                Double.compare(order.totalPrice, totalPrice) == 0 &&
                Objects.equals(cart, order.cart);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paid, cart, cart_id, totalPrice);
    }
}
