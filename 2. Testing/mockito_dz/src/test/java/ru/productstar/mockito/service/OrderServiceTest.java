package ru.productstar.mockito.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.productstar.mockito.ProductNotFoundException;
import ru.productstar.mockito.model.*;
import ru.productstar.mockito.repository.OrderRepository;
import ru.productstar.mockito.repository.ProductRepository;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private WarehouseService warehouseService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    @Captor
    private ArgumentCaptor<Customer> customerCaptor;
    @Captor
    private ArgumentCaptor<Delivery> deliveryCaptor;

    private Product phoneProduct;

    @BeforeEach
    void setUp() {
        phoneProduct = new Product("phone");
        phoneProduct.setId(0);

        lenient().when(productRepository.getByName("phone")).thenReturn(phoneProduct);
    }

    /**
     * Создание ордера для существующего клиента
     */
    @Test
    public void testCreateOrderForExistingCustomer() {
        Customer existingCustomer = new Customer("Ivan");
        when(customerService.getOrCreate("Ivan")).thenReturn(existingCustomer);
        when(orderRepository.create(any(Customer.class))).thenAnswer(invocation -> {
            Customer customer = invocation.getArgument(0);
            Order order = new Order(customer);
            order.setId(0);
            return order;
        });

        Order order = orderService.create("Ivan");

        verify(customerService, times(1)).getOrCreate("Ivan");
        verify(orderRepository, times(1)).create(existingCustomer);
        assertNotNull(order);
        assertEquals(0, order.getId());
        assertEquals("Ivan", order.getCustomer().getName());
    }

    /**
     * Создание ордера для нового клиента
     */
    @Test
    public void testCreateOrderForNewCustomer() {
        Customer newCustomer = new Customer("Oleg");
        when(customerService.getOrCreate("Oleg")).thenReturn(newCustomer);
        when(orderRepository.create(any(Customer.class))).thenAnswer(invocation -> {
            Customer customer = invocation.getArgument(0);
            Order order = new Order(customer);
            order.setId(0);
            return order;
        });

        Order order = orderService.create("Oleg");

        verify(customerService, times(1)).getOrCreate("Oleg");
        verify(orderRepository, times(1)).create(newCustomer);
        assertNotNull(order);
        assertEquals(0, order.getId());
        assertEquals("Oleg", order.getCustomer().getName());
    }

    /**
     * Добавление существующего товара в достаточном количестве
     */
    @Test
    public void testAddExistingProductSufficientQuantity() throws ProductNotFoundException {
        Customer customer = new Customer("Ivan");
        Order order = new Order(customer);
        order.setId(0);

        Warehouse warehouse = new Warehouse("Warehouse0", 30);
        warehouse.setId(0);

        when(warehouseService.findWarehouse("phone", 2)).thenReturn(warehouse);
        when(warehouseService.getStock(warehouse, "phone")).thenReturn(new Stock(phoneProduct, 400, 5));

        when(orderRepository.addDelivery(anyInt(), any(Delivery.class))).thenAnswer(invocation -> {
            int orderId = invocation.getArgument(0);
            Delivery delivery = invocation.getArgument(1);
            Order updatedOrder = new Order(customer);
            updatedOrder.setId(orderId);
            updatedOrder.addDelivery(delivery);
            return updatedOrder;
        });

        Order result = orderService.addProduct(order, "phone", 2, false);

        verify(warehouseService, times(1)).findWarehouse("phone", 2);
        verify(productRepository, times(1)).getByName("phone"); // ✅ Проверка вызова
        verify(orderRepository, times(1)).addDelivery(eq(0), deliveryCaptor.capture());
        assertEquals(800, result.getTotal());
        assertEquals(2, deliveryCaptor.getValue().getCount());
        assertEquals(400, deliveryCaptor.getValue().getPrice());
    }

    /**
     * Добавление несуществующего товара
     */
    @Test
    public void testAddNonExistingProduct() {
        Customer customer = new Customer("Ivan");
        Order order = new Order(customer);
        order.setId(0);

        lenient().when(productRepository.getByName("nonExistingProduct")).thenReturn(null);

        assertThrows(ProductNotFoundException.class, () ->
                orderService.addProduct(order, "nonExistingProduct", 2, false));
    }

    /**
     * Добавление товара в недостаточном количестве
     */
    @Test
    public void testAddProductInsufficientQuantity() {
        Customer customer = new Customer("Ivan");
        Order order = new Order(customer);
        order.setId(0);

        // ✅ НЕ заглушаем getByName("phone") — он уже заглушен в @BeforeEach
        // ✅ НЕ заглушаем findWarehouse — он должен вернуть null (недостаточно товара)

        assertThrows(ProductNotFoundException.class, () ->
                orderService.addProduct(order, "phone", 10, false));
    }

    /**
     * Заказ товара с быстрой доставкой
     */
    @Test
    public void testAddProductWithFastestDelivery() throws ProductNotFoundException {
        Customer customer = new Customer("Ivan");
        Order order = new Order(customer);
        order.setId(0);

        Warehouse warehouse = new Warehouse("Warehouse0", 30);
        warehouse.setId(0);

        when(warehouseService.findClosestWarehouse("phone", 2)).thenReturn(warehouse);
        when(warehouseService.getStock(warehouse, "phone")).thenReturn(new Stock(phoneProduct, 400, 5));

        when(orderRepository.addDelivery(anyInt(), any(Delivery.class))).thenAnswer(invocation -> {
            int orderId = invocation.getArgument(0);
            Delivery delivery = invocation.getArgument(1);
            Order updatedOrder = new Order(customer);
            updatedOrder.setId(orderId);
            updatedOrder.addDelivery(delivery);
            return updatedOrder;
        });

        Order result = orderService.addProduct(order, "phone", 2, true);

        verify(warehouseService, times(1)).findClosestWarehouse("phone", 2);
        verify(warehouseService, never()).findWarehouse("phone", 2);
        verify(productRepository, times(1)).getByName("phone");
        verify(orderRepository, times(1)).addDelivery(eq(0), deliveryCaptor.capture());
        assertEquals(800, result.getTotal());
    }
}