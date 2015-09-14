package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Landing Application Controller.
 */
@Controller
public class LandingController {

    /**
     * Exemplary Landing Method.
     *
     * @return Response Message
     */
    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.HEAD})
    final String home() {
        return "redirect:docs/index.html";
    }

}
