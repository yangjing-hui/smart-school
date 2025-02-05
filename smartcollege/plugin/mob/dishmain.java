package kxr1.smartcollege.smartcollege.plugin.mob;

import kd.bos.bill.BillShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.form.MobileFormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.AbstractMobFormPlugin;
import kd.bos.list.*;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.DispatchServiceHelper;
import kd.bos.servicehelper.operation.SaveServiceHelper;
import kd.bos.url.UrlService;

public class dishmain extends AbstractMobFormPlugin {
    @Override
    public void beforeDoOperation(BeforeDoOperationEventArgs e) {

        super.beforeDoOperation(e);
        String opGpt = ((FormOperate) e.getSource()).getOperateKey();
      /*  if (StringUtils.equals("entershop", opGpt)) {
            BillList billlist = this.getControl("kxr1_billlistap");
            ListSelectedRow currentSelectedRowInfo = billlist.getCurrentSelectedRowInfo();
       //     Object number = currentSelectedRowInfo.getNumber();
      //      QFilter idFilter = new QFilter("kxr1_basedatafield.number", QCP.equals, number);
            MobileFormShowParameter lsp = new MobileFormShowParameter();
      //      ListFilterParameter listFilterParameter=new ListFilterParameter();
      //      listFilterParameter.setFilter(idFilter);
            lsp.setFormId("kxr1_dishall");
       //     lsp.setCustomParam("kxr1_shopname",currentSelectedRowInfo.getName());
       //     lsp.setCustomParam("kxr1_shopname",currentSelectedRowInfo.getName());
      //      lsp.setBillFormId("kxr1_shopevaluate");
      //      lsp.setListFilterParameter(listFilterParameter);
            lsp.getOpenStyle().setShowType(ShowType.Floating);
            //lsp.setStatus(OperationStatus.EDIT);
            this.getView().showForm(lsp);

            BillShowParameter showParameter = new BillShowParameter();
            showParameter.setFormId("kxr1_dishall");
            showParameter.getOpenStyle().setShowType(ShowType.Floating);
            showParameter.setStatus(OperationStatus.EDIT);
        //    showParameter.setPkId(pkId);
            this.getView().showForm(showParameter);
        }*/
        if (StringUtils.equals("choosedish", opGpt)) {
            // 获取列表选中行
            BillList billlist = this.getControl("kxr1_billlistap");
            ListSelectedRowCollection selectedRows = billlist.getSelectedRows();
            //   ListSelectedRow currentSelectedRowInfo = billlist.getCurrentSelectedRowInfo();
            //  Object primaryKey = currentSelectedRowInfo.getPrimaryKeyValue();
            //new一个DynamicObject表单对象
            DynamicObject dynamicObject = BusinessDataServiceHelper.newDynamicObject("kxr1_dishorder");
            //设置对应属性
            dynamicObject.set("creator", RequestContext.get().getCurrUserId());
            dynamicObject.set("billstatus", "A");

            // 查用户的订餐情况
            DynamicObjectCollection dynamicObjectCollection = dynamicObject.getDynamicObjectCollection("entryentity");
            for (int i = 0; i < selectedRows.size(); i++) {
                Object primaryKeyValue = selectedRows.get(i).getPrimaryKeyValue();
                //获取当前实体
                QFilter idFilter = new QFilter("id", QCP.equals, primaryKeyValue);

                DynamicObject[] selfdishes =
                        BusinessDataServiceHelper.load("kxr1_dishes", "id", new QFilter[]{idFilter});
                for (DynamicObject selfcourse : selfdishes) {
                    DynamicObject dynamicObjectEntry = dynamicObjectCollection.addNew();
                    dynamicObjectEntry.set("kxr1_dish", selfcourse.getPkValue());
                    dynamicObjectEntry.set("kxr1_qtyfield", 1);
                }
            }

            SaveServiceHelper.saveOperate("kxr1_dishorder", new DynamicObject[]{dynamicObject}, null);
            Long pkId = (Long) dynamicObject.getPkValue();
            String domain = UrlService.getDomainContextUrl()+"/index.html";
            this.getView().openUrl(domain+"?formId="+"kxr1_dishorder"+"&accountId="+ RequestContext.get().getAccountId()+"&pkId="+pkId);

          /*  BillShowParameter showParameter = new BillShowParameter();
            showParameter.setFormId("kxr1_dishorder");
            showParameter.getOpenStyle().setShowType(ShowType.Floating);
            showParameter.setStatus(OperationStatus.EDIT);
            showParameter.setPkId(pkId);
            this.getView().showForm(showParameter);*/
        }
    }
}
