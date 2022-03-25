package uz.nukuslab.dogovor.service;

import org.springframework.stereotype.Service;
import uz.nukuslab.dogovor.entity.Address;
import uz.nukuslab.dogovor.payload.AddressDto;
import uz.nukuslab.dogovor.payload.ApiResponse;
import uz.nukuslab.dogovor.repository.AddressRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    private final
    AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public ApiResponse addAddress(AddressDto addressDto) {
        boolean exist = addressRepository.existsByCityAndDistrictAndNumber(addressDto.getCity(), addressDto.getDistrict(), addressDto.getNumber());
        if (exist){
            return new ApiResponse("Bunday address bazada bar!!!", false);
        }
        Address address = new Address();

        address.setCity(addressDto.getCity());
        address.setDistrict(addressDto.getDistrict());
        address.setNumber(addressDto.getNumber());

        addressRepository.save(address);

        return new ApiResponse("Address saqlandi!", true);
    }

    public ApiResponse getAllAddresses() {
        List<Address> addresses = addressRepository.findAll();
        return new ApiResponse("Addressler listi", true, addresses);
    }

    public ApiResponse deleteAddress(Long id) {
        try {
            addressRepository.deleteById(id);
            return new ApiResponse("Address o`shirildi", true);
        }catch (Exception e){
            return new ApiResponse("Error!!!", false);
        }
    }

    public ApiResponse editAddress(Long id, AddressDto addressDto) {
        Optional<Address> optioanlAddress = addressRepository.findById(id);
        if (optioanlAddress.isPresent()){
            Address address = optioanlAddress.get();

            address.setCity(addressDto.getCity());
            address.setDistrict(addressDto.getDistrict());
            address.setNumber(addressDto.getNumber());

            addressRepository.save(address);
            return new ApiResponse("Addres edited", true);
        }
        return  new ApiResponse("Bunday id li address bazada joq!!!", false);
    }

    public ApiResponse getAddress(Long id) {
        Optional<Address> byId = addressRepository.findById(id);
        if (byId.isPresent()){
            Address address = byId.get();
            return new ApiResponse("Success", true, address);
        }
        return new ApiResponse("Bunday id li address bazada tabilmadi!!!", false);
    }

}
