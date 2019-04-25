import au.com.dius.pact.consumer.*;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.model.RequestResponsePact;
import de.vinogradoff.pact101.consumer.rest.Controller;
import de.vinogradoff.pact101.consumer.rest.Weather;
import io.pactfoundation.consumer.dsl.LambdaDsl;
import org.apache.commons.collections4.MapUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.web.client.HttpServerErrorException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(PactConsumerTestExt.class)
@PactFolder("build/pact-files")
public class PactConsumerWeatherTest {

  Map<String, String> headers = MapUtils.putAll(new HashMap<String, String>(),
          new String[]{"Content-Type", "application/json"});

  /*

Tasks

Create two pact interatcion for different states: "Sensor are functioning" and "Sensors are down"

1. add second  pact method
2. use given in both pact methods
3. add second test for consumer method
4. annotate test methods with @PactTestFor(pactMethod)

  */




























  @Pact(provider="weather_service", consumer="news_portal")
  public RequestResponsePact createPact(PactDslWithProvider builder) {

    return builder
            .given("Sensors are functioning")
            .uponReceiving("Request the weather conditions for city")
            .path("/weather/now")
            .method("GET")
            .matchQuery("city", ".*", "Moscow")
            .willRespondWith()
            .headers(headers)
            .status(200)
            .body( //new PactDslJsonBody()
                    //.stringType("city", "Moscow")
                    //.decimalType("temperature", 29.0d)
                    //.decimalType("money", 25d)
                    LambdaDsl.newJsonBody((o) -> {
                      o.stringType("city","Moscow");
                      o.decimalType("temperature",29.2d);
                    }).build()
            )
            .toPact();
  }

  @Pact(provider="weather_service", consumer="news_portal")
  public RequestResponsePact createPactServerDown(PactDslWithProvider builder) {

    return builder
            .given("Sensors are down")
            .uponReceiving("Request the weather conditions for location")
            .path("/weather/now")
            .method("GET")
            .matchQuery("city", ".*", "Moscow")
            .willRespondWith()
            .headers(headers)
            .status(503)
            .toPact();
  }


  /* this just creates a pact file */
  //@Test
  void runMock(MockServer mockServer) throws IOException {
    HttpResponse httpResponse = Request.Get(mockServer.getUrl() + "/weather/now?city=London").execute().returnResponse();
    assertThat(httpResponse.getStatusLine().getStatusCode()).isEqualTo(200);
  }


  @Test
  @PactTestFor(pactMethod = "createPact")
  void unitTestConsumerMethod(MockServer mockServer) {
    Controller ctrl=new Controller();
    ctrl.serviceUrl=mockServer.getUrl()+"/weather/now";

    Weather weather=  ctrl.weatherNow("Moscow2");
    assertThat(weather.getCity()).isEqualTo("Moscow");
    assertThat(weather.getTemperature()).isGreaterThan(0);
    assertThat(weather.getHot()).isFalse();

  }

  @Test
  @PactTestFor(pactMethod = "createPactServerDown")
  void unitTestConsumerMethodServerDown(MockServer mockServer) {
    Controller ctrl=new Controller();
    ctrl.serviceUrl=mockServer.getUrl()+"/weather/now";

    assertThatExceptionOfType(HttpServerErrorException.ServiceUnavailable.class)
            .isThrownBy(() ->ctrl.weatherNow("Moscow2"));

  }

}
