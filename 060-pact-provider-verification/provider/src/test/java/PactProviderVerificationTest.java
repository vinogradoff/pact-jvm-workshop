import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit5.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import java.net.MalformedURLException;
import java.net.URL;


/*

Tasks

For public class PactProviderVerificationTest {}

1. Annotate provide with @Provider and @PactFolder
2. Write @TestTemplate method (PactVerificationContext) @ExtendWith(PactVerificationInvocationContextProvider.class)
3. Write context initialization with provider url in @BeforeEach method(PactVericationContext)


*/




















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


