package ningbo.media.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ningbo.media.bean.SystemUser;
import ningbo.media.core.service.impl.BaseServiceImpl;
import ningbo.media.dao.SystemUserDao;
import ningbo.media.service.SystemUserService;

@Service("systemUserService")
public class SystemUserServiceImpl extends BaseServiceImpl<SystemUser, Integer> implements
		SystemUserService {
	
	
	//private SystemUserDao systemUserDao ;
	
	@Autowired(required = false)
	public SystemUserServiceImpl(@Qualifier("systemUserDao")SystemUserDao systemUserDao) {
		super(systemUserDao);
	}
	
	public SystemUser verificationUser(String email,String password){
		final String hql = "from SystemUser as model where model.email = ? and model.password = ? " ;
		SystemUser u = (SystemUser)super.findUnique(hql,email,password) ;
		return u ;
	}
	
	
	public boolean isExistsKey(String key,Integer userId){
		boolean flag = false ;
		final String hql = "from SystemUser as u where u.key = ? and u.id = ? " ;
		SystemUser u = (SystemUser)super.findUnique(hql,key,userId) ;
		if(u != null){
			flag = true ; 
		}
		return flag;
		//return systemUserDao.isExistsKey(key,userId) ;
	}

	public Boolean login(String username, String password) {
		boolean flag = false ;
		final String hql = "from SystemUser as u where u.username = ? and u.password = ? " ;
		SystemUser u = (SystemUser)super.findUnique(hql,username,password) ;
		if(u != null){
			flag = true ; 
		}
		return flag;
	}

}
