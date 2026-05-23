package org.example.labo3.services.impl;

import org.example.labo3.common.mappers.SpecimenMapper;
import org.example.labo3.domain.dto.request.CreateSpecimenRequest;
import org.example.labo3.domain.dto.request.UpdateSpecimenRequest;
import org.example.labo3.domain.dto.response.PageableResponse;
import org.example.labo3.domain.dto.response.SpecimenResponse;
import org.example.labo3.domain.entities.Specimen;
import org.example.labo3.exceptions.ResourceNotFoundException;
import org.example.labo3.repositories.SpecimenRepository;
import org.example.labo3.services.SpecimenService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SpecimenServiceImpl implements SpecimenService {

    private final SpecimenRepository specimenRepository;
    private final SpecimenMapper specimenMapper;

    @Override
    @Transactional
    public SpecimenResponse createSpecimen(CreateSpecimenRequest request) {
        return specimenMapper.toDto(
                specimenRepository.save(specimenMapper.toEntityCreate(request))
        );
    }

    @Override
    public PageableResponse getAllSpecimens(int page, int size, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Specimen> specimenPage = specimenRepository.findAll(pageable);

        if (specimenPage.isEmpty()) {
            throw new ResourceNotFoundException("No specimens are registered in Hyrule");
        }

        return specimenMapper.toPageableResponse(specimenPage);
    }

    @Override
    public SpecimenResponse getSpecimenById(UUID id) {
        return specimenMapper.toDto(
                specimenRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Specimen not found in Sheikah Slate records"))
        );
    }

    @Override
    @Transactional
    public SpecimenResponse updateSpecimen(UUID id, UpdateSpecimenRequest request) {
        this.getSpecimenById(id);
        return specimenMapper.toDto(
                specimenRepository.save(specimenMapper.toEntityUpdate(request, id))
        );
    }

    @Override
    @Transactional
    public SpecimenResponse deleteSpecimen(UUID id) {
        SpecimenResponse existSpecimen = this.getSpecimenById(id);
        specimenRepository.deleteById(id);
        return existSpecimen;
    }
}