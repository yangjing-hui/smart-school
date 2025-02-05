package kxr1.smartcollege.smartcollege.plugin.form;

import kd.bos.dataentity.utils.StringUtils;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.list.plugin.AbstractListPlugin;
import kd.bos.orm.query.QFilter;

import java.util.List;

public class bookfilter extends AbstractListPlugin {
    @Override
    public void filterColumnSetFilter(SetFilterEvent args) {
        super.filterColumnSetFilter(args);
        //获取所有过滤条件（不包括权限相关的一级插件设置的过滤）
        List<QFilter> filters=args.getQFilters();
        //获取过滤字段控件映射的实体字段名
        String fieldName=args.getFieldName();
        if(StringUtils.equals(fieldName,"materielfield.group")){
            //常用过滤视图下的物料字段只展示名称包含"可乐"的数据
            args.addCustomQFilter(new QFilter("name",QFilter.like,"%可乐%"));
        }
    }
}
