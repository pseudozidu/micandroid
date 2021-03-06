package ningbo.media.rest.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import ningbo.media.bean.Favorite;
import ningbo.media.bean.PersonUserProfile;
import ningbo.media.bean.SystemUser;
import ningbo.media.bean.enums.SendEmailType;
import ningbo.media.oauth2.utils.StringCode;
import ningbo.media.rest.dto.SystemUserData;
import ningbo.media.rest.dto.UserProfileData;
import ningbo.media.rest.util.Constant;
import ningbo.media.rest.util.FileUpload;
import ningbo.media.rest.util.FileUploadUtil;
import ningbo.media.rest.util.JSONCode;
import ningbo.media.rest.util.Jerseys;
import ningbo.media.service.FavoriteService;
import ningbo.media.service.FriendsService;
import ningbo.media.service.PersonUserProfileService;
import ningbo.media.service.SendManagerService;
import ningbo.media.service.SystemUserService;
import ningbo.media.util.ApplicationContextUtil;
import ningbo.media.util.MD5;
import ningbo.media.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;

@Path("/user")
@Component
@Scope("request")
public class SystemUserRest {

	@Resource
	private SystemUserService systemUserService;

	@Resource
	private FavoriteService favoriteService;

	@Resource
	private PersonUserProfileService personUserProfileService;

	@Resource
	private FriendsService friendsService;

	private SendManagerService sendMgrService = (SendManagerService) ApplicationContextUtil
			.getContext().getBean("sendMail");

	/**
	 * 
	 * @param id
	 * @return SystemUser
	 */
	@Path("/show/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSystemUserById(@PathParam("id")
	String id) {
		SystemUser u = systemUserService.get(Constant.MD5_FIELD, id);
		if (null == u) {
			String message = "The User Id [" + id + "] No Exists.";
			throw Jerseys.buildException(Status.NOT_FOUND, message);
		}
		return Response.ok(getSystemUserData(u)).build();
	}

	@Path("/showAll")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<SystemUser> getAll() {
		return systemUserService.getAll();
	}

