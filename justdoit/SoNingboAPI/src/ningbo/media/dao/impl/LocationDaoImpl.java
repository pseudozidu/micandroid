package ningbo.media.dao.impl;

import java.util.List;
import ningbo.media.bean.Location;
import ningbo.media.core.dao.impl.BaseDaoImpl;
import ningbo.media.core.page.Pagination;
import ningbo.media.dao.LocationDao;
import ningbo.media.util.Pinyin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ningbo.media.core.page.Finder;

@Repository("locationDao")
public class LocationDaoImpl extends BaseDaoImpl<Location, Integer> implements
		LocationDao {

	private static final Logger log = LoggerFactory
			.getLogger(LocationDaoImpl.class);

	public LocationDaoImpl() {
		super(Location.class);
	}

	public List<Location> queryLocationByName(String locationName) {
		if (locationName == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();

		sb.append("from Location m where 1=1 ");
		if (Pinyin.isChinese(locationName)) {
			sb
					.append(" and m.name_cn like ? or m.tags_cn like ? or m.address_cn like ? order by id  desc,name_cn asc ");
		} else {
			sb
					.append(" and m.name_en like ? or m.tags_en like ? or m.address_en like ? order by id  desc,name_en asc ");
		}
		List<Location> list = findByHql(sb.toString(), true, null,
				locationName, locationName, locationName);
		return list;
	}

	public Pagination<Location> queryLocationByPage(int pageNo, int pageSize) {
		Finder f = Finder.create("from Location bean where 1=1 ");
		f.append(" order by bean.id desc ");
		return findByPage(f, pageNo, pageSize);
	}

	public List<Location> queryLocationsById(Integer categoryId) {
		try {
			if (null == categoryId) {
				return null;
			}
			String hql = "select bean from Location as bean left join bean.secondCategorys as s where 1=1 and s.id = ? order by bean.id desc";
			List<Location> loc = findByHql(hql, categoryId);
			if (null != loc) {
				return loc;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("Query Location By CategoryId Exception:categoryId="
					+ categoryId);
		}
		return null;
	}
}
