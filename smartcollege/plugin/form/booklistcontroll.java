package kxr1.smartcollege.smartcollege.plugin.form;

import com.grapecity.documents.excel.Q;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.filter.FilterParameter;
import kd.bos.filter.CommonFilterColumn;
import kd.bos.filter.FilterColumn;
import kd.bos.filter.FilterContainer;
import kd.bos.form.control.events.AfterSearchClickListener;
import kd.bos.form.control.events.FilterContainerInitEvent;
import kd.bos.form.control.events.SearchClickEvent;
import kd.bos.form.events.*;
import kd.bos.form.events.AfterBindDataEvent;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.list.BillList;
import kd.bos.form.control.events.FilterContainerInitListener;
import kd.bos.list.plugin.AbstractListPlugin;
import kd.bos.mvc.list.AbstractListView;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Filter;

public class booklistcontroll extends AbstractFormPlugin implements FilterContainerInitListener,AfterSearchClickListener,AfterBindDataListener,BeforeBindDataListener {
    private static String BILL_FORM_ID = "kxr1_book";
    private static final String FILTERCONTAINERID = "kxr1_filtercontainerap";
    private static String BILLLISTAP = "kxr1_billlistap";//图书列表单据列表

    @Override
    public void initialize() {
        super.initialize();
        BillList billList = getControl("billlistap");
        FilterContainer filterContainer = this.getControl(FILTERCONTAINERID);
        if (filterContainer != null) {
            filterContainer.setBillFormId(BILL_FORM_ID);
            filterContainer.setNeedShareScheme(false);
            filterContainer.addFilterContainerInitListener(this);

        }
    }


    @Override
    public void registerListener(EventObject e) {
        super.registerListener(e);
        BillList list = this.getControl(BILLLISTAP);
        if (list != null) {
            list.addAfterBindDataListener(this);
        }
        FilterContainer filterContainer = this.getControl(FILTERCONTAINERID);
        if (filterContainer != null) {
            filterContainer.addAfterSearchClickListener(this);
        }

    }

    @Override
    public void beforeBindData(BeforeBindDataEvent beforeBindDataEvent) {
        //单据列表触发绑定数据前的事件,需要先增加单据列表事件监听
    }

    @Override
    public void beforeBindData(EventObject e) {
        super.beforeBindData(e);
        BillList list = this.getControl(BILLLISTAP);
        if (list != null) {
            list.addAfterBindDataListener(this);
            list.addBeforeBindDataListener(this);
            // 设置 常用、方案过滤条件参数
            list.setClientQueryFilterParameter(new FilterParameter(filterInit(), null));
        }
    }

    @Override
    public void afterBindData(AfterBindDataEvent afterBindDataEvent) {
        //单据列表触发绑定数据后的事件,需要先增加单据列表事件监听
    }

    @Override
    public void afterBindData(EventObject e) {
        super.afterBindData(e);
    }

    /**
     * 初始化过滤根据业务需求过滤
     *
     * @return
     */
    private QFilter filterInit() {
        //TODO 初始化过滤
        return null;
    }

    /**
     * 初始化过滤列默认为单据->列表配置的过滤列
     *
     * @param filterContainerInitEvent
     */
    @Override
    public void filterContainerInit(FilterContainerInitEvent filterContainerInitEvent) {
        List<FilterColumn> columnList = filterContainerInitEvent.getCommonFilterColumns();
        List<FilterColumn> schemeList = filterContainerInitEvent.getSchemeFilterColumns();
        Iterator<FilterColumn> filterColumnIterator = columnList.iterator();

        while (filterColumnIterator.hasNext()) {
            FilterColumn filterColumn = filterColumnIterator.next();
            String fieldName = filterColumn.getFieldName();
            if (StringUtils.equals(fieldName, "group.name")) {
                //设置初始值
                filterColumn.setDefaultValues("");

            } else if (StringUtils.equals(fieldName, "enable")) {

            } else {
                filterColumnIterator.remove();
            }
        }

        Iterator<FilterColumn> filterColumnSchemeIterator = schemeList.iterator();
        while (filterColumnSchemeIterator.hasNext()) {
            FilterColumn filterColumn = filterColumnSchemeIterator.next();
            String fieldName = filterColumn.getFieldName();
            if (!StringUtils.equals(fieldName, "group") && !StringUtils.equals(fieldName, "enable")) {
                filterColumnSchemeIterator.remove();
            }
        }

    }

    @Override
    public void click(SearchClickEvent searchClickEvent) {
        BillList list=this.getControl(BILLLISTAP);

        List<QFilter> qFiltersClient=new ArrayList();
        QFilter myPartner=filterInit();
        qFiltersClient.add(myPartner);

        //只过滤制定属性
        QFilter qFilterGroup=searchClickEvent.getQFilter("group");
        if(qFilterGroup!=null){
            qFiltersClient.add(qFilterGroup);
        }

        //只过滤制定属性
        QFilter qFilterEnable=searchClickEvent.getQFilter("enable");
        if(qFilterEnable!=null){
            qFiltersClient.add(qFilterEnable);
        }

        //设置常用、方案过滤条件参数
        list.setClientQueryFilterParameter(new FilterParameter(qFiltersClient,null));
        //设置快速过滤/左数过滤参数
        list.setQueryFilterParameter(new FilterParameter(searchClickEvent.getFastQFilters(),null));
        this.getView().updateView(BILLLISTAP);

    }

    @Override
    public void afterDoOperation(AfterDoOperationEventArgs afterDoOperationEventArgs) {
        super.afterDoOperation(afterDoOperationEventArgs);
        //点击刷新刷新页面
        if(StringUtils.equals("refresh",afterDoOperationEventArgs.getOperateKey())){
            BillList billList=this.getControl(BILLLISTAP);
            if (billList != null) {
                billList.clearSelection();
                billList.refresh();
            }


        }


    }
}
