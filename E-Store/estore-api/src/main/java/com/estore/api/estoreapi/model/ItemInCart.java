package com.estore.api.estoreapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ItemInCart {
    // Package private for tests
    static final String STRING_FORMAT = "cart [id=%d, quantity=%d]";

    @JsonProperty("productId") private int productId;
    @JsonProperty("quantity") private int quantity;


    public ItemInCart(@JsonProperty("productId") int id, @JsonProperty("quantity") int quantity) {
        this.productId = id;
        this.quantity = quantity;
    }

    public int getId() {return productId;}

    public void setId(int productId) {this.productId = productId;}

    public int getQuantity() {return quantity;}

    public void setQuantity(int quantity) {this.quantity = quantity;}

    @Override
    public String toString() {
        return String.format(STRING_FORMAT,productId,quantity);
    }

}
