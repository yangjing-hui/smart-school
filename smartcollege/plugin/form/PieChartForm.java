package kxr1.smartcollege.smartcollege.plugin.form;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.form.FormShowParameter;
import kd.bos.form.chart.*;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.sdk.plugin.Plugin;

import java.math.BigDecimal;
import java.util.*;

public class PieChartForm extends AbstractFormPlugin implements Plugin {
    public PieChartForm() {
    }

    @Override
    public void afterCreateNewData(EventObject e) {
        super.afterCreateNewData(e);
        PieChart pieChart = (PieChart) this.getControl("kxr1_piechartap");
        pieChart.clearData();
        FormShowParameter formShowParameter = this.getView().getFormShowParameter();
        Object pkId = formShowParameter.getCustomParam("pkId");
        DynamicObject kxr1showdish = BusinessDataServiceHelper.loadSingle(pkId, "kxr1_dishorder");
        DynamicObjectCollection entryentity = kxr1showdish.getDynamicObjectCollection("entryentity");
        List<String> xDimensions = new ArrayList();
        int[] handleTaskData = new int[entryentity.size()];
        for (int i = 0; i < entryentity.size(); i++) {
            String kxr1Dish = entryentity.get(i).getDynamicObject("kxr1_dish").getString(2);
            //    int year = kxr1Datefield.getYear();
            xDimensions.add(kxr1Dish);
            int kxr1Qtyfield = entryentity.get(i).getInt("kxr1_qtyfield");
            handleTaskData[i] = kxr1Qtyfield;
        }
        pieChart.setShowTitle(false);
        pieChart.setShowTooltip(true);
        pieChart.addTooltip("trigger", "item");
        pieChart.setShowLegend(true);
        pieChart.setLegendPropValue("left", "center");

        //创建echarts中的series对象
        PieSeries series = pieChart.createPieSeries(ResManager.loadKDString("Access From", "PieChartCardDemoPlugin_0", "bos-portal-plugin", new Object[0]));
        ItemValue[] items = this.getDefaultProfitData(entryentity.size(), xDimensions, handleTaskData);
        series.setData(items);
        series.setPropValue("name", "Access From");
        series.setPropValue("type", "pie");
        series.setPropValue("avoidLabelOverlap", Boolean.FALSE);
        //设置内圆与外圆半径
        series.setRadius("40%", "70%");
        //构造series子属性
        //itemStyle
        Map map = new HashMap();
        map.put("borderRadius", 10);
        map.put("borderColor", "#fff");
        map.put("borderWidth", 2);
        series.setPropValue("itemStyle", map);
        //label
        map = new HashMap();
        Map normalMap = new HashMap();
        map.put("show", Boolean.FALSE);
        map.put("position", "center");
        series.setPropValue("label", map);
        //labelLine
        map = new HashMap();
        map.put("show", Boolean.FALSE);
        series.setPropValue("labelLine", map);
        //emphasis
        map = new HashMap();
        normalMap = new HashMap();
        map.put("label", normalMap);
        normalMap.put("show", Boolean.TRUE);
        normalMap.put("fontSize", 20);
        normalMap.put("fontWeight", "bold");
        series.setPropValue("emphasis", map);


    }

    private ItemValue[] getDefaultProfitData(int size, List<String> xDimensions, int[] value) {
        ItemValue[] items = new ItemValue[size];
        for (int i = 0; i < size; i++) {
            items[i] = new ItemValue(xDimensions.get(i), new BigDecimal(value[i]));
        }
        return items;
    }
}
