package com.heroku.seiyu.source;

import com.heroku.seiyu.Routes.ObservableRoute;

public interface SourceDefinition {

  public ObservableRoute createObservableRoute(SourceMap sourceMap);
}
