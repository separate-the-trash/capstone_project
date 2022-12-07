package com.example.demo.controller;

import com.example.demo.Service.MapService;
import com.example.demo.dto.MapDTO;
import com.example.demo.model.Map;
import com.example.demo.repository.MapRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MapController {

    private final MapService mapService;
    @Autowired
    private MapRepository mapRepository;

    @GetMapping("/map")
    public List<Map> loadMap(){
        return mapService.load();
    }

    @GetMapping("/addMap")
    public String addMap() throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = (JSONArray) jsonParser.parse(new FileReader("json/data.json")); // 이부분이 문제다!
//        JSONArray jsonArray = (JSONArray) jsonObject.clone();
        List<Map> list = new ArrayList<>();
        MapDTO mapDto;
        Map map;


        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject json = (JSONObject) jsonArray.get(i);

            String address = (String) json.get("address");
            double x = (double) json.get("X");
            double y = (double) json.get("Y");

            mapDto = new MapDTO(address, x, y);

            map = new Map(mapDto);
            mapRepository.save(map);
        }

        return null;
    }
}