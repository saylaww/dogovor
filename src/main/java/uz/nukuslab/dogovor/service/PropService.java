package uz.nukuslab.dogovor.service;

import org.springframework.stereotype.Service;
import uz.nukuslab.dogovor.entity.Prop;
import uz.nukuslab.dogovor.payload.ApiResponse;
import uz.nukuslab.dogovor.payload.PropDto;
import uz.nukuslab.dogovor.repository.PropRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PropService {

    private final
    PropRepository propRepository;

    public PropService(PropRepository propRepository) {
        this.propRepository = propRepository;
    }

    public ApiResponse getAllProps() {
        List<Prop> props = propRepository.findAll();
        return new ApiResponse("Propa list", true, props);
    }

    public ApiResponse getProp(Long id) {
        Optional<Prop> byId = propRepository.findById(id);
        if (!byId.isPresent()) {
            return new ApiResponse("Bunday id li prop bazada tabilmadi!!!", false);
        }
        Prop prop = byId.get();
        return new ApiResponse("Success", true, prop);
    }

    public ApiResponse editProp(Long id, PropDto propDto) {
        Optional<Prop> byId = propRepository.findById(id);
        if (!byId.isPresent()) {
            return new ApiResponse("Bunday id li prop bazada tabilmadi!!!", false);
        }
        Prop prop = byId.get();
        prop.setAccountNumber(propDto.getAccountNumber());
        prop.setBankName(propDto.getBankName());
        prop.setInn(propDto.getInn());
        prop.setMfo(propDto.getMfo());

        propRepository.save(prop);

        return new ApiResponse("Prop saved", true);
    }

    public ApiResponse addProp(PropDto propDto) {
        boolean exist = propRepository.existsByAccountNumberAndMfoAndInn(propDto.getAccountNumber(), propDto.getMfo(), propDto.getInn());
        if (exist) {
            return new ApiResponse("Bunday prop bazada bar!!!", false);
        }
        Prop prop = new Prop();
        prop.setBankName(propDto.getBankName());
        prop.setAccountNumber(propDto.getAccountNumber());
        prop.setMfo(propDto.getMfo());
        prop.setInn(propDto.getInn());

        propRepository.save(prop);

        return new ApiResponse("Prop saved", true);
    }

    public ApiResponse deleteProp(Long id) {
        try {
            propRepository.deleteById(id);
            return new ApiResponse("Prop deleted", true);
        } catch (Exception e) {
            return new ApiResponse("Error!!!", false);
        }
    }
}
