package guru.springframework.api.v1.mapper;

import guru.springframework.domain.Customer;
import guru.springframework.model.CustomerDTO;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CustomerMapperTest {
    public static final String FIRST_NAME = "AKASH";
    public static final String LAST_NAME = "AKASH";
    public static final long ID = 1L;

    CustomerMapper customerMapper = CustomerMapper.INSTANCE;


    @Test
    public void customerToCustomerDTO() {
        Customer customer = new Customer();
        customer.setId(ID);
        customer.setFirstName(FIRST_NAME);
        customer.setLastName(LAST_NAME);

        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);

        assertEquals(FIRST_NAME, customerDTO.getFirstname());
        assertEquals(LAST_NAME, customerDTO.getLastname());
    }

    @Test
    public void customerDTOToCustomer() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstname(FIRST_NAME);
        customerDTO.setLastname(LAST_NAME);

        Customer customer = customerMapper.customerDTOToCustomer(customerDTO);

        assertEquals(FIRST_NAME, customer.getFirstName());
        assertEquals(LAST_NAME, customer.getLastName());
    }
}