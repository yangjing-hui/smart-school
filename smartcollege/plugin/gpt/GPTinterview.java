package kxr1.smartcollege.smartcollege.plugin.gpt;

import com.alibaba.fastjson.JSONObject;
import kd.bos.entity.datamodel.IDataModel;
import kd.bos.form.IFormView;
import kd.bos.form.gpt.IGPTFormAction;
import kd.bos.mvc.SessionManager;

import java.util.HashMap;
import java.util.Map;

public class GPTinterview implements IGPTFormAction {
    @Override
    public Map<String, String> invokeAction(String pageId, String s, Map<String, String> map) {
        IFormView formView = SessionManager.getCurrent().getView(pageId);
        IDataModel model = formView.getModel();
        Object id = model.getValue( "id" );
        //获取日任务信息，并且以JSON字符串的形式展现
        JSONObject jsonResultObject = new JSONObject();
        jsonResultObject.put("name", model.getValue("kxr1_name").toString());
        jsonResultObject.put("email", model.getValue("kxr1_email").toString());
        jsonResultObject.put("tel", model.getValue("kxr1_textfield").toString());
        jsonResultObject.put("born", model.getValue("kxr1_datefield").toString());
        jsonResultObject.put("college", model.getValue("kxr1_college1").toString());
        jsonResultObject.put("grade", model.getValue("kxr1_grade1").toString());
        jsonResultObject.put("major", model.getValue("kxr1_major1").toString());
        jsonResultObject.put("education", model.getValue("kxr1_education").toString());
        jsonResultObject.put("majorclass", model.getValue("kxr1_class1").toString());
        jsonResultObject.put("class2", model.getValue("kxr1_class2").toString());
        jsonResultObject.put("zhengshu", model.getValue("kxr1_textareafield1").toString());
        jsonResultObject.put("huodong", model.getValue("kxr1_textareafield2").toString());
        jsonResultObject.put("rongyu", model.getValue("kxr1_textareafield3").toString());
        jsonResultObject.put("selfevaluate", model.getValue("kxr1_largetextfield").toString());

        Map<String, String> variableMap = new HashMap<>();
        variableMap.put("resumeResult", jsonResultObject.toString());

        return variableMap;
    }
}