	/**
	 * @param form
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Path("/verifystatus/{id}/{key}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response verifystatus(@PathParam("id")
	String id, @PathParam("key")
	String key) throws Exception {
		JSONObject json = new JSONObject();
		StringCode stringCode = new StringCode();
		String tempKey = stringCode.decrypt(key);

		if (key.isEmpty()) {
			json.put(Constant.RESULT, JSONCode.RESULT_FAIL);
			json.put(Constant.MESSAGE, JSONCode.MSG_KEY_ISNULL);
			return Response.ok(json.toString()).build();

		} else if (!Constant.KEY.equals(tempKey)) {
			json.put(Constant.RESULT, JSONCode.RESULT_FAIL);
			json.put(Constant.MESSAGE, JSONCode.MSG_KEY_INVALID);
			return Response.ok(json.toString()).build();
		}
		SystemUser u = systemUserService.getSystemUserByMd5Value(id);
		u.setStatus(true);
		try {
			systemUserService.update(u);
			json.put(Constant.RESULT, JSONCode.RESULT_SUCCESS);
			json.put(Constant.MESSAGE, JSONCode.MSG_USER_ACTIVATED);
			return Response.ok(json.toString()).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			json.put(Constant.RESULT, JSONCode.RESULT_FAIL);
			json.put(Constant.CODE, JSONCode.THROWEXCEPTION);
			return Response.ok(json.toString()).build();
		}
	}

	/**
	 * 
	 * @param form
	 * @param request
	 * @return
	 * @throws JSONException
	 */
	@Path("/registration")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response addSimpleSystemUser(@FormParam("key")
	String key, @FormParam("username")
	String username, @FormParam("email")
	String email, @FormParam("password")
	String password) throws JSONException {
		JSONObject json = new JSONObject();
		if (key.isEmpty()) {
			json.put(Constant.RESULT, JSONCode.RESULT_FAIL);
			json.put(Constant.MESSAGE, JSONCode.MSG_KEY_ISNULL);
			return Response.ok(json.toString()).build();
		} else if (!Constant.KEY.equals(key)) {
			json.put(Constant.RESULT, JSONCode.RESULT_FAIL);
			json.put(Constant.MESSAGE, JSONCode.MSG_KEY_INVALID);
			return Response.ok(json.toString()).build();
		}

		if ("".equals(username) || username == null) {
			json.put(Constant.MESSAGE, JSONCode.MSG_USER_USERNAME_NO_INPUT);
			json.put(Constant.RESULT, JSONCode.RESULT_FAIL);
			return Response.ok(json.toString()).build();
		}

		boolean bool_username = systemUserService.isExist(Constant.USERNAME,
				username);
		if (bool_username) {
			json.put(Constant.MESSAGE, JSONCode.MSG_USERNAME_EXISTS);
			json.put(Constant.RESULT, JSONCode.RESULT_FAIL);
			return Response.ok(json.toString()).build();
		}

		if ("".equals(email) || email == null) {
			json.put(Constant.MESSAGE, JSONCode.MSG_USERNAME_EXISTS);
			json.put(Constant.RESULT, JSONCode.RESULT_FAIL);
			return Response.ok(json.toString()).build();
		}
		boolean bool_email = systemUserService.isExist(Constant.USER_EMAIL,
				email);
		if (bool_email) {
			json.put(Constant.MESSAGE, JSONCode.MSG_USER_EMAIL_EXISTS);
			json.put(Constant.RESULT, JSONCode.RESULT_FAIL);
			return Response.ok(json.toString()).build();
		}

		if ("".equals(password) || password == null) {
			json.put(Constant.MESSAGE, JSONCode.MSG_USER_PASSWORD_NOINPUT);
			json.put(Constant.RESULT, JSONCode.RESULT_FAIL);
			return Response.ok(json.toString()).build();
		}

		SystemUser u = new SystemUser();
		String encodePassword = MD5.calcMD5(password);
		u.setEmail(email);
		u.setPassword(encodePassword);
		u.setUsername(username);
		u.setDatetime(new Date());
		u.setUserType(Constant.USERTYPE_USER);
		u.setStatus(false);
		try {
			Integer id = systemUserService.save(u);
			String md5Value = MD5.calcMD5(String.valueOf(id));
			u = systemUserService.get(id);
			u.setMd5Value(md5Value);
			systemUserService.update(u);

			StringCode code = new StringCode();
			String tempKey = code.encrypt(key);
			sendMgrService.sendHtmlMail(email, username, md5Value, tempKey,
					SendEmailType.REGISTER);
			json.put(Constant.RESULT, JSONCode.RESULT_SUCCESS);
			json.put(Constant.USERID, md5Value);
			return Response.ok(json.toString()).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			json.put(Constant.CODE, JSONCode.SERVER_EXCEPTION);
			return Response.ok(json.toString()).build();
		}
	}

