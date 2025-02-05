package kxr1.smartcollege.smartcollege.plugin.form;


import com.alibaba.fastjson.JSONObject;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.ext.form.control.Markdown;
import kd.bos.form.control.events.ItemClickEvent;
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

public class healthplan extends AbstractFormPlugin implements Plugin {
    @Override
    public void beforeDoOperation(BeforeDoOperationEventArgs e) {

        super.beforeDoOperation(e);
        String opGpt = ((FormOperate) e.getSource()).getOperateKey();
        if(StringUtils.equals("health", opGpt)){
            //获取日任务信息，并且以JSON字符串的形式展现
            JSONObject jsonResultObject = new JSONObject();
            jsonResultObject.put("taskName", this.getModel().getValue("creator").toString());
            jsonResultObject.put("createTime", this.getModel().getValue("createtime").toString());
            jsonResultObject.put("height", this.getModel().getValue("kxr1_height").toString());
            jsonResultObject.put("weight", this.getModel().getValue("kxr1_weight").toString());
            jsonResultObject.put("BMI", this.getModel().getValue("kxr1_bmi").toString());
            jsonResultObject.put("shousuoya", this.getModel().getValue("kxr1_decimalfield").toString());
            jsonResultObject.put("shuzhangya", this.getModel().getValue("kxr1_decimalfield1").toString());
            jsonResultObject.put("yingyang", this.getModel().getValue("kxr1_textfield").toString());
            jsonResultObject.put("mianrong", this.getModel().getValue("kxr1_textfield1").toString());
            jsonResultObject.put("xinlv", this.getModel().getValue("kxr1_textfield2").toString());
            jsonResultObject.put("xinyin", this.getModel().getValue("kxr1_textfield3").toString());
            jsonResultObject.put("xinzangzayin", this.getModel().getValue("kxr1_textfield4").toString());
            jsonResultObject.put("feibu", this.getModel().getValue("kxr1_textfield5").toString());
            jsonResultObject.put("fubu", this.getModel().getValue("kxr1_textfield6").toString());
            jsonResultObject.put("ganzang", this.getModel().getValue("kxr1_textfield7").toString());
            jsonResultObject.put("pizang", this.getModel().getValue("kxr1_textfield8").toString());
            jsonResultObject.put("pifu", this.getModel().getValue("kxr1_textfield9").toString());
            jsonResultObject.put("jizhu", this.getModel().getValue("kxr1_textfield10").toString());
            jsonResultObject.put("qianbiaolinbajie", this.getModel().getValue("kxr1_textfield11").toString());
            jsonResultObject.put("jiazhuangxian", this.getModel().getValue("kxr1_textfield12").toString());
            jsonResultObject.put("sizhi", this.getModel().getValue("kxr1_textfield11").toString());

            //调用GPT开发平台微服务
            Map<String , String> variableMap = new HashMap<>();
            variableMap.put("taskResult", jsonResultObject.toJSONString());

            Object[] params = new Object[] {
                    //GPT提示编码
                    getPromptFid("prompt-24070101A29C0A"),
                    "",
                    variableMap
            };
            Map<String, Object> result = DispatchServiceHelper.invokeBizService("ai", "gai", "GaiPromptService", "syncCall", params);
            JSONObject jsonObjectResult = new JSONObject(result);
            JSONObject jsonObjectData = jsonObjectResult.getJSONObject("data");
            //设置值
            this.getModel().setValue("kxr1_evaluate_all", jsonObjectData.getString("llmValue"));
            Markdown mk = this.getView().getControl("kxr1_markdown");
            mk.setText(jsonObjectData.getString("llmValue"));
        }
        else if(StringUtils.equals("healthplan", opGpt)){
            String pageId = this.getView().getMainView().getPageId();
            String assistant ="您好!请输入“健康计划”，我们将根据您的健康分析，规划您的健康计划日任务~";
            Object pkValue = Long.parseLong("1987315383933101056");
            DispatchServiceHelper.invokeBizService( "ai" , "gai" ,"GaiService","selectProcessInSideBar",pkValue,pageId,assistant);
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
        Markdown mk = this.getView().getControl("kxr1_markdown");
        mk.setText(this.getModel().getValue("kxr1_evaluate_all").toString());
    }
}
