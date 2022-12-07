package com.example.demo.Service;

import com.example.demo.model.Map;
import com.example.demo.repository.MapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MapService {

    private final MapRepository mapRepository;

    @Transactional
    public List<Map> load(){

        return mapRepository.findAll();
    }
}