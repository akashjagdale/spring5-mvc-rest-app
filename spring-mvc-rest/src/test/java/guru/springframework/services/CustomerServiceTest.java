package guru.springframework.services;

import guru.springframework.api.v1.mapper.CustomerMapper;
import guru.springframework.domain.Customer;
import guru.springframework.model.CustomerDTO;
import guru.springframework.repositories.CustomerRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class CustomerServiceTest {


    public static final String FIRST_NAME = "Akash";
    public static final String LAST_NAME = "Jagdale";
    public static final long ID = 1L;

    CustomerService customerService;

    @Mock
    CustomerRepository customerRepository;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        customerService = new CustomerServiceImpl(CustomerMapper.INSTANCE, customerRepository);
    }

    @Test
    public void getCustomers() {
        List<Customer> customers = Arrays.asList(new Customer(), new Customer(), new Customer());

        Mockito.when(customerRepository.findAll()).thenReturn(customers);

        List<CustomerDTO> customerDTOS = customerService.getCustomers();

        assertEquals(3, customerDTOS.size());
    }

    @Test
    public void getCustomerById() {
        Customer customer = new Customer();
        customer.setId(ID);
        customer.setFirstName(FIRST_NAME);
        customer.setLastName(LAST_NAME);

        Mockito.when(customerRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(customer));

        CustomerDTO customerDTO = customerService.getCustomerById(1L);

        assertEquals(LAST_NAME, customerDTO.getLastname());
        assertEquals(FIRST_NAME, customerDTO.getFirstname());
    }

    @Test
    public void createNewCustomer() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstname(FIRST_NAME);
        customerDTO.setLastname(LAST_NAME);

        Customer savedCustomer = new Customer();
        savedCustomer.setFirstName(customerDTO.getFirstname());
        savedCustomer.setLastName(customerDTO.getLastname());
        savedCustomer.setId(ID);

        Mockito.when(customerRepository.save(ArgumentMatchers.any(Customer.class))).thenReturn(savedCustomer);

        CustomerDTO savedDTO = customerService.createNewCustomer(customerDTO);

        assertEquals(customerDTO.getFirstname(), savedDTO.getFirstname());
        assertEquals("/api/v1/customers/1", savedDTO.getCustomerUrl());

    }

    @Test
    public void saveCustomerByDTO() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstname(FIRST_NAME);
        customerDTO.setLastname(LAST_NAME);

        Customer savedCustomer = new Customer();
        savedCustomer.setFirstName(customerDTO.getFirstname());
        savedCustomer.setLastName(customerDTO.getLastname());
        savedCustomer.setId(ID);

        Mockito.when(customerRepository.save(ArgumentMatchers.any(Customer.class))).thenReturn(savedCustomer);

        CustomerDTO savedDTO = customerService.saveCustomerByDTO(1L, customerDTO);

        assertEquals(customerDTO.getFirstname(), savedDTO.getFirstname());
        assertEquals("/api/v1/customers/1", savedDTO.getCustomerUrl());
    }

    @Test
    public void testDeleteCustomerById() {
        customerService.deleteCustomerById(ID);

        Mockito.verify(customerRepository, Mockito.times(1)).deleteById(ArgumentMatchers.anyLong());
    }
}