package ru.coderiders.Main.service;

import ru.coderiders.Main.rest.dto.BeeFamilyNoteRqDto;
import ru.coderiders.Main.rest.dto.BeeFamilyRqDto;
import ru.coderiders.Main.rest.dto.BeeFamilyRsDto;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BeeFamilyService {

    BeeFamilyRsDto create(@NonNull BeeFamilyRqDto beeFamilyRqDto);

    BeeFamilyRsDto getById(@NonNull Long id);

    Page<BeeFamilyRsDto> getAll(@NonNull Pageable pageable);

    BeeFamilyRsDto update(@NonNull Long id, @NonNull BeeFamilyNoteRqDto beeFamilyNoteRqDto);

    BeeFamilyRsDto release(@NonNull Long id);

    void deleteById(@NonNull Long id);
}