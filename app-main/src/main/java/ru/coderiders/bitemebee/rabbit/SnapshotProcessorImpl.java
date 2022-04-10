package ru.coderiders.bitemebee.rabbit;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.coderiders.bitemebee.entity.HiveSnapshot;
import ru.coderiders.bitemebee.service.HiveService;
import ru.coderiders.bitemebee.service.HiveSnapshotService;
import ru.coderiders.commons.rest.dto.HiveSnapshotGeneratorDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class SnapshotProcessorImpl implements SnapshotProcessor {
    private final HiveService hiveService;
    private final HiveSnapshotService hiveSnapshotService;

    @Override
    @Transactional
    public void processHiveSnapshot(@NonNull HiveSnapshotGeneratorDto hiveSnapshotGeneratorDto) {
        Long hiveId = hiveSnapshotGeneratorDto.getHiveId();
        if(!hiveService.hiveExists(hiveId)) {
            log.warn("Не найден улей по идентификатору, id = {}", hiveId);
            return;
        }
        HiveSnapshot hiveSnapshot = hiveSnapshotService.createSnapshot(hiveSnapshotGeneratorDto);
        hiveService.updateHoneyAmount(hiveId, hiveSnapshot.getHoneyIncrease());
    }
}
