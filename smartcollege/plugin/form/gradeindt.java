package kxr1.smartcollege.smartcollege.plugin.form;


import com.alibaba.fastjson.JSONArray;
import kd.bos.dataentity.OperateOption;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.metadata.dynamicobject.DynamicObjectType;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.datamodel.IEntryOperate;
import kd.bos.entity.operate.result.OperationResult;
import kd.bos.form.control.Control;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.metadata.entity.EntryEntity;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.operation.SaveServiceHelper;

import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class gradeindt extends AbstractFormPlugin {
    private static int row=0;
    @Override
    public void afterCreateNewData(EventObject e) {
        Map<String,Object> students=this.getView().getFormShowParameter().getCustomParams();

        JSONArray snumber= (JSONArray) students.get("student");

        // 转换为 String 数组
        String[] stringArray = new String[snumber.size()];
        for (int i = 0; i < snumber.size(); i++) {
            stringArray[i] = snumber.getString(i); // 使用 getString 方法安全地获取 String 类型
        }
        Object primaryKey = students.get("courseId");
        if (null != primaryKey) {
            this.getModel().setValue("kxr1_basedatafield", primaryKey);//绑定基础资料字段
        }
        if(stringArray.length!=0){
            row=stringArray.length;
            int index=0;
            do {
                String s = stringArray[index]; // 获取当前元素
                QFilter cidFilter = new QFilter("number", QCP.equals, s);
                DynamicObject student = BusinessDataServiceHelper.loadSingle("kxr1_perinform", "id,number", new QFilter[]{cidFilter});

                this.getModel().setValue("kxr1_basedatafield1", student, index);
                index++; // 更新计数器
                if(index<stringArray.length){
                    this.getModel().createNewEntryRow("kxr1_entryentity");
                }


            } while (index < stringArray.length); // 循环条件

        }

        //Control entry = this.getView().getControl("kxr1_entryentity");


    }

    @Override
    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        String opKey = ((FormOperate) args.getSource()).getOperateKey();
        if (StringUtils.equals("confirm", opKey)) {
            if(row==0){
                this.getView().showTipNotification("没有要更新的数据。");
                return;
            }else{
                Map<String,Object> students=this.getView().getFormShowParameter().getCustomParams();
                JSONArray jsonArray= (JSONArray) students.get("sprimaryKey");
                Object[] Pkvalues = jsonArray.toArray(new Object[jsonArray.size()]);
                DynamicObject temp=BusinessDataServiceHelper.loadSingle(Pkvalues[0],"kxr1_mycourse");

                DynamicObject[] studentcourse=BusinessDataServiceHelper.load(Pkvalues,temp.getDynamicObjectType());
                int sum=studentcourse.length;
                for(int i=0;i<row;i++){
                    Object grade=  this.getModel().getValue("kxr1_integerfield3",i);
                    int value = Integer.parseInt(grade.toString());
                    studentcourse[i].set("kxr1_integerfield",value);

                }
                SaveServiceHelper.update(studentcourse);
                Map<String,Object> students1=this.getView().getFormShowParameter().getCustomParams();

                Object primaryKey =students1.get("tutorcourse");
                QFilter idFilter = new QFilter("id", QCP.equals, primaryKey);
                DynamicObject dynamicObject = BusinessDataServiceHelper.loadSingle("kxr1_mycoursetutor", "id,kxr1_combofield", new QFilter[]{idFilter});
                dynamicObject.set("kxr1_combofield","1");
                SaveServiceHelper.update(dynamicObject);
                // 返回成功结果到父页面
                this.getView().returnDataToParent(true);
                this.getView().close();
            }

        }
    }
}
