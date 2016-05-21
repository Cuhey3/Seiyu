package com.heroku.seiyu.source.combined;

import com.heroku.seiyu.Routes.ObservableRoute;
import com.heroku.seiyu.source.SourceProvider;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rx.Observable;
import rx.observables.ConnectableObservable;

@Component
public class SeiyuCategoryMemberAndIncludeTemplate extends ObservableRoute {

  @Autowired
  public SeiyuCategoryMemberAndIncludeTemplate(SourceProvider sourceProvider, SeiyuCategoryMembers seiyuCategoryMembers) {
    ConnectableObservable<Set> set = sourceProvider.getSource("seiyu_template_include_pages").byAttrSet("title");
    ConnectableObservable<List<Map<String, Object>>> list = seiyuCategoryMembers.getObservableSource().byMapList();
    Observable<List<Map<String, Object>>> combineLatest = Observable.combineLatest(set, list, (s, l) -> {
      List<Map<String, Object>> collect = l.stream().filter((map) -> s.contains(map.get("title"))).collect(Collectors.toList());
      return collect;
    });
    sendReceiveObservable(combineLatest, sourceProvider.getSourceMap());
  }
}
