package kxr1.smartcollege.smartcollege.plugin.form;


import com.alibaba.fastjson.JSONObject;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.ext.form.control.Markdown;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.krpc.common.json.JSONArray;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.DispatchServiceHelper;
import kd.sdk.plugin.Plugin;

import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

public class healthask extends AbstractFormPlugin implements Plugin {
    @Override
    public void beforeDoOperation(BeforeDoOperationEventArgs e) {

        super.beforeDoOperation(e);
        String opGpt = ((FormOperate) e.getSource()).getOperateKey();
        if(StringUtils.equals("sick", opGpt)){
            //获取日任务信息，并且以JSON字符串的形式展现
            JSONObject jsonResultObject = new JSONObject();
            jsonResultObject.put("taskName", this.getModel().getValue("creator").toString());
            jsonResultObject.put("total", this.getModel().getValue("kxr1_textfield1").toString());

            DynamicObjectCollection dynamicObjectCollection = this.getModel().getEntryEntity("entryentity");
            JSONArray jsonTaskArray = new JSONArray();
            for (DynamicObject dynamicObjectSingle : dynamicObjectCollection) {
                JSONObject jsonObjectSingle = new JSONObject();
                jsonObjectSingle.put("time", dynamicObjectSingle.getDate("kxr1_datetimefield"));
                jsonObjectSingle.put("specific", dynamicObjectSingle.getString("kxr1_textfield"));
                jsonTaskArray.add(jsonObjectSingle);
                jsonResultObject.put("taskIntroduction", jsonTaskArray);
            }


            //调用GPT开发平台微服务
            Map<String , String> variableMap = new HashMap<>();
            variableMap.put("taskResult", jsonResultObject.toJSONString());

            Object[] params = new Object[] {
                    //GPT提示编码
                    getPromptFid("prompt-240702C677DF8A"),
                    "",
                    variableMap
            };
            Map<String, Object> result = DispatchServiceHelper.invokeBizService("ai", "gai", "GaiPromptService", "syncCall", params);
            JSONObject jsonObjectResult = new JSONObject(result);
            JSONObject jsonObjectData = jsonObjectResult.getJSONObject("data");
            //设置值
            this.getModel().setValue("kxr1_evaluate_all", jsonObjectData.getString("llmValue"));
            Markdown mk = this.getView().getControl("kxr1_markdownap");
            mk.setText(jsonObjectData.getString("llmValue"));
        }

    }

    //获取GPT提示的Fid
    public long getPromptFid(String billNo) {
        DynamicObject dynamicObject = BusinessDataServiceHelper.loadSingle("gai_prompt",
                "number," +
                        "id",
                (new QFilter("number", QCP.equals, billNo)).toArray());
        return dynamicObject.getLong("id");
    }
    @Override
    public void afterBindData(EventObject eventObject) {
        Markdown mk = this.getView().getControl("kxr1_markdownap");
        mk.setText(this.getModel().getValue("kxr1_evaluate_all").toString());
    }
}
