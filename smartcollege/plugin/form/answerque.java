package kxr1.smartcollege.smartcollege.plugin.form;

import kd.bos.bill.AbstractBillPlugIn;
import kd.bos.cache.CacheFactory;
import kd.bos.cache.TempFileCache;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.fileservice.FileItem;
import kd.bos.fileservice.FileService;
import kd.bos.fileservice.FileServiceFactory;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.operate.FormOperate;
import kd.bos.list.IListView;
import kd.bos.servicehelper.AttachmentServiceHelper;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.user.UserServiceHelper;
import kd.bos.util.FileNameUtils;

import java.io.InputStream;
import java.util.*;

public class answerque extends AbstractBillPlugIn {

    @Override
    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        String opKey = ((FormOperate) args.getSource()).getOperateKey();
        if (StringUtils.equals("answer", opKey)) {
            String questionstate=this.getModel().getValue("kxr1_combofield").toString();
            if(questionstate.equals("1")){
                FormShowParameter formShowParameter = new FormShowParameter();
                formShowParameter.setFormId("kxr1_answerq");
                formShowParameter.setCustomParam("question", this.getModel().getDataEntity(true).getPkValue());
                formShowParameter.setCloseCallBack(new CloseCallBack(this, "baritemap"));
                formShowParameter.getOpenStyle().setShowType(ShowType.Modal);
                this.getView().showForm(formShowParameter);
            }
            else if(questionstate.equals("2")){
                this.getView().showTipNotification("该问题已回答。");
            }

        }

        }
    @Override
    public void registerListener(EventObject e) {
        // TODO Auto-generated method stub
        super.registerListener(e);
    }
    // 弹窗返回事件,未落库附件面板到落库附件面板
    @Override
    public void closedCallBack(ClosedCallBackEvent ccb) {
        if (ccb.getActionId().equals("baritemap")) {
            Object returnData = ccb.getReturnData();
            if (null != returnData) {
                Boolean result = (Boolean)returnData;
                if (result) {
                    this.getView().showSuccessNotification("回答成功。");
                }
            }
        }
    }
    // 构建AttachmentServiceHelper.upload的附件面板数据
    private Map<String, Object> createAttMap(DynamicObject attDoj) {
        Map<String, Object> map = new HashMap<String, Object>();
        String url = attDoj.getString("url");
        String name = attDoj.getString("name");
        if (url.contains("configKey=redis.serversForCache&id=tempfile")) {
            // 持久化附件到服务器
            url = uploadTempfile(url, name);
            map.put("url", url);
        }
        map.put("creator", UserServiceHelper.getCurrentUserId());
        long time = new Date().getTime();
        map.put("modifytime", time);
        map.put("createdate", time);
        map.put("status", "success");
        map.put("type", attDoj.get("type"));
        map.put("name", name);
        StringBuffer uid = new StringBuffer();
        uid.append("rc-upload-");
        uid.append(time);
        uid.append("-");
        uid.append("1");
        map.put("uid", uid.toString());
        map.put("size", attDoj.get("size"));
        return map;
    }

    /**
     * 上传临时文件到服务器中
     *
     * @param url
     * @param name
     * @return
     */
    private String uploadTempfile(String url, String name) {
        TempFileCache cache = CacheFactory.getCommonCacheFactory().getTempFileCache();
        InputStream in = cache.getInputStream(url);
        FileService service = FileServiceFactory.getAttachmentFileService();
        FileService fs = FileServiceFactory.getAttachmentFileService();
        RequestContext requestContext = RequestContext.get();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        // 生成文件路径-上传附件时远程服务器需要存储文件的位置
        String pathParam = FileNameUtils.getAttachmentFileName(requestContext.getTenantId(),
                requestContext.getAccountId(), uuid, name);
        FileItem fileItem = new FileItem(name, pathParam, in);
        // cache.remove(url);
        // 上传附件到文件服务器 UrlService.getDomainContextUrl()+ "/attachment/download.do?path=/"
        // +
        String downUrl = service.upload(fileItem);
        return downUrl;
    }


}
