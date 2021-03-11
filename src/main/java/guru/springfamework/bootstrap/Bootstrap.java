package guru.springfamework.bootstrap;

import guru.springfamework.domain.Category;
import guru.springfamework.domain.Customer;
import guru.springfamework.domain.Vendor;
import guru.springfamework.repositories.CategoryRepository;
import guru.springfamework.repositories.CustomerRepository;
import guru.springfamework.repositories.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements CommandLineRunner {

    private CategoryRepository categoryRepository;
    private CustomerRepository customerRepository;
    private VendorRepository vendorRepository;

    public Bootstrap(CategoryRepository categoryRepository, CustomerRepository customerRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.customerRepository = customerRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadCategories();

        loadCustomers();

        loadVendors();

    }

    private void loadVendors() {
        /* Bootstrapping Vendors*/
        Vendor v1 = new Vendor();
        v1.setName("Akash Jagdale");

        Vendor v2 = new Vendor();
        v2.setName("Tom Hardy");

        Vendor v3 = new Vendor();
        v3.setName("Nick Burkhardt");

        vendorRepository.save(v1);
        vendorRepository.save(v2);
        vendorRepository.save(v3);

        System.out.println("Vendor Data Loaded = " + customerRepository.count());
    }

    private void loadCustomers() {
        /* Bootstrapping Customers*/
        Customer aj = new Customer();
        aj.setFirstName("Akash");
        aj.setLastName("Jagdale");

        Customer jd = new Customer();
        jd.setFirstName("Jonny");
        jd.setLastName("Depp");

        Customer hp = new Customer();
        hp.setFirstName("Harry");
        hp.setLastName("Potter");

        customerRepository.save(aj);
        customerRepository.save(jd);
        customerRepository.save(hp);

        System.out.println("Customer Data Loaded = " + customerRepository.count());
    }

    private void loadCategories() {
        /* Bootstrapping Categories*/
        Category fruits = new Category();
        fruits.setName("Fruits");

        Category dried = new Category();
        dried.setName("Dried");

        Category fresh = new Category();
        fresh.setName("Fresh");

        Category exotic = new Category();
        exotic.setName("Exotic");

        Category nuts = new Category();
        nuts.setName("Nuts");

        categoryRepository.save(fruits);
        categoryRepository.save(dried);
        categoryRepository.save(fresh);
        categoryRepository.save(exotic);
        categoryRepository.save(nuts);

        System.out.println("Category Data Loaded = " + categoryRepository.count());
    }
}
