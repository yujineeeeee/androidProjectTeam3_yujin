package com.bitc.springapp.controller;

import com.bitc.springapp.dto.KeepDto;
import com.bitc.springapp.service.KeepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KeepController {

    @Autowired
    private KeepService keepService;

    @PostMapping("/keepInsert")
    @ResponseBody
    public int keepInsert(@RequestBody KeepDto keep) throws Exception{
        keepService.keepInsert(keep);


        return 1;
    }
}
