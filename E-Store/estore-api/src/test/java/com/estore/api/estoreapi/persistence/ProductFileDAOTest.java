package com.estore.api.estoreapi.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.estore.api.estoreapi.model.Product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit test for ProductFileDAO class
 * 
 * @author Khandaker Fahmid, Quan Quy
 */
@Tag("Persistence-tier")
public class ProductFileDAOTest {
    ProductFileDAO productFileDAO;
    Product[] testProducts;
    ObjectMapper mockObjectMapper;


    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void setupHeroFileDAO() throws IOException {
        // Setup
        mockObjectMapper = mock(ObjectMapper.class);
        testProducts = new Product[3];
        testProducts[0] = new Product(99, "LMAO", 10.99, 11,"testurl");
        testProducts[1] = new Product(100, "Stane", 11.99, 12,"testurl");
        testProducts[2] = new Product(101, "Plane", 12.99, 15,"testurl");
            
        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the product array above

        when(mockObjectMapper
            .readValue(new File("random_name.txt"), Product[].class))
                .thenReturn(testProducts);
        productFileDAO = new ProductFileDAO("random_name.txt", mockObjectMapper);
    }

    @Test
    public void testGetProducts(){
        //Invoke
        Product[] products = productFileDAO.getProducts();

        //Analyze
        assertEquals(products.length, testProducts.length);
        for(int i = 0; i < testProducts.length; ++i){
            assertEquals(products[i], testProducts[i]);
        }
    }

    @Test
    public void testFindProducts(){
        //Invoke
        Product[] products = productFileDAO.findProducts("ane");  

        //Analyze
        assertEquals(products.length, 2);
        assertEquals(products[0], testProducts[1]);
        assertEquals(products[1], testProducts[2]);
    }

    @Test
    public void testGetProduct(){
        //Invoke
        Product product = productFileDAO.getProduct(99);
        
        //Analyze
        assertEquals(product, testProducts[0]);
    }

    @Test
    public void testCreateProduct() {
        // Setup
        Product product = new Product(102,"Another Product", 13.99, 200,"testurl");

        // Invoke
        Product result = assertDoesNotThrow(() -> productFileDAO.createProduct(product),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Product actual = productFileDAO.getProduct(product.getId());
        assertEquals(actual.getId(),product.getId());
        assertEquals(actual.getName(),product.getName());
        assertEquals(actual.getPrice(),product.getPrice());
        assertEquals(actual.getQuantity(),product.getQuantity());
    }
    
    @Test
    public void testUpdateProduct(){
        // Setup
        Product product = new Product(99,"Peaches", 14.99, 150,"testurl");

        //Invoke
        Product result = assertDoesNotThrow(() -> productFileDAO.updateProduct(product),
                                "Unexpected exception thrown");
                                
        //Analyze
        assertNotNull(result);
        Product actual = productFileDAO.getProduct(product.getId());
        assertEquals(actual, product);
    }

    @Test
    public void testDeleteProduct(){
        //Invoke
        boolean result = assertDoesNotThrow(() -> productFileDAO.deleteProduct(99),
                            "Unexpected exception thrown");
        
        //Analyze
        assertEquals(result, true);
        // We check the internal tree map size against the length
        // of the test heroes array - 1 (because of the delete)
        // Because heroes attribute of HeroFileDAO is package private
        // we can access it directly
        assertEquals(productFileDAO.products.size(), testProducts.length-1);
    }

    @Test
    public void testSaveProductException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(Product[].class));
        Product product = new Product(102, "LMAO", 10.99, 11,"testurl");
        assertThrows(IOException.class,
                        () -> productFileDAO.createProduct(product),
                        "IOException not thrown");
    }

    @Test
    public void testGetProductNotFound(){
        //Invoke
        Product product = productFileDAO.getProduct(98);

        //Analyze
        assertEquals(product, null);
    }

    @Test
    public void testUpdateProductNotFound(){
        //Setup
        Product product = new Product(98, "Ball", 5.67, 12,"testurl");

        //Invoke
        Product result = assertDoesNotThrow(() -> productFileDAO.updateProduct(product),
                                                "Unexpected exception thrown");
        
        //Analyze
        assertNull(result);
    }

    @Test
    public void testConstructorException() throws IOException{
        //Setup
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        // We want to simulate with a Mock Object Mapper that an
        // exception was raised during JSON object deseerialization
        // into Java objects
        // When the Mock Object Mapper readValue method is called
        // from the ProductFileDAO load method, an IOException is
        // raised
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("random_name.txt"),Product[].class);

        //Invoke & Analyze
        assertThrows(IOException.class,
                        () -> new ProductFileDAO("random_name.txt",mockObjectMapper),
                        "IOException not thrown");       
    }


}