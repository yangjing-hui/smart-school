package kxr1.smartcollege.smartcollege.plugin.form;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.form.control.Control;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.org.utils.Consts;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.operation.OperationServiceHelper;
import kd.bos.servicehelper.operation.SaveServiceHelper;
import kd.bos.servicehelper.user.UserServiceHelper;

import java.util.Date;
import java.util.EventObject;
import java.util.List;

public class CourseConfirm extends AbstractFormPlugin{

    private Log courseConfirmLog = LogFactory.getLog("CourseConfirm");
    @Override
    public void afterCreateNewData(EventObject e) {
        Object primaryKey = this.getView().getFormShowParameter().getCustomParam("courseId");
        if (null != primaryKey) {
            this.getModel().setValue("kxr1_basedatafield", primaryKey);//绑定基础资料字段
        }

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
            Object primaryKey = this.getView().getFormShowParameter().getCustomParam("courseId");
            QFilter idFilter = new QFilter("id", QCP.equals, primaryKey);
            DynamicObject chooseCourse = BusinessDataServiceHelper.loadSingle("kxr1_coursei", "id,kxr1_coursetime1,kxr1_coursetime1.number,kxr1_integerfield1,kxr1_integerfield,kxr1_textfield1", new QFilter[]{idFilter});
            if (null == chooseCourse) {
                this.getView().showTipNotification("选择课程数据有误，请重新选择或联系管理员。");
                return;
            }
            // 校验是否可选，页面返回了提示信息。
            if (!verify(chooseCourse)) {
                return;
            }
            // 选择该课程（给我的课程中插入一条记录）
            // todo: 加锁
            DynamicObject selfCourse = BusinessDataServiceHelper.newDynamicObject("kxr1_mycourse");
            selfCourse.set("billstatus", "C");
            selfCourse.set("kxr1_datefield", new Date());
            selfCourse.set("kxr1_basedatafield", chooseCourse);
            Long userId=RequestContext.get().getCurrUserId();
            selfCourse.set("creator", userId);
            Object number= UserServiceHelper.getUserInfoByID(userId).get("number");
            QFilter numberFilter = new QFilter("number", QCP.equals, number);
            DynamicObject me =
                    BusinessDataServiceHelper.loadSingle("kxr1_perinform","id,kxr1_combofield3",new QFilter[]{numberFilter});
            String xq=me.getString("kxr1_combofield3");
            selfCourse.set("kxr1_combofield",xq);
            OperationResult saveResult = SaveServiceHelper.saveOperate("kxr1_mycourse", new DynamicObject[]{selfCourse}, OperateOption.create());
            List<Object> successPkIds = saveResult.getSuccessPkIds();
            System.out.println(successPkIds.get(0));
            // 返回成功结果到父页面
            this.getView().returnDataToParent(true);
            this.getView().close();

        }
    }

    // 校验是否能选择本课程
    // 1.当前已有课程时间是否和该课程冲突
    // 2.课程可选人数是否已经满
    public boolean verify(DynamicObject chooseCourse) {
        courseConfirmLog.info("verify begin ...");
        if (null != chooseCourse) {
            RequestContext requestContext = RequestContext.get();
            long currUserId = requestContext.getCurrUserId();
            QFilter userFilter = new QFilter("creator", QCP.equals, currUserId);
            QFilter stateFilter = new QFilter("kxr1_basedatafield.kxr1_combofield", QCP.equals, "RegistrationAvailable");
            // 查用户的选课情况
            DynamicObject[] selfcourses =
                    BusinessDataServiceHelper.load("kxr1_mycourse", "id,kxr1_basedatafield,kxr1_basedatafield.kxr1_coursetime1,kxr1_basedatafield.kxr1_coursetime1.number,creator", new QFilter[]{userFilter,stateFilter});
            if (null != selfcourses && selfcourses.length > 0) {
                for (DynamicObject selfcourse : selfcourses) {
                    DynamicObject course = selfcourse.getDynamicObject("kxr1_basedatafield");
                    if (course != null) {
                        String pkValue = course.getPkValue().toString();
                        // 课程id相同，该学生是否已经选过该门课程
                        if (StringUtils.equalsIgnoreCase(chooseCourse.getPkValue().toString(), pkValue)) {
                            this.getView().showTipNotification("已经选择过该课程，无需重复选择。");
                            return false;
                        }else{

                            String choosetime=chooseCourse.get("kxr1_coursetime1.number").toString();
                            String coursetime=course.get("kxr1_coursetime1.number").toString();
                            if(choosetime.equals(coursetime)){
                                this.getView().showTipNotification("该时间段已经有课程了");
                                return false;
                            }
                        }
                        /*else {
                            // 判断星期和课节是否相同
                            DynamicObjectCollection Choosetime = chooseCourse.getDynamicObjectCollection("kxr1_mulbasedatafield");
                            DynamicObjectCollection Coursetime = course.getDynamicObjectCollection("kxr1_mulbasedatafield");
                           // if(Choosetime.get)
                            for(DynamicObject choosetime:Choosetime){
                               for(DynamicObject coursetime:Coursetime){
                                   //choosetime.get("number").equals(coursetime.get("number"))
                                   if (choosetime.get("number").toString().equals(coursetime.get("number").toString())){
                                       this.getView().showTipNotification("该时间段已经有课程了，请选择其他时间段的课程。");
                                       return false;
                                   }
                                   else{
                                       this.getView().showTipNotification("该时间段已经有课程了");
                                       return false;
                                   }
                               }
                           }

                        }*/

                    }
                }
            }
            int num= (int) chooseCourse.get("kxr1_integerfield1")+1;//报名人数
            int num1= (int) chooseCourse.get("kxr1_integerfield");//可选人数
            if(num>=num1){
                chooseCourse.set("kxr1_textfield1","已满");
                SaveServiceHelper.update(chooseCourse);
            }
            else{
                chooseCourse.set("kxr1_integerfield1",num);
                chooseCourse.set("kxr1_textfield1","可报名");
                SaveServiceHelper.update(chooseCourse);
                return true;
            }
            return false;
        } else {
            this.getView().showTipNotification("没有选择课程。");
            return false;
        }
    }

}
