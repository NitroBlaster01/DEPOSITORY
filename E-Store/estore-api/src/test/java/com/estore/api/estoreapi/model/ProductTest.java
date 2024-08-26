package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;


import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


/**
 * Unit test for User class
 * 
 * @author Khandaker Fahmid
 */
@Tag("Model-tier")
public class ProductTest {
    @Test
    public void testCtor() {
        // Setup
        int productid = 56;
        String productname = "Piece";
        double productprice = 15.66;
        int productquantity = 10;
        String productImageUrl = "testurl";

        // Invoke
        Product product = new Product(productid, productname, productprice, productquantity, productImageUrl);

        // Analyze
        assertEquals(productid,product.getId());
        assertEquals(productname,product.getName());
        assertEquals(productprice,product.getPrice());
        assertEquals(productquantity,product.getQuantity());
    }

    @Test
    public void testName() {
        // Setup
        int productid = 56;
        String productname = "Piece";
        double productprice = 15.66;
        int productquantity = 10;
        String productImageUrl = "testurl";


        Product product = new Product(productid, "Default", productprice, productquantity, productImageUrl);

        String expected_name = "Piece";

        // Invoke
        product.setName(productname);

        // Analyze
        assertEquals(expected_name,product.getName());
    }

    @Test
    public void testPrice() {
        // Setup
        int productid = 56;
        String productname = "Piece";
        double productprice = 15.66;
        int productquantity = 10;
        String productImageUrl = "testurl";

        Product product = new Product(productid, productname, 11, productquantity, productImageUrl);

        double expected_price = 15.66;

        // Invoke
        product.setPrice(productprice);

        // Analyze
        assertEquals(expected_price,product.getPrice());
    }

    @Test
    public void testQuantity() {
        // Setup
        int productid = 56;
        String productname = "Piece";
        double productprice = 15.66;
        int productquantity = 10;
        String productImageUrl = "testurl";

        Product product = new Product(productid, productname, productprice, 0, productImageUrl);

        int expected_quantity = 10;

        // Invoke
        product.setQuantity(productquantity);

        // Analyze
        assertEquals(expected_quantity,product.getQuantity());
    }


    @Test
    public void testToString() {
        // Setup
        int productid = 56;
        String productname = "Piece";
        double productprice = 15.66;
        int productquantity = 10;
        String productImageUrl = "testurl";


        String expected_string = String.format(Product.STRING_FORMAT,productid,productname,productprice,productquantity);
        Product product = new Product(productid,productname,productprice,productquantity,productImageUrl);

        // Invoke
        String actual_string = product.toString();

        // Analyze
        assertEquals(expected_string,actual_string);
    }
}
