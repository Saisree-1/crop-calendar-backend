package jar.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jar.model.WeatherData;
import jar.repo.WeatherDataRepo;

@RestController
@RequestMapping("/weather")
public class WeatherDataController {

    @Autowired
    private WeatherDataRepo weatherDataRepo;

    @GetMapping
    public List<WeatherData> getWeatherData() {
        return weatherDataRepo.findAll();
    }

    @PostMapping
    public WeatherData createWeatherData(@RequestBody WeatherData weatherData) {
        return weatherDataRepo.save(weatherData);
    }
}