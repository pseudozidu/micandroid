package com.jshop.action;

import java.sql.Date;
import java.text.SimpleDateFormat;
import javax.annotation.Resource;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.json.annotations.JSON;
import org.springframework.stereotype.Controller;
import com.jshop.action.tools.BaseTools;
import com.jshop.action.tools.PrintExpressParam;
import com.jshop.action.tools.Validate;
import com.jshop.entity.ExpresstempleteT;
import com.jshop.entity.OrderT;
import com.jshop.entity.ShippingAddressT;
import com.jshop.service.impl.ExpresstempleteTServiceImpl;
import com.jshop.service.impl.JshopbasicInfoTServiceImpl;
import com.jshop.service.impl.OrderTServiceImpl;
import com.jshop.service.impl.ShippingAddressTServiceImpl;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
@ParentPackage("jshop")

@Controller("printExpressSingleTAction")
public class PrintExpressSingleTAction extends ActionSupport {
	@Resource(name="expresstempleteTServiceImpl")
	private ExpresstempleteTServiceImpl expresstempleteTServiceImpl;
	@Resource(name="orderTServiceImpl")
	private OrderTServiceImpl orderTServiceImpl;
	@Resource(name="jshopbasicInfoTServiceImpl")
	private JshopbasicInfoTServiceImpl jshopbasicInfoTServiceImpl;
	@Resource(name="shippingAddressTServiceImpl")
	private ShippingAddressTServiceImpl shippingAddressTServiceImpl;
	private String orderid;
	private PrintExpressParam pe = new PrintExpressParam();
	private String logisticsid;
	private boolean slogin = false;
	private boolean sprintexpressflag = false;

	
	@JSON(serialize = false)
	public ExpresstempleteTServiceImpl getExpresstempleteTServiceImpl() {
		return expresstempleteTServiceImpl;
	}

	public void setExpresstempleteTServiceImpl(ExpresstempleteTServiceImpl expresstempleteTServiceImpl) {
		this.expresstempleteTServiceImpl = expresstempleteTServiceImpl;
	}
	@JSON(serialize = false)
	public OrderTServiceImpl getOrderTServiceImpl() {
		return orderTServiceImpl;
	}

	public void setOrderTServiceImpl(OrderTServiceImpl orderTServiceImpl) {
		this.orderTServiceImpl = orderTServiceImpl;
	}
	@JSON(serialize = false)
	public JshopbasicInfoTServiceImpl getJshopbasicInfoTServiceImpl() {
		return jshopbasicInfoTServiceImpl;
	}

	public void setJshopbasicInfoTServiceImpl(JshopbasicInfoTServiceImpl jshopbasicInfoTServiceImpl) {
		this.jshopbasicInfoTServiceImpl = jshopbasicInfoTServiceImpl;
	}
	@JSON(serialize = false)
	public ShippingAddressTServiceImpl getShippingAddressTServiceImpl() {
		return shippingAddressTServiceImpl;
	}

	public void setShippingAddressTServiceImpl(ShippingAddressTServiceImpl shippingAddressTServiceImpl) {
		this.shippingAddressTServiceImpl = shippingAddressTServiceImpl;
	}

	public PrintExpressParam getPe() {
		return pe;
	}

