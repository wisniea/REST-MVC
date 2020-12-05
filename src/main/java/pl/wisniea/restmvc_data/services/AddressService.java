package pl.wisniea.restmvc_data.services;



import pl.wisniea.restmvc_data.response.AddressRest;

import java.util.List;

public interface AddressService {

    List<AddressRest> getAddressesByUserId(String userId);

    List<AddressRest> getAllAddresses();
}
