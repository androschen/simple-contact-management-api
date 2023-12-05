package com.restapi.service.address;

import com.restapi.entity.Address;
import com.restapi.entity.User;
import com.restapi.model.request.address.CreateAddressRequest;

public interface AddressService {
   Address create(User user, CreateAddressRequest request);
   Address get(User user, String contactId, String addressId);
}
