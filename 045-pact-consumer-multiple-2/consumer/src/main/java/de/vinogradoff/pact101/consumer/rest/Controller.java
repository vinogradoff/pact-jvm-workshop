package de.vinogradoff.pact101.consumer.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class Controller {

  private RestTemplate rest;

  public Controller(){
    rest=new RestTemplate();
  }

  public Controller(RestTemplate rest){
    this.rest=rest;
  }

  @Value("${weather.service}")
  public String serviceUrl;

  @RequestMapping(value = "/newsportal/getWeather", method = RequestMethod.GET)
  public Weather weatherNow(@RequestParam(value = "city",defaultValue = "Moscow") String city){
    Weather weather= rest.getForObject(serviceUrl+"?city="+city,Weather.class);
    if (weather.getTemperature()>=30) weather.setHot(true); else weather.setHot(false);
    return weather;
  }



}
