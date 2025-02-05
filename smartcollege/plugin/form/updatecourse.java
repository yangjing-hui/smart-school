package kxr1.smartcollege.smartcollege.plugin.form;

import kd.bos.bill.AbstractBillPlugIn;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.operate.FormOperate;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.operation.SaveServiceHelper;

public class updatecourse extends AbstractBillPlugIn {
    @Override
    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        String opKey = ((FormOperate) args.getSource()).getOperateKey();
        if (StringUtils.equals("submit", opKey)) {
            DynamicObject choosecourse= (DynamicObject) this.getModel().getValue("kxr1_basedatafield");
            QFilter qFilter=new QFilter("kxr1_basedatafield.id", QCP.equals,choosecourse.getPkValue());
            // 查用户
            DynamicObject[] selfcourses =
                    BusinessDataServiceHelper.load("kxr1_mycoursetutor","kxr1_basedatafield.id",null);
            if(selfcourses.length>0){
                this.getModel().setValue("kxr1_textfield","建议驳回");

                this.getView().showTipNotification("该课程已加入我的课程。");
            }

        }
    }
}
