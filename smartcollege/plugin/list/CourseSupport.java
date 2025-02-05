package kxr1.smartcollege.smartcollege.plugin.list;

import kd.bos.bill.AbstractBillPlugIn;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.form.gpt.IGPTFormAction;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;

import java.util.Map;

public class CourseSupport extends AbstractBillPlugIn implements IGPTFormAction {
    @Override
    public Map<String, String> invokeAction(String s, String s1, Map<String, String> map) {

        QFilter filter = new QFilter("kxr1_combofield", QCP.equals,"CompletedClasses");
        DynamicObject[] courselist =
                BusinessDataServiceHelper.load("kxr1_coursei","id,number,kxr1_combofield",new QFilter[]{filter});
        for(DynamicObject c:courselist){
            String number=c.getString("number");
        }

        return null;
    }
}
