package kxr1.smartcollege.smartcollege.plugin.form;

import kd.bos.bill.AbstractBillPlugIn;
import kd.bos.bill.BillShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.form.ShowType;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.operate.FormOperate;
import kd.bos.list.BillList;
import kd.bos.list.ListFilterParameter;
import kd.bos.list.ListShowParameter;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;

public class enterproblem extends AbstractBillPlugIn {
    private static String LIST_KEY = "kxr1_billlistap";//店铺列表单据列表

    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        String opKey = ((FormOperate) args.getSource()).getOperateKey();
        if (StringUtils.equals("openproblem", opKey)) {
            BillList billlist = this.getControl(LIST_KEY);
            ListSelectedRow currentSelectedRowInfo = billlist.getCurrentSelectedRowInfo();
            Object pkId = currentSelectedRowInfo.getPrimaryKeyValue();
      //      QFilter idFilter = new QFilter("number", QCP.equals, number);

            BillShowParameter showParameter = new BillShowParameter();
            showParameter.setFormId("kxr1_problem");
            showParameter.getOpenStyle().setShowType(ShowType.InCurrentForm);
            showParameter.setStatus(OperationStatus.EDIT);
            showParameter.setPkId(pkId);
            this.getView().showForm(showParameter);
        }
    }
}
