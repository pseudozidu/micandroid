package ningbo.media.website.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import ningbo.media.core.mapper.SqlMapper;
import ningbo.media.website.entity.FirstCategory;

public interface FirstCategoryMapper extends SqlMapper{

	public FirstCategory getFirstCategoryById(Integer id);
	
	public List<FirstCategory> getAll();
	
	@Select("select * from o_category1 where 1=1 and name_cn = #{name}")
	public FirstCategory getFirstCategoryByName(String name);
	
	//public Integer addFirstCategory(FirstCategory firstCategory); 
	
	//public void deleteFirstCategory(Integer id);
	
	//public void updateFirstCategory(FirstCategory firstCategory);
}
