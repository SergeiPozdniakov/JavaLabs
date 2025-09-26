package ru.productstar.mockito.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.productstar.mockito.model.Product;
import ru.productstar.mockito.model.Stock;
import ru.productstar.mockito.model.Warehouse;
import ru.productstar.mockito.repository.WarehouseRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WarehouseServiceTest {

    @Mock
    private WarehouseRepository warehouseRepository;

    @InjectMocks
    private WarehouseService warehouseService;

    @Test
    public void testFindNonExistingProduct() {
        // Arrange
        
        Warehouse wh1 = new Warehouse("Warehouse1", 10);
        Warehouse wh2 = new Warehouse("Warehouse2", 20);
        Warehouse wh3 = new Warehouse("Warehouse3", 30);
        when(warehouseRepository.all()).thenReturn(Arrays.asList(wh1, wh2, wh3));

        // Act
        List<Warehouse> result = warehouseService.findWarehouses("nonExistingProduct", 1);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindExistingProductSufficientQuantity() {
        // Arrange
        Product phone = new Product("phone");
        Product laptop = new Product("laptop");

        Stock phoneStock1 = new Stock(phone, 400, 10);
        Stock laptopStock1 = new Stock(laptop, 900, 5);
        Stock phoneStock2 = new Stock(phone, 380, 8);
        Stock laptopStock2 = new Stock(laptop, 850, 3);
        Stock phoneStock3 = new Stock(phone, 450, 6);
        Stock laptopStock3 = new Stock(laptop, 200, 4);

        Warehouse wh1 = new Warehouse("Warehouse1", 10);
        wh1.getStocks().add(phoneStock1);
        wh1.getStocks().add(laptopStock1);

        Warehouse wh2 = new Warehouse("Warehouse2", 20);
        wh2.getStocks().add(phoneStock2);
        wh2.getStocks().add(laptopStock2);

        Warehouse wh3 = new Warehouse("Warehouse3", 30);
        wh3.getStocks().add(phoneStock3);
        wh3.getStocks().add(laptopStock3);

        when(warehouseRepository.all()).thenReturn(Arrays.asList(wh1, wh2, wh3));

        // Act
        List<Warehouse> phoneWarehouses = warehouseService.findWarehouses("phone", 5);
        List<Warehouse> laptopWarehouses = warehouseService.findWarehouses("laptop", 3);

        // Assert
        assertEquals(3, phoneWarehouses.size());
        assertEquals(3, laptopWarehouses.size());
    }

    @Test
    public void testFindExistingProductInsufficientQuantity() {
        // Arrange
        Product phone = new Product("phone");
        Product laptop = new Product("laptop");

        Stock phoneStock1 = new Stock(phone, 400, 10);
        Stock laptopStock1 = new Stock(laptop, 900, 5);
        Stock phoneStock2 = new Stock(phone, 380, 8);
        Stock laptopStock2 = new Stock(laptop, 850, 3);
        Stock phoneStock3 = new Stock(phone, 450, 6);
        Stock laptopStock3 = new Stock(laptop, 200, 4);

        Warehouse wh1 = new Warehouse("Warehouse1", 10);
        wh1.getStocks().add(phoneStock1);
        wh1.getStocks().add(laptopStock1);

        Warehouse wh2 = new Warehouse("Warehouse2", 20);
        wh2.getStocks().add(phoneStock2);
        wh2.getStocks().add(laptopStock2);

        Warehouse wh3 = new Warehouse("Warehouse3", 30);
        wh3.getStocks().add(phoneStock3);
        wh3.getStocks().add(laptopStock3);

        when(warehouseRepository.all()).thenReturn(Arrays.asList(wh1, wh2, wh3));

        // Act
        List<Warehouse> phoneWarehouses = warehouseService.findWarehouses("phone", 15);
        List<Warehouse> laptopWarehouses = warehouseService.findWarehouses("laptop", 6);

        // Assert
        assertTrue(phoneWarehouses.isEmpty());
        assertTrue(laptopWarehouses.isEmpty());
    }

    @Test
    public void testFindClosestWarehouse() {
        // Arrange
        Product phone = new Product("phone");

        Stock phoneStock1 = new Stock(phone, 400, 10);
        Stock phoneStock2 = new Stock(phone, 380, 8);
        Stock phoneStock3 = new Stock(phone, 450, 6);

        Warehouse wh1 = new Warehouse("Warehouse1", 10);
        wh1.getStocks().add(phoneStock1);

        Warehouse wh2 = new Warehouse("Warehouse2", 20);
        wh2.getStocks().add(phoneStock2);

        Warehouse wh3 = new Warehouse("Warehouse3", 30);
        wh3.getStocks().add(phoneStock3);

        when(warehouseRepository.all()).thenReturn(Arrays.asList(wh1, wh2, wh3));

        // Act
        Warehouse closestWarehouse = warehouseService.findClosestWarehouse("phone", 5);

        // Assert
        assertNotNull(closestWarehouse);
        assertEquals(10, closestWarehouse.getDistance());
        assertEquals("Warehouse1", closestWarehouse.getName());
    }

    @Test
    public void testFindWarehouse() {
        // Arrange
        Product phone = new Product("phone");

        Stock phoneStock1 = new Stock(phone, 400, 10);
        Stock phoneStock2 = new Stock(phone, 380, 8);
        Stock phoneStock3 = new Stock(phone, 450, 6);

        Warehouse wh1 = new Warehouse("Warehouse1", 10);
        wh1.getStocks().add(phoneStock1);

        Warehouse wh2 = new Warehouse("Warehouse2", 20);
        wh2.getStocks().add(phoneStock2);

        Warehouse wh3 = new Warehouse("Warehouse3", 30);
        wh3.getStocks().add(phoneStock3);

        when(warehouseRepository.all()).thenReturn(Arrays.asList(wh1, wh2, wh3));

        // Act
        Warehouse warehouse = warehouseService.findWarehouse("phone", 5);

        // Assert
        assertNotNull(warehouse);
        assertEquals("Warehouse1", warehouse.getName());
        assertEquals(10, warehouse.getDistance());
    }
}