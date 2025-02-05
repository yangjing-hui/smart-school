package kxr1.smartcollege.smartcollege.plugin.form;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.form.gpt.IGPTAction;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.operation.SaveServiceHelper;

import java.util.HashMap;
import java.util.Map;



public class GPTdaytask implements IGPTAction {

    @Override
    public Map<String, String> invokeAction(String action, Map<String, String> params) {
        Map<String , String> result = new HashMap<>();
        if ("GET_JSON_STRING".equalsIgnoreCase(action)) {
            //将无效字符进行处理
            String jsonResult = params.get("jsonResult").replaceAll("\\s*|\r|\n|\t","");
            JSONObject resultJsonObject = null;
            try {
                //若全部生成JSON字符串，则不会进入catch
                resultJsonObject = JSON.parseObject(jsonResult);
            } catch (Exception ee) {
                //将"dayname"的上一个字符作为开始，以}]}字符作为结束，则最后需要+3
                jsonResult = jsonResult.substring(jsonResult.indexOf("\"dayName\"")-1 , jsonResult.indexOf("}]}")+3);
                resultJsonObject = JSON.parseObject(jsonResult);
            }
            //new一个DynamicObject表单对象
            DynamicObject dynamicObject = BusinessDataServiceHelper.newDynamicObject("kxr1_daytask");
            StringBuilder sb1 = new StringBuilder();
            for (int i = 1; i <= 10; i++) {
                int ascii = 48 + (int) (Math.random() * 9);
                char c = (char) ascii;
                sb1.append(c);
            }
            //设置对应属性
            dynamicObject.set("number", sb1.toString());
            dynamicObject.set("name", resultJsonObject.getString("dayName"));
            dynamicObject.set("status", "A");
            dynamicObject.set("enable", 1);
            dynamicObject.set("creator", RequestContext.get().getCurrUserId());
            //操作单据体
            DynamicObjectCollection dynamicObjectCollection = dynamicObject.getDynamicObjectCollection("kxr1_entryentity");
            for (Object object : resultJsonObject.getJSONArray("dayTaskList")) {
                JSONObject jsonObjectSingle = (JSONObject) object;
                DynamicObject dynamicObjectEntry = dynamicObjectCollection.addNew();
                dynamicObjectEntry.set("kxr1_task_name", jsonObjectSingle.getString("taskName"));
                dynamicObjectEntry.set("kxr1_expect_minutes", jsonObjectSingle.getString("finishMinutes"));
                dynamicObjectEntry.set("kxr1_diff", jsonObjectSingle.getString("diff"));
            }
            SaveServiceHelper.saveOperate("kxr1_daytask", new DynamicObject[] {dynamicObject}, null);
            Long pkId = (Long) dynamicObject.getPkValue();
            //拼接URL字符串
            String targetForm = "bizAction://currentPage?gaiShow=1&selectedProcessNumber=processNumber&gaiAction=showBillForm&gaiParams={\"appId\":\"kxr1_college\",\"billFormId\":\"kxr1_daytask\",\"billPkId\":\""+pkId+"\"}&title=日任务生成表单 &iconType=bill&method=bizAction";
            System.out.println(targetForm);
            result.put("formUrl", targetForm);
            //    result.put("formUrl", "1111");
            //    result.put("resultJsonObject", "sssss");
            result.put("resultJsonObject", resultJsonObject.toJSONString());
        }
        return result;
    }
}
