package kxr1.smartcollege.smartcollege.plugin.form;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

import kd.bos.form.FormShowParameter;
import kd.bos.form.IFormView;
import kd.bos.form.ShowType;
import kd.bos.form.container.Tab;
import kd.bos.form.plugin.AbstractFormPlugin;
public class MainPage extends AbstractFormPlugin{
    @Override
    public void afterCreateNewData(EventObject e) {
        // TODO Auto-generated method stub
        super.afterCreateNewData(e);
        showSpecificForm("kxr1_college", "智慧校园", "kxr1_tabpageap", "_apphome");
    }
    @Override
    public void afterBindData(EventObject e) {
// TODO Auto-generated method stub
        super.afterBindData(e);
        Tab tab = getControl("tabap");
        tab.activeTab("kxr1_tabpageap"); //这里填写新增页签那个的标识
    }

    private void showSpecificForm(String appId,String appName,String targetKey,String formId)
    {
        FormShowParameter fp = new FormShowParameter();
        IFormView mv = getView().getMainView();
        fp.setAppId(appId);
        fp.getOpenStyle().setTargetKey(targetKey);
        fp.getOpenStyle().setShowType(ShowType.InContainer);
        fp.setHasRight(true);
        fp.setFormId(appId+formId);
        fp.setPageId("kxr1_college"+getView().getMainView().getPageId());
        Map<String,Object> a = new HashMap();
        a.put("appid", appId);
        a.put("appname", appName);
        a.put("appmainnumber", appId+formId);
        fp.setCustomParams(a);
        getView().showForm(fp);
    }
}
