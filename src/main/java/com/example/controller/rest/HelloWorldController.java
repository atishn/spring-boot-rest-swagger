package com.example.controller.rest;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello World REST Controller.
 */
@RestController
@Api(value = "Hello World PING API", description = "Hello World EndPoint.")
@RequestMapping(HelloWorldController.HELLO)
public class HelloWorldController {


    /**
     * Hello World URL.
     */
    public static final String HELLO = "/hello";


    /**
     * Exemplary Hello World Method.
     *
     * @return Response Message
     */
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.HEAD})
    @ResponseBody
    @ApiOperation(value = "Hello World Endpoint", notes = "Hello World")
    final String home() {
        return "Hello World!";
    }
}
