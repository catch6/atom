package cn.mindit.atom.test.mybatisplus.mapper;

import cn.mindit.atom.test.mybatisplus.entity.TestUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestUserMapper extends BaseMapper<TestUser> {
}
