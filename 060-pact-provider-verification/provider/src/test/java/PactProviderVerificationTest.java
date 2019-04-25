import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit5.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import java.net.MalformedURLException;
import java.net.URL;

@Provider("weather_service")
@PactFolder("../consumer/build/pact-files")
public class PactProviderVerificationTest {

  @TestTemplate
  @ExtendWith(PactVerificationInvocationContextProvider.class)
  void pactVerificationTestTemplate(PactVerificationContext context) {
    context.verifyInteraction();
  }


  @BeforeEach
  void before(PactVerificationContext context) throws MalformedURLException {
    context.setTarget(HttpTestTarget.fromUrl(new URL("http://localhost:8888")));
    // or something like
    // context.setTarget(new HttpTestTarget("localhost", myProviderPort, "/"));
  }


}


