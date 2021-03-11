package guru.springfamework.api.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CustomerDTO {

    @ApiModelProperty(value = "Customer first name", required = true)
    private String firstName;

    @ApiModelProperty(required = true)
    private String lastName;

    @JsonProperty("customer_url")
    private String customerUrl;
}
