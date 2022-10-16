package com.baeldung.testcontainers;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.platform.commons.annotation.Testable;
import org.testcontainers.containers.GenericContainer;

@Testable
public class GenericContainerLiveTest {
  
  /** [N]:test-containers - We construct a GenericContainer test rule by specifying a docker image name. Then, we configure it with builder methods.<p/>
   * N]:test-containers - The {@code @ClassRule} annotation defines a container rule. It starts the Docker container before any test in that class runs. The container will be destroyed after all methods are executed.<p/>
   * N]:test-containers - If you apply {@code @Rule} annotation, {@code GenericContainer} rule will start a new container for each test method. And it will stop the container when that test method finishes. 
   */
  @ClassRule
    public static GenericContainer simpleWebServer =
      new GenericContainer("alpine:3.2")
        // Exposes a port from the container.
        .withExposedPorts(80)
        // Defines a container command. It will be executed when the container starts.
        .withCommand("/bin/sh", "-c", "while true; do echo "
          + "\"HTTP/1.1 200 OK\n\nHello World!\" | nc -l -p 80; done");

    @Test
    public void givenSimpleWebServerContainer_whenGetReuqest_thenReturnsResponse()
      throws Exception {

        // We can use IP address and port to communicate with the process running in the container.
        String address = "http://" 
          + simpleWebServer.getContainerIpAddress() 
          + ":" + simpleWebServer.getMappedPort(80);
        String response = simpleGetRequest(address);
        
        assertEquals(response, "Hello World!");
    }

    private String simpleGetRequest(String address) throws Exception {
        URL url = new URL(address);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(
          new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        return content.toString();
    }
}
