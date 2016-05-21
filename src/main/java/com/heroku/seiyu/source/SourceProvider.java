package com.heroku.seiyu.source;

import com.heroku.seiyu.Routes.SourceInitializer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SourceProvider {

  private final SourceMap sourceMap;
  private final Aliases aliases;
  private final LinkedHashMap<String, Object> sourceObjects = new LinkedHashMap<>();

  @Autowired
  public SourceProvider(SourceMap sourceMap, SourceInitializer sourceInitializer, Aliases aliases) {
    this.sourceMap = sourceMap;
    this.aliases = aliases;
  }

  public ObservableSource getSource(String key) {
    if (sourceMap.containsKey(key)) {
      return sourceMap.get(key);
    } else if (aliases.containsKey(key)) {
      return sourceMap.get(aliases.get(key));
    } else {
      return null;
    }
  }

  public void setSource(String key, ObservableSource source) {
    sourceMap.put(key, source);
  }

  public Object getSourceObject(String key) {
    if (sourceObjects.containsKey(key)) {
      return sourceObjects.get(key);
    } else if (aliases.containsKey(key)) {
      return sourceObjects.get(aliases.get(key));
    } else {
      return new ArrayList<>();
    }
  }

  public void setSourceObject(String key, Object json) {
    sourceObjects.put(key, json);
  }

  public SourceMap getSourceMap() {
    return sourceMap;
  }

  public Processor setSourceObjectProcessor() {
    return (Exchange exchange) -> {
      String header = exchange.getIn().getHeader("sourceAddress", String.class);
      setSourceObject(header, exchange.getIn().getBody());
    };
  }

  public Processor getSourceObjectProcessor() {
    return (Exchange exchange) -> {
      String sourceName = exchange.getIn().getHeader("sourceName", String.class);
      exchange.getIn().setBody(getSourceObject(sourceName));
    };
  }
}
