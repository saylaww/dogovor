package uz.nukuslab.dogovor.service;

import org.springframework.stereotype.Service;
import uz.nukuslab.dogovor.entity.Company;
import uz.nukuslab.dogovor.entity.Phone;
import uz.nukuslab.dogovor.payload.ApiResponse;
import uz.nukuslab.dogovor.payload.PhoneDto;
import uz.nukuslab.dogovor.repository.CompanyRepository;
import uz.nukuslab.dogovor.repository.PhoneRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PhoneService {

    private final
    PhoneRepository phoneRepository;
    private final
    CompanyRepository companyRepository;

    public PhoneService(PhoneRepository phoneRepository, CompanyRepository companyRepository) {
        this.phoneRepository = phoneRepository;
        this.companyRepository = companyRepository;
    }

    public ApiResponse getAllPhone() {
        List<Phone> phones = phoneRepository.findAll();

        return new ApiResponse("Phone list", true, phones);
    }

    public ApiResponse getPhone(Long id) {
        Optional<Phone> byId = phoneRepository.findById(id);
        if (!byId.isPresent()) {
            return new ApiResponse("Bunday id li phone bazada joq!!!", false);
        }
        Phone phone = byId.get();

        return new ApiResponse("One phone:", true, phone);
    }

    public ApiResponse addPhone(PhoneDto phoneDto) {
        boolean exists = phoneRepository.existsByPhoneNumber(phoneDto.getPhoneNumber());
        if (exists) {
            return new ApiResponse("Bunday number li phone bazada bar!!!", false);
        }
        Phone phone = new Phone();
        phone.setPhoneNumber(phoneDto.getPhoneNumber());
        Optional<Company> byId = companyRepository.findById(phoneDto.getCompanyId());
        if (!byId.isPresent()) {
            return new ApiResponse("Bunday id li company bazada tabilmadi!!!", false);
        }
        Company company = byId.get();

        phone.setCompany(company);
        phone.setDescription(phoneDto.getDescription());

        phoneRepository.save(phone);

        return new ApiResponse("Phone saved", true);
    }

    public ApiResponse editPhone(Long id, PhoneDto phoneDto) {
        Optional<Phone> byId = phoneRepository.findById(id);
        if (!byId.isPresent()) {
            return new ApiResponse("Bunday id li phone bazada joq!!!", false);
        }
        Phone phone = byId.get();

        phone.setPhoneNumber(phoneDto.getPhoneNumber());
        phone.setDescription(phoneDto.getDescription());

        Optional<Company> optionalCompany = companyRepository.findById(phoneDto.getCompanyId());
        if (!optionalCompany.isPresent()) {
            return new ApiResponse("Bunday id li company bazada joq!!!", false);
        }
        Company company = optionalCompany.get();

        phone.setCompany(company);

        phoneRepository.save(phone);

        return new ApiResponse("Phone edited", true);
    }

    public ApiResponse deletePhone(Long id) {
        try {
            phoneRepository.deleteById(id);
            return new ApiResponse("Phone deleted", true);
        } catch (Exception e) {
            return new ApiResponse("Error", false);
        }
    }
}
