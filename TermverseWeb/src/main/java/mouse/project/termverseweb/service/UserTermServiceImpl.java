package mouse.project.termverseweb.service;

import mouse.project.termverseweb.dto.progress.TermProgressResponseDTO;
import mouse.project.termverseweb.dto.progress.TermProgressUpdateDTO;
import mouse.project.termverseweb.lib.service.container.ServiceProviderContainer;
import mouse.project.termverseweb.mapper.TermProgressMapper;
import mouse.project.termverseweb.repository.StudySetRepository;
import mouse.project.termverseweb.repository.UserTermRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserTermServiceImpl implements UserTermService {
    private final StudySetRepository studySetRepository;
    private final UserTermRepository repository;
    private final TermProgressMapper mapper;
    private final ServiceProviderContainer services;
    @Autowired
    public UserTermServiceImpl(StudySetRepository studySetRepository,
                               UserTermRepository repository,
                               TermProgressMapper mapper,
                               ServiceProviderContainer services) {
        this.studySetRepository = studySetRepository;
        this.repository = repository;
        this.mapper = mapper;
        this.services = services;
    }

    @Override
    public TermProgressResponseDTO update(TermProgressUpdateDTO dto) {
        return null;
    }

    @Override
    public TermProgressResponseDTO getById(Long id) {
        return null;
    }

    @Override
    public List<TermProgressResponseDTO> updateAll(List<TermProgressUpdateDTO> dtoList) {
        return null;
    }

    @Override
    public List<TermProgressResponseDTO> getAll() {
        return null;
    }

    @Override
    public List<TermProgressResponseDTO> getForTerms(Long userId, List<Long> termIds) {
        return null;
    }

    @Override
    public List<TermProgressResponseDTO> removeAll(Long userId, List<Long> termIds) {
        return null;
    }

    @Override
    public List<TermProgressResponseDTO> getForStudySet(Long userId, Long studySetId) {
        return null;
    }

    @Override
    public TermProgressResponseDTO save(Long userId, Long studySetId, String progress) {
        return null;
    }
}
