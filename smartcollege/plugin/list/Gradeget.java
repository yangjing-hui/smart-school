package kxr1.smartcollege.smartcollege.plugin.list;

import kd.bos.bill.AbstractBillPlugIn;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.entity.EntityType;
import kd.bos.entity.MainEntityType;
import kd.bos.entity.datamodel.IDataModel;
import kd.bos.entity.datamodel.IListModel;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.form.IFormView;
import kd.bos.form.control.Control;
import kd.bos.form.gpt.IGPTFormAction;
import kd.bos.list.BillList;
import kd.bos.list.IListView;
import kd.bos.mvc.SessionManager;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;

import java.util.HashMap;
import java.util.Map;

public class Gradeget extends AbstractBillPlugIn implements IGPTFormAction {
    @Override
    public Map<String, String> invokeAction(String pageId, String action, Map<String, String> map) {
        StringBuilder str = new StringBuilder();
        IListView listView = (IListView) SessionManager.getCurrent().getView(pageId);
        if (listView != null) {
            EntityType mainEntityType = listView.getListModel().getDataEntityType();
            ListSelectedRowCollection selectedRows = listView.getSelectedRows();
            if (!selectedRows.isEmpty()) {
                Object[] primaryKeyValues = selectedRows.getPrimaryKeyValues();
                if (primaryKeyValues != null) {
                    DynamicObject[] dynamicObjects = BusinessDataServiceHelper.load(primaryKeyValues, mainEntityType);

                    for (DynamicObject dynamicObject : dynamicObjects) {
                        String courseName = dynamicObject.getString("kxr1_basedatafield.name");
                        Integer score = dynamicObject.getInt("kxr1_integerfield");
                        String semester = dynamicObject.getString("kxr1_combofield");
                        Integer credit = dynamicObject.getInt("kxr1_basedatafield.kxr1_integerfield2");
                        if (courseName != null && score != null && semester != null) {
                            str.append("课程名: ").append(courseName)
                                    .append(", 成绩: ").append(score).append("分")
                                    .append(", 学期: ").append(semester)
                                    .append(", 学分: ").append(credit)
                                    .append("\n"); // 添加换行符以分隔不同的条目
                        }
                    }
                    // 这里可以处理或显示str的内容
                    System.out.println(str.toString());

                }
            }
        }
        Map<String,String> map1 = new HashMap<>();
        String property = System.getProperty( "domain.contextUrl");
        //String url = "/index.html?formId=kxr1_showdish&pkId="+id+"";
       // map1.put( "out_url" , property+url);
        map1.put("grade_data", str.toString());
        map1.put( "name", RequestContext.get().getUserName() );
        return map1;
    }
}
