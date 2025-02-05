package kxr1.smartcollege.smartcollege.plugin.form;

import kd.bos.bill.AbstractBillPlugIn;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.operation.SaveServiceHelper;

import java.util.EventObject;


public class coursestatecontroll extends AbstractBillPlugIn {
    //控制课程状态，报名人数
    @Override
    public void afterLoadData(EventObject e) {
        super.afterLoadData(e);
        DynamicObject[] allcourses =
                BusinessDataServiceHelper.load("kxr1_coursei", "id", new QFilter[]{});
        if (null != allcourses ) {
            for (DynamicObject acourse : allcourses) {
                QFilter filter=new QFilter("id", QCP.equals,acourse.getPkValue());
                DynamicObject[] allselectcourses =
                        BusinessDataServiceHelper.load("kxr1_mycourse", "id,kxr1_integerfield1,kxr1_integerfield,kxr1_textfield1", new QFilter[]{});
                acourse.set("kxr1_integerfield1",allselectcourses.length);//报名人数
                if((int)acourse.get("kxr1_integerfield1")>=(int)acourse.get("kxr1_integerfield")){
                    acourse.set("kxr1_textfield1","已满");
                }
                else{
                    acourse.set("kxr1_textfield1","可报名");
                }
                SaveServiceHelper.update(acourse);
            }
        }

    }

}
