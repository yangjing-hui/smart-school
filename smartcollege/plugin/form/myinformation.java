package kxr1.smartcollege.smartcollege.plugin.form;
import kd.bos.bill.AbstractBillPlugIn;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.datamodel.events.BizDataEventArgs;
import kd.bos.form.events.LoadCustomControlMetasArgs;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.user.UserServiceHelper;
import oadd.org.apache.drill.exec.rpc.user.UserServer;

import java.util.EventObject;

public class myinformation extends AbstractBillPlugIn{

    @Override
    public void afterCreateNewData(EventObject e) {
        super.afterCreateNewData(e);
        Long userId=RequestContext.get().getCurrUserId();
        Object number=UserServiceHelper.getUserInfoByID(userId).get("number");
        QFilter idFilter = new QFilter("number", QCP.equals, number);
        DynamicObject myinfor = BusinessDataServiceHelper.loadSingle("kxr1_perinform", "id,number,name,kxr1_combofield1,kxr1_telephonefield,kxr1_textfield2," +
                "kxr1_combofield2,kxr1_textfield1,kxr1_emailfield,kxr1_basedatafield.fullname,kxr1_addressfield.name", new QFilter[]{idFilter});
        Object primaryKey = myinfor.getPkValue();
        if (null != primaryKey) {
            this.getModel().setValue("kxr1_basedatafield", primaryKey);//绑定基础资料字段
        }




    }
}
