package kxr1.smartcollege.smartcollege.plugin.form;

import kd.bos.context.RequestContext;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.form.control.Control;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.operation.SaveServiceHelper;

import java.util.Date;
import java.util.EventObject;
import java.util.List;
import java.util.Objects;

public class borrowconfirm extends AbstractFormPlugin {
    private Log courseConfirmLog = LogFactory.getLog("borrowConfirm");

    @Override
    public void afterCreateNewData(EventObject e) {
        Object primaryKey = this.getView().getFormShowParameter().getCustomParam("bookId");


    }

    @Override
    public void registerListener(EventObject e) {
        super.registerListener(e);
        // 侦听文本字段按钮点击事件
        this.addClickListeners("btnok");
    }

    @Override
    public void click(EventObject evt) {
        super.click(evt);
        Control source = (Control) evt.getSource();
        if (StringUtils.equals("btnok", source.getKey())) {
            Object primaryKey = this.getView().getFormShowParameter().getCustomParam("bookId");
            QFilter idFilter = new QFilter("id", QCP.equals, primaryKey);
            DynamicObject choosebook = BusinessDataServiceHelper.loadSingle("kxr1_book", "id,kxr1_integerfield,kxr1_textfield,kxr1_textfield1,kxr1_integerfield1", new QFilter[]{idFilter});
            if (null == choosebook) {
                this.getView().showTipNotification("选择书籍数据有误，请重新选择或联系管理员。");
                return;
            }
            // 校验是否可选，页面返回了提示信息。
            if (!verify(choosebook)) {
                return;
            }
            // 选择该课程（给我的课程中插入一条记录）
            // todo: 加锁
            int num=(int)choosebook.get("kxr1_integerfield");
            choosebook.set("kxr1_integerfield",num-1);
            SaveServiceHelper.update(choosebook);
            DynamicObject selfBook = BusinessDataServiceHelper.newDynamicObject("kxr1_bookshelf");
            selfBook.set("billstatus", "A");
            selfBook.set("kxr1_datefield", new Date());
            selfBook.set("kxr1_basedatafield", choosebook);
            selfBook.set("creator", RequestContext.get().getCurrUserId());
            //selfBook.submit();
            //selfBook.set("kxr1_combofield","inReview");
            OperationResult saveResult = SaveServiceHelper.saveOperate("kxr1_bookshelf", new DynamicObject[]{selfBook}, OperateOption.create());
            List<Object> successPkIds = saveResult.getSuccessPkIds();
            System.out.println(successPkIds.get(0));
            // 返回成功结果到父页面
            this.getView().returnDataToParent(true);
            this.getView().close();
        }

    }

    public boolean verify(DynamicObject chooseBook) {
        courseConfirmLog.info("verify begin ...");
        if (null != chooseBook) {
            RequestContext requestContext = RequestContext.get();
            long currUserId = requestContext.getCurrUserId();
            QFilter userFilter = new QFilter("creator", QCP.equals, currUserId);
            //QFilter stateFilter = new QFilter("kxr1_combofield", QCP.equals, "Borrowing");
            // 查用户的借书情况
            DynamicObject[] selfbooks =
                    BusinessDataServiceHelper.load("kxr1_bookshelf", "id,creator,billstatus,kxr1_combofield,kxr1_basedatafield.kxr1_integerfield", new QFilter[]{userFilter});
            if (null != selfbooks && selfbooks.length > 0) {
                for (DynamicObject selfbook : selfbooks) {
                    DynamicObject book = selfbook.getDynamicObject("kxr1_basedatafield");
                    if (book != null) {
                        String pkValue = book.getPkValue().toString();
                        // 图书id相同，该学生是否已经借过本书
                        if (StringUtils.equalsIgnoreCase(chooseBook.getPkValue().toString(), pkValue)) {
                            if(Objects.equals(selfbook.get("billstatus").toString(), "E")){
                                this.getView().showTipNotification("已在借阅中。");
                                return false;
                            }else if(Objects.equals(selfbook.get("billstatus").toString(), "B")){
                                this.getView().showTipNotification("已在申请中。");
                                return false;
                            }else if(Objects.equals(selfbook.get("billstatus").toString(), "A")){
                                this.getView().showTipNotification("申请已创建。");
                                return false;
                            }

                        } else if((int)book.get("kxr1_integerfield")<=0){
                            //没有借阅过本书，检查库存
                            this.getView().showTipNotification("库存不足。");
                            return false;

                        }
                    }
                }
                return true;
            }
            else{
                return true;
            }

        }
        return false;
    }
}