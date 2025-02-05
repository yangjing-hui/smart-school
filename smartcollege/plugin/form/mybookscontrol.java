package kxr1.smartcollege.smartcollege.plugin.form;

import kd.bos.bill.AbstractBillPlugIn;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.operate.FormOperate;
import kd.bos.list.BillList;
import kd.bos.list.plugin.AbstractListPlugin;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.operation.SaveServiceHelper;

import java.util.Date;

public class mybookscontrol extends AbstractListPlugin {

    private static String LIST_KEY = "kxr1_bookshelf";//选课列表单据列表
    public void beforeDoOperation(BeforeDoOperationEventArgs e) {
        super.beforeDoOperation(e);
        String opKey = ((FormOperate)e.getSource()).getOperateKey();
        if ( StringUtils.equals("returnbook", opKey)){

            // 获取列表选中行
            Object primaryKey =this.getFocusRowPkId();
            RequestContext requestContext = RequestContext.get();
            QFilter bookFilter = new QFilter("id", QCP.equals, primaryKey);
            DynamicObject chooseBook = BusinessDataServiceHelper.loadSingle("kxr1_bookshelf", "id,kxr1_basedatafield,billstatus,kxr1_timereturn", new QFilter[]{bookFilter});
            if(chooseBook.get("billstatus").equals("C")){
                chooseBook.set("billstatus","D");//状态设置为已归还
                chooseBook.set("kxr1_timereturn",new Date());
                SaveServiceHelper.update(chooseBook);//归还时间设置
                DynamicObject book = chooseBook.getDynamicObject("kxr1_basedatafield");
                int num=(int)book.get("kxr1_integerfield")+1;
                book.set("kxr1_integerfield",num);
                SaveServiceHelper.update(book);//图书库存+1
                this.getView().showSuccessNotification("归还成功。");
            }
            else{
                this.getView().showErrorNotification("无法归还");
            }

        }

    }
}
