package com.bitc.springapp.controller;

import com.bitc.springapp.dto.DumDTO;
import com.bitc.springapp.dto.KeepDto;
import com.bitc.springapp.service.KeepService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class KeepController {

    @Autowired
    private KeepService keepService;

    @PostMapping("/keepInsert")
    @ResponseBody
    public int keepInsert(@RequestBody KeepDto keep) throws Exception {

        String testId = keep.getKpId();
        ArrayList<DumDTO> test = keepService.selectItemCheck(testId);
        Boolean YN = false;


        for (int i = 0; i < test.size(); i++) {

            String keepCd = test.get(i).getKpCd();
            String keepDumCd = keep.getKpCd();

            if (keepCd.equals(keepDumCd)) {
                YN = true;
            }

        }

        if (YN) {
            keepService.keepUpdate(keep);
        } else {
            keepService.keepInsert(keep);
        }

        return 1;
    }

    @GetMapping("/keepView")
    public List<KeepDto> keepView(HttpServletRequest request) throws Exception{

        String id = request.getParameter("kp_id");

        List<KeepDto> keeps = keepService.SelectKeepView(id);

        return keeps;
    }

    @PostMapping("/keepCntUpdate")
    @ResponseBody
    public List<KeepDto> keepCntUpdate(HttpServletRequest request) throws Exception{

        String id = request.getParameter("kp_id");
        String cd = request.getParameter("kpCd");
        int cnt = Integer.parseInt(request.getParameter("kpCnt"));

        KeepDto keep = new KeepDto();

        keep.setKpCd(cd);
        keep.setKpCnt(cnt);
        keep.setKpId(id);

        keepService.keepCntUpdate(keep);

        List<KeepDto> keeps = keepService.SelectKeepView(id);

        return keeps;
    }

    @PostMapping("/keepDelete")
    @ResponseBody
    public List<KeepDto> keepDelete(HttpServletRequest request) throws Exception{

        String id = request.getParameter("kp_id");
        String cd = request.getParameter("kpCd");

        KeepDto keep = new KeepDto();

        keep.setKpCd(cd);
        keep.setKpId(id);

        keepService.keepDelete(keep);

        List<KeepDto> keeps = keepService.SelectKeepView(id);

        return keeps;
    }

    @PostMapping("/basketInsert")
    @ResponseBody
    public int basketInsert(@RequestBody List<KeepDto> keep) throws Exception{

        for(KeepDto item : keep) {

            keepService.basketInsert(item);
            keepService.keepDelete(item);
        }
        return 1;
    }


}
