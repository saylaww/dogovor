package uz.nukuslab.dogovor.service;

import org.springframework.stereotype.Service;
import uz.nukuslab.dogovor.entity.Address;
import uz.nukuslab.dogovor.entity.Company;
import uz.nukuslab.dogovor.entity.Prop;
import uz.nukuslab.dogovor.payload.ApiResponse;
import uz.nukuslab.dogovor.payload.CompanyDto;
import uz.nukuslab.dogovor.repository.AddressRepository;
import uz.nukuslab.dogovor.repository.CompanyRepository;
import uz.nukuslab.dogovor.repository.PropRepository;


import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private final
    CompanyRepository companyRepository;
    private final
    PropRepository propRepository;
    private final
    AddressRepository addressRepository;

    public CompanyService(CompanyRepository companyRepository, AddressRepository addressRepository, PropRepository propRepository) {
        this.companyRepository = companyRepository;
        this.addressRepository = addressRepository;
        this.propRepository = propRepository;
    }

    public ApiResponse getAllCompany() {
        List<Company> companies = companyRepository.findAll();
        return new ApiResponse("Company list", true, companies);
    }

    public ApiResponse getCompany(Long id) {
        Optional<Company> byId = companyRepository.findById(id);
        if (!byId.isPresent()) {
            return new ApiResponse("Bunday id li company bazada tabilmadi!!!", false);
        }
        Company company = byId.get();
        return new ApiResponse("Success", true, company);
    }

    public ApiResponse editCompany(Long id, CompanyDto companyDto) {
        Optional<Company> byId = companyRepository.findById(id);
        if (!byId.isPresent()) {
            return new ApiResponse("Bunday id li company bazada tabilmadi!!!", false);
        }
        Company company = byId.get();

        company.setDirectorName(companyDto.getDirectorName());
        company.setName(companyDto.getName());

        Optional<Prop> byProp = propRepository.findById(companyDto.getPropId());
        if (!byProp.isPresent()) {
            return new ApiResponse("Bunday prop id bazada tabilmadi!!!", false);
        }
        Prop prop = byProp.get();

        company.setProp(prop);

        Optional<Address> byAddress = addressRepository.findById(companyDto.getAddressId());
        if (!byAddress.isPresent()) {
            return new ApiResponse("Bunday address id bazada tabilmadi!!!", false);
        }
        Address address = byAddress.get();

        company.setAddress(address);

        companyRepository.save(company);
        return new ApiResponse("Company saved", true);
    }

    public ApiResponse deleteCompany(Long id) {
        try {
            companyRepository.findById(id);
            return new ApiResponse("Company deleted!", true);
        } catch (Exception e) {
            return new ApiResponse("Error!!!", false);
        }
    }

    public ApiResponse addCompany(CompanyDto companyDto) {
        boolean existsByName = companyRepository.existsByName(companyDto.getName());
        if (existsByName) {
            return new ApiResponse("Bunday name li company bazada bar!!!", false);
        }
        Company company = new Company();
        company.setName(companyDto.getName());
        company.setDirectorName(companyDto.getDirectorName());

        Optional<Prop> optionalProp = propRepository.findById(companyDto.getPropId());
        if (!optionalProp.isPresent()) {
            return new ApiResponse("Bunday id li prop bazada tabilmadi!!!", false);
        }
        Prop prop = optionalProp.get();

        company.setProp(prop);

        Optional<Address> optionalAddress = addressRepository.findById(companyDto.getAddressId());
        if (!optionalAddress.isPresent()) {
            return new ApiResponse("Bunday id li address bazada tabilmadi!!!", false);
        }
        Address address = optionalAddress.get();

        company.setAddress(address);

        companyRepository.save(company);

        return new ApiResponse("Company saved", true);
    }
}
