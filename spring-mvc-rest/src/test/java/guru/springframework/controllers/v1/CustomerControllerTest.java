package guru.springframework.controllers.v1;

import guru.springframework.model.CustomerDTO;
import guru.springframework.services.CustomerService;
import guru.springframework.services.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static guru.springframework.controllers.v1.AbstractRestControllerTest.asJsonString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomerControllerTest {

    public static final String FIRST_NAME = "Akash";
    public static final String LAST_NAME = "Jagdale";
    public static final String CUSTOMER_URL = "/api/v1/customers/1";

    @Mock
    CustomerService customerService;

    @InjectMocks
    CustomerController customerController;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(customerController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
    }

    @Test
    public void getAllCustomers() throws Exception {
        CustomerDTO customer1 = new CustomerDTO();
        customer1.setFirstname(FIRST_NAME);
        customer1.setLastname(LAST_NAME);


        CustomerDTO customer2 = new CustomerDTO();
        customer2.setFirstname("Jonny");
        customer2.setLastname("Depp");


        List<CustomerDTO> customers = Arrays.asList(customer1, customer2);

        when(customerService.getCustomers()).thenReturn(customers);

        mockMvc.perform(get("/api/v1/customers/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customers", hasSize(2)));
    }

    @Test
    public void getCustomerById() throws Exception {
        CustomerDTO customer1 = new CustomerDTO();
        customer1.setFirstname(FIRST_NAME);
        customer1.setLastname(LAST_NAME);
        customer1.setCustomerUrl(CUSTOMER_URL);

        when(customerService.getCustomerById(1L)).thenReturn(customer1);

        mockMvc.perform(get(CUSTOMER_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname", equalTo(FIRST_NAME)));
    }

    @Test
    public void createNewCustomer() throws Exception {
        CustomerDTO customer = new CustomerDTO();
        customer.setFirstname(FIRST_NAME);
        customer.setLastname(LAST_NAME);

        CustomerDTO returnedDTO = new CustomerDTO();
        returnedDTO.setFirstname(customer.getFirstname());
        returnedDTO.setLastname(customer.getLastname());
        returnedDTO.setCustomerUrl(CUSTOMER_URL);

        when(customerService.createNewCustomer(any(CustomerDTO.class))).thenReturn(returnedDTO);

        mockMvc.perform(post("/api/v1/customers/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(customer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstname", equalTo(FIRST_NAME)))
                .andExpect(jsonPath("$.lastname", equalTo(LAST_NAME)))
                .andExpect(jsonPath("$.customerUrl", equalTo(CUSTOMER_URL)));
    }

    @Test
    public void updateCustomerTest() throws Exception {

        CustomerDTO customer = new CustomerDTO();
        customer.setFirstname(FIRST_NAME);
        customer.setLastname(LAST_NAME);

        CustomerDTO returnedDTO = new CustomerDTO();
        returnedDTO.setFirstname(customer.getFirstname());
        returnedDTO.setLastname(customer.getLastname());
        returnedDTO.setCustomerUrl(CUSTOMER_URL);

        Mockito.when(customerService.saveCustomerByDTO(anyLong(), any(CustomerDTO.class))).thenReturn(returnedDTO);

        mockMvc.perform(put("/api/v1/customers/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(customer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname", equalTo(FIRST_NAME)))
                .andExpect(jsonPath("$.lastname", equalTo(LAST_NAME)))
                .andExpect(jsonPath("$.customerUrl", equalTo(CUSTOMER_URL)));

    }

    @Test
    public void testPatchCustomer() throws Exception {
        CustomerDTO customer = new CustomerDTO();
        customer.setFirstname(FIRST_NAME);
        customer.setLastname(LAST_NAME);

        CustomerDTO returnedDTO = new CustomerDTO();
        returnedDTO.setFirstname(customer.getFirstname());
        returnedDTO.setLastname("Some Last Name");
        returnedDTO.setCustomerUrl(CUSTOMER_URL);

        Mockito.when(customerService.patchCustomer(anyLong(), any(CustomerDTO.class))).thenReturn(returnedDTO);

        mockMvc.perform(patch("/api/v1/customers/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(customer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname", equalTo(FIRST_NAME)))
                .andExpect(jsonPath("$.lastname", equalTo("Some Last Name")))
                .andExpect(jsonPath("$.customerUrl", equalTo(CUSTOMER_URL)));
    }

    @Test
    public void deleteCustomer() throws Exception {
        mockMvc.perform(delete("/api/v1/customers/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(customerService, times(1)).deleteCustomerById(anyLong());
    }

    @Test
    public void testGetByIdNotFound() throws Exception {
        Mockito.when(customerService.getCustomerById(anyLong())).thenThrow(ResourceNotFoundException.class);
        mockMvc.perform(get(CustomerController.BASE_URL + "/122")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}