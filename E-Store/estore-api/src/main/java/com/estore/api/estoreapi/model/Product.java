package com.estore.api.estoreapi.model;

import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Represents a product entity
 * 
 * @author Quan Quy
 */
public class Product {
    private static final Logger LOG = Logger.getLogger(Product.class.getName());

    // Package private for tests
    static final String STRING_FORMAT = "product [id=%d, name=%s, price=%f, quantity=%d]";

    @JsonProperty("id") private int id;
    @JsonProperty("name") private String name;
    @JsonProperty("price") private double price;
    @JsonProperty("quantity") private int quantity;
    @JsonProperty("imageUrl") private String imageUrl;

    /**
     * Create a product with the given id and name
     * @param id The id of the product
     * @param name The name of the product
     * 
     * {@literal @}JsonProperty is used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int
     */
    public Product(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("price") double price, @JsonProperty("quantity") int quantity, @JsonProperty("imageUrl") String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    public void setImageUrl(String imageUrl){this.imageUrl = imageUrl;}

    public String getImageUrl() {return imageUrl;}

    /**
     * Retrieves the id of the product
     * @return The id of the product
     */
    public int getId() {return id;}

    /**
     * Retrieves the price of the product
     * @return The price of the product
     */
    public double getPrice() {return price;}

    /**
     * Sets the price of the product
     */
    public void setPrice(double price) {this.price = price;}

    /**
     * Retrieves the quantity of the product
     * @return The quantity of the product
     */
    public int getQuantity() {return quantity;}

    /**
     * Sets the quantity of the product
     */
    public void setQuantity(int quantity) {this.quantity = quantity;}

    /**
     * Sets the name of the product - necessary for JSON object to Java object deserialization
     * @param name The name of the product
     */
    public void setName(String name) {this.name = name;}

    /**
     * Retrieves the name of the product
     * @return The name of the product
     */
    public String getName() {return name;}

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT,id,name,price,quantity);
    }
}