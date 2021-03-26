package guru.springframework.services;

import guru.springframework.api.v1.mapper.CustomerMapper;
import guru.springframework.controllers.v1.CustomerController;
import guru.springframework.domain.Customer;
import guru.springframework.model.CustomerDTO;
import guru.springframework.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerMapper customerMapper, CustomerRepository customerRepository) {
        this.customerMapper = customerMapper;
        this.customerRepository = customerRepository;
    }

    private String getCustomerUrl(Long id) {
        return CustomerController.BASE_URL + "/" + id;
    }

    @Override
    public List<CustomerDTO> getCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customer -> {
                    CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);
                    customerDTO.setCustomerUrl(getCustomerUrl(customer.getId()));
                    return customerDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(customer -> {
                    CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);
                    customerDTO.setCustomerUrl(getCustomerUrl(customer.getId()));
                    return customerDTO;
                })
                .orElseThrow(ResourceNotFoundException::new);
    }

    private CustomerDTO saveAndReturnCustomerDTO(Customer customer) {
        Customer savedCustomer = customerRepository.save(customer);

        CustomerDTO returnDTO = customerMapper.customerToCustomerDTO(savedCustomer);
        returnDTO.setCustomerUrl(getCustomerUrl(savedCustomer.getId()));

        return returnDTO;
    }

    @Override
    public CustomerDTO createNewCustomer(CustomerDTO customerDTO) {

        return saveAndReturnCustomerDTO(customerMapper.customerDTOToCustomer(customerDTO));
    }

    @Override
    public CustomerDTO saveCustomerByDTO(Long id, CustomerDTO customerDTO) {
        Customer customer = customerMapper.customerDTOToCustomer(customerDTO);
        customer.setId(id);

        return saveAndReturnCustomerDTO(customer);
    }

    @Override
    public CustomerDTO patchCustomer(Long id, CustomerDTO customerDTO) {
        return customerRepository.findById(id)
                .map(customer -> {
                    if (customerDTO.getFirstname() != null) {
                        customer.setFirstname(customerDTO.getFirstname());
                    }

                    if (customerDTO.getLastname() != null) {
                        customer.setLastname(customerDTO.getLastname());
                    }

                    Customer patchedCustomer = customerRepository.save(customer);

                    CustomerDTO returnDTO = customerMapper.customerToCustomerDTO(patchedCustomer);
                    returnDTO.setCustomerUrl(getCustomerUrl(patchedCustomer.getId()));

                    return returnDTO;
                }).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public void deleteCustomerById(Long id) {
        customerRepository.deleteById(id);
    }

}
