package kxr1.smartcollege.smartcollege.plugin.gpt;

import com.alibaba.fastjson.JSONObject;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.gpt.IGPTFormAction;
import kd.bos.form.operate.FormOperate;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.metadata.IDataEntityProperty;
import kd.bos.dataentity.metadata.clr.DataEntityPropertyCollection;
import kd.bos.dataentity.metadata.dynamicobject.DynamicObjectType;
import kd.bos.entity.datamodel.IDataModel;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.form.FormMetadataCache;
import kd.bos.form.IFormView;
import kd.bos.form.gpt.IGPTAction;
import kd.bos.mvc.SessionManager;

import java.util.HashMap;
import java.util.Map;

public class GPThealth implements IGPTFormAction {

    @Override
    public Map<String, String> invokeAction(String pageId,String s, Map<String, String> map) {
        IFormView formView = SessionManager.getCurrent().getView(pageId);
        IDataModel model = formView.getModel();
        Object id = model.getValue( "id" );
        //获取日任务信息，并且以JSON字符串的形式展现
            JSONObject jsonResultObject = new JSONObject();
            jsonResultObject.put("taskName", model.getValue("creator").toString());
            jsonResultObject.put("createTime", model.getValue("createtime").toString());
            jsonResultObject.put("height", model.getValue("kxr1_height").toString());
            jsonResultObject.put("weight", model.getValue("kxr1_weight").toString());
            jsonResultObject.put("BMI", model.getValue("kxr1_bmi").toString());
            jsonResultObject.put("shousuoya", model.getValue("kxr1_decimalfield").toString());
            jsonResultObject.put("shuzhangya", model.getValue("kxr1_decimalfield1").toString());
            jsonResultObject.put("yingyang", model.getValue("kxr1_textfield").toString());
            jsonResultObject.put("mianrong", model.getValue("kxr1_textfield1").toString());
            jsonResultObject.put("xinlv", model.getValue("kxr1_textfield2").toString());
            jsonResultObject.put("xinyin", model.getValue("kxr1_textfield3").toString());
            jsonResultObject.put("xinzangzayin", model.getValue("kxr1_textfield4").toString());
            jsonResultObject.put("feibu", model.getValue("kxr1_textfield5").toString());
            jsonResultObject.put("fubu", model.getValue("kxr1_textfield6").toString());
            jsonResultObject.put("ganzang", model.getValue("kxr1_textfield7").toString());
            jsonResultObject.put("pizang", model.getValue("kxr1_textfield8").toString());
            jsonResultObject.put("pifu", model.getValue("kxr1_textfield9").toString());
            jsonResultObject.put("jizhu", model.getValue("kxr1_textfield10").toString());
            jsonResultObject.put("qianbiaolinbajie", model.getValue("kxr1_textfield11").toString());
            jsonResultObject.put("jiazhuangxian",model.getValue("kxr1_textfield12").toString());
            jsonResultObject.put("sizhi", model.getValue("kxr1_textfield11").toString());

            Map<String, String> variableMap = new HashMap<>();
            variableMap.put("taskResult", jsonResultObject.toString());

        return variableMap;
    }
}

