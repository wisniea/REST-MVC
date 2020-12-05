package pl.wisniea.restmvc_data.response;

import lombok.Data;


@Data
public class AddressRest {

    private String addressId;
    private String city;
    private String country;
    private String streetName;
    private String zipCode;

}
