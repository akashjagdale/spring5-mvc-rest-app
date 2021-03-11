package guru.springfamework.services;

import guru.springfamework.api.v1.mapper.VendorMapper;
import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.api.v1.model.VendorListDTO;
import guru.springfamework.controllers.v1.VendorController;
import guru.springfamework.domain.Vendor;
import guru.springfamework.repositories.VendorRepository;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class VendorServiceImplTestSFG {

    public static final String NAME = "Akash Jagdale";
    public static final long ID = 1L;
    public static final String VENDOR_URL = VendorController.BASE_URL + "/" + ID;

    VendorService vendorService;

    @Mock
    VendorRepository vendorRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        vendorService = new VendorServiceImpl(VendorMapper.INSTANCE, vendorRepository);
    }

    @Test
    public void testGetVendorById() {
        Vendor vendor = new Vendor();
        vendor.setId(ID);
        vendor.setName(NAME);


        // Mockito Behaviour Driven Development (BDD)
        BDDMockito.given(vendorRepository.findById(ArgumentMatchers.anyLong())).willReturn(Optional.of(vendor));

        //Mockito.when(vendorRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(vendor));

        VendorDTO vendorDTO = vendorService.getVendorById(ID);

        // then
        BDDMockito.then(vendorRepository).should(Mockito.times(1))
                .findById(ArgumentMatchers.anyLong());

        assertThat(vendorDTO.getName(), Is.is(IsEqual.equalTo(NAME)));

    }

    @Test
    public void getAllVendors() {
        List<Vendor> vendors = Arrays.asList(new Vendor(), new Vendor(), new Vendor());

        Mockito.when(vendorRepository.findAll()).thenReturn(vendors);

        VendorListDTO vendorListDTO = vendorService.getAllVendors();

        assertEquals(3, vendorListDTO.getVendors().size());
    }

    @Test
    public void testCreateVendor() {
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName(NAME);

        Vendor savedVendor = new Vendor();
        savedVendor.setName(NAME);
        savedVendor.setId(ID);

        Mockito.when(vendorRepository.save(ArgumentMatchers.any(Vendor.class))).thenReturn(savedVendor);

        VendorDTO savedDTO = vendorService.createNewVendor(vendorDTO);

        assertEquals(vendorDTO.getName(), savedDTO.getName());
        assertEquals(VENDOR_URL, savedDTO.getVendorUrl());
    }

    @Test
    public void testSaveVendorByDTO() {
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName(NAME);

        Vendor savedVendor = new Vendor();
        savedVendor.setName(NAME);
        savedVendor.setId(ID);

        Mockito.when(vendorRepository.save(ArgumentMatchers.any(Vendor.class))).thenReturn(savedVendor);

        VendorDTO savedDTO = vendorService.saveVendorByDTO(ID, vendorDTO);

        assertEquals(vendorDTO.getName(), savedDTO.getName());
        assertEquals(VENDOR_URL, savedDTO.getVendorUrl());
    }

    @Test
    public void testDeleteVendorById() {
        vendorService.deleteVendor(ID);

        Mockito.verify(vendorRepository, Mockito.times(1)).deleteById(ArgumentMatchers.anyLong());
    }
}
