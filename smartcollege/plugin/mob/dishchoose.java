package kxr1.smartcollege.smartcollege.plugin.mob;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.form.plugin.AbstractMobFormPlugin;
import kd.bos.list.MobileSearch;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;

import java.util.EventObject;

public class dishchoose extends AbstractMobFormPlugin {

    Object primaryname = this.getView().getFormShowParameter().getCustomParam("kxr1_shopname");
  //  QFilter idFilter = new QFilter("name", QCP.equals, primaryname);
    @Override
    public void afterBindData(EventObject e) {
        //  MobileSearch mobAp = this.getControl("kxr1_shopname");
     //   mobAp.setText(primaryname.toString());
      //  this.getModel().setValue("kxr1_shopname",primaryname);
     //   MobileFormShowParameter.getCustomParam("name");
    }

}
