package kxr1.smartcollege.smartcollege.plugin.form;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.operate.FormOperate;
import kd.bos.list.plugin.AbstractListPlugin;
import kd.bos.servicehelper.DispatchServiceHelper;
import kd.bos.servicehelper.operation.SaveServiceHelper;

import java.util.EventObject;

public class teachingplan extends AbstractListPlugin {
    @Override
    public void afterCreateNewData(EventObject e) {
        if(this.getView().getFormShowParameter()!=null){
            Object primaryKey = this.getView().getFormShowParameter().getCustomParam("courseId");
            if (null != primaryKey) {
                this.getModel().setValue("kxr1_basedatafield", primaryKey);//绑定基础资料字段
            }
        }


    }

    @Override
    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        String opGpt = ((FormOperate) args.getSource()).getOperateKey();
        if(StringUtils.equals("planning", opGpt)){
            String pageId = this.getView().getMainView().getPageId();
           // this.getView().getFormShowParameter().get
            String assistant ="我会为您推荐合适的教学计划~";
            Object pkValue = Long.parseLong("1995800402733313024");
            DispatchServiceHelper.invokeBizService( "ai" , "gai" ,"GaiService","selectProcessInSideBar",pkValue,pageId,assistant);
        }
    }

    @Override
    public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
        super.afterDoOperation(afterDoOperationEventArgs);
        String opGpt = ((FormOperate) afterDoOperationEventArgs.getSource()).getOperateKey();
        if(StringUtils.equals("submit", opGpt)){
            DynamicObject dynamicObject= (DynamicObject) this.getModel().getValue("kxr1_basedatafield");
            dynamicObject.set("kxr1_combofield1","1");
            SaveServiceHelper.update(dynamicObject);
        }
    }
}
