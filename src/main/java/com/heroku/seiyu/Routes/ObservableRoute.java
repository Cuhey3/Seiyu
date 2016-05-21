package com.heroku.seiyu.Routes;

import com.heroku.seiyu.source.Aliases;
import com.heroku.seiyu.source.ObservableSource;
import com.heroku.seiyu.source.SourceMap;
import java.util.List;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.rx.ReactiveCamel;
import rx.Observable;
import rx.observables.ConnectableObservable;

public abstract class ObservableRoute extends RouteBuilder {

  protected String sourceAddress = Aliases.name(this);
  protected String start_route = "direct:observable_" + sourceAddress + "_start";
  protected String end_route = "direct:observable_" + sourceAddress + "_end";
  protected ObservableSource observableSource;

  public final void sendReceiveObservable(Observable sendObservable, SourceMap sourceMap) {
    new ReactiveCamel(sourceMap.getContext()).sendTo(sendObservable, start_route);
    receiveObservable(sourceMap);
  }

  public final void receiveObservable(SourceMap sourceMap) {
    ConnectableObservable observable = new ReactiveCamel(sourceMap.getContext()).toObservable(end_route, Object.class).publish();
    observable.connect();
    observableSource = new ObservableSource(observable);
    putObservableSourceTo(sourceMap);
  }

  @Override
  public void configure() throws Exception {
    from(start_route)
            .to("log:" + this.sourceAddress)
            .setHeader("sourceAddress", constant(sourceAddress))
            .process((exchange) -> {
              List body = exchange.getIn().getBody(List.class);
              if (body != null) {
                System.out.println(this.sourceAddress + " size is..." + body.size());
              }
            })
            .to(end_route)
            .to("direct:snapshot");
  }

  public void setSourceAddress(String sourceAddress) {
    this.sourceAddress = sourceAddress;
    this.start_route = "direct:observable_" + sourceAddress + "_start";
    this.end_route = "direct:observable_" + sourceAddress + "_end";
  }

  public void putObservableSourceTo(SourceMap sourceMap) {
    sourceMap.put(this.sourceAddress, observableSource);
  }

  public ObservableSource getObservableSource() {
    return this.observableSource;
  }
}
