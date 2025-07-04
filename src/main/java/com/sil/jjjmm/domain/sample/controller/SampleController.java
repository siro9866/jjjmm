package com.sil.jjjmm.domain.sample.controller;

import com.sil.jjjmm.domain.sample.service.SampleService;
import com.sil.jjjmm.domain.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SampleController {

    private final SampleService sampleService;

    @GetMapping("/sample")
    public String sampleList(Model model, UserDto.Search search) {
        List<UserDto.Response> users = sampleService.findUserAll();
        model.addAttribute("users", users);
        return "sample/sampleList";
    }


}
