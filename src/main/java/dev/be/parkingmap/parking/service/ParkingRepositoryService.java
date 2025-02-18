package dev.be.parkingmap.parking.service;

import dev.be.parkingmap.parking.entity.Parking;
import dev.be.parkingmap.parking.repository.ParkingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParkingRepositoryService {
    private final ParkingRepository parkingRepository;

    // self invocation test
    public void bar(List<Parking> parkingList) {
        log.info("bar CurrentTransactionName: "+ TransactionSynchronizationManager.getCurrentTransactionName());
        foo(parkingList);
    }

    // self invocation test
    @Transactional
    public void foo(List<Parking> parkingList) {
        log.info("foo CurrentTransactionName: "+ TransactionSynchronizationManager.getCurrentTransactionName());
        parkingList.forEach(parking -> {
            parkingRepository.save(parking);
            throw new RuntimeException("error");
        });
    }


    // read only test
    @Transactional(readOnly = true)
    public void startReadOnlyMethod(Long id) {
        parkingRepository.findById(id).ifPresent(parking ->
                parking.changeParkingAddress("인천광역시 부평구"));
    }


    @Transactional
    public List<Parking> saveAll(List<Parking> parkingList) {
        if(CollectionUtils.isEmpty(parkingList)) return Collections.emptyList();
        return parkingRepository.saveAll(parkingList);
    }

    @Transactional
    public void updateAddress(Long id, String address){
        Parking entity = parkingRepository.findById(id).orElse(null);

        if(Objects.isNull(entity)){
            log.error("Parking with id {} not found", id);
            return;
        }

        entity.changeParkingAddress(address);
    }

    public void updateAddressWithoutTransaction(Long id, String address){
        Parking entity = parkingRepository.findById(id).orElse(null);

        if(Objects.isNull(entity)){
            log.error("Parking with id {} not found", id);
            return;
        }

        entity.changeParkingAddress(address);
    }

    @Transactional(readOnly = true)
    public List<Parking> findAll() {
        return parkingRepository.findAll();
    }
}
