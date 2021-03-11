package guru.springfamework.controllers.v1;

import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.api.v1.model.VendorListDTO;
import guru.springfamework.services.VendorService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VendorControllerTest {
    public static final String NAME = "Akash Jagdale";
    public static final Long ID = 1L;
    public static final String VENDOR_URL = VendorController.BASE_URL + "/" + ID;

    @Mock
    VendorService vendorService;

    @InjectMocks
    VendorController vendorController;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(vendorController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
    }

    @Test
    public void testGetVendorById() throws Exception {
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName(NAME);
        vendorDTO.setVendorUrl(VENDOR_URL);

        Mockito.when(vendorService.getVendorById(ArgumentMatchers.anyLong())).thenReturn(vendorDTO);

        mockMvc.perform(get(VENDOR_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(NAME)));
    }

    @Test
    public void getVendors() throws Exception {
        VendorDTO vendorDTO1 = new VendorDTO();
        vendorDTO1.setName(NAME);

        VendorDTO vendorDTO2 = new VendorDTO();
        vendorDTO2.setName("Shaun Renard");

        VendorListDTO vendors = new VendorListDTO(Arrays.asList(vendorDTO1, vendorDTO2));

        Mockito.when(vendorService.getAllVendors()).thenReturn(vendors);

        mockMvc.perform(get(vendorController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vendors", hasSize(2)));
    }

    @Test
    public void createNewVendor() throws Exception {
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName(NAME);

        VendorDTO returnedDTO = new VendorDTO();
        returnedDTO.setName(vendorDTO.getName());
        returnedDTO.setVendorUrl(VENDOR_URL);

        Mockito.when(vendorService.createNewVendor(Mockito.any(VendorDTO.class))).thenReturn(returnedDTO);

        mockMvc.perform(post(VendorController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractRestControllerTest.asJsonString(vendorDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", equalTo(NAME)))
                .andExpect(jsonPath("$.vendor_url", equalTo(VENDOR_URL)));
    }

    @Test
    public void updateVendor() throws Exception {
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName(NAME);

        VendorDTO returnedDTO = new VendorDTO();
        returnedDTO.setName(vendorDTO.getName());
        returnedDTO.setVendorUrl(VENDOR_URL);

        Mockito.when(vendorService.saveVendorByDTO(ArgumentMatchers.anyLong(), ArgumentMatchers.any(VendorDTO.class)))
                .thenReturn(returnedDTO);

        mockMvc.perform(put(VENDOR_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractRestControllerTest.asJsonString(vendorDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(NAME)))
                .andExpect(jsonPath("$.vendor_url", equalTo(VENDOR_URL)));
    }

    @Test
    public void patchVendor() throws Exception {

        VendorDTO vendor = new VendorDTO();
        vendor.setName(NAME);

        VendorDTO returnedDTO = new VendorDTO();
        returnedDTO.setName("Some First Name");
        returnedDTO.setVendorUrl(VENDOR_URL);

        Mockito.when(vendorService.patchVendor(ArgumentMatchers.anyLong(), ArgumentMatchers.any(VendorDTO.class)))
                .thenReturn(returnedDTO);

        mockMvc.perform(patch(VENDOR_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractRestControllerTest.asJsonString(vendor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("Some First Name")))
                .andExpect(jsonPath("$.vendor_url", equalTo(VENDOR_URL)));
    }

    @Test
    public void deleteVendor() throws Exception {

        mockMvc.perform(delete(VENDOR_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(vendorService, Mockito.times(1)).deleteVendor(ArgumentMatchers.anyLong());
    }
}