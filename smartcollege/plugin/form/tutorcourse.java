package kxr1.smartcollege.smartcollege.plugin.form;

import kd.bos.bill.BillShowParameter;
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
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;

import java.util.HashMap;
import java.util.Map;

public class tutorcourse extends AbstractListPlugin {
    private static String ACTION_KEY = "teachingplan";//课程选择确认编码
    private static String ACTION_KEY1 = "gradeindt";//课程选择确认编码
    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        String opGpt = ((FormOperate) args.getSource()).getOperateKey();
        if(StringUtils.equals("newteachingplan", opGpt)){//教学计划
            // 获取列表选中行
            IListView listView = (IListView) this.getView();
            ListSelectedRow listSelectedRow=listView.getCurrentSelectedRowInfo();
            Object primaryKey = listSelectedRow.getPrimaryKeyValue();
            QFilter idFilter = new QFilter("id", QCP.equals, primaryKey);
            DynamicObject dynamicObject = BusinessDataServiceHelper.loadSingle("kxr1_mycoursetutor", "id,kxr1_basedatafield,kxr1_basedatafield.number,kxr1_basedatafield.kxr1_combofield,kxr1_basedatafield.kxr1_createrfield", new QFilter[]{idFilter});
            DynamicObject course=dynamicObject.getDynamicObject("kxr1_basedatafield");
            Object cprimaryKey=course.getPkValue();//课程pk
            String coursenumber= (String) dynamicObject.get("kxr1_basedatafield.number");
            QFilter idFilter1 = new QFilter("kxr1_basedatafield.id", QCP.equals, cprimaryKey);
            DynamicObject tdynamicObject = BusinessDataServiceHelper.loadSingle("kxr1_teachingplan", "id,kxr1_basedatafield,kxr1_basedatafield.kxr1_combofield,kxr1_basedatafield.kxr1_createrfield", new QFilter[]{idFilter1});
            if(tdynamicObject==null){
                    this.getView().showTipNotification("该课程还未创建教学计划或在审核中。");
            }else{
                BillShowParameter billShowParameter=new BillShowParameter();
                billShowParameter.setFormId("kxr1_teachingplan");
                billShowParameter.setPkId(tdynamicObject.getPkValue());
                billShowParameter.getOpenStyle().setShowType(ShowType.Modal);
                this.getView().showForm(billShowParameter);
            }
        }else if(StringUtils.equals("scoreentry", opGpt)){//成绩录入

            // 获取列表选中行
            IListView listView = (IListView) this.getView();
            ListSelectedRow listSelectedRow=listView.getCurrentSelectedRowInfo();
            Object primaryKey = listSelectedRow.getPrimaryKeyValue();
            QFilter idFilter = new QFilter("id", QCP.equals, primaryKey);
            DynamicObject dynamicObject = BusinessDataServiceHelper.loadSingle("kxr1_mycoursetutor", "id,kxr1_basedatafield,kxr1_basedatafield.number,kxr1_basedatafield.kxr1_combofield,kxr1_basedatafield.kxr1_createrfield,kxr1_combofield", new QFilter[]{idFilter});
            DynamicObject course=dynamicObject.getDynamicObject("kxr1_basedatafield");
            Object cprimaryKey=course.getPkValue();//课程pk

            if(dynamicObject.get("kxr1_combofield").equals("2")){
                if(course.get("kxr1_combofield").equals("CompletedClasses")){

            QFilter cidFilter = new QFilter("kxr1_basedatafield.id", QCP.equals, cprimaryKey);
            DynamicObject[] studentcourse=BusinessDataServiceHelper.load("kxr1_mycourse","id,creator.number",new QFilter[]{cidFilter});
            int sum=studentcourse.length;
            String[] snumber=new String[sum];
            Object[] sprimaryKey=new Object[sum];
            Object type=studentcourse[0].getDynamicObjectType();
            for(int i=0;i<sum;i++){
                snumber[i]= (String) studentcourse[i].get("creator.number");
                sprimaryKey[i]=studentcourse[i].getPkValue();


            }
            // 新增成绩录入
            FormShowParameter formShowParameter = new FormShowParameter();
            formShowParameter.setFormId("kxr1_gradeindt");
            //formShowParameter.setCustomParam("courseId", cprimaryKey);
            Map<String,Object> students=new HashMap<>();
            students.put("student",snumber);
            students.put("tutorcourse",primaryKey);
            students.put("courseId", cprimaryKey);
            students.put("sprimaryKey",sprimaryKey);
            //students.put("type",type);
            formShowParameter.setCustomParams(students);
            formShowParameter.setCloseCallBack(new CloseCallBack(this, ACTION_KEY1));
            formShowParameter.getOpenStyle().setShowType(ShowType.Modal);
            this.getView().showForm(formShowParameter);
                }
                else{
                    this.getView().showErrorNotification("课程未结课。");
                }
            }
            else{
                this.getView().showErrorNotification("该课程成绩已经录入。");
            }
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
                    this.getView().showSuccessNotification("教学计划创建成功。");
                }
            }
        }else if(ACTION_KEY1.equalsIgnoreCase(actionId)){
            Object returnData = e.getReturnData();
            if (null != returnData) {
                Boolean result = (Boolean)returnData;
                if (result) {
      
                    this.getView().showSuccessNotification("成绩录入成功。");
                }
            }
        }

    }
}
