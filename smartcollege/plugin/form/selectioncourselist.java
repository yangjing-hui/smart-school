package kxr1.smartcollege.smartcollege.plugin.form;

import com.kingdee.bos.qing.core.model.analysis.common.brief.WarningRuleDefinition;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.bill.AbstractBillPlugIn;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.operate.FormOperate;
import kd.bos.list.BillList;
import kd.bos.org.utils.Consts;
import kd.bos.orm.ORM;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.operation.DeleteServiceHelper;
import kd.bos.servicehelper.operation.OperationServiceHelper;
import kd.bos.servicehelper.operation.SaveServiceHelper;


public class selectioncourselist extends AbstractBillPlugIn {
    private static String LIST_KEY = "kxr1_billlistap";//选课列表单据列表
    private static String ACTION_KEY = "csconfirma";//课程选择确认编码
    @Override
    public void beforeDoOperation(BeforeDoOperationEventArgs e) {
        super.beforeDoOperation(e);
        String opKey = ((FormOperate) e.getSource()).getOperateKey();
        if (StringUtils.equals("selectcourse", opKey)) {
            // 选课操作逻辑
            // 获取列表选中行
            BillList billlist = this.getControl(LIST_KEY);
            ListSelectedRow currentSelectedRowInfo = billlist.getCurrentSelectedRowInfo();
            Object primaryKey = currentSelectedRowInfo.getPrimaryKeyValue();
            // 弹窗课程确定页面
            FormShowParameter formShowParameter = new FormShowParameter();
            formShowParameter.setFormId("kxr1_csconfirma");
            formShowParameter.setCustomParam("courseId", primaryKey);
            formShowParameter.setCloseCallBack(new CloseCallBack(this, ACTION_KEY));
            formShowParameter.getOpenStyle().setShowType(ShowType.Modal);
            this.getView().showForm(formShowParameter);
        } else if (StringUtils.equals("deselect", opKey)) {
            if (verify1()) {

                this.getView().showSuccessNotification("删除成功。");
            } else {
                this.getView().showTipNotification("删除失败。");
            }

        } else if (StringUtils.equals("refresh", opKey)) {
            DynamicObject[] allcourses =
                    BusinessDataServiceHelper.load("kxr1_coursei", "id,kxr1_integerfield1,kxr1_integerfield,kxr1_textfield1", new QFilter[]{});
            if (null != allcourses) {
                for (DynamicObject acourse : allcourses) {
                    QFilter filter = new QFilter("kxr1_basedatafield.number", QCP.equals, acourse.get("number"));
                    DynamicObjectCollection allselectcourses = ORM.create().query("kxr1_mycourse", "id,kxr1_basedatafield.number",new QFilter[]{filter});

                    acourse.set("kxr1_integerfield1", allselectcourses.size());//报名人数
                    if ((int) acourse.get("kxr1_integerfield1") >= (int) acourse.get("kxr1_integerfield")) {
                        acourse.set("kxr1_textfield1", "已满");
                    } else {
                        acourse.set("kxr1_textfield1", "可报名");

                    }

                }
                SaveServiceHelper.update(allcourses);
                // 刷新列表数据
                BillList billlist = this.getControl(LIST_KEY);
                billlist.refresh();
                billlist.refreshData();

            }
        }
    }
    public boolean verify1(){
        // 获取列表选中行
        BillList billlist = this.getControl(LIST_KEY);
        ListSelectedRow currentSelectedRowInfo = billlist.getCurrentSelectedRowInfo();
        Object primaryKey = currentSelectedRowInfo.getPrimaryKeyValue();
        //获取当前实体
        QFilter idFilter = new QFilter("id", QCP.equals, primaryKey);
        DynamicObject chooseCourse = BusinessDataServiceHelper.loadSingle("kxr1_coursei", "id,kxr1_mulbasedatafield,kxr1_integerfield1,kxr1_integerfield,kxr1_textfield1,kxr1_combofield", new QFilter[]{idFilter});
        if (null == chooseCourse) {
            this.getView().showTipNotification("选择课程数据有误，请重新选择或联系管理员。");
        }
        else{
            RequestContext requestContext = RequestContext.get();
            long currUserId = requestContext.getCurrUserId();
            QFilter userFilter = new QFilter("creator", QCP.equals, currUserId);
            // 查用户的选课情况
            DynamicObject[] selfcourses =
                    BusinessDataServiceHelper.load("kxr1_mycourse", "id,kxr1_basedatafield,kxr1_basedatafield.kxr1_mulbasedatafield,creator", new QFilter[]{userFilter});
            if (null != selfcourses && selfcourses.length > 0){
                for (DynamicObject selfcourse : selfcourses) {
                    DynamicObject course = selfcourse.getDynamicObject("kxr1_basedatafield");
                    if (course != null) {
                        String pkValue = course.getPkValue().toString();
                        // 课程id相同，该学生是否已经选过该门课程
                        if (StringUtils.equalsIgnoreCase(chooseCourse.getPkValue().toString(), pkValue)) {

                            QFilter Filter = new QFilter("id", QCP.equals, selfcourse.getPkValue());
                            if(!chooseCourse.get("kxr1_combofield").equals("CompletedClasses")){
                                if(DeleteServiceHelper.delete("kxr1_mycourse", new QFilter[]{Filter,userFilter})>0) {
                                    int x= (int) chooseCourse.get("kxr1_integerfield1")-1;
                                    chooseCourse.set("kxr1_integerfield1",x);
                                    return true;
                                }
                            }

                        }
                    }
                }
            }

        }
        return false;
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
                    this.getView().showSuccessNotification("选课成功。");
                }
            }
        }
        // 刷新列表数据
        BillList billlist = this.getControl(LIST_KEY);
        billlist.refresh();
        billlist.refreshData();

    }


}
