package kxr1.smartcollege.smartcollege.plugin.form;

import kd.bos.bill.AbstractBillPlugIn;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.operate.FormOperate;
import kd.bos.list.BillList;

public class questiona extends AbstractBillPlugIn {
    private static String ACTION_KEY = "queans";
    @Override
    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        String opGpt = ((FormOperate) args.getSource()).getOperateKey();
        if(StringUtils.equals("question", opGpt)){
            // 弹窗问题输入界面
            FormShowParameter formShowParameter = new FormShowParameter();
            formShowParameter.setFormId("kxr1_queans");
            formShowParameter.setCustomParam("type", "question");
            formShowParameter.setCloseCallBack(new CloseCallBack(this, ACTION_KEY));
            formShowParameter.getOpenStyle().setShowType(ShowType.Modal);

        }
    }

    @Override
    public void closedCallBack(ClosedCallBackEvent e) {
        super.closedCallBack(e);
        String actionId = e.getActionId();
        if (ACTION_KEY.equalsIgnoreCase(actionId)) {
            Object returnData = e.getReturnData();
            if (null != returnData) {
                Boolean result = (Boolean)returnData;
                if (result) {
                    this.getView().showSuccessNotification("提问成功。");
                }
            }
        }


    }
}
