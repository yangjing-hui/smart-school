package kxr1.smartcollege.smartcollege.plugin.mob;

import kd.bos.bill.AbstractBillPlugIn;
import kd.bos.bill.AbstractMobBillPlugIn;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.form.plugin.AbstractMobFormPlugin;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.user.UserServiceHelper;

import java.util.EventObject;

public class myinformat extends AbstractMobFormPlugin {
    @Override
    public void beforeBindData(EventObject e) {
        super.beforeBindData(e);
        Long userId= RequestContext.get().getCurrUserId();
        Object number= UserServiceHelper.getUserInfoByID(userId).get("number");
        QFilter idFilter = new QFilter("number", QCP.equals, number);
        DynamicObject myinfor = BusinessDataServiceHelper.loadSingle("kxr1_perinform", "id,number,name,kxr1_combofield1,kxr1_telephonefield,kxr1_textfield2," +
                "kxr1_combofield2,kxr1_textfield1,kxr1_emailfield,kxr1_basedatafield.fullname,kxr1_addressfield.name", new QFilter[]{idFilter});
        Object primaryKey = myinfor.getPkValue();
        if(primaryKey!=null){
            this.getModel().setValue("kxr1_basedatafield",primaryKey);
        }

    }
}
