package com.jshop.action.templates;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.json.annotations.JSON;
import org.springframework.stereotype.Controller;

import com.jshop.action.TemplateTAction;
import com.jshop.action.tools.BaseTools;
import com.jshop.action.tools.ContentTag;
import com.jshop.action.tools.FreeMarkerController;
import com.jshop.action.tools.PageModel;
import com.jshop.entity.ArticleT;
import com.jshop.entity.GoodsCategoryT;
import com.jshop.entity.GoodsT;
import com.jshop.entity.PageEditareaT;
import com.jshop.entity.TemplateT;
import com.jshop.entity.TemplatesetT;
import com.jshop.service.impl.ArticleTServiceImpl;
import com.jshop.service.impl.GoodsCategoryTServiceImpl;
import com.jshop.service.impl.GoodsTServiceImpl;
import com.jshop.service.impl.TemplateTServiceImpl;
import com.jshop.service.impl.TemplatesetTServiceImpl;
import com.opensymphony.xwork2.ActionSupport;

import freemarker.template.TemplateException;
@ParentPackage("json-default")
@Namespace("")
@Controller("createHtml")
public class CreateHtml extends ActionSupport {
	@Resource(name="templateTAction")
	private TemplateTAction templateTAction;
	@Resource(name="templateTServiceImpl")
	private TemplateTServiceImpl templateTServiceImpl;
	@Resource(name="templatesetTServiceImpl")
	private TemplatesetTServiceImpl templatesetTServiceImpl;
	@Resource(name="goodsTServiceImpl")
	private GoodsTServiceImpl goodsTServiceImpl;
	@Resource(name="articleTServiceImpl")
	private ArticleTServiceImpl articleTServiceImpl;
	@Resource(name="goodsCategoryTServiceImpl")
	private GoodsCategoryTServiceImpl goodsCategoryTServiceImpl;
	@Resource(name="dataCollectionTAction")
	private DataCollectionTAction dataCollectionTAction;
	
	private Map<String, Object> map = new HashMap<String, Object>();
	private Map<String,Object> session=new HashMap<String,Object>();
	private TemplateT bean = new TemplateT();
	private TemplatesetT setbean = new TemplatesetT();
	private FreeMarkerController fc = new FreeMarkerController();
	private PageModel pm = new PageModel();
	private String status;
	private boolean slogin;
	
	
	@JSON(serialize = false)
	public TemplateTAction getTemplateTAction() {
		return templateTAction;
	}
	public void setTemplateTAction(TemplateTAction templateTAction) {
		this.templateTAction = templateTAction;
	}
	@JSON(serialize = false)
	public DataCollectionTAction getDataCollectionTAction() {
		return dataCollectionTAction;
	}
	public void setDataCollectionTAction(DataCollectionTAction dataCollectionTAction) {
		this.dataCollectionTAction = dataCollectionTAction;
	}
	@JSON(serialize = false)
	public TemplateTServiceImpl getTemplateTServiceImpl() {
		return templateTServiceImpl;
	}

	public void setTemplateTServiceImpl(TemplateTServiceImpl templateTServiceImpl) {
		this.templateTServiceImpl = templateTServiceImpl;
	}
	@JSON(serialize = false)
	public TemplatesetTServiceImpl getTemplatesetTServiceImpl() {
		return templatesetTServiceImpl;
	}

	public void setTemplatesetTServiceImpl(TemplatesetTServiceImpl templatesetTServiceImpl) {
		this.templatesetTServiceImpl = templatesetTServiceImpl;
	}
	@JSON(serialize = false)
	public ArticleTServiceImpl getArticleTServiceImpl() {
		return articleTServiceImpl;
	}

	public void setArticleTServiceImpl(ArticleTServiceImpl articleTServiceImpl) {
		this.articleTServiceImpl = articleTServiceImpl;
	}
	@JSON(serialize = false)
	public GoodsCategoryTServiceImpl getGoodsCategoryTServiceImpl() {
		return goodsCategoryTServiceImpl;
	}

	public void setGoodsCategoryTServiceImpl(GoodsCategoryTServiceImpl goodsCategoryTServiceImpl) {
		this.goodsCategoryTServiceImpl = goodsCategoryTServiceImpl;
	}

	@JSON(serialize = false)
	public GoodsTServiceImpl getGoodsTServiceImpl() {
		return goodsTServiceImpl;
	}

