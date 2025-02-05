package kxr1.smartcollege.smartcollege.plugin.form;

import kd.bos.cache.CacheFactory;
import kd.bos.cache.TempFileCache;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.fileservice.FileItem;
import kd.bos.fileservice.FileService;
import kd.bos.fileservice.FileServiceFactory;
import kd.bos.form.control.AttachmentPanel;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.list.BillList;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.AttachmentServiceHelper;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.QueryServiceHelper;
import kd.bos.servicehelper.operation.SaveServiceHelper;
import kd.bos.servicehelper.user.UserServiceHelper;
import kd.bos.url.UrlService;
import kd.bos.util.FileNameUtils;

import java.io.InputStream;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InputInterface extends AbstractFormPlugin {
    @Override
    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        String opKey = ((FormOperate) args.getSource()).getOperateKey();
        if (StringUtils.equals("sub", opKey)) {


                //新建答疑单据
                // todo: 加锁
                DynamicObject newquestion = BusinessDataServiceHelper.newDynamicObject("kxr1_answeringquestion");
                newquestion.set("billstatus", "C");
                Long userId= RequestContext.get().getCurrUserId();
                newquestion.set("creator", userId);
                newquestion.set("kxr1_combofield", "1");
                newquestion.set("billno",number());
                OperationResult saveResult = SaveServiceHelper.saveOperate("kxr1_mycourse", new DynamicObject[]{newquestion}, OperateOption.create());
                List<Object> successPkIds = saveResult.getSuccessPkIds();
                System.out.println(successPkIds.get(0));
                if(!saveResult.isSuccess()){
                    this.getView().showTipNotification("数据出错！");
                    // 返回失败结果到父页面
                    this.getView().returnDataToParent(false);
                    this.getView().close();
                }
                else{
                    Object question = this.getModel().getValue("kxr1_textareafield");
                    DynamicObject tutor= (DynamicObject) this.getModel().getValue("kxr1_basedatafield");
                    Object number=tutor.get("number");
                    Object picture1= this.getModel().getValue("kxr1_picturefield");
                    Object picture2= this.getModel().getValue("kxr1_picturefield1");
                    Object picture3= this.getModel().getValue("kxr1_picturefield2");
                    QFilter qFilter = new QFilter("number", QCP.equals, number);
                    DynamicObject dobj = QueryServiceHelper.queryOne("bos_user", "id" ,qFilter.toArray());
                    Long tutorid=dobj.getLong("id");
                    // QFilter numberFilter = new QFilter("number", QCP.equals, number);
                   // DynamicObject tutor = BusinessDataServiceHelper.loadSingle("kxr1_perinform",new QFilter[]{numberFilter});
                    newquestion.set("kxr1_textareafield",question);
                    newquestion.set("kxr1_basedatafield",tutor);
                    newquestion.set("kxr1_userfield",tutorid);
                    QFilter qFilter1 = new QFilter("number", QCP.equals, "ID-000002");
                    QFilter qFilter2 = new QFilter("number", QCP.equals, "ID-001004");
                    Long admin1=QueryServiceHelper.queryOne("bos_user", "id" ,qFilter1.toArray()).getLong("id");
                    Long admin2=QueryServiceHelper.queryOne("bos_user", "id" ,qFilter2.toArray()).getLong("id");
                    newquestion.set("kxr1_userfield1",admin1);
                    newquestion.set("kxr1_userfield2",admin2);
                    newquestion.set("kxr1_picturefield",picture1);
                    newquestion.set("kxr1_picturefield1",picture2);
                    newquestion.set("kxr1_picturefield2",picture3);
                    SaveServiceHelper.update(newquestion);
                    this.getView().showSuccessNotification("提问成功。");

                }



            }

    }
    private String number(){
        // 生成包含数字和大写字母的8位随机码
        String chars = "0123456789ABCDEFGHIJKLMNOPQRST";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        String randomCode = sb.toString();
        return randomCode;
    }


}

