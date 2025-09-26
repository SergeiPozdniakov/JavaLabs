package ru.productstar.mockito.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.productstar.mockito.model.Customer;
import ru.productstar.mockito.repository.CustomerRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Captor
    private ArgumentCaptor<Customer> customerCaptor;

    /**
     * Тест 1 - Получение покупателя "Ivan"
     * Проверки:
     * - очередность и точное количество вызовов каждого метода из CustomerRepository
     */
    @Test
    public void testGetExistingCustomer() {
        // Arrange
        Customer existingCustomer = new Customer("Ivan");
        when(customerRepository.getByName("Ivan")).thenReturn(existingCustomer);

        // Act
        Customer result = customerService.getOrCreate("Ivan");

        // Assert
        verify(customerRepository, times(1)).getByName("Ivan");
        verify(customerRepository, never()).add(any(Customer.class));
        assertEquals(existingCustomer, result);
    }

    /**
     * Тест 2 - Получение покупателя "Oleg"
     * Проверки:
     * - очередность и точное количество вызовов каждого метода из CustomerRepository
     * - в метод getOrCreate была передана строка "Oleg"
     */
    @Test
    public void testGetNewCustomer() {
        // Arrange
        when(customerRepository.getByName("Oleg")).thenReturn(null);
        Customer newCustomer = new Customer("Oleg");
        when(customerRepository.add(any(Customer.class))).thenReturn(newCustomer);

        // Act
        Customer result = customerService.getOrCreate("Oleg");

        // Assert
        verify(customerRepository, times(1)).getByName("Oleg");
        verify(customerRepository, times(1)).add(customerCaptor.capture());
        assertEquals("Oleg", customerCaptor.getValue().getName());
        assertEquals(newCustomer, result);
    }
}