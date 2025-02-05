package kxr1.smartcollege.smartcollege.plugin.form;

import java.util.EventObject;

import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.entity.datamodel.IDataModel;
import kd.bos.entity.datamodel.ListSelectedRow;
import kd.bos.entity.filter.FilterParameter;
import kd.bos.ext.form.control.CountDown;
import kd.bos.ext.form.control.events.CountDownEvent;
import kd.bos.ext.form.control.events.CountDownListener;
import kd.bos.form.IFormView;
import kd.bos.form.ShowType;
import kd.bos.form.control.events.ItemClickEvent;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.list.BillList;
import kd.bos.list.ListFilterParameter;
import kd.bos.list.ListShowParameter;
import kd.bos.mvc.SessionManager;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.DispatchServiceHelper;
import kd.bos.servicehelper.operation.SaveServiceHelper;

/**
 * 倒计时控件使用方法
 * 场景：在页面添加控件用来记录停留时间（用于答题计时）
 * @author zhangyizhe
 *
 */

public class CountdownapPlugIn extends AbstractFormPlugin implements CountDownListener{
	@Override
	public void registerListener(EventObject e) {
		super.registerListener(e);
		this.addItemClickListeners("kxr1_toolbarap");
		CountDown countdown = this.getView().getControl("kxr1_countdownap");
		countdown.addCountDownListener(this);
	}
	
//	@Override
//	public void afterCreateNewData(EventObject e) {
//		super.afterCreateNewData(e);
//		CountDown countdown = this.getView().getControl("kxr1_countdownap");
		// 设置倒计时时间为70秒
//		countdown.setDuration(1800);
//		countdown.start();//可以在进入页面时就开始计时
//	}
	public void beforeBindData(EventObject e){
		super.afterBindData(e);
		CountDown countdown = this.getView().getControl("kxr1_countdownap");
		// 设置倒计时时间为70秒
		countdown.setDuration(1800);
		countdown.start();//可以在进入页面时就开始计时
	}
	/**
	 * 当倒计时结束时会触发此事件
	 * @param evt
	 */
	@Override
	public void onCountDownEnd(CountDownEvent evt) {
		CountDownListener.super.onCountDownEnd(evt);
		this.getView().showMessage("时间到了！！！");
		this.getView().close();
	}
	@Override
	public void beforeDoOperation(BeforeDoOperationEventArgs args) {

		super.beforeDoOperation(args);
		String opKey = ((FormOperate)args.getSource()).getOperateKey();
		if ( StringUtils.equals("analysisanswer", opKey)){
			String pageId = this.getView().getMainView().getPageId();
			String assistant ="您好!请输入您的要求，我们将根据您的回答和需求做出报告。您可以输入：分析；简单分析... ";
			Object pkValue = Long.parseLong("1994433985219942400");
			DispatchServiceHelper.invokeBizService( "ai" , "gai" ,"GaiService","selectProcessInSideBar",pkValue,pageId,assistant);
		}

	}
}
