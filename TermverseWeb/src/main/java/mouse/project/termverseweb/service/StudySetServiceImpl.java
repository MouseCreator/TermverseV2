package mouse.project.termverseweb.service;

import jakarta.transaction.Transactional;
import mouse.project.lib.data.page.PageDescription;
import mouse.project.lib.data.page.PageDescriptionImpl;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.termverseweb.dto.data.StudySetSearchParams;
import mouse.project.termverseweb.dto.studyset.*;

import mouse.project.termverseweb.lib.service.container.ServiceProviderContainer;
import mouse.project.termverseweb.mapper.StudySetMapper;
import mouse.project.termverseweb.model.StudySet;
import mouse.project.termverseweb.repository.StudySetRepository;
import mouse.project.termverseweb.service.search.StudySetSearch;
import mouse.project.termverseweb.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@mouse.project.lib.ioc.annotation.Service
public class StudySetServiceImpl implements StudySetService {
    private final StudySetRepository repository;
    private final StudySetMapper studySetMapper;
    private final ServiceProviderContainer services;
    private final StudySetSearch studySetSearch;
    @Auto
    @Autowired
    public StudySetServiceImpl(StudySetRepository repository, StudySetMapper studySetMapper, ServiceProviderContainer services,
                               StudySetSearch studySetSearch) {
        this.repository = repository;
        this.studySetMapper = studySetMapper;
        this.services = services;
        this.studySetSearch = studySetSearch;
    }

    @Override
    public List<StudySetResponseDTO> findAll() {
        return services.crud(repository)
                .findAll()
                .to(studySetMapper::toResponse);
    }

    private void assignTime(StudySet studySet) {
        LocalDateTime timeNow = DateUtils.timeNowToSeconds();
        studySet.setCreatedAt(timeNow);
    }

    @Override
    public List<StudySetResponseDTO> findAllIncludeDeleted() {
        return services.soft(repository).findAllWithDeleted().to(studySetMapper::toResponse);
    }

    @Override
    public List<StudySetResponseDTO> findAllByNameIgnoreCase(String name) {
        return services.use(repository).multi(r -> r.findAllByNameIgnoreCase(name)).to(studySetMapper::toResponse);
    }

    @Override
    public List<StudySetResponseDTO> findAllByCreatedDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return services.
                use(repository).
                multi(r -> r.findAllByCreatedDateRange(startDate, endDate)).
                to(studySetMapper::toResponse);
    }

    @Override
    public void deleteById(Long id) {
        services.crud(repository).removeById(id);
    }

    @Override
    public void restoreById(Long id) {
        services.soft(repository).restoreById(id);
    }

    @Override
    public StudySetResponseDTO save(StudySetCreateDTO dto) {
        return services.crud(repository).save(() -> {
            StudySet studySet = studySetMapper.fromCreate(dto);
            assignTime(studySet);
            return studySet;
        }).to(studySetMapper::toResponse);
    }

    @Override
    public StudySetResponseDTO update(StudySetUpdateDTO model) {
        return services.crud(repository).update(model, studySetMapper::fromUpdate).to(studySetMapper::toResponse);
    }

    @Override
    public StudySetResponseDTO findById(Long id) {
        return services.crud(repository).findById(id).orThrow(studySetMapper::toResponse);
    }

    @Override
    public StudySetResponseDTO findByIdIncludeDeleted(Long id) {
        return services.soft(repository).getByIdIncludeDeleted(id).orThrow(studySetMapper::toResponse);
    }

    @Override
    public List<StudySetResponseDTO> findStudySetsByUser(Long userId) {
        return services.use(repository).multi(r -> r.findAllByUserId(userId)).to(studySetMapper::toResponse);
    }

    @Override
    public StudySetResponseDTO saveWithCustomTime(StudySetCreateDTO createDTO, LocalDateTime customTime) {
        return services.crud(repository).save(() -> {
            StudySet studySet = studySetMapper.fromCreate(createDTO);
            studySet.setCreatedAt(DateUtils.toSeconds(customTime));
            return studySet;
        }).to(studySetMapper::toResponse);
    }

    @Override
    @Transactional
    public StudySetWithTermsResponseDTO findByIdWithTerms(Long id) {
        return services.use(repository)
                .optional(r -> r.findAllByIdWithTerms(id))
                .orThrow(studySetMapper::toResponseWithTerms);
    }

    @Override
    public List<StudySetWithOwnerDTO> findAllBySearchParams(StudySetSearchParams searchParams) {
        PageDescription pageDescription = new PageDescriptionImpl(searchParams.getPageNumber(), searchParams.getPageSize());
        String query = searchParams.getSearchParam();
        String category = searchParams.getCategory();
        Long userId = searchParams.getUserId();
        return studySetSearch.search(category, query, userId, pageDescription);
    }
}
