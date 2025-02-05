package kxr1.smartcollege.smartcollege.plugin.form;

import kd.bos.bill.AbstractBillPlugIn;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.operate.FormOperate;
import kd.bos.servicehelper.DispatchServiceHelper;

public class gradeanalysis extends AbstractBillPlugIn {

    @Override
    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        String opGpt = ((FormOperate) args.getSource()).getOperateKey();
        if(StringUtils.equals("gptgrade", opGpt)){
            String pageId = this.getView().getMainView().getPageId();
            String assistant ="您好!请输入“分析”，我们将根据您的订单信息分析营养价值，并给出合理的营养健康建议~ ";
            Object pkValue = Long.parseLong("1984205234846566400");
            DispatchServiceHelper.invokeBizService( "ai" , "gai" ,"GaiService","selectProcessInSideBar",pkValue,pageId,assistant);
        }
    }
}
