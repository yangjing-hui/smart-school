package kxr1.smartcollege.smartcollege.plugin.mob;

import kd.bos.context.RequestContext;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.AbstractMobFormPlugin;
import kd.bos.url.UrlService;

public class dishperson extends AbstractMobFormPlugin {
    @Override
    public void beforeDoOperation(BeforeDoOperationEventArgs e) {
        super.beforeDoOperation(e);
        String opGpt = ((FormOperate) e.getSource()).getOperateKey();
        if (StringUtils.equals("evaluate", opGpt)) {

            String domain = UrlService.getDomainContextUrl() + "/index.html";
            this.getView().openUrl(domain + "?formId=" + "kxr1_dishevaluate" + "&accountId=" + RequestContext.get().getAccountId());
        }
    }
}
