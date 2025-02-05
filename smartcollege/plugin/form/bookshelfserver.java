package kxr1.smartcollege.smartcollege.plugin.form;

import kd.bos.bill.AbstractBillPlugIn;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.ShowType;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.operate.FormOperate;
import kd.bos.list.BillList;

public class bookshelfserver extends AbstractBillPlugIn {
    private static String LIST_KEY = "kxr1_billlistap";//借阅列表单据列表
    private static String ACTION_KEY = "borrowconfirm";//课程选择确认编码
    @Override
    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        String opKey = ((FormOperate)args.getSource()).getOperateKey();
        if ( StringUtils.equals("borrow", opKey)){
            // 选课操作逻辑
            // 获取列表选中行
            BillList billlist = this.getControl(LIST_KEY);
            ListSelectedRow currentSelectedRowInfo = billlist.getCurrentSelectedRowInfo();
            Object primaryKey = currentSelectedRowInfo.getPrimaryKeyValue();
            // 弹窗课程确定页面
            FormShowParameter formShowParameter = new FormShowParameter();
            formShowParameter.setFormId("kxr1_borrowconfirm");
            formShowParameter.setCustomParam("bookId", primaryKey);
            formShowParameter.setCloseCallBack(new CloseCallBack(this, ACTION_KEY));
            formShowParameter.getOpenStyle().setShowType(ShowType.Modal);
            this.getView().showForm(formShowParameter);
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
                    this.getView().showSuccessNotification("申请创建成功。");

                }
            }
        }
        // 刷新列表数据
        BillList billlist = this.getControl(LIST_KEY);
        billlist.refresh();
        billlist.refreshData();

    }
}
