package kxr1.smartcollege.smartcollege.plugin.form;

import kd.bos.bill.AbstractBillPlugIn;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.datamodel.IBillModel;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.operate.FormOperate;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.operation.SaveServiceHelper;

import java.util.Date;

public class newcourse extends AbstractBillPlugIn {
    @Override
    public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
        super.afterDoOperation(afterDoOperationEventArgs);

        String opGpt = ((FormOperate) afterDoOperationEventArgs.getSource()).getOperateKey();
        if(StringUtils.equals("submit", opGpt)){
            IBillModel billModel=(IBillModel)this.getModel();
            Object primaryKey=billModel.getPKValue();
            QFilter idFilter=new QFilter("id", QCP.equals, primaryKey);;
            DynamicObject newc=BusinessDataServiceHelper.loadSingle("kxr1_coursei","id,kxr1_combofield3",new QFilter[]{idFilter});
            if(newc!=null){
                DynamicObject selfCourse = BusinessDataServiceHelper.newDynamicObject("kxr1_mycoursetutor");
                selfCourse.set("billstatus", "C");
                Long userId= RequestContext.get().getCurrUserId();
                selfCourse.set("creator", userId);
                selfCourse.set("kxr1_basedatafield", newc);
                selfCourse.set("kxr1_combofield", "2");
                OperationResult saveResult = SaveServiceHelper.saveOperate("kxr1_mycourse", new DynamicObject[]{selfCourse}, OperateOption.create());
                if(!saveResult.isSuccess()){
                    this.getView().showTipNotification("加入我的课程出错。");
                }
            }
            else {
                this.getView().showTipNotification("找不到课程。");
            }

        }

    }
}
