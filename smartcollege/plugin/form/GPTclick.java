package kxr1.smartcollege.smartcollege.plugin.form;

//import com.alibaba.druid.util.StringUtils;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.datamodel.IDataModel;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.ShowType;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.mvc.SessionManager;
import kd.bos.servicehelper.DispatchServiceHelper;
import kd.sdk.plugin.Plugin;

public class GPTclick extends AbstractFormPlugin implements Plugin {

    @Override
    public void beforeDoOperation(BeforeDoOperationEventArgs e) {

        super.beforeDoOperation(e);
        String opGpt = ((FormOperate) e.getSource()).getOperateKey();
        if(StringUtils.equals("opengpt", opGpt)){
            String pageId = this.getView().getMainView().getPageId();
            String assistant ="您好!我们将根据您的订单信息分析营养价值，并给出合理的营养健康建议~ 您可以输入：具体分析；简单分析营养价值...";
            Object pkValue = Long.parseLong("1982027273556998144");
            DispatchServiceHelper.invokeBizService( "ai" , "gai" ,"GaiService","selectProcessInSideBar",pkValue,pageId,assistant);
        }
        else if(StringUtils.equals("dishrecommend", opGpt)){
            String pageId = this.getView().getMainView().getPageId();
            String assistant ="您好!为了更好地满足您的口味，我们想了解您的菜品需求。请告诉我们您喜欢的口味、食材偏好或任何特殊的饮食要求。您可以输入：不要辣；喜欢香菜...";
            Object pkValue = Long.parseLong("1978399388099163136");
            DispatchServiceHelper.invokeBizService( "ai" , "gai" ,"GaiService","selectProcessInSideBar",pkValue,pageId,assistant);
        }
        else if(StringUtils.equals("gotoevaluate", opGpt)){
            FormShowParameter formShowParameter = new FormShowParameter();
            formShowParameter.setFormId("kxr1_dishevaluate");
            formShowParameter.getOpenStyle().setShowType(ShowType.Modal);
            this.getView().showForm(formShowParameter);
        }
    }

}
