package kxr1.smartcollege.smartcollege.plugin.form;

import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.form.control.Label;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;

import java.util.EventObject;

public class schooltimetable extends AbstractFormPlugin {
    @Override
    public void beforeDoOperation(BeforeDoOperationEventArgs e) {
        super.beforeDoOperation(e);
        String opKey = ((FormOperate) e.getSource()).getOperateKey();
        if (StringUtils.equals("refresh", opKey)) {
            beforeBindData(e);
        }
    }
    @Override
    public void beforeBindData(EventObject e) {
        super.beforeBindData(e);
        //获取我的课程
        RequestContext requestContext = RequestContext.get();
        long currUserId = requestContext.getCurrUserId();
        QFilter userFilter = new QFilter("creator", QCP.equals, currUserId);
        QFilter stateFilter = new QFilter("kxr1_basedatafield.kxr1_combofield", QCP.not_equals, "CompletedClasses");
        // 查用户的选课情况
        DynamicObject[] selfcourses =
                BusinessDataServiceHelper.load("kxr1_mycourse", "id,kxr1_basedatafield,kxr1_basedatafield.kxr1_createrfield.name,kxr1_basedatafield.kxr1_textfield,kxr1_basedatafield.kxr1_coursetime1,kxr1_basedatafield.kxr1_coursetime1.number,kxr1_basedatafield.kxr1_basedatafield.fullname,creator", new QFilter[]{userFilter,stateFilter});
        for(DynamicObject selfcourse:selfcourses){
            String number=selfcourse.get("kxr1_basedatafield.kxr1_coursetime1.number").toString();
            String coursename=selfcourse.get("kxr1_basedatafield.name").toString();
            String tutor=selfcourse.get("kxr1_basedatafield.kxr1_createrfield.name").toString();
            String classroom=selfcourse.get("kxr1_basedatafield.kxr1_basedatafield.fullname").toString();
            String weeks=selfcourse.get("kxr1_basedatafield.kxr1_textfield").toString();
            String LABEL1 =null;
            String LABEL2 =null;
            switch (number){
                case "11":
                    LABEL1="kxr1_labelap121";
                    LABEL2="kxr1_labelap1211";break;
                case "12":
                    LABEL1="kxr1_labelap12111";
                    LABEL2="kxr1_labelap12112";break;
                case "13":
                    LABEL1="kxr1_labelap1212";
                    LABEL2="kxr1_labelap12113";break;
                case "14":
                    LABEL1="kxr1_labelap121111";
                    LABEL2="kxr1_labelap121121";break;
                case "15":
                    LABEL1="kxr1_labelap1211211";
                    LABEL2="kxr1_labelap1211111";break;
                case "21":
                    LABEL1="kxr1_labelap131";
                    LABEL2="kxr1_labelap1311";break;
                case "22":
                    LABEL1="kxr1_labelap13111";
                    LABEL2="kxr1_labelap13112";break;
                case "23":
                    LABEL1="kxr1_labelap1312";
                    LABEL2="kxr1_labelap13113";break;
                case "24":
                    LABEL1="kxr1_labelap131111";
                    LABEL2="kxr1_labelap131121";break;
                case "25":
                    LABEL1="kxr1_labelap1311211";
                    LABEL2="kxr1_labelap1311111";break;
                case "31":
                    LABEL1="kxr1_labelap141";
                    LABEL2="kxr1_labelap1411";break;
                case "32":
                    LABEL1="kxr1_labelap14111";
                    LABEL2="kxr1_labelap14112";break;
                case "33":
                    LABEL1="kxr1_labelap1412";
                    LABEL2="kxr1_labelap14113";break;
                case "34":
                    LABEL1="kxr1_labelap141111";
                    LABEL2="kxr1_labelap141121";break;
                case "35":
                    LABEL1="kxr1_labelap1411211";
                    LABEL2="kxr1_labelap1411111";break;
                case "41":
                    LABEL1="kxr1_labelap151";
                    LABEL2="kxr1_labelap1511";break;
                case "42":
                    LABEL1="kxr1_labelap15111";
                    LABEL2="kxr1_labelap15112";break;
                case "43":
                    LABEL1="kxr1_labelap1512";
                    LABEL2="kxr1_labelap15113";break;
                case "44":
                    LABEL1="kxr1_labelap151111";
                    LABEL2="kxr1_labelap151121";break;
                case "45":
                    LABEL1="kxr1_labelap1511211";
                    LABEL2="kxr1_labelap1511111";break;
                case "51":
                    LABEL1="kxr1_labelap161";
                    LABEL2="kxr1_labelap1611";break;
                case "52":
                    LABEL1="kxr1_labelap16111";
                    LABEL2="kxr1_labelap16112";break;
                case "53":
                    LABEL1="kxr1_labelap1612";
                    LABEL2="kxr1_labelap16113";break;
                case "54":
                    LABEL1="kxr1_labelap161111";
                    LABEL2="kxr1_labelap161121";break;
                case "55":
                    LABEL1="kxr1_labelap1611211";
                    LABEL2="kxr1_labelap1611111";break;
                case "61":
                    LABEL1="kxr1_labelap171";
                    LABEL2="kxr1_labelap1711";break;
                case "62":
                    LABEL1="kxr1_labelap17111";
                    LABEL2="kxr1_labelap17112";break;
                case "63":
                    LABEL1="kxr1_labelap1712";
                    LABEL2="kxr1_labelap17113";break;
                case "64":
                    LABEL1="kxr1_labelap171111";
                    LABEL2="kxr1_labelap171121";break;
                case "65":
                    LABEL1="kxr1_labelap1711211";
                    LABEL2="kxr1_labelap1711111";break;
                case "71":
                    LABEL1="kxr1_labelap18";
                    LABEL2="kxr1_labelap181";break;
                case "72":
                    LABEL1="kxr1_labelap1811";
                    LABEL2="kxr1_labelap1812";break;
                case "73":
                    LABEL1="kxr1_labelap182";
                    LABEL2="kxr1_labelap1813";break;
                case "74":
                    LABEL1="kxr1_labelap18111";
                    LABEL2="kxr1_labelap18121";break;
                case "75":
                    LABEL1="kxr1_labelap181211";
                    LABEL2="kxr1_labelap181111";break;

            }
            Label label = this.getView().getControl(LABEL1);
            label.setText(coursename+"\n"+classroom);
            Label label1 = this.getView().getControl(LABEL2);
            label1.setText("教师："+tutor+"\n"+"("+weeks+")");
        }

    }

}
