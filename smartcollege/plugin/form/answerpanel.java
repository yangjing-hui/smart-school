package kxr1.smartcollege.smartcollege.plugin.form;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.serialization.SerializationUtils;
import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.form.control.AttachmentPanel;
import kd.bos.form.control.Button;
import kd.bos.form.control.Control;
import kd.bos.form.control.events.ItemClickEvent;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.list.BillList;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.operation.SaveServiceHelper;

public class answerpanel extends AbstractFormPlugin {
    @Override
    public void registerListener(EventObject e) {
        Button btt = this.getView().getControl("btnok");
        btt.addClickListener(this);
        super.registerListener(e);
    }

    @Override
    public void itemClick(ItemClickEvent evt) {
        // TODO Auto-generated method stub

        super.itemClick(evt);
    }

    @Override
    public void click(EventObject evt) {
        if (evt.getSource() instanceof Button) {
            Button source = (Button) evt.getSource();
            if (source.getKey().equals("btnok")) {
                Object pk= this.getView().getFormShowParameter().getCustomParam("question");
                DynamicObject dynamicObject= BusinessDataServiceHelper.loadSingle(pk,"kxr1_answeringquestion");
                dynamicObject.set("kxr1_textareafield1",this.getModel().getValue("kxr1_textareafield"));
                dynamicObject.set("kxr1_picturefield3",this.getModel().getValue("kxr1_picturefield"));
                dynamicObject.set("kxr1_picturefield4",this.getModel().getValue("kxr1_picturefield2"));
                dynamicObject.set("kxr1_combofield","2");
                SaveServiceHelper.update(dynamicObject);
                this.getView().returnDataToParent(true);
                this.getView().close();

            }

        }

        super.click(evt);
    }

}
