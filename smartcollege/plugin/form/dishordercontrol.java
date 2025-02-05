package kxr1.smartcollege.smartcollege.plugin.form;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import kd.bos.bill.BillShowParameter;
import kd.bos.bill.OperationStatus;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.form.control.Hyperlink;
import kd.bos.list.*;
import kd.bos.servicehelper.BusinessDataServiceHelper;

import kd.bos.bill.AbstractBillPlugIn;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.ValueTextItem;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.filter.FilterParameter;
import kd.bos.filter.CommonFilterColumn;
import kd.bos.filter.FilterColumn;
import kd.bos.filter.FilterContainer;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.control.EntryGrid;
import kd.bos.form.control.events.AfterSearchClickListener;
import kd.bos.form.control.events.FilterContainerInitEvent;
import kd.bos.form.control.events.FilterContainerInitListener;
import kd.bos.form.control.events.SearchClickEvent;
import kd.bos.form.events.*;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.DispatchServiceHelper;
import kd.bos.servicehelper.operation.SaveServiceHelper;

import java.util.*;

public class dishordercontrol extends AbstractBillPlugIn {
    private static String LIST_KEY = "kxr1_billlistap";//店铺列表单据列表
    private static String BILL_KEY = "kxr1_billlistap1";//店铺列表单据列表
    @Override
    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        String opKey = ((FormOperate)args.getSource()).getOperateKey();
        if ( StringUtils.equals("chooseshop", opKey)){
            BillList billlist = this.getControl(LIST_KEY);
            ListSelectedRow currentSelectedRowInfo = billlist.getCurrentSelectedRowInfo();
            Object primaryKey = currentSelectedRowInfo.getPrimaryKeyValue();
            QFilter idFilter = new QFilter("group.id", QCP.equals, primaryKey);
            BillList list = this.getControl(BILL_KEY);
            list.setClientQueryFilterParameter(new FilterParameter(idFilter, null));
            list.refresh();
            list.refreshData();
        }
        else if(StringUtils.equals("choosedish", opKey)){
            verify();
        }
        else if(StringUtils.equals("dishrecommend", opKey)){

            String pageId = this.getView().getMainView().getPageId();
            String assistant ="您好!为了更好地满足您的口味，我们想了解您的菜品需求。请告诉我们您喜欢的口味、食材偏好或任何特殊的饮食要求。您可以输入：不要辣；喜欢香菜...";
            Object pkValue = Long.parseLong("1978399388099163136");
            DispatchServiceHelper.invokeBizService( "ai" , "gai" ,"GaiService","selectProcessInSideBar",pkValue,pageId,assistant);

        }
        else if(StringUtils.equals("shopshow", opKey)){
            BillList billlist = this.getControl(LIST_KEY);
            ListSelectedRow currentSelectedRowInfo = billlist.getCurrentSelectedRowInfo();
            Object number = currentSelectedRowInfo.getNumber();
            QFilter idFilter = new QFilter("kxr1_basedatafield.number", QCP.equals, number);
            ListShowParameter lsp = new ListShowParameter();
            ListFilterParameter listFilterParameter=new ListFilterParameter();
            listFilterParameter.setFilter(idFilter);
            lsp.setBillFormId("kxr1_shopevaluate");
            lsp.setListFilterParameter(listFilterParameter);
            lsp.getOpenStyle().setShowType(ShowType.Modal);
            //lsp.setStatus(OperationStatus.EDIT);
            this.getView().showForm(lsp);
        }

    }
  //  @Override
    //public void beforeBindData(EventObject e) {
      //  super.beforeBindData(e);
        //F7SelectedList mobF7SelectedList = this.getControl("kxr1_f7selectedlistap");
//        List<ValueTextItem> selectedData = new ArrayList<>(16);
//
  //      String value = "some value";
    //    String text = "Some text"; // 假设要设置的文本信息

// 创建 ValueTextItem 对象
   //     ValueTextItem valueTextItem = new ValueTextItem(value, text);
     //   selectedData.add(valueTextItem);
      //  mobF7SelectedList.addItems(selectedData);
   // }
    public void verify(){
        // 获取列表选中行
        BillList billlist = this.getControl(BILL_KEY);
          ListSelectedRowCollection selectedRows =billlist.getSelectedRows();
        //   ListSelectedRow currentSelectedRowInfo = billlist.getCurrentSelectedRowInfo();
        //  Object primaryKey = currentSelectedRowInfo.getPrimaryKeyValue();
        //new一个DynamicObject表单对象
        DynamicObject dynamicObject = BusinessDataServiceHelper.newDynamicObject("kxr1_dishorder");
        //设置对应属性
        dynamicObject.set("creator", RequestContext.get().getCurrUserId());
        dynamicObject.set("billstatus", "A");

        // 查用户的订餐情况
        DynamicObjectCollection dynamicObjectCollection = dynamicObject.getDynamicObjectCollection("entryentity");
        for(int i=0;i<selectedRows.size();i++) {
            Object primaryKeyValue = selectedRows.get(i).getPrimaryKeyValue();
            //获取当前实体
            QFilter idFilter = new QFilter("id", QCP.equals, primaryKeyValue);

            DynamicObject[] selfdishes =
                    BusinessDataServiceHelper.load("kxr1_dishes", "id", new QFilter[]{idFilter});
            for(DynamicObject selfcourse : selfdishes){
                DynamicObject dynamicObjectEntry = dynamicObjectCollection.addNew();
                dynamicObjectEntry.set("kxr1_dish", selfcourse.getPkValue());
                dynamicObjectEntry.set("kxr1_qtyfield", 1);
            }
        }

        SaveServiceHelper.saveOperate("kxr1_dishorder", new DynamicObject[] {dynamicObject}, null);
        Long pkId = (Long) dynamicObject.getPkValue();

        BillShowParameter showParameter = new BillShowParameter();
        showParameter.setFormId("kxr1_dishorder");
        showParameter.getOpenStyle().setShowType(ShowType.InCurrentForm);
        showParameter.setStatus(OperationStatus.EDIT);
        showParameter.setPkId(pkId);
        this.getView().showForm(showParameter);
     //   FormShowParameter formShowParameter = new FormShowParameter();
      //  formShowParameter.setFormId("kxr1_dishorder");
      //  formShowParameter.setCustomParam("id", pkId);
      //  formShowParameter.setPkId(id);
      //  formShowParameter.getOpenStyle().setShowType(ShowType.InCurrentForm);
      //  this.getView().showForm(formShowParameter);
    //    String url = "bizAction://currentPage?gaiShow=1&selectedProcessNumber=processNumber&gaiAction=showBillForm&gaiParams={\"appId\":\"kxr1_college\",\"billFormId\":\"kxr1_dishorder\",\"billPkId\":\""+pkId+"\"}&title=订单生成 &iconType=bill&method=bizAction";
      //  Hyperlink link = this.getView().getControl("kxr1_hyperlinkap");
        // 插件动态设置超链接URL
      //  link.setUrl(url);
        this.getView().updateView();
    }
}