	public void setGoodsTServiceImpl(GoodsTServiceImpl goodsTServiceImpl) {
		this.goodsTServiceImpl = goodsTServiceImpl;
	}
	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@JSON(serialize = false)
	public TemplateT getBean() {
		return bean;
	}

	public void setBean(TemplateT bean) {
		this.bean = bean;
	}

	@JSON(serialize = false)
	public FreeMarkerController getFc() {
		return fc;
	}

	public void setFc(FreeMarkerController fc) {
		this.fc = fc;
	}

	public TemplatesetT getSetbean() {
		return setbean;
	}

	public void setSetbean(TemplatesetT setbean) {
		this.setbean = setbean;
	}

	public boolean isSlogin() {
		return slogin;
	}

	public void setSlogin(boolean slogin) {
		this.slogin = slogin;
	}

	@JSON(serialize = false)
	public PageModel getPm() {
		return pm;
	}

	public void setPm(PageModel pm) {
		this.pm = pm;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	/**
	 * 清理错误
	 */
	@Override
	public void validate() {
		this.clearErrorsAndMessages();

	}

	/**
	 * 生成登录页面
	 * @param map
	 * @throws TemplateException 
	 * @throws IOException 
	 */
	public void buildLogin(Map<String,Object>map) throws IOException, TemplateException{
		this.createLogin(BaseTools.getApplicationthemesig()+"_"+ContentTag.TEMPLATENAMEFORLOGIN, null, map);
	}
	
	/**
	 * 生成注册页面
	 * @throws TemplateException 
	 * @throws IOException 
	 */
	public void buildRegister(Map<String,Object>map) throws IOException, TemplateException{
		this.createRegister(BaseTools.getApplicationthemesig()+"_"+ContentTag.TEMPLATEFORREGISTER,null,map);
	}
	/**
	 * 生成首页
	 * @param map
	 * @throws TemplateException 
	 * @throws IOException 
	 */
	public void buildIndex(Map<String,Object>map) throws IOException, TemplateException{
		this.createIndex(BaseTools.getApplicationthemesig()+"_"+ContentTag.TEMPLATENAMEFORINDEX, null,map);
	}
	
	
	/**
	 * 生成商品静态页
	 */
	public void buildGoodsdetailsPage(Map<String,Object>map) throws IOException, TemplateException {
		List<GoodsT> glist = this.getDataCollectionTAction().findAllGoodsT();
		for (Iterator it = glist.iterator(); it.hasNext();) {
			GoodsT gt = (GoodsT) it.next();
			map.put("goodsdetail", gt);
			String htmlPath = this.createGoodsT(BaseTools.getApplicationthemesig()+"_"+ContentTag.TEMPLATENAMEFORGOODSDETAIL, gt.getGoodsid(), map);
			this.getGoodsTServiceImpl().updateHtmlPath(gt.getGoodsid(), htmlPath);
		}
	}

	/**
	 * 生成文章静态页
	 * 
	 * @throws IOException
	 * @throws TemplateException
	 */
	public void buildArticlesPage(Map<String,Object>map) throws IOException, TemplateException {
		List<ArticleT> alist = this.getDataCollectionTAction().findAllArticleT();
		if (!alist.isEmpty()) {
			for (Iterator it = alist.iterator(); it.hasNext();) {
				ArticleT at = (ArticleT) it.next();
				map.put("article", at);
				String htmlPath = this.createArticleT(BaseTools.getApplicationthemesig()+"_"+ContentTag.TEMPLATENAMEFORARTICLE, at.getArticleid(), map);
				this.getArticleTServiceImpl().updateHtmlPath(at.getArticleid(), htmlPath);
			}
		}
	}

	/**
	 * 商城上架状态的商品分类静态页
	 * 
	 * @throws IOException
	 * @throws TemplateException
	 */
	public void buildGoodsCategoryPage(Map<String,Object>map) throws IOException, TemplateException {
		String salestate = "1";
		List<GoodsCategoryT> gclist = this.getDataCollectionTAction().findAllGoodsCategoryTByState();
		if (!gclist.isEmpty()) {
			List<GoodsT> list = new ArrayList<GoodsT>();
			for (Iterator it = gclist.iterator(); it.hasNext();) {
				GoodsCategoryT gct = (GoodsCategoryT) it.next();
				map.put("goodsCategory", gct);
				if (gct.getGrade().equals("0")) {
					String navid = gct.getGoodsCategoryTid();
					//生成上架状态的商品分类静态页
					list = this.getGoodsTServiceImpl().findAllGoodsBynavid(navid, salestate);
					if (list != null) {
						map.put("allgoods", list);
						map.put("sign", gct.getSign());//页面链接标示
						String htmlPath = this.createGoodsCategoryT(BaseTools.getApplicationthemesig()+"_"+ContentTag.TEMPLATENAMEFORGOODSCATEGORYLIST, gct.getSign(), map);
						this.getGoodsCategoryTServiceImpl().updateHtmlPath(gct.getGoodsCategoryTid(), htmlPath);
					}
					//生成按照销量排序
					list = this.getGoodsTServiceImpl().findAllGoodsBynavidorderbyParams(navid, salestate, "sales", null, null, null, null, null, null, null);
					if (list != null) {
						map.put("allgoods", list);
						map.put("sign", gct.getSign() + "sales");
						this.createGoodsCategoryT(BaseTools.getApplicationthemesig()+"_"+ContentTag.TEMPLATENAMEFORGOODSCATEGORYLIST, gct.getSign() + "sales", map);

					}
					//生成按照会员价格排序
					list = this.getGoodsTServiceImpl().findAllGoodsBynavidorderbyParams(navid, salestate, null, "memberprice", null, null, null, null, null, null);
					if (list != null) {
						map.put("allgoods", list);
						map.put("sign", gct.getSign() + "memberprice");
						this.createGoodsCategoryT(BaseTools.getApplicationthemesig()+"_"+ContentTag.TEMPLATENAMEFORGOODSCATEGORYLIST, gct.getSign() + "memberprice", map);

					}
					//生成按照评论排序
					list = this.getGoodsTServiceImpl().findAllGoodsBynavidorderbyParams(navid, salestate, null, null, "totalcomment", null, null, null, null, null);
					if (list != null) {
						map.put("allgoods", list);
						map.put("sign", gct.getSign() + "totalcomment");
						this.createGoodsCategoryT(BaseTools.getApplicationthemesig()+"_"+ContentTag.TEMPLATENAMEFORGOODSCATEGORYLIST, gct.getSign() + "totalcomment", map);

					}

					//生成按照特价筛选
					//List<GoodsT>listb=new ArrayList<GoodsT>();
					list = this.getGoodsTServiceImpl().findAllGoodsBynavidorderbyParams(navid, salestate, null, null, null, "bargainprice", null, null, null, "1");
					if (list != null) {
						map.put("allgoods", list);
						map.put("sign", gct.getSign() + "bargainprice");
						this.createGoodsCategoryT(BaseTools.getApplicationthemesig()+"_"+ContentTag.TEMPLATENAMEFORGOODSCATEGORYLIST, gct.getSign() + "bargainprice",map);
					}
					//生成按照热销筛选
					list = this.getGoodsTServiceImpl().findAllGoodsBynavidorderbyParams(navid, salestate, null, null, null, null, "hotsale", null, null, "1");
					if (list != null) {
						map.put("allgoods", list);
						map.put("sign", gct.getSign() + "hotsale");
						this.createGoodsCategoryT(BaseTools.getApplicationthemesig()+"_"+ContentTag.TEMPLATENAMEFORGOODSCATEGORYLIST, gct.getSign() + "hotsale",map);
					}

					//生成按照推荐排序
					list = this.getGoodsTServiceImpl().findAllGoodsBynavidorderbyParams(navid, salestate, null, null, null, null, null, "recommended", null, "1");
					map.put("allgoods", list);
					map.put("sign", gct.getSign() + "recommended");
					this.createGoodsCategoryT(BaseTools.getApplicationthemesig()+"_"+ContentTag.TEMPLATENAMEFORGOODSCATEGORYLIST, gct.getSign() + "recommended",map);
					//生成按照新品排序
					list = this.getGoodsTServiceImpl().findAllGoodsBynavidorderbyParams(navid, salestate, null, null, null, null, null, null, "isNew", "1");
					map.put("allgoods", list);
					map.put("sign", gct.getSign() + "isNew");
					this.createGoodsCategoryT(BaseTools.getApplicationthemesig()+"_"+ContentTag.TEMPLATENAMEFORGOODSCATEGORYLIST, gct.getSign() + "isNew",map);
				} else if (gct.getGrade().equals("1")) {
					String ltypeid = gct.getGoodsCategoryTid();
					//生成上架状态的商品分类静态页
					list = this.getGoodsTServiceImpl().findGoodsByLtypeid(ltypeid, salestate);
					if (list != null) {
						map.put("allgoods", list);
						map.put("sign", gct.getSign());//页面链接标示
						String htmlPath = this.createGoodsCategoryT(BaseTools.getApplicationthemesig()+"_"+ContentTag.TEMPLATENAMEFORGOODSCATEGORYLIST, gct.getSign(), map);
						this.getGoodsCategoryTServiceImpl().updateHtmlPath(gct.getGoodsCategoryTid(), htmlPath);
					}
					//生成按照销量排序
					list = this.getGoodsTServiceImpl().findAllGoodsByLtypeidorderbyParams(ltypeid, salestate, "sales", null, null, null, null, null, null, null);
					if (list != null) {
						map.put("allgoods", list);
						map.put("sign", gct.getSign() + "sales");
						this.createGoodsCategoryT(BaseTools.getApplicationthemesig()+"_"+ContentTag.TEMPLATENAMEFORGOODSCATEGORYLIST, gct.getSign() + "sales", map);

					}
					//生成按照会员价格排序
					list = this.getGoodsTServiceImpl().findAllGoodsByLtypeidorderbyParams(ltypeid, salestate, null, "memberprice", null, null, null, null, null, null);
					if (list != null) {
						map.put("allgoods", list);
						map.put("sign", gct.getSign() + "memberprice");
						this.createGoodsCategoryT(BaseTools.getApplicationthemesig()+"_"+ContentTag.TEMPLATENAMEFORGOODSCATEGORYLIST, gct.getSign() + "memberprice",map);

					}
					//生成按照评论排序
					list = this.getGoodsTServiceImpl().findAllGoodsByLtypeidorderbyParams(ltypeid, salestate, null, null, "totalcomment", null, null, null, null, null);
					if (list != null) {
						map.put("allgoods", list);
						map.put("sign", gct.getSign() + "totalcomment");
						this.createGoodsCategoryT(BaseTools.getApplicationthemesig()+"_"+ContentTag.TEMPLATENAMEFORGOODSCATEGORYLIST, gct.getSign() + "totalcomment", map);

					}

					//生成按照特价筛选
					//List<GoodsT>listb=new ArrayList<GoodsT>();
					list = this.getGoodsTServiceImpl().findAllGoodsByLtypeidorderbyParams(ltypeid, salestate, null, null, null, "bargainprice", null, null, null, "1");
					if (list != null) {
						map.put("allgoods", list);
						map.put("sign", gct.getSign() + "bargainprice");
						this.createGoodsCategoryT(BaseTools.getApplicationthemesig()+"_"+ContentTag.TEMPLATENAMEFORGOODSCATEGORYLIST, gct.getSign() + "bargainprice", map);
					}
					//生成按照热销筛选
					list = this.getGoodsTServiceImpl().findAllGoodsByLtypeidorderbyParams(ltypeid, salestate, null, null, null, null, "hotsale", null, null, "1");
					if (list != null) {
						map.put("allgoods", list);
						map.put("sign", gct.getSign() + "hotsale");
						this.createGoodsCategoryT(BaseTools.getApplicationthemesig()+"_"+ContentTag.TEMPLATENAMEFORGOODSCATEGORYLIST, gct.getSign() + "hotsale", map);
					}

					//生成按照推荐排序
					list = this.getGoodsTServiceImpl().findAllGoodsByLtypeidorderbyParams(ltypeid, salestate, null, null, null, null, null, "recommended", null, "1");
					map.put("allgoods", list);
					map.put("sign", gct.getSign() + "recommended");
					this.createGoodsCategoryT(BaseTools.getApplicationthemesig()+"_"+ContentTag.TEMPLATENAMEFORGOODSCATEGORYLIST, gct.getSign() + "recommended",map);
					//生成按照新品排序
					list = this.getGoodsTServiceImpl().findAllGoodsByLtypeidorderbyParams(ltypeid, salestate, null, null, null, null, null, null, "isNew", "1");
					map.put("allgoods", list);
					map.put("sign", gct.getSign() + "isNew");
					this.createGoodsCategoryT(BaseTools.getApplicationthemesig()+"_"+ContentTag.TEMPLATENAMEFORGOODSCATEGORYLIST, gct.getSign() + "isNew", map);
				} else {
					String stypeid = gct.getGoodsCategoryTid();
					list = this.getGoodsTServiceImpl().findGoodsByStypeid(stypeid, salestate);
					map.put("allgoods", list);
					String htmlPath = this.createGoodsCategoryT(BaseTools.getApplicationthemesig()+"_"+ContentTag.TEMPLATENAMEFORGOODSCATEGORYLIST, gct.getSign(), map);
					this.getGoodsCategoryTServiceImpl().updateHtmlPath(gct.getGoodsCategoryTid(), htmlPath);
				}
			}

		}
	}

	/**
	 * 生成激活邮件模板数据
	 * 
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	public String buildEmail(String emailcontent) throws IOException, TemplateException {
		map.put("basePath", this.getDataCollectionTAction().getBasePath());
		map.put("jshopbasicinfo", this.getDataCollectionTAction().findJshopbasicInfo());
		map.put("useractivates", emailcontent);
		return this.createEmail(ContentTag.TEMPLATENAMEFOREMAIL, BaseTools.adminCreateId(), map);

	}

	/**
	 * 生成活动邮件模板数据
	 * 
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	public String buildactivityEmail(String emailcontent) throws IOException, TemplateException {
		map.put("basePath", this.getBasePath());
		map.put("jshopbasicinfo", this.getDataCollectionTAction().findJshopbasicInfo());
		map.put("emailcontent", emailcontent);
		return this.createactivityEmail(ContentTag.TEMPLATENAMEFOREMAILTEMPLATE, ContentTag.CREATORID, map);
	}

	/**
	 * 邮件激活回调页面模板
	 * 
	 * @return
	 * @throws TemplateException
	 * @throws IOException
	 */
	public void buildUseractivatescallback(Map<String,Object>map) throws IOException, TemplateException {
		map.put("basePath", this.getDataCollectionTAction().getBasePath());
		map.put("jshopbasicinfo", this.getDataCollectionTAction().findJshopbasicInfo());
		this.createUseractivatescallback(BaseTools.getApplicationthemesig()+"_"+ContentTag.TEMPLATENAMEFORUSERACTIVATESCALLBACK, null, map);
	}

	

	/**
	 * 根据当前页和每页大小拿到分页数据
	 * 
	 * @param pageNo
	 *            当前页
	 * @param pageSize
	 *            每页大小
	 * @param lstArticle
	 *            数据
	 * @return List<Article>
	 */
	public static List<GoodsT> getDataByPageNo(int pageNo, int pageSize, List<GoodsT> lstGoods) {
		int totalpage = pageNo * pageSize > lstGoods.size() ? lstGoods.size() : pageNo * pageSize;
		return lstGoods.subList((pageNo - 1) * pageSize, totalpage);
	}

	/**
	 * 获取网站根目录
	 * 
	 * @param map
	 * @throws UnknownHostException
	 * InetAddress inet = InetAddress.getLocalHost();
	 * request.getScheme() + "://" + inet.getHostAddress() + ":" + request.getServerPort() +
	 */
	public String getBasePath() throws UnknownHostException {
		HttpServletRequest request = ServletActionContext.getRequest();
		String path = request.getContextPath();
		String basePath =  path + "/";
		return basePath;
	}


	/**
	 * 根据传入参数创建文章详细的静态页
	 * 
	 * @param note
	 *            模板表中得备注说明这个值和模板关系设定表中得系统内容要一致才可得到静态页输出目录或者输出文件
	 * @param sign
	 *            标示
	 * @param filename
	 *            生成的html文件名称
	 * @param creatorid
	 *            用户id
	 * @param map
	 *            模板参数
	 * @param ftlname
	 *            模板表中模板文件名
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	public String createArticleT(String sign,String filename, Map<String, Object> data) throws IOException, TemplateException {
		//如果articleCategoryName==顶级分类那么就让articleCategoryName==文章顶级分类，然后在templateset表中得到对应的
		//templateurl和buildhtmlpath
		//那么当文章顶级分类的时候，生成的路径是buildhtmlpath（包含了article目录）
		//当不是顶级分类是其他分类如常见问题，表示创建的就是单个文章了，那么buildhtmlpath==buildhtmlpath/sign/articleid.html
		//生成的
		String realhtmlpath = "";
		setbean = this.getTemplatesetTServiceImpl().findTemplatesetTBysign(sign);
		if (setbean != null) {
			String ftl = setbean.getTemplateurl();
			String folder = setbean.getBuildhtmlpath();
			String fileName = filename + ".html";
			String relativePath = "";
			fc.init(ftl, folder, fileName, data, relativePath);
			realhtmlpath = folder + fileName;
		}
		return realhtmlpath;
	}

	/**
	 * 生成商品静态页
	 * 
	 * @param ftlname
	 * @param note
	 * @param filename
	 * @param creatorid
	 * @param data
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	public String createGoodsT(String sign,String filename,Map<String, Object> data) throws IOException, TemplateException {
		String realhtmlpath = "";
		setbean = this.getTemplatesetTServiceImpl().findTemplatesetTBysign(sign);
		if (setbean != null) {
			//map.put("basePath", this.getBasePath());
			String ftl = setbean.getTemplateurl();
			String folder = setbean.getBuildhtmlpath();
			String fileName = filename + ".html";
			String relativePath = "";
			fc.init(ftl, folder, fileName, data, relativePath);
			realhtmlpath = folder + fileName;
		}
		return realhtmlpath;
	}

	/**
	 * 生成首页静态页面
	 * 
	 * @param ftlname
	 * @param note
	 * @param filename
	 * @param creatorid
	 * @param data
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	public String createIndex(String sign, String filename,Map<String, Object> data) throws IOException, TemplateException {
		String realhtmlpath = "";
		setbean = this.getTemplatesetTServiceImpl().findTemplatesetTBysign(sign);
		if (setbean != null) {
			String ftl = setbean.getTemplateurl();
			String folder = "";
			String fileName = "";
			String strs[] = setbean.getBuildhtmlpath().split("/");
			for (int i = 0; i < strs.length; i++) {
				if (strs[i].contains(".html")) {
					fileName = strs[i];
				} else {
					folder += strs[i] + "/";
				}
			}
			String relativePath = "";
			fc.init(ftl, folder, fileName, data, relativePath);
			realhtmlpath = folder + fileName;
		}
		return realhtmlpath;
	}

	/**
	 * 生成商品分类静态页
	 * 
	 * @param ftlname
	 * @param note
	 * @param filename
	 * @param creatorid
	 * @param data
	 * @return
	 * @throws TemplateException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public String createGoodsCategoryT(String sign,String filename, Map<String, Object> data) throws IOException, TemplateException {
		String realhtmlpath = "";
		int pageSize = 1;
		setbean = this.getTemplatesetTServiceImpl().findTemplatesetTBysign(sign);
		if (setbean != null) {
			map.put("basePath", this.getBasePath());
			String ftl = setbean.getTemplateurl();
			String folder = setbean.getBuildhtmlpath();
			String fileName;
			String relativePath = "";
			List<GoodsT> allGoods = (List<GoodsT>) data.get("allgoods");
			if (allGoods != null && allGoods.size() > 0) {
				PageModel<GoodsT> pms = new PageModel<GoodsT>(1, pageSize, null, allGoods.size());
				long totalPage = pms.getTotalpage();
				for (int i = 1; i <= totalPage; i++) {
					PageModel<GoodsT> pm = new PageModel<GoodsT>(i, pageSize, CreateHtml.getDataByPageNo(i, pageSize, allGoods), allGoods.size());
					data.put("goods", pm);
					if (i == 1) {
						fileName = filename + "_" + i + ".html";
						fc.init(ftl, folder, fileName, data, relativePath);
						realhtmlpath = folder + fileName;
					} else {
						fileName = filename + "_" + i + ".html";
						fc.init(ftl, folder, fileName, data, relativePath);
					}
				}
			}
		}
		return realhtmlpath;
	}

	/**
	 * 生成登录页面
	 * @param sign
	 * @param filename
	 * @param data
	 * @return
	 * @throws TemplateException
	 * @throws IOException
	 */
	public String createLogin(String sign, String filename,Map<String, Object> data) throws IOException, TemplateException {
		String realhtmlpath = "";
		setbean = this.getTemplatesetTServiceImpl().findTemplatesetTBysign(sign);
		if (setbean != null) {
			String ftl = setbean.getTemplateurl();
			String folder = "";
			String fileName = "";
			String strs[] = setbean.getBuildhtmlpath().split("/");
			for (int i = 0; i < strs.length; i++) {
				if (strs[i].contains(".html")) {
					fileName = strs[i];
				} else {
					folder += strs[i] + "/";
				}
			}
			String relativePath = "";
			fc.init(ftl, folder, fileName, data, relativePath);
			realhtmlpath = folder + fileName;
		}
		return realhtmlpath;
	}

	/**
	 * 生成注册页面
	 * 
	 * @param filename
	 * @param creatorid
	 * @param data
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	public String createRegister(String sign, String filename,Map<String, Object> data) throws IOException, TemplateException {
		String realhtmlpath = "";
		setbean = this.getTemplatesetTServiceImpl().findTemplatesetTBysign(sign);
		if (setbean != null) {
			String ftl =setbean.getTemplateurl();
			String folder = "";
			String fileName = "";
			String strs[] = setbean.getBuildhtmlpath().split("/");
			for (int i = 0; i < strs.length; i++) {
				if (strs[i].contains(".html")) {
					fileName = strs[i];
				} else {
					folder += strs[i] + "/";
				}
			}
			String relativePath = "";
			fc.init(ftl, folder, fileName, data, relativePath);
			realhtmlpath = folder + fileName;
		}
		return realhtmlpath;
	}

	/**
	 * 创建邮件回调页面
	 * 
	 * @param ftlname
	 * @param note
	 * @param filename
	 * @param creatorid
	 * @param data
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	public String createUseractivatescallback(String sign, String filename, Map<String, Object> data) throws IOException, TemplateException {
		String realhtmlpath = "";
		setbean = this.getTemplatesetTServiceImpl().findTemplatesetTBysign(sign);
		if (setbean != null) {
			String ftl = setbean.getTemplateurl();
			String folder = "";
			String fileName = "";
			String strs[] = setbean.getBuildhtmlpath().split("/");
			for (int i = 0; i < strs.length; i++) {
				if (strs[i].contains(".html")) {
					fileName = strs[i];
				} else {
					folder += strs[i] + "/";
				}
			}
			String relativePath = "";
			fc.init(ftl, folder, fileName, data, relativePath);
			realhtmlpath = folder + fileName;
		}
		return realhtmlpath;
	}

	/**
	 * 创建激活邮件模板
	 * 
	 * @param ftlname
	 * @param creatorid
	 * @param data
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	public String createEmail(String ftlname, String creatorid, Map<String, Object> data) throws IOException, TemplateException {
		String htmlText = "";
		bean = this.getTemplateTServiceImpl().findTemplateByname(creatorid, ftlname);
		if (bean != null) {
			map.put("basePath", this.getBasePath());
			String ftl = bean.getUrl();
			String relativePath = "";
			htmlText = fc.init(ftl, data, relativePath);
		}
		return htmlText;
	}

	/**
	 * 创建活动邮件模板
	 * 
	 * @param ftlname
	 * @param creatorid
	 * @param data
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	public String createactivityEmail(String ftlname, String creatorid, Map<String, Object> data) throws IOException, TemplateException {
		String htmlText = "";
		bean = this.getTemplateTServiceImpl().findTemplateByname(creatorid, ftlname);
		if (bean != null) {
			map.put("basePath", this.getBasePath());
			String ftl = bean.getUrl();
			String relativePath = "";
			htmlText = fc.init(ftl, data, relativePath);
		}
		return htmlText;
	}
	
	/**
	 * 重新生成模板文件
	 * @throws IOException
	 * @throws IllegalAccessException
	 */
	public void recreateTemplate() throws IOException, IllegalAccessException{
		List<TemplateT> tlist = this.getTemplateTServiceImpl().findAllTemplateWithNoParam(BaseTools.adminCreateId(),"1");
		if (!tlist.isEmpty()) {
			for (Iterator it = tlist.iterator(); it.hasNext();) {
				TemplateT tt = (TemplateT) it.next();
				this.getTemplateTAction().createFTLFile(tt);
			}
		}
	}
	
	
	
	
}