	public void setPe(PrintExpressParam pe) {
		this.pe = pe;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public boolean isSprintexpressflag() {
		return sprintexpressflag;
	}

	public void setSprintexpressflag(boolean sprintexpressflag) {
		this.sprintexpressflag = sprintexpressflag;
	}

	public boolean isSlogin() {
		return slogin;
	}

	public void setSlogin(boolean slogin) {
		this.slogin = slogin;
	}

	public String getLogisticsid() {
		return logisticsid;
	}

	public void setLogisticsid(String logisticsid) {
		this.logisticsid = logisticsid;
	}

	/**
	 * 清理错误
	 */
	@Override
	public void validate() {
		this.clearErrorsAndMessages();

	}

	/**
	 * 验证登陆
	 */
	public void CheckLogin() {
		String adminid = (String) ActionContext.getContext().getSession().get(BaseTools.BACK_USER_SESSION_KEY);
		if (adminid != null) {
			this.setSlogin(false);
		} else {
			this.setSlogin(true);
		}
	}
	/**
	 * 获取商家信息作为快递单内容
	 */
	public void GetJshopSendInfo() {
		//		JshopbasicInfoT jbi=this.getJshopbasicinfotserviceimpl().findJshopbasicInfoSingleForExpress();
		//		if(jbi!=null){
		//			pe.setSendName(jbi.getSendName());
		//			pe.setSendCountry(jbi.getSendCountry());
		//			pe.setSendProvince(jbi.getSendProvince());
		//			pe.setSendCity(jbi.getSendCity());
		//			pe.setSendDistrict(jbi.getSendDistrict());
		//			pe.setSendStreet(jbi.getSendStreet());
		//			pe.setSendTelno(jbi.getSendTelno());
		//			pe.setSendMobile(jbi.getSendMobile());
		//			pe.setSendContactor(jbi.getSendContactor());
		//		}
	}

	/**
	 * 获取订单发货地址
	 * 
	 * @param orderid
	 */
	public void GetOrderShippingAddress(String orderid) {
		ShippingAddressT st = this.getShippingAddressTServiceImpl().findShippingAddressByOrderid(orderid, "1");
		if (st != null) {
			pe.setRecipientName(st.getUsername());
			pe.setRecipientCountry(st.getCountry());
			pe.setRecipientProvince(st.getProvince());
			pe.setRecipientCity(st.getCity());
			pe.setRecipientDistrict(st.getDistrict());
			pe.setRecipientStreet(st.getStreet());
			pe.setRecipientTelno(st.getTelno());
			pe.setRecipientMobile(st.getMobile());
			pe.setRecipientPostcode(st.getPostcode());
			pe.setRecipientContactor(st.getUsername());
		}
	}

	public void GetChangeTime(Date t) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-hh-yy-ss");
		String time = formatter.format(t.getTime()).toString();
		String[] temptime = time.split("-");
		pe.setYear(temptime[0]);
		pe.setMonth(temptime[1]);
		pe.setDay(temptime[2]);
		pe.setHour(temptime[3]);
		pe.setMinutes(temptime[4]);
	}

	/**
	 *根据id获取订单详细
	 * 
	 * @param orderid
	 */
	public void GetOrderDetailByOrderId(String orderid) {
		OrderT o = this.getOrderTServiceImpl().findOrderDetailByorderid(orderid);
		if (o != null) {
			pe.setQuantity(o.getNeedquantity().toString());
			pe.setNotes(o.getCustomernotes());
			pe.setLogisticsid(o.getLogisticsid());
			GetChangeTime(Date.valueOf(o.getPurchasetime().toString()));//这里修改了
		}
	}

	/**
	 * 读取快递单模板
	 */
	public void GetPrintExpressValue() {
		ExpresstempleteT et = this.getExpresstempleteTServiceImpl().findExpresstempleteByLogisticsid(pe.getLogisticsid());
		if (et != null) {
			pe.setKindeditorCode(et.getKindeditorCode());
			pe.setRighttag("勾");//需要用图片代替勾符号
		}
	}

	/**
	 * 初始化快递单打印数据
	 * 
	 * @return
	 */
	@Action(value = "InitPrintExpress", results = { 
			@Result(name = "json",type="json")
	})
	public String InitPrintExpress() {
		this.CheckLogin();
		if(!this.isSlogin()){
			if (Validate.StrNotNull(this.getOrderid())) {
				GetJshopSendInfo();
				GetOrderShippingAddress(this.getOrderid().trim());
				GetOrderDetailByOrderId(this.getOrderid().trim());
				this.setSprintexpressflag(true);
				//设定模板值
				GetPrintExpressValue();
				return "json";
			} else {
				this.setSprintexpressflag(false);
				return "json";
			}
		}else{
			this.setSprintexpressflag(false);
			return "json";
		}
		
	}
}