	/**
	 * 
	 * @param form
	 * @param request
	 * @return
	 * @throws JSONException
	 */
	@Path("/register")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addSystemUser(FormDataMultiPart form, @Context
	HttpServletRequest request) throws JSONException {
		JSONObject json = new JSONObject();
		String key = form.getField("key").getValue();
		String username = form.getField("username").getValue();
		String email = form.getField("email").getValue();
		String password = form.getField("password").getValue();
		FormDataBodyPart part = form.getField("photo_path");

		if (key.isEmpty()) {
			json.put(Constant.RESULT, JSONCode.RESULT_FAIL);
			json.put(Constant.MESSAGE, JSONCode.MSG_KEY_ISNULL);
			return Response.ok(json.toString()).build();
		} else if (!Constant.KEY.equals(key)) {
			json.put(Constant.RESULT, JSONCode.RESULT_FAIL);
			json.put(Constant.MESSAGE, JSONCode.MSG_KEY_INVALID);
			return Response.ok(json.toString()).build();
		}

		if ("".equals(username) || username == null) {
			json.put(Constant.MESSAGE, JSONCode.MSG_USER_USERNAME_NO_INPUT);
			json.put(Constant.RESULT, JSONCode.RESULT_FAIL);
			return Response.ok(json.toString()).build();
		}

		boolean bool_username = systemUserService.isExist(Constant.USERNAME,
				username);
		if (bool_username) {
			json.put(Constant.MESSAGE, JSONCode.MSG_USERNAME_EXISTS);
			json.put(Constant.RESULT, JSONCode.RESULT_FAIL);
			return Response.ok(json.toString()).build();
		}

		if ("".equals(email) || email == null) {
			json.put(Constant.MESSAGE, JSONCode.MSG_USERNAME_EXISTS);
			json.put(Constant.RESULT, JSONCode.RESULT_FAIL);
			return Response.ok(json.toString()).build();
		}
		boolean bool_email = systemUserService.isExist(Constant.USER_EMAIL,
				email);
		if (bool_email) {
			json.put(Constant.MESSAGE, JSONCode.MSG_USER_EMAIL_EXISTS);
			json.put(Constant.RESULT, JSONCode.RESULT_FAIL);
			return Response.ok(json.toString()).build();
		}

		if ("".equals(password) || password == null) {
			json.put(Constant.MESSAGE, JSONCode.MSG_USER_PASSWORD_NOINPUT);
			json.put(Constant.RESULT, JSONCode.RESULT_FAIL);
			return Response.ok(json.toString()).build();
		}

		String fileName = part.getContentDisposition().getFileName();
		String photo_path = FileUpload.uploadLocation(part, fileName, request);

		SystemUser u = new SystemUser();
		String encodePassword = MD5.calcMD5(password);
		u.setEmail(email);
		u.setPassword(encodePassword);
		u.setUsername(username);
		u.setDatetime(new Date());
		u.setUserType(Constant.USERTYPE_USER);
		u.setStatus(false);
		if ("".equals(photo_path) || null == photo_path) {
			u.setPhoto_path("0");
		} else {
			u.setPhoto_path(photo_path);
		}
		try {
			Integer id = systemUserService.save(u);
			String md5Value = MD5.calcMD5(String.valueOf(id));
			u = systemUserService.get(id);
			u.setMd5Value(md5Value);
			systemUserService.update(u);

			StringCode code = new StringCode();
			String tempKey = code.encrypt(key);
			sendMgrService.sendHtmlMail(email, username, md5Value, tempKey,
					SendEmailType.REGISTER);
			json.put(Constant.RESULT, JSONCode.RESULT_SUCCESS);
			json.put(Constant.USERID, id);
			return Response.ok(json.toString()).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			json.put(Constant.CODE, JSONCode.SERVER_EXCEPTION);
			return Response.ok(json.toString()).build();
		}
	}

	@Path("/resend/email/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response resendEmail(@PathParam("id")
	String id) throws Exception {
		SystemUser u = systemUserService.getSystemUserByMd5Value(id);
		String md5Value = u.getMd5Value();
		String email = u.getEmail();
		String username = u.getUsername();
		JSONObject json = new JSONObject();
		if (null == u) {
			json.put(Constant.RESULT, JSONCode.RESULT_FAIL);
			json.put(Constant.MESSAGE, JSONCode.MSG_USER_NOEXISTS);
			return Response.ok(json.toString()).build();
		} else {
			StringCode code = new StringCode();
			String tempKey = code.encrypt(Constant.KEY);
			sendMgrService.sendHtmlMail(email, username, md5Value, tempKey,
					SendEmailType.REGISTER);
			json.put(Constant.RESULT, JSONCode.RESULT_SUCCESS);
			json.put(Constant.MESSAGE, JSONCode.MSG_USER_RESEND_EMAIL);
			return Response.ok(json.toString()).build();
		}
	}

