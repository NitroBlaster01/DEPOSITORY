package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.estore.api.estoreapi.persistence.ProductDAO;
import com.estore.api.estoreapi.model.Product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test the Product Controller class
 * 
 * @auther Aydan Reyes
 */
@Tag("Controller-tier")
public class ProductControllerTest {
    private ProductController productController;
    private ProductDAO mockProductDAO;

    /**
     * Before each test, create a new ProductController objet and inject
     * a mock product DAO
     */
    @BeforeEach
    public void setupProductController() {
        mockProductDAO = mock(ProductDAO.class);
        productController = new ProductController(mockProductDAO);
    }

    @Test
    public void testGetProduct() throws IOException {
        //Setup
        Product product = new Product(99,"Test",9.99,99,"testurl");

        when(mockProductDAO.getProduct(product.getId())).thenReturn(product);

        //Invoke
        ResponseEntity<Product> response = productController.getProduct(product.getId());

        //Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(product, response.getBody());
    }

    @Test
    public void testGetProductNotFound() throws Exception {
        //Setup
        int productId = 99;

        when(mockProductDAO.getProduct(productId)).thenReturn(null);

        //Invoke
        ResponseEntity<Product> response = productController.getProduct(productId);

        //Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    
    @Test
    public void testGetProductHandleException() throws Exception {
        //Setup
        int productId = 99;

        doThrow(new IOException()).when(mockProductDAO).getProduct(productId);

        //Invoke
        ResponseEntity<Product> response = productController.getProduct(productId);

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testCreateProduct() throws IOException {
        //Setup
        Product product = new Product(99, "Test", 9.99, 99,"testurl");

        when(mockProductDAO.createProduct(product)).thenReturn(product);

        //Invoke
        ResponseEntity<Product> response = productController.createProduct(product);

        //Analyze
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(product, response.getBody());
    }

    @Test
    public void testCreateProductFailed() throws IOException {
        //Setup
        Product product = new Product(99, "Test", 9.99, 99,"testurl");

        when(mockProductDAO.createProduct(product)).thenReturn(null);

        //Invoke
        ResponseEntity<Product> response = productController.createProduct(product);

        //Analyze
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testCreateProductHandleException() throws IOException {
        //Setup
        Product product = new Product(99, "Test", 9.99, 99,"testurl");

        doThrow(new IOException()).when(mockProductDAO).createProduct(product);

        //Invoke
        ResponseEntity<Product> response = productController.createProduct(product);

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testUpdateProduct() throws IOException {
        //Setup
        Product product = new Product(99, "Test", 9.99, 99,"testurl");

        when (mockProductDAO.updateProduct(product)).thenReturn(product);
        ResponseEntity<Product> response = productController.updateProduct(product);
        product.setName("Update");

        //Invoke
        response = productController.updateProduct(product);

        //Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(product, response.getBody());
    }

    @Test
    public void testUpdateProductFailed() throws IOException {
        //Setup
        Product product = new Product(99, "Test", 9.99, 99,"testurl");

        when(mockProductDAO.updateProduct(product)).thenReturn(null);

        //Invoke
        ResponseEntity<Product> response = productController.updateProduct(product);

        //Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateProductHandleException() throws IOException {
        //Setup
        Product product = new Product(99, "Test", 9.99, 99,"testurl");

        doThrow(new IOException()).when(mockProductDAO).updateProduct(product);

        //Invoke
        ResponseEntity<Product> response = productController.updateProduct(product);

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetProducts() throws IOException {
        //Setup
        Product[] products = new Product[2];
        products[0] = new Product(99, "Test1", 9.99, 99,"testurl");
        products[1] = new Product(100, "Test2", 10, 100,"testurl");

        when(mockProductDAO.getProducts()).thenReturn(products);

        //Invoke
        ResponseEntity<Product[]> response = productController.getProducts();

        //Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(products, response.getBody());
    }

    @Test
    public void testGetProductsHandleException() throws IOException {
        doThrow(new IOException()). when(mockProductDAO).getProducts();

        //Invoke
        ResponseEntity<Product[]> response = productController.getProducts();

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testSearchProducts() throws IOException {
        //Setup
        String searchString = "es";
        Product[] products = new Product[2];
        products[0] = new Product(99, "Test", 9.99, 99,"testurl");
        products[1] = new Product(100, "Test2", 10, 100,"testurl");

        when(mockProductDAO.findProducts(searchString)).thenReturn(products);

        //Invoke
        ResponseEntity<Product[]> response = productController.searchProducts(searchString);

        //Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(products, response.getBody());
    }

    @Test
    public void testSearchProductsHandleException() throws IOException {
        //Setup
        String searchString = "an";

        doThrow(new IOException()).when(mockProductDAO).findProducts(searchString);

        ResponseEntity<Product[]> response = productController.searchProducts(searchString);

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testDeleteProduct() throws IOException {
        //Setup
        int productId = 99;

        when(mockProductDAO.deleteProduct(productId)).thenReturn(true);

        //Invoke
        ResponseEntity<Product> response = productController.deleteProduct(productId);

        //Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteProductNotFound() throws IOException {
        //Setup
        int productId = 99;

        when(mockProductDAO.deleteProduct(productId)).thenReturn(false);

        //Invoke
        ResponseEntity<Product> response = productController.deleteProduct(productId);

        //Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteProductHandleException() throws IOException {
        //Setup
        int productId = 99;

        doThrow(new IOException()).when(mockProductDAO).deleteProduct(productId);

        //Invoke
        ResponseEntity<Product> response = productController.deleteProduct(productId);
        
        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
