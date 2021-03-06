package org.javaside.cms.service;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.javaside.cms.core.DefaultEntityManager;
import org.javaside.cms.core.Page;
import org.javaside.cms.entity.Member;
import org.javaside.cms.entity.SendMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SendMessageManager extends DefaultEntityManager<SendMessage, Long> {

	/**
	 * 发件箱总数
	 * 
	 * @param member
	 * @return
	 */
	public Integer getSendCount(Member member) {
		Criteria c = this.entityDao.createCriteria();
		c.add(Restrictions.eq("fromMember", member));
		return (Integer) c.setProjection(Projections.rowCount()).uniqueResult();
	}

	/**
	 * 发件箱
	 * 
	 * @param page
	 * @param member
	 * @return
	 */
	public Page getSendMessage(Page page, Member member) {
		page.setOrder(page.DESC);
		page.setOrderBy("createDate");
		Criteria c = this.entityDao.createCriteria();
		c.add(Restrictions.eq("fromMember", member));
		return this.entityDao.findByCriteria(page, c);
	}

	/**
	 * 根据邮件ID集合批量删除,只有属于此会员的收件信息才有权删除
	 * 
	 * @param ids
	 */
	public void deleteBatch(Long[] ids, Member member) {
		for (Long id : ids) {
			this.delete(id, member);
		}
	}

	/**
	 * 只有属于此会员的收件信息才有权删除
	 * 
	 * @param id
	 * @param member
	 */
	public void delete(Long id, Member member) {
		SendMessage mes = entityDao.get(id);
		if (mes != null && member != null && mes.getFromMember().getId().intValue() == member.getId().intValue()) {
			entityDao.delete(id);
		}
	}
}
