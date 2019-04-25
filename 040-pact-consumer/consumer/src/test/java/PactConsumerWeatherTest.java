import au.com.dius.pact.consumer.*;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.model.RequestResponsePact;
import de.vinogradoff.pact101.consumer.rest.Controller;
import de.vinogradoff.pact101.consumer.rest.Weather;
import org.apache.commons.collections4.MapUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/*

Tasks:

With this class do:

public class PactConsumerWeatherTest {
}

1. Annotate to be used as a pact test
2. Create method for defining Pact method(PactDSLWithProvider builder) returning RequestResponsePact
3.  Write unit test on on consumer method testmethod(MockServer mockServer)
4. (optional) write test method using plain HttpResponse/Request classes that creates pact, without testing consumer method

*/





























@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "weather_service")
@PactFolder("build/pact-files")
public class PactConsumerWeatherTest {

  Map<String, String> headers = MapUtils.putAll(new HashMap<String, String>(),
          new String[]{"Content-Type", "application/json"});

  @Pact(provider="weather_service", consumer="news_portal")
  public RequestResponsePact createPact(PactDslWithProvider builder) {

    return builder
            .uponReceiving("Request the weather conditions for city")
            .path("/weather/now")
            .method("GET")
            .matchQuery("city", ".*", "Moscow")
            .willRespondWith()
            .headers(headers)
            .status(200)
            .body( "{\"city\":\"Berlin\"," +
                    "\"temperature\":23.4}"
            )
            .toPact();
  }



  /* this just creates a pact file */
  //@Test
  void runMock(MockServer mockServer) throws IOException {
    HttpResponse httpResponse = Request.Get(mockServer.getUrl() + "/weather/now?city=London").execute().returnResponse();
    assertThat(httpResponse.getStatusLine().getStatusCode()).isEqualTo(200);
  }


  @Test
  void unitTestConsumerMethod(MockServer mockServer) {
    Controller ctrl=new Controller();
    ctrl.serviceUrl=mockServer.getUrl()+"/weather/now";
    Weather weather=  ctrl.weatherNow("Moscow2");
    assertThat(weather.getCity()).isEqualTo("Berlin");
    assertThat(weather.getTemperature()).isGreaterThan(0);
    assertThat(weather.getHot()).isFalse();

  }

}
