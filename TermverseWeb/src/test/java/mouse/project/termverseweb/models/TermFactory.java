package mouse.project.termverseweb.models;

import mouse.project.termverseweb.dto.term.TermCreateDTO;
import mouse.project.termverseweb.dto.term.TermResponseDTO;
import mouse.project.termverseweb.dto.term.TermUpdateDTO;
import mouse.project.termverseweb.model.Term;
import org.springframework.stereotype.Service;

@Service
@mouse.project.lib.ioc.annotation.Service
public class TermFactory implements Factory{

    public TermCreateDTO termCreateDTO(String term, int order) {
        TermCreateDTO termCreateDTO = new TermCreateDTO();
        termCreateDTO.setTerm(term);
        termCreateDTO.setDefinition(term.trim() + " definition");
        termCreateDTO.setOrder(order);
        return termCreateDTO;
    }

    public Term term(String termStr, int order) {
        Term term = new Term();
        term.setTerm(termStr);
        term.setDefinition(termStr + " definition");
        term.setOrder(order);
        return term;
    }

    public TermUpdateDTO termUpdateDTO(TermResponseDTO source, String newTerm) {
        TermUpdateDTO termUpdateDTO = new TermUpdateDTO();
        termUpdateDTO.setId(source.getId());
        termUpdateDTO.setTerm(newTerm);
        termUpdateDTO.setDefinition(source.getDefinition());
        termUpdateDTO.setHint(source.getHint());
        termUpdateDTO.setPicture_url(source.getPicture_url());
        termUpdateDTO.setOrder(source.getOrder());
        return termUpdateDTO;
    }
}
