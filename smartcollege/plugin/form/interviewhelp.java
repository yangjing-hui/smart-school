package kxr1.smartcollege.smartcollege.plugin.form;

import com.alibaba.fastjson.JSONObject;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.ext.form.control.Markdown;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.DispatchServiceHelper;
import kd.sdk.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class interviewhelp extends AbstractFormPlugin implements Plugin {
    @Override
    public void beforeDoOperation(BeforeDoOperationEventArgs e) {

        super.beforeDoOperation(e);
        String opGpt = ((FormOperate) e.getSource()).getOperateKey();
        if (StringUtils.equals("interviewhelp", opGpt)) {
                String pageId = this.getView().getMainView().getPageId();
                String assistant ="您好!请输入您的意向岗位，我们会根据您的意向生成在线面试题目，请您根据题目作答，我们将对您的回答进行评估并给出合理建议~您可以输入：机器学习，前端开发...";
                Object pkValue = Long.parseLong("1992307429102074880");
                DispatchServiceHelper.invokeBizService( "ai" , "gai" ,"GaiService","selectProcessInSideBar",pkValue,pageId,assistant);

        }
    }
}