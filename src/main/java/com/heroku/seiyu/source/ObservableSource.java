package com.heroku.seiyu.source;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import rx.observables.ConnectableObservable;

public class ObservableSource {

  public ObservableSource(ConnectableObservable observable) {
    this.observable = observable;
  }

  protected ConnectableObservable observable;

  public final ConnectableObservable<List<Map<String, Object>>> byMapList() {
    return (ConnectableObservable<List<Map<String, Object>>>) observable;
  }

  public final ConnectableObservable<Set<String>> byStringSet() {
    return (ConnectableObservable<Set<String>>) observable;
  }

  public final ConnectableObservable<Set> byAttrSet(String attr) {
    ConnectableObservable publish = observable.map((list) -> {
      return ((List<Map>) list).stream()
              .map((map) -> map.get(attr))
              .collect(Collectors.toSet());
    }).publish();
    publish.connect();
    return (ConnectableObservable<Set>) publish;
  }
}
