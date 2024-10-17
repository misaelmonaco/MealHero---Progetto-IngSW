package it.unisannio.webpagesmanagementservice.controller;

import it.unisannio.webpagesmanagementservice.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @Autowired
    private IndexService indexService;

    @GetMapping
    @RequestMapping("/restaurant/find")
    public String findRestaurant(@RequestParam(value = "name", required = false) String name,
                                 @RequestParam(value = "location", required = false) String location){

        return indexService.findRestaurantsByNameAndLocation(name, location);
    }
}
