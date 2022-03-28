package ru.coderiders.BiteMeBee.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.coderiders.BiteMeBee.entity.BeeFamily;
import ru.coderiders.BiteMeBee.entity.Hive;
import ru.coderiders.BiteMeBee.mapper.HiveMapper;
import ru.coderiders.BiteMeBee.mapper.HiveSnapshotMapper;
import ru.coderiders.BiteMeBee.repository.HiveRepository;
import ru.coderiders.BiteMeBee.repository.HiveSnapshotRepository;
import ru.coderiders.BiteMeBee.rest.dto.HiveRqDto;
import ru.coderiders.BiteMeBee.rest.dto.HiveRsDto;
import ru.coderiders.BiteMeBee.rest.exception.BadRequestException;
import ru.coderiders.BiteMeBee.rest.exception.NotFoundException;
import ru.coderiders.BiteMeBee.service.HiveService;
import ru.coderiders.BiteMeBee.utils.BeanUtilsHelper;
import ru.coderiders.Library.rest.dto.HiveSnapshotRqDto;
import ru.coderiders.Library.rest.dto.HiveSnapshotRsDto;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HiveServiceImpl implements HiveService {

    private static final String[] IGNORED_ON_COPY_FIELDS = {"id"};

    private final String HIVE_NOT_FOUND = "Улей с id=%s не найден";
    private final String HIVE_ALREADY_EXISTS = "Улей с названием «%s» уже существует";

    private final HiveRepository hiveRepository;
    private final HiveSnapshotRepository hiveSnapshotRepository;
    private final HiveMapper hiveMapper;
    private final HiveSnapshotMapper hiveSnapshotMapper;

    @Override
    public List<HiveSnapshotRsDto> getSnapshots(@NonNull HiveSnapshotRqDto hiveSnapshotRqDto) {
        log.debug("Запрос на получение всех снимков улья за период, hiveSnapshotRqDto = {}", hiveSnapshotRqDto);

        Long hiveId = hiveSnapshotRqDto.getHiveId();
        Instant dateFrom = hiveSnapshotRqDto.getDateFrom();
        Instant dateTo = hiveSnapshotRqDto.getDateTo();

        hiveRepository.findById(hiveId)
                .orElseThrow(() -> new NotFoundException(String.format(HIVE_NOT_FOUND, hiveId)));

        return hiveSnapshotRepository.findByCreatedAtBetweenAndHive_Id(dateFrom, dateTo, hiveId)
                .stream()
                .map(hiveSnapshotMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Page<HiveRsDto> getAll(@NonNull Pageable pageable) {
        log.debug("Запрос на получение всех ульев, pageable = {}", pageable);

        return hiveRepository.findAll(pageable)
                .map(hiveMapper::toDto);
    }

    @Override
    @Transactional
    public HiveRsDto getById(@NonNull Long id) {
        log.debug("Запрос на получение улья по id = {}", id);

        return hiveRepository.findById(id)
                .map(hiveMapper::toDto)
                .orElseThrow(() -> new NotFoundException(String.format(HIVE_NOT_FOUND, id)));
    }

    @Override
    @Transactional
    public HiveRsDto create(@NonNull HiveRqDto hiveRqDto) {
        log.debug("Запрос на создание нового улья, HiveRqDto = {}", hiveRqDto);

        var hiveName = hiveRqDto.getName();
        hiveRepository.findByName(hiveName)
                .ifPresent(found -> {
                    throw new BadRequestException(String.format(HIVE_ALREADY_EXISTS, hiveName));
                });

        Hive toCreate = hiveMapper.toEntity(hiveRqDto);
        Hive created = hiveRepository.save(toCreate);
        return hiveMapper.toDto(created);
    }

    @Override
    @Transactional
    public HiveRsDto update(@NonNull Long id, @NonNull HiveRqDto hiveRqDto) {
        log.debug("Запрос на обновление улья, HiveRqDto = {}", hiveRqDto);

        return hiveRepository.findById(id)
                .map(found -> {
                    var update = hiveMapper.toEntity(hiveRqDto);
                    BeanUtils.copyProperties(update, found,
                            BeanUtilsHelper.getNullPropertyNames(update, IGNORED_ON_COPY_FIELDS));
                    return found;
                })
                .map(hiveMapper::toDto)
                .orElseThrow(() -> new NotFoundException(String.format(HIVE_NOT_FOUND, id)));
    }

    @Override
    @Transactional
    public void deleteById(@NonNull Long id) {
        log.debug("Запрос на удаление улья по id = {}", id);

        hiveRepository.findById(id)
                .ifPresent(hiveRepository::delete);
    }

    @Override
    @Transactional
    public boolean isOccupied(@NonNull Long id) {
        log.debug("Проверяем, является ли улей занятым живой пчелиной семьёй, id = {}", id);

        return hiveRepository.findById(id)
                .map(hive -> hive.getBeeFamilies().stream().anyMatch(BeeFamily::getIsAlive))
                .orElseThrow(() -> new NotFoundException(String.format(HIVE_NOT_FOUND, id)));
    }
}