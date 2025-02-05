package kxr1.smartcollege.smartcollege.plugin.mob;

import kd.bos.context.RequestContext;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.AbstractMobFormPlugin;
import kd.bos.url.UrlService;

public class openapp extends AbstractMobFormPlugin {
    @Override
    public void beforeDoOperation(BeforeDoOperationEventArgs e) {
        super.beforeDoOperation(e);
        String opGpt = ((FormOperate) e.getSource()).getOperateKey();
        if (StringUtils.equals("openmycourse", opGpt)) {
            String domain = UrlService.getDomainContextUrl()+"/index.html";
            this.getView().openUrl(domain+"?formId="+"kxr1_schooltimetable"+"&accountId="+ RequestContext.get().getAccountId());
        }
        else if (StringUtils.equals("choosecourse", opGpt)) {
            String domain = UrlService.getDomainContextUrl()+"/index.html";
            this.getView().openUrl(domain+"?formId="+"kxr1_cselectionlist"+"&accountId="+ RequestContext.get().getAccountId());
        }
        else if (StringUtils.equals("openbooklist", opGpt)) {
            String domain = UrlService.getDomainContextUrl()+"/index.html";
            this.getView().openUrl(domain+"?formId="+"kxr1_booklist"+"&accountId="+ RequestContext.get().getAccountId());
        }
        else if (StringUtils.equals("change", opGpt)) {
            String domain = UrlService.getDomainContextUrl()+"/index.html";
            this.getView().openUrl(domain+"?formId="+"kxr1_dorchange"+"&accountId="+ RequestContext.get().getAccountId());
        }
        else if (StringUtils.equals("leave", opGpt)) {
            String domain = UrlService.getDomainContextUrl()+"/index.html";
            this.getView().openUrl(domain+"?formId="+"kxr1_leave"+"&accountId="+ RequestContext.get().getAccountId());
        }

        else if (StringUtils.equals("resume", opGpt)) {
            String domain = UrlService.getDomainContextUrl()+"/index.html";
            this.getView().openUrl(domain+"?formId="+"kxr1_resume"+"&accountId="+ RequestContext.get().getAccountId());
        }
        else if (StringUtils.equals("opendish", opGpt)) {
            String domain = UrlService.getDomainContextUrl()+"/index.html";
            this.getView().openUrl(domain+"?formId="+"kxr1_dishshow"+"&accountId="+ RequestContext.get().getAccountId());
        }
    }
}