	/**
	 * 
	 * @param form
	 * @param request
	 * @return
	 */
	@Path("/edit")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response editSystemUser(@FormParam("key")
	String key, @FormParam("nickName")
	String nickName, @FormParam("md5Value")
	String md5Value, @FormParam("gender")
	String gender, @FormParam("intro")
	String intro, @FormParam("realName")
	String name_cn, @FormParam("website")
	String website) throws JSONException {
		JSONObject json = new JSONObject();
		try {
			if (key.isEmpty()) {
				json.put(Constant.RESULT, JSONCode.RESULT_FAIL);
				json.put(Constant.MESSAGE, JSONCode.MSG_KEY_ISNULL);
				return Response.ok(json.toString()).build();

			} else if (!Constant.KEY.equals(key)) {
				json.put(Constant.RESULT, JSONCode.RESULT_FAIL);
				json.put(Constant.MESSAGE, JSONCode.MSG_KEY_INVALID);
				return Response.ok(json.toString()).build();
			}

			SystemUser u = systemUserService.getSystemUserByMd5Value(md5Value);
			if (null != u) {
				u.setWebsite(website);
				u.setName_cn(name_cn);
				u.setGender(gender);
				u.setIntro(intro);
				u.setNickName(nickName);
				u.setLastModifyTime(new Date());
				u.setUserType(Constant.USERTYPE_USER);
				systemUserService.update(u);
				json.put(Constant.RESULT, JSONCode.RESULT_SUCCESS);
				json.put(Constant.USERID, u.getMd5Value());
			} else {
				json.put(Constant.RESULT, JSONCode.RESULT_FAIL);
				json.put(Constant.MESSAGE, JSONCode.MSG_USER_NOEXISTS);
			}
			return Response.ok(json.toString()).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			json.put(Constant.RESULT, JSONCode.RESULT_FAIL);
			json.put(Constant.CODE, JSONCode.THROWEXCEPTION);
			return Response.ok(json.toString()).build();
		}
	}

	/**
	 * 
	 * @param form
	 * @param request
	 * @return
	 */
	@Path("/modify/password")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response modifyPassword(@FormParam("key")
	String key, @FormParam("md5Value")
	String md5Value, @FormParam("password")
	String password) throws JSONException {
		JSONObject json = new JSONObject();
		try {
			if (key.isEmpty()) {
				json.put(Constant.RESULT, JSONCode.RESULT_FAIL);
				json.put(Constant.MESSAGE, JSONCode.MSG_KEY_ISNULL);
				return Response.ok(json.toString()).build();

			} else if (!Constant.KEY.equals(key)) {
				json.put(Constant.RESULT, JSONCode.RESULT_FAIL);
				json.put(Constant.MESSAGE, JSONCode.MSG_KEY_INVALID);
				return Response.ok(json.toString()).build();
			}

			SystemUser u = systemUserService.getSystemUserByMd5Value(md5Value);
			if (null != u) {
				String newPsd = MD5.calcMD5(password);
				u.setPassword(newPsd);
				u.setLastModifyTime(new Date());
				systemUserService.update(u);
				json.put(Constant.RESULT, JSONCode.RESULT_SUCCESS);
				json.put(Constant.USERID, md5Value);
			} else {
				json.put(Constant.RESULT, JSONCode.RESULT_FAIL);
				json.put(Constant.MESSAGE, JSONCode.MSG_USER_NOEXISTS);
			}
			return Response.ok(json.toString()).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			json.put(Constant.RESULT, JSONCode.RESULT_FAIL);
			json.put(Constant.CODE, JSONCode.THROWEXCEPTION);
			return Response.ok(json.toString()).build();
		}
	}

	/**
	 * @Description 修改用户的头像
	 * 
	 * @param key
	 * @param userId
	 * @return
	 * @throws JSONException
	 */
	@Path("/modify/avatar")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeUserHeader(FormDataMultiPart form, @Context
	HttpServletRequest request) throws JSONException {
		JSONObject json = new JSONObject();
		try {
			String key = form.getField("key").getValue();
			String md5Value = form.getField("userId").getValue();
			FormDataBodyPart part = form.getField("photo_path");
			String photo_path = "";
			if (key.isEmpty()) {
				json.put(Constant.RESULT, JSONCode.RESULT_FAIL);
				json.put(Constant.MESSAGE, JSONCode.MSG_KEY_ISNULL);
				return Response.ok(json.toString()).build();
			} else if (!Constant.KEY.equals(key)) {
				json.put(Constant.RESULT, JSONCode.RESULT_FAIL);
				json.put(Constant.MESSAGE, JSONCode.MSG_KEY_INVALID);
				return Response.ok(json.toString()).build();
			}

			if ("".equals(md5Value) || null == md5Value) {
				json.put(Constant.RESULT, JSONCode.RESULT_FAIL);
				json.put(Constant.MESSAGE, JSONCode.MSG_USER_USER_MD5VALUE);
				return Response.ok(json.toString()).build();
			}
			SystemUser u = systemUserService.getSystemUserByMd5Value(md5Value);
			if (null == u) {
				json.put(Constant.RESULT, JSONCode.RESULT_FAIL);
				json.put(Constant.MESSAGE, JSONCode.MSG_USER_NOEXISTS);
				return Response.ok(json.toString()).build();
			}

			String fileName = part.getContentDisposition().getFileName();
			photo_path = FileUpload.uploadLocation(part, fileName, request);

			String uuid = u.getPhoto_path();
			if (!("0".equals(uuid)) && (null != uuid)) {
				FileUploadUtil.delFile(uuid, request);
			}

			u.setPhoto_path(photo_path);
			systemUserService.update(u);

			json.put(Constant.RESULT, JSONCode.RESULT_SUCCESS);
			json.put(Constant.MESSAGE, JSONCode.MSG_USER_AVATAR_UPDATE);
			return Response.ok(json.toString()).build();
		} catch (Exception ex) {
			json.put(Constant.RESULT, JSONCode.RESULT_FAIL);
			json.put(Constant.MESSAGE, JSONCode.MSG_USER_NOEXISTS);
			return Response.ok(json.toString()).build();
		}
	}

