package kxr1.smartcollege.smartcollege.plugin.form;

import kd.bos.context.RequestContext;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.list.plugin.AbstractListPlugin;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.sdk.plugin.Plugin;

import java.util.Iterator;
import java.util.List;

public class OrderRestrict extends AbstractListPlugin implements Plugin {

    @Override
    public void setFilter(SetFilterEvent e) {
        super.setFilter(e);
        String nowname = RequestContext.get().getUserName();
        //移除常用过滤条件
        List<QFilter> qFilters = e.getQFilters();
        Iterator<QFilter> iterator = qFilters.iterator();
        while (iterator.hasNext()) {
            QFilter next = iterator.next();
            iterator.remove();
        }
        //添加过滤条件
        QFilter qFilter = new QFilter("creator.name", QCP.equals, nowname);
        e.getQFilters().add(qFilter);
        //设置列表排序
        e.setOrderBy("order by entry.id");

    }

}
