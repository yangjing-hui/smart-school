package kxr1.smartcollege.smartcollege.plugin.form;

import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.filter.FilterParameter;
import kd.bos.filter.FilterContainer;
import kd.bos.form.control.events.AfterSearchClickListener;
import kd.bos.form.control.events.FilterContainerInitListener;
import kd.bos.form.events.*;
import kd.bos.list.BillList;
import kd.bos.list.plugin.AbstractListPlugin;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.user.UserServiceHelper;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

public class questionlist extends AbstractListPlugin implements AfterBindDataListener, BeforeBindDataListener {
    private static String BILLLISTAP = "kxr1_billlistap";//单据列表
    @Override
    public void registerListener(EventObject e) {
        super.registerListener(e);
        BillList list = this.getControl(BILLLISTAP);
        if (list != null) {
            list.addAfterBindDataListener(this);
        }

    }
    @Override
    public void beforeBindData(EventObject e) {
        super.beforeBindData(e);
        BillList list = this.getControl(BILLLISTAP);
        Long userId= RequestContext.get().getCurrUserId();
        Object number= UserServiceHelper.getUserInfoByID(userId).get("number");
        //获取当前实体
        QFilter idFilter = new QFilter("number", QCP.equals, number);
        DynamicObject user = BusinessDataServiceHelper.loadSingle(
                "kxr1_perinform", "id,number,kxr1_combofield1", new QFilter[]{idFilter});
        if(user!=null){
            String type= user.getString("kxr1_combofield1");
            if(type.equals("student")){
                //学生视图
                QFilter billnoFilter = new QFilter("creator", QCP.equals, userId);
                List<QFilter> qFilterList=new ArrayList<>();
                qFilterList.add(billnoFilter);
                //list.addAfterBindDataListener(this);
                list.addBeforeBindDataListener(this);
                // 设置 常用、方案过滤条件参数
                list.setClientQueryFilterParameter(new FilterParameter(billnoFilter, null));
            }
            else if(type.equals("tutor")){
                //教师视图
                QFilter billnoFilter = new QFilter("kxr1_basedatafield.number", QCP.equals, number);
                list.setFilter(billnoFilter);
            }
        }


    }


    @Override
    public void afterBindData(AfterBindDataEvent afterBindDataEvent) {

    }
    @Override
    public void afterBindData(EventObject e) {
        super.afterBindData(e);
    }
    @Override
    public void beforeBindData(BeforeBindDataEvent beforeBindDataEvent) {

    }
}
