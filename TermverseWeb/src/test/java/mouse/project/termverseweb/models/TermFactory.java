package mouse.project.termverseweb.models;

import mouse.project.termverseweb.dto.term.TermCreateDTO;
import org.springframework.stereotype.Service;

@Service
public class TermFactory implements Factory{

    public TermCreateDTO termCreateDTO(String term, int order) {
        TermCreateDTO termCreateDTO = new TermCreateDTO();
        termCreateDTO.setTerm(term);
        termCreateDTO.setDefinition(term.trim() + " definition");
        termCreateDTO.setOrder(order);
        return termCreateDTO;
    }
}
