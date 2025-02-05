package kxr1.smartcollege.smartcollege.plugin.gpt;

import kd.bos.bill.AbstractBillPlugIn;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.datamodel.IDataModel;
import kd.bos.form.IFormView;
import kd.bos.form.gpt.IGPTFormAction;
import kd.bos.list.plugin.AbstractListPlugin;
import kd.bos.mvc.SessionManager;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;

import java.util.HashMap;
import java.util.Map;

public class GetCourseinfor extends AbstractBillPlugIn implements IGPTFormAction {
    @Override
    public Map<String, String> invokeAction(String pageId, String s1, Map<String, String> map) {
        IFormView formView = SessionManager.getCurrent().getView(pageId);
        IDataModel model = formView.getModel();
        Object id = model.getValue( "id" );
        DynamicObject course= (DynamicObject) model.getValue("kxr1_basedatafield");
        if(course!=null){
            Object coursePkvalue=course.getPkValue();
            QFilter idFilter = new QFilter("id", QCP.equals, coursePkvalue);
            DynamicObject dynamicObject = BusinessDataServiceHelper.loadSingle("kxr1_coursei", "id,name,number,kxr1_textfield", new QFilter[]{idFilter});
            String coursename=dynamicObject.get("name").toString();
            String weeks=dynamicObject.get("kxr1_textfield").toString();
            StringBuilder str = new StringBuilder();
            str.append("课程名:").append(coursename).append(",周期:").append(weeks);
            Map<String,String> map1 = new HashMap<>();
            //String property = System.getProperty( "domain.contextUrl");
            //String url = "/index.html?formId=kxr1_teachingplan&pkId="+id+"";
            //map1.put( "out_url" , property+url);
            map1.put("course_data",str.toString());
            return map1;
        }
        return null;

    }
}
