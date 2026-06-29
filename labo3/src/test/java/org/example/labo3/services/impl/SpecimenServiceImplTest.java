package org.example.labo3.services.impl;

import org.example.labo3.common.mappers.SpecimenMapper;
import org.example.labo3.domain.dto.request.CreateSpecimenRequest;
import org.example.labo3.domain.dto.request.UpdateSpecimenRequest;
import org.example.labo3.domain.dto.response.PageableResponse;
import org.example.labo3.domain.dto.response.SpecimenResponse;
import org.example.labo3.domain.entities.Specimen;
import org.example.labo3.exceptions.ResourceNotFoundException;
import org.example.labo3.repositories.SpecimenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpecimenServiceImplTest {

    @Mock
    private SpecimenRepository specimenRepository;

    @Mock
    private SpecimenMapper specimenMapper;

    @InjectMocks
    private SpecimenServiceImpl specimenService;

    private UUID specimenId;
    private CreateSpecimenRequest createRequest;
    private UpdateSpecimenRequest updateRequest;
    private Specimen specimenEntity;
    private SpecimenResponse specimenResponse;

    @BeforeEach
    void setUp() {
        specimenId = UUID.randomUUID();

        createRequest = CreateSpecimenRequest.builder()
                .name("Bokoblin")
                .region("Hyrule Field")
                .dangerLevel(3)
                .isFriendly(false)
                .build();

        updateRequest = UpdateSpecimenRequest.builder()
                .name("Blue Bokoblin")
                .region("Akkala")
                .dangerLevel(5)
                .isFriendly(false)
                .build();

        specimenEntity = Specimen.builder()
                .id(specimenId)
                .name("Bokoblin")
                .region("Hyrule Field")
                .dangerLevel(3)
                .isFriendly(false)
                .build();

        specimenResponse = SpecimenResponse.builder()
                .id(specimenId)
                .name("Bokoblin")
                .region("Hyrule Field")
                .dangerLevel(3)
                .isFriendly(false)
                .build();
    }

    @Test
    void createSpecimen_shouldMapSaveAndReturnDto() {
        when(specimenMapper.toEntityCreate(createRequest)).thenReturn(specimenEntity);
        when(specimenRepository.save(specimenEntity)).thenReturn(specimenEntity);
        when(specimenMapper.toDto(specimenEntity)).thenReturn(specimenResponse);

        SpecimenResponse result = specimenService.createSpecimen(createRequest);

        assertThat(result).isEqualTo(specimenResponse);
        verify(specimenRepository).save(specimenEntity);
    }

    @Test
    void getSpecimenById_shouldReturnDto_whenSpecimenExists() {
        when(specimenRepository.findById(specimenId)).thenReturn(Optional.of(specimenEntity));
        when(specimenMapper.toDto(specimenEntity)).thenReturn(specimenResponse);

        SpecimenResponse result = specimenService.getSpecimenById(specimenId);

        assertThat(result).isEqualTo(specimenResponse);
    }

    @Test
    void getSpecimenById_shouldThrow_whenSpecimenNotFound() {
        when(specimenRepository.findById(specimenId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> specimenService.getSpecimenById(specimenId))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(specimenMapper, never()).toDto(any());
    }

    @Test
    void getAllSpecimens_shouldReturnPageable_whenDataExists() {
        Page<Specimen> page = new PageImpl<>(List.of(specimenEntity));
        PageableResponse pageableResponse = PageableResponse.builder()
                .content(List.of(specimenResponse))
                .currentPage(0)
                .totalPages(1)
                .totalElements(1)
                .pageSize(1)
                .isFirst(true)
                .isLast(true)
                .build();

        when(specimenRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(specimenMapper.toPageableResponse(page)).thenReturn(pageableResponse);

        PageableResponse result = specimenService.getAllSpecimens(0, 10, "name", "asc");

        assertThat(result).isEqualTo(pageableResponse);
        verify(specimenRepository).findAll(any(Pageable.class));
    }

    @Test
    void getAllSpecimens_shouldThrow_whenNoData() {
        when(specimenRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());

        assertThatThrownBy(() -> specimenService.getAllSpecimens(0, 10, "name", "asc"))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(specimenMapper, never()).toPageableResponse(any());
    }

    @Test
    void updateSpecimen_shouldUpdateAndReturnDto_whenSpecimenExists() {
        Specimen updatedEntity = Specimen.builder()
                .id(specimenId)
                .name("Blue Bokoblin")
                .region("Akkala")
                .dangerLevel(5)
                .isFriendly(false)
                .build();

        SpecimenResponse updatedResponse = SpecimenResponse.builder()
                .id(specimenId)
                .name("Blue Bokoblin")
                .region("Akkala")
                .dangerLevel(5)
                .isFriendly(false)
                .build();

        when(specimenRepository.findById(specimenId)).thenReturn(Optional.of(specimenEntity));
        when(specimenMapper.toDto(specimenEntity)).thenReturn(specimenResponse);
        when(specimenMapper.toEntityUpdate(updateRequest, specimenId)).thenReturn(updatedEntity);
        when(specimenRepository.save(updatedEntity)).thenReturn(updatedEntity);
        when(specimenMapper.toDto(updatedEntity)).thenReturn(updatedResponse);

        SpecimenResponse result = specimenService.updateSpecimen(specimenId, updateRequest);

        assertThat(result).isEqualTo(updatedResponse);
        verify(specimenRepository).save(updatedEntity);
    }

    @Test
    void deleteSpecimen_shouldDeleteAndReturnDto_whenSpecimenExists() {
        when(specimenRepository.findById(specimenId)).thenReturn(Optional.of(specimenEntity));
        when(specimenMapper.toDto(specimenEntity)).thenReturn(specimenResponse);

        SpecimenResponse result = specimenService.deleteSpecimen(specimenId);

        assertThat(result).isEqualTo(specimenResponse);
        verify(specimenRepository).deleteById(specimenId);
    }

    @Test
    void deleteSpecimen_shouldThrow_whenSpecimenNotFound() {
        when(specimenRepository.findById(specimenId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> specimenService.deleteSpecimen(specimenId))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(specimenRepository, never()).deleteById(any());
    }
}