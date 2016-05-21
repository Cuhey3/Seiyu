package com.heroku.seiyu.source;

import java.util.LinkedHashMap;
import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SourceMap extends LinkedHashMap<String, ObservableSource> {

  @Autowired
  CamelContext context;

  public CamelContext getContext() {
    return this.context;
  }
}
