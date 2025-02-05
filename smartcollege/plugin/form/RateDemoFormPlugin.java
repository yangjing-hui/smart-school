package kxr1.smartcollege.smartcollege.plugin.form;

import java.util.EventObject;

import com.alibaba.fastjson.JSONObject;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.filter.FilterParameter;
import kd.bos.form.ShowType;
import kd.bos.form.chart.ItemValue;
import kd.bos.form.chart.PieChart;
import kd.bos.form.chart.PieSeries;
import kd.bos.form.chart.Position;
import kd.bos.form.chart.RoseType;
import kd.bos.form.control.Rate;
import kd.bos.form.control.events.RateEvent;
import kd.bos.form.control.events.RateListener;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.list.BillList;
import kd.bos.list.ListFilterParameter;
import kd.bos.list.ListShowParameter;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.DispatchServiceHelper;
import kd.bos.servicehelper.operation.SaveServiceHelper;

public class RateDemoFormPlugin extends AbstractFormPlugin implements RateListener {
        @Override
        public void registerListener(EventObject e) {
            // TODO Auto-generated method stub
            super.registerListener(e);
            Rate rate = this.getControl("kxr1_rateap");
            rate.addRateListener(this);
            Rate rate1 = this.getControl("kxr1_rateap1");
            rate1.addRateListener(this);
            Rate rate2 = this.getControl("kxr1_rateap2");
            rate2.addRateListener(this);
        }

        @Override
        public void afterCreateNewData(EventObject e) {
            // TODO Auto-generated method stub
            super.afterCreateNewData(e);
            PieChart pieChart = this.getControl("kxr1_piechartap");
            this.drawChart(pieChart);
        }

        @Override
        public void beforeBindData(EventObject e) {
            // TODO Auto-generated method stub
            super.beforeBindData(e);
        }

        @Override
        public void update(RateEvent evt) {
            // TODO Auto-generated method stub
            Number rateScore = evt.getRateScore();
            Rate rate = (Rate) evt.getSource();
            String key = rate.getKey();
            this.getPageCache().put(key, rateScore.toString());
            Number sum = this.handleSumRate(rateScore, key);
            this.getModel().setValue("kxr1_textfield", sum);
            PieChart pieChart = this.getControl("kxr1_piechartap");
            this.drawChart(pieChart);
            //必须刷新，否则不触发签到渲染
            pieChart.refresh();
        }

        public Number handleSumRate(Number score, String key) {
            Number sum = score;
            if (!key.equals("kxr1_rateap")) {
                sum = getSum(sum, "kxr1_rateap");
            }
            if (!key.equals("kxr1_rateap1")) {
                sum = getSum(sum, "kxr1_rateap1");
            }
            if (!key.equals("kxr1_rateap2")) {
                sum = getSum(sum, "kxr1_rateap2");
            }
            Number rate5 = sum.intValue() / 3;
            Rate rate = this.getControl("kxr1_rateap21");
            rate.setRateScore(rate5);
            return rate5;
        }

        private Number getSum(Number sum, String key) {
            String rate = this.getPageCache().get(key);
            if (rate != null) {
                sum = sum.intValue() + Integer.valueOf(rate);
            }
            return sum;
        }

        public void drawChart(PieChart pieChart) {
            pieChart.clearData();
            pieChart.setShowTooltip(true);
            //设置为位置
            pieChart.setMargin(Position.right, "30px");
            pieChart.setMargin(Position.top, "30px");
            pieChart.setMargin(Position.bottom, "10px");
            pieChart.setMargin(Position.left, "20px");
            //添加数据
            PieSeries series = pieChart.createPieSeries("评分");
            String rateStr = this.getPageCache().get("kxr1_rateap");
            String rate2Str = this.getPageCache().get("kxr1_rateap1");
            String rate3Str = this.getPageCache().get("kxr1_rateap2");
            ItemValue[] data = new ItemValue[3];
            ItemValue data1 = new ItemValue("总体", getRateNumber(rateStr), "#ffb61e");
            ItemValue data2 = new ItemValue("口味", getRateNumber(rate2Str), "#ff8936");
            ItemValue data3 = new ItemValue("包装", getRateNumber(rate3Str), "#ff7500");
            data[0] = data1;
            data[1] = data2;
            data[2] = data3;
            series.setData(data);
            //series样式
            series.setRoseType(RoseType.radius);
        }

        private Number getRateNumber(String rateStr) {
            Number rate = 0;
            if (rateStr != null) {
                rate = Integer.valueOf(rateStr);
            }
            return rate;
        }
    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        String opKey = ((FormOperate)args.getSource()).getOperateKey();
        if ( StringUtils.equals("evaluate", opKey)){
            //new一个DynamicObject表单对象
            DynamicObject dynamicObject = BusinessDataServiceHelper.newDynamicObject("kxr1_shopevaluate");
            StringBuilder sb1 = new StringBuilder();
            for (int i = 1; i <= 10; i++) {
                int ascii = 48 + (int) (Math.random() * 9);
                char c = (char) ascii;
                sb1.append(c);
            }
            //设置对应属性
            dynamicObject.set("number", sb1.toString());
            dynamicObject.set("name", "评价");
            dynamicObject.set("status", "B");
            dynamicObject.set("creator", RequestContext.get().getCurrUserId());
            dynamicObject.set("enable", 1);
            dynamicObject.set("kxr1_basedatafield", this.getModel().getValue("kxr1_basedatafield"));
            dynamicObject.set("kxr1_textfield", this.getModel().getValue("kxr1_textfield").toString());
            dynamicObject.set("kxr1_textfield1", this.getModel().getValue("kxr1_textareafield").toString());
            dynamicObject.set("kxr1_picturefield", this.getModel().getValue("kxr1_picturefield"));

            SaveServiceHelper.saveOperate("kxr1_shopevaluate", new DynamicObject[] {dynamicObject}, null);
            this.getView().showSuccessNotification("已提交！");
        }
    }
}


