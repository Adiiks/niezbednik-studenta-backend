package pl.uam.wmi.niezbednikstudenta.services;

import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import pl.uam.wmi.niezbednikstudenta.entities.Term;
import pl.uam.wmi.niezbednikstudenta.repositories.TermRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TermService {

    private final TermRepository termRepository;

    public TermService(TermRepository termRepository) {
        this.termRepository = termRepository;
    }

    public List<Term> findAll() {
        return termRepository.findAll();
    }

    public Term save(Term term) {
        return termRepository.save(term);
    }

    public Term findById(Long id) throws NotFoundException {
        Optional<Term> term = termRepository.findById(id);
        if (term.isPresent())
            return term.get();
        else
            throw new NotFoundException("Term not found. Wrong id.");
    }
}
