package com.iit.lab2.service;

import com.iit.lab2.persist.entity.Game;
import com.iit.lab2.persist.entity.Key;
import com.iit.lab2.persist.repo.KeyRepository;
import com.iit.lab2.repr.KeyRepr;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class KeyService {
    private final KeyRepository keyRepository;

    @Autowired
    public KeyService(KeyRepository keyRepository) {
        this.keyRepository = keyRepository;
    }

    public void create(KeyRepr key) {
        Key newKey = new Key();
        newKey.setKey(key.getKey());
        newKey.setGame(key.getGame());
        keyRepository.save(newKey);
        log.info("Key was created");
    }

    public List<Key> findAll() {
        Iterable<Key> source = keyRepository.findAll();
        List<Key> target = new ArrayList<>();
        source.forEach(target::add);
        log.info("{} addresses was found", target.size());
        return target;
    }

    public void delete(Long id){
        keyRepository.deleteById(id);
        log.info("The key has been removed");
    }

    public Optional<Key> findById(Long id){
        Optional<Key> key = keyRepository.findById(id);
        if(key.isPresent()){
            log.info("Key with id {} was found", id);
        }else {
            log.info("Key with id {} wasn't found", id);
        }
        return key;
    }

    public void update(KeyRepr key){
        Key newKey = new Key();
        newKey.setId(key.getId());
        newKey.setKey(key.getKey());
        newKey.setGame(key.getGame());
        keyRepository.save(newKey);
        log.info("The key has been updated");
    }
}
