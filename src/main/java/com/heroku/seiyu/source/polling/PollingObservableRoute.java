package com.heroku.seiyu.source.polling;

import com.heroku.seiyu.Routes.ObservableRoute;
import com.heroku.seiyu.source.SourceMap;
import org.apache.camel.Processor;

public class PollingObservableRoute extends ObservableRoute {

  String periodExpression;
  Processor processor;

  public PollingObservableRoute(String sourceName, String periodExpression, Processor processor, SourceMap sourceMap) {
    super.setSourceAddress(sourceName);
    this.periodExpression = periodExpression;
    this.processor = processor;
    receiveObservable(sourceMap);
  }

  @Override
  public void configure() throws Exception {
    fromF("timer:%s?period=%s", sourceAddress, periodExpression)
            .setHeader("sourceAddress", constant(sourceAddress))
            .process(processor)
            .to(end_route)
            .to("direct:snapshot");
  }
}