	@Path("/login")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(@FormParam("username")
	String username, @FormParam("password")
	String password, @FormParam("key")
	String key, @FormParam("device_id")
	String device_id) throws JSONException {
		JSONObject json = new JSONObject();
		if (key.isEmpty()) {
			json.put(Constant.RESULT, JSONCode.RESULT_FAIL);
			json.put(Constant.MESSAGE, JSONCode.MSG_KEY_ISNULL);
			return Response.ok(json.toString()).build();
		} else if (!Constant.KEY.equals(key)) {
			json.put(Constant.RESULT, JSONCode.RESULT_FAIL);
			json.put(Constant.MESSAGE, JSONCode.MSG_KEY_INVALID);
			return Response.ok(json.toString()).build();
		}
		if (username.isEmpty()) {
			json.put(Constant.MESSAGE, JSONCode.MSG_USER_USERNAME_NO_INPUT);
			json.put(Constant.RESULT, JSONCode.RESULT_FAIL);
			return Response.ok(json.toString()).build();
		}
		if (password.isEmpty()) {
			json.put(Constant.MESSAGE, JSONCode.MSG_USER_PASSWORD_NOINPUT);
			json.put(Constant.RESULT, JSONCode.RESULT_FAIL);
			return Response.ok(json.toString()).build();
		}

		try {
			SystemUser tempUser = systemUserService.login(username, password);
			if (null == tempUser) {
				json.put(Constant.RESULT, JSONCode.RESULT_FAIL);
				json.put(Constant.MESSAGE, JSONCode.MSG_USER_NOEXISTS);
				return Response.ok(json.toString()).build();
			}

			if (!"".equals(device_id.trim())) {
				List<Favorite> listFav = favoriteService
						.findFavoriteByDeviceForUser(device_id);
				if (null != listFav && listFav.size() > 0) {
					for (int i = 0; i < listFav.size(); i++) {
						Favorite entity = listFav.get(i);
						entity.setId(listFav.get(i).getId());
						entity.setUserId(tempUser.getMd5Value());
						favoriteService.update(entity);
					}
				}
			}

			return Response.ok(getSystemUserData(tempUser)).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			json.put(Constant.CODE, JSONCode.THROWEXCEPTION);
			return Response.ok(json.toString()).build();
		}

	}

