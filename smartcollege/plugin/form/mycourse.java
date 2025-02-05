package kxr1.smartcollege.smartcollege.plugin.form;

import kd.bos.bill.AbstractBillPlugIn;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.operate.FormOperate;
import kd.bos.list.BillList;
import kd.bos.list.IListView;
import kd.bos.list.plugin.AbstractListPlugin;
import kd.bos.mvc.SessionManager;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.DispatchServiceHelper;

import java.util.Objects;

public class mycourse extends AbstractListPlugin {
    private static String ACTION_KEY = "courseevaluate";//确认编码
    @Override
    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        String opGpt = ((FormOperate) args.getSource()).getOperateKey();
        if(StringUtils.equals("gptgrade", opGpt)){
            String pageId = this.getView().getMainView().getPageId();
            String assistant ="请输入您的要求，我会帮你进行成绩分析,您可以输入'简单分析'或者'具体分析'。";
            Object pkValue = Long.parseLong("1984205234846566400");
            DispatchServiceHelper.invokeBizService( "ai" , "gai" ,"GaiService","selectProcessInSideBar",pkValue,pageId,assistant);
        }else if(StringUtils.equals("evaluate", opGpt)){
            // 获取列表选中行
            IListView listView = (IListView) this.getView();
            ListSelectedRow listSelectedRow=listView.getCurrentSelectedRowInfo();
            Object primaryKey = listSelectedRow.getPrimaryKeyValue();
            QFilter idFilter = new QFilter("id", QCP.equals, primaryKey);
            DynamicObject dynamicObject = BusinessDataServiceHelper.loadSingle("kxr1_mycourse", "id,kxr1_basedatafield,kxr1_basedatafield.kxr1_combofield", new QFilter[]{idFilter});
            Object courseid=dynamicObject.getDynamicObject("kxr1_basedatafield").getPkValue();
            String coursestate= dynamicObject.getString("kxr1_basedatafield.kxr1_combofield");
            if(!Objects.equals(coursestate, "CompletedClasses")){
                this.getView().showTipNotification("还未结课，无法评价！");
                return;
            }
            // 弹窗课程确定页面
            FormShowParameter formShowParameter = new FormShowParameter();
            formShowParameter.setFormId("kxr1_courseevaluate");
            formShowParameter.setCustomParam("courseId", courseid);
            formShowParameter.setCloseCallBack(new CloseCallBack(this, ACTION_KEY));
            formShowParameter.getOpenStyle().setShowType(ShowType.Modal);
            this.getView().showForm(formShowParameter);

        }
    }
    public void closedCallBack(ClosedCallBackEvent e) {
        super.closedCallBack(e);
        String actionId = e.getActionId();
        if (ACTION_KEY.equalsIgnoreCase(actionId)) {
            Object returnData = e.getReturnData();
            if (null != returnData) {
                Boolean result = (Boolean)returnData;
                if (result) {
                    this.getView().showSuccessNotification("评价成功。");
                }
            }
        }
    }
}
