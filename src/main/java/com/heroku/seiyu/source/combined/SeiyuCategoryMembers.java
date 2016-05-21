package com.heroku.seiyu.source.combined;

import com.heroku.seiyu.Routes.ObservableRoute;
import com.heroku.seiyu.source.SourceProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rx.Observable;
import rx.observables.ConnectableObservable;

@Component
public class SeiyuCategoryMembers extends ObservableRoute {

  @Autowired
  public SeiyuCategoryMembers(SourceProvider sourceProvider) {
    ConnectableObservable<List<Map<String, Object>>> list1 = sourceProvider.getSource("male_seiyu_category_members").byMapList();
    ConnectableObservable<List<Map<String, Object>>> list2 = sourceProvider.getSource("female_seiyu_category_members").byMapList();
    Observable<List<Map<String, Object>>> combineLatest = Observable.combineLatest(list1, list2, (l1, l2) -> {
      List<Map<String, Object>> al = new ArrayList<>();
      al.addAll(l1);
      al.addAll(l2);
      return al;
    });
    sendReceiveObservable(combineLatest, sourceProvider.getSourceMap());
  }
}
