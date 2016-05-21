package com.heroku.seiyu.Routes;

import com.heroku.seiyu.source.SourceProvider;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyRoute extends RouteBuilder {

  @Autowired
  SourceProvider sourceProvider;

  public static Map<String, Object> staticResourceMap = new LinkedHashMap<>();
  public static final String PORT = System.getenv("PORT") == null ? "2525" : System.getenv("PORT");

  @Override
  public void configure() throws Exception {
    restConfiguration().component("jetty")
            .bindingMode(RestBindingMode.json)
            .enableCORS(true)
            .dataFormatProperty("prettyPrint", "true")
            .port(PORT);
    from("jetty:http://0.0.0.0:" + PORT + "/static/?matchOnUriPrefix=true")
            .process((Exchange exchange) -> {
              String path = exchange.getIn().getHeader(Exchange.HTTP_PATH, String.class);
              exchange.getIn().setBody(staticResourceMap.get(path));
            });
    from("file:public?recursive=true&noop=true")
            .process((Exchange exchange) -> {
              String camelFileName = exchange.getIn().getHeader(Exchange.FILE_NAME, String.class).replace("\\", "/");
              staticResourceMap.put(camelFileName, exchange.getIn().getBody(Object.class));
            });
    rest("/").get("/api/{sourceName}").to("direct:api");
    from("direct:snapshot").process(sourceProvider.setSourceObjectProcessor());
    from("direct:api").process(sourceProvider.getSourceObjectProcessor());
  }
}
