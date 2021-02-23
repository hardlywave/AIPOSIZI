package com.iit.lab2.service;

import com.iit.lab2.persist.entity.Key;
import com.iit.lab2.persist.repo.KeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class KeyService {
    private final KeyRepository keyRepository;

    @Autowired
    public KeyService(KeyRepository keyRepository) {
        this.keyRepository = keyRepository;
    }

    public void create(Key key) {
        Key newKey = new Key();
        newKey.setKey(key.getKey());
        newKey.setGame(key.getGame());
        keyRepository.save(newKey);
    }

    public List<Key> findAll() {
        Iterable<Key> source = keyRepository.findAll();
        List<Key> target = new ArrayList<>();
        source.forEach(target::add);
        return target;
    }

    public void delete(Long id){
        keyRepository.deleteById(id);
    }

    public Optional<Key> findById(Long id){
        return keyRepository.findById(id);
    }

    public void update(Key key){
        keyRepository.save(key);
    }
}
