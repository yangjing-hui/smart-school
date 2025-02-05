package kxr1.smartcollege.smartcollege.plugin.mob;

import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.form.control.Label;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.AbstractMobFormPlugin;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.user.UserServiceHelper;
import kd.bos.url.UrlService;

import java.util.EventObject;

public class personcenter extends AbstractMobFormPlugin {
    @Override
    public void beforeBindData(EventObject e) {
        super.beforeBindData(e);
        Long userId= RequestContext.get().getCurrUserId();
        Object number= UserServiceHelper.getUserInfoByID(userId).get("number");
        QFilter idFilter = new QFilter("number", QCP.equals, number);
        DynamicObject myinfor = BusinessDataServiceHelper.loadSingle("kxr1_perinform", "id,number,name", new QFilter[]{idFilter});
        Object primaryKey = myinfor.getPkValue();
        if(primaryKey!=null){

            Label label = this.getView().getControl("kxr1_labelap");
            label.setText(myinfor.get("name").toString());
        }

    }

}
