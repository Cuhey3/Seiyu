package com.heroku.seiyu.source.combined;

import com.heroku.seiyu.Routes.ObservableRoute;
import com.heroku.seiyu.source.SourceProvider;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rx.Observable;
import rx.observables.ConnectableObservable;

@Component
public class KoepotaSeiyu extends ObservableRoute {

  @Autowired
  public KoepotaSeiyu(SourceProvider sourceProvider, SeiyuCategoryMemberAndIncludeTemplate seiyu) {
    ConnectableObservable<List<Map<String, Object>>> list1 = sourceProvider.getSource("koepota_events").byMapList();
    ConnectableObservable<List<Map<String, Object>>> list2 = seiyu.getObservableSource().byMapList();
    Observable<List<Map<String, Object>>> combineLatest = Observable.combineLatest(list1, list2, (l1, l2) -> {
      final String l1String = l1.toString();
      return l2.stream().filter((map) -> l1String.contains(((String) map.get("title")).replaceFirst(" \\(.+", ""))).collect(Collectors.toList());
    });
    sendReceiveObservable(combineLatest, sourceProvider.getSourceMap());
  }
}
