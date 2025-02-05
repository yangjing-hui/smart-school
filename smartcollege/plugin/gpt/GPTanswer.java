package kxr1.smartcollege.smartcollege.plugin.gpt;

import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.entity.datamodel.IDataModel;
import kd.bos.form.IFormView;
import kd.bos.form.gpt.IGPTFormAction;
import kd.bos.mvc.SessionManager;

import java.util.*;

public class GPTanswer implements IGPTFormAction {

    @Override
    public Map<String, String> invokeAction(String pageId, String action, Map<String, String> map) {
        IFormView formView = SessionManager.getCurrent().getView(pageId);
        IDataModel model = formView.getModel();
        Object id = model.getValue( "id" );
        //   String pageId1 = formView.getPageId();
        DynamicObjectCollection entryEntity = model.getEntryEntity(  "kxr1_entryentity");
        StringBuffer str = new StringBuffer();
        for (DynamicObject dynamicObject : entryEntity) {
            String kxr1Dish = dynamicObject.getString("kxr1_problem_name");
            String kxr1Qtyfield = dynamicObject.getString( "kxr1_textfield1");
            str.append("题目"+kxr1Dish);
            str.append("回答"+kxr1Qtyfield);

        }
        Map<String,String> map1 = new HashMap<>();
        String property = System.getProperty( "domain.contextUrl");
     //   String url = "/index.html?formId=kxr1_showdish&pkId="+id+"";
    //    map1.put( "out_url" , property+url);
        map1.put("data", str.toString());
        map1.put( "name", RequestContext.get().getUserName() );
        return map1;

    }
}