	@Path("/forgot/password")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response forgetUserPassword(@FormParam("user_email")
	String userEmail, @FormParam("key")
	String key) throws JSONException {
		JSONObject json = new JSONObject();
		try {
			if (key.isEmpty()) {
				json.put(Constant.RESULT, JSONCode.RESULT_FAIL);
				json.put(Constant.MESSAGE, JSONCode.MSG_KEY_ISNULL);
				return Response.ok(json.toString()).build();

			} else if (!Constant.KEY.equals(key)) {
				json.put(Constant.RESULT, JSONCode.RESULT_FAIL);
				json.put(Constant.MESSAGE, JSONCode.MSG_KEY_INVALID);
				return Response.ok(json.toString()).build();
			}

			SystemUser user = systemUserService.get(Constant.USER_EMAIL,
					userEmail);
			if (null == user) {
				json.put(Constant.RESULT, JSONCode.RESULT_FAIL);
				json.put(Constant.MESSAGE, JSONCode.MSG_USER_NOEXISTS);
				return Response.ok(json.toString()).build();
			}
			String randomPass = StringUtil.randomString();
			String encodPass = MD5.calcMD5(randomPass);
			user.setPassword(encodPass);
			systemUserService.update(user);

			String username = user.getUsername();
			if (null == username) {
				username = " ";
			}

			StringCode code = new StringCode();
			String tempKey = code.encrypt(key);
			sendMgrService.sendHtmlMail(userEmail, username, randomPass,
					tempKey, SendEmailType.FORGOTPASSWORD);
			json.put(Constant.RESULT, JSONCode.RESULT_SUCCESS);
			json.put(Constant.MESSAGE, JSONCode.MSG_USER_FORGOT_PASSWORD);
			return Response.ok(json.toString()).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			json.put(Constant.CODE, JSONCode.SERVER_EXCEPTION);
			return Response.ok(json.toString()).build();
		}
	}

	@Path("/username/{username}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response isExistsUserName(@PathParam("username")
	String username) {
		JSONObject json = new JSONObject();
		boolean flag = false ;
		try {
			flag = systemUserService.isExist(Constant.SYSTEMUSER_USERNAME, username);
			json.put(Constant.RESULT,flag);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return Response.ok(json.toString()).build() ;
	}

	@Path("/email/{email}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response isExistsEmail(@PathParam("email")
	String email) {
		JSONObject json = new JSONObject();
		boolean flag = false ;
		try {
			flag = systemUserService.isExist(Constant.SYSTEMUSER_EMAIL, email);
			json.put(Constant.RESULT,flag);
		} catch (Exception ex) {
			ex.printStackTrace();

		}
		return Response.ok(json.toString()).build() ;
	}

	private SystemUserData getSystemUserData(SystemUser user) {
		if (null == user) {
			return null;
		}
		SystemUserData data = new SystemUserData();
		UserProfileData uData = new UserProfileData();
		String md5Value = user.getMd5Value();
		long followedNum = friendsService.getFollowedNumber(md5Value);
		long followingNum = friendsService.getFollowingNumber(md5Value);

		Integer uid = user.getId();
		data.setMd5Value(md5Value);
		data.setNickName(user.getNickName());
		data.setName_cn(user.getName_cn());
		data.setName_en(user.getName_en());
		data.setEmail(user.getEmail());
		data.setUsername(user.getUsername());
		data.setPhoto_path(user.getPhoto_path());
		data.setGender(user.getGender());
		data.setStatus(user.isStatus());
		data.setWebsite(user.getWebsite());
		data.setDatetime(user.getDatetime());
		data.setLastModifyTime(user.getLastModifyTime());
		data.setIntro(user.getIntro());
		data.setFollowed(followedNum);
		data.setFollowing(followingNum);

		String temp = user.getUserType();
		if (null != temp) {
			data.setUserType(temp);
		} else {
			data.setUserType(Constant.SYSTEM_USER_TYPE);
		}

		PersonUserProfile p = personUserProfileService.get(
				Constant.PROFILE_EXT_USERID, uid);
		if (null != p) {
			uData.setId(p.getId());
			uData.setBirthday(p.getBirthday());
			uData.setBlood(p.getBlood());
			uData.setCellPhone(p.getCellPhone());
			uData.setConstellation(p.getConstellation());
			uData.setHomeArea(p.getHomeArea());
			uData.setLikeBooks(p.getLikeBooks());
			uData.setLikeGames(p.getLikeGames());
			uData.setLikeMovies(p.getLikeMovies());
			uData.setLikeMusic(p.getLikeMusic());
			uData.setMsn(p.getMsn());
			uData.setPhone(p.getPhone());
			uData.setQq(p.getQq());
			uData.setStature(p.getStature());
			uData.setAge(p.getAge());

			data.setUserProfileData(uData);
		}
		return data;
	}

}
