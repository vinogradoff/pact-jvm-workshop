import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactBroker;
import au.com.dius.pact.provider.junit.loader.PactBrokerAuth;
import au.com.dius.pact.provider.junit5.*;
import de.vinogradoff.example.rest.ApplicationProvider;
import de.vinogradoff.example.rest.Controller;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.SpringApplication;

import java.net.MalformedURLException;
import java.net.URL;

@Provider("weather_service")


/* 
Tasks
configure PactBroker

1. using plain user/pwd
2. using secure  way with system properties

*/
































@PactBroker(host = "comaqa.pact.dius.com.au",port = "443",scheme = "https",
        authentication = @PactBrokerAuth(username = "${pactbroker.auth.username}",
                password = "${pactbroker.auth.password}"))
public class PactProviderVerificationTest {

  @TestTemplate
  @ExtendWith(PactVerificationInvocationContextProvider.class)
  void pactVerificationTestTemplate(PactVerificationContext context) {
    context.verifyInteraction();
  }

  @BeforeAll
  static void setUpService() {
    //Run DB, create schema
    //Run service
    //...
    SpringApplication.run(ApplicationProvider.class, new String[]{});
  }

  @BeforeEach
  void before(PactVerificationContext context) throws MalformedURLException {
    context.setTarget(HttpTestTarget.fromUrl(new URL("http://localhost:8888")));
    // or something like
    // context.setTarget(new HttpTestTarget("localhost", myProviderPort, "/"));
  }

  @State("Sensors are functioning")
  void setupApplicationOK(){
    Controller.offline=false;
  }

  @State("Sensors are down")
  void setupApplication503(){
    Controller.offline=true;
  }

  //@State(value = "Sensors are down", action = StateChangeAction.TEARDOWN)
  void cleanUp503(){
    Controller.offline=false;
  }

}


