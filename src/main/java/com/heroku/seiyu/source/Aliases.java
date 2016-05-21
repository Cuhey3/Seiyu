package com.heroku.seiyu.source;

import com.heroku.seiyu.source.combined.KoepotaSeiyu;
import com.heroku.seiyu.source.combined.SeiyuCategoryMemberAndIncludeTemplate;
import com.heroku.seiyu.source.combined.SeiyuCategoryMembers;
import static com.heroku.seiyu.source.polling.definition.CommonMediawikiApiSourceDefinition.*;
import static com.heroku.seiyu.source.polling.definition.ExternalSourceDefinition.koepota_events;
import java.util.LinkedHashMap;
import org.springframework.stereotype.Component;

@Component
public final class Aliases {

  private final LinkedHashMap<String, String> map = new LinkedHashMap<>();

  public Aliases() {
    map.put("koepota_seiyu", KoepotaSeiyu.class.getName());
    map.put("seiyu_category_member_and_include_template", name(SeiyuCategoryMemberAndIncludeTemplate.class));
    map.put("seiyu_category_members", name(SeiyuCategoryMembers.class));
    map.put("koepota_events", name(koepota_events));
    map.put("female_seiyu_category_members", name(female_seiyu_category_members));
    map.put("male_seiyu_category_members", name(male_seiyu_category_members));
    map.put("seiyu_template_include_pages", name(seiyu_template_include_pages));
  }

  public boolean containsKey(String key) {
    return map.containsKey(key);
  }

  public String get(String key) {
    return map.get(key);
  }

  public static String name(Object obj) {
    if (obj instanceof Class) {
      return ((Class) obj).getName();
    } else if (obj instanceof Enum) {
      Enum e = (Enum) obj;
      return e.getClass().getSimpleName() + "/" + e.name();
    } else {
      return obj.getClass().getName();
    }
  }
}
