import de.vinogradoff.pact101.consumer.rest.Controller;
import de.vinogradoff.pact101.consumer.rest.Weather;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static org.assertj.core.api.Assertions.assertThat;

public class IntegrationTest {

 // Task. Write an end2end test for news portal

 @Test
 void newsPortalShouldReturn(){
  // e2e Test
  Weather weather=get("http://localhost:8080/newsportal/getWeather?city=Minsk")
          .then()
          .statusCode(200)
          .extract().as(Weather.class);

  assertThat(weather.getCity()).isEqualTo("Minsk");
  assertThat(weather.getTemperature()).isGreaterThan(0);

 }

 // Task. Write direct test for weather service

 @Test
 void weatherServiceShouldReturn(){
  Weather weather=get("http://localhost:8888/weather/now?city=Boston")
          .then()
          .statusCode(200)
          .extract().as(Weather.class);

  assertThat(weather.getCity()).isEqualTo("Boston");
  assertThat(weather.getTemperature()).isGreaterThan(0);
 }

 // Task. Write a test for the method of consumer's controller

 @Test
 void consumerShouldReturn(){
  Controller ctrl=new Controller();
  Weather weather=  ctrl.weatherNow("Moscow");
  assertThat(weather.getCity()).isEqualTo("Moscow");
  assertThat(weather.getTemperature()).isGreaterThan(0);
 }

}
