package com.heroku.seiyu.Routes;

import com.heroku.seiyu.source.SourceDefinition;
import com.heroku.seiyu.source.SourceMap;
import com.heroku.seiyu.source.polling.definition.CommonMediawikiApiSourceDefinition;
import com.heroku.seiyu.source.polling.definition.ExternalSourceDefinition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SourceInitializer {

  @Autowired
  public SourceInitializer(SourceMap sourceMap) throws Exception {
    for (SourceDefinition definition : new PollingSourceDefinitions().getDefinitions()) {
      ObservableRoute observableRoute = definition.createObservableRoute(sourceMap);
      sourceMap.getContext().addRoutes(observableRoute);
    }
  }
}

class PollingSourceDefinitions {

  List<SourceDefinition> definitions = new ArrayList<>();

  public PollingSourceDefinitions() {
    definitions.addAll(Arrays.asList(CommonMediawikiApiSourceDefinition.values()));
    definitions.addAll(Arrays.asList(ExternalSourceDefinition.values()));
  }

  public List<SourceDefinition> getDefinitions() {
    return this.definitions;
  }
}
