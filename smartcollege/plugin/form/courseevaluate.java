package kxr1.smartcollege.smartcollege.plugin.form;

import kd.bos.context.RequestContext;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.form.control.Control;
import kd.bos.form.control.Rate;
import kd.bos.form.control.events.RateEvent;
import kd.bos.form.control.events.RateListener;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.operation.SaveServiceHelper;

import java.util.Date;
import java.util.EventObject;
import java.util.List;

public class courseevaluate  extends AbstractFormPlugin implements RateListener{
    private static Number number = null;
    @Override
    public void afterCreateNewData(EventObject e) {
        Object primaryKey = this.getView().getFormShowParameter().getCustomParam("courseId");
        if (null != primaryKey) {
            this.getModel().setValue("kxr1_basedatafield", primaryKey);//绑定基础资料字段
        }
        Rate rate = this.getControl("kxr1_rateap");
    }

    @Override
    public void registerListener(EventObject e) {
        super.registerListener(e);
        // 侦听文本字段按钮点击事件
        this.addClickListeners("btnok");
        Rate rate = this.getControl("kxr1_rateap");
        rate.addRateListener(this);
    }
    @Override
    public void click(EventObject evt) {
        super.click(evt);
        Control source = (Control) evt.getSource();
        if (StringUtils.equals("btnok", source.getKey())) {
            Object primaryKey = this.getView().getFormShowParameter().getCustomParam("courseId");
            QFilter idFilter = new QFilter("id", QCP.equals, primaryKey);
            DynamicObject chooseCourse = BusinessDataServiceHelper.loadSingle("kxr1_coursei", "id,kxr1_createrfield.name,kxr1_createrfield.number,number,name", new QFilter[]{idFilter});
            if (null == chooseCourse) {
                this.getView().showTipNotification("选择课程数据有误，请重新选择或联系管理员。");
                return;
            }
            else if(number==null){
                this.getView().showTipNotification("请输入评分。");
                return;
            }
            String s1=this.getModel().getValue("kxr1_textfield").toString();
            DynamicObject course = BusinessDataServiceHelper.newDynamicObject("kxr1_courseevaluation");
            course.set("kxr1_textfield", s1);
            course.set("kxr1_basedatafield", chooseCourse);
            course.set("status", "C");
            course.set("enable", 1);
            course.set("creator", RequestContext.get().getCurrUserId());
            course.set("kxr1_integerfield",number);
            OperationResult saveResult = SaveServiceHelper.saveOperate("kxr1_courseevaluation", new DynamicObject[]{course}, OperateOption.create());
            List<Object> successPkIds = saveResult.getSuccessPkIds();
            System.out.println(successPkIds.get(0));
            // 返回成功结果到父页面
            this.getView().returnDataToParent(true);
            this.getView().close();
        }
    }

    @Override
    public void update(RateEvent rateEvent) {
        Number rateScore = rateEvent.getRateScore();
        number=rateScore;
    }
}
