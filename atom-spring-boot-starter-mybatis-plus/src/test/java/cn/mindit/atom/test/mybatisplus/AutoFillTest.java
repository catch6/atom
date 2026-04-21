package cn.mindit.atom.test.mybatisplus;

import cn.mindit.atom.test.mybatisplus.entity.TestUser;
import cn.mindit.atom.test.mybatisplus.mapper.TestUserMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = "atom.mybatis-plus.auto-fill=true")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AutoFillTest {

    @Autowired
    private TestUserMapper testUserMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute(TestDDLHelper.createTestUserTable(dataSource));
        jdbcTemplate.execute("DELETE FROM test_user");
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS test_user");
    }

    @Test
    @Order(1)
    void insert_fillsCreateTimeAndUpdateTime() {
        LocalDateTime before = LocalDateTime.now().minusSeconds(1);

        TestUser user = new TestUser();
        user.setName("自动填充");
        user.setAge(25);
        testUserMapper.insert(user);

        LocalDateTime after = LocalDateTime.now().plusSeconds(1);

        TestUser result = testUserMapper.selectById(user.getId());
        assertThat(result.getCreateTime()).isAfter(before).isBefore(after);
        assertThat(result.getUpdateTime()).isAfter(before).isBefore(after);
    }

    @Test
    @Order(2)
    void update_fillsUpdateTimeOnly() {
        TestUser user = new TestUser();
        user.setName("更新测试");
        user.setAge(30);
        testUserMapper.insert(user);

        LocalDateTime createTime = testUserMapper.selectById(user.getId()).getCreateTime();

        try {
            Thread.sleep(1100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        LocalDateTime beforeUpdate = LocalDateTime.now().truncatedTo(java.time.temporal.ChronoUnit.SECONDS).minusSeconds(2);
        user.setAge(31);
        testUserMapper.updateById(user);
        LocalDateTime afterUpdate = LocalDateTime.now().plusSeconds(2);

        TestUser result = testUserMapper.selectById(user.getId());
        assertThat(result.getCreateTime()).isEqualTo(createTime);
        assertThat(result.getUpdateTime()).isAfter(beforeUpdate).isBefore(afterUpdate);
    }

    @Test
    @Order(3)
    void insert_doesNotOverrideExistingValues() {
        LocalDateTime customTime = LocalDateTime.of(2020, 1, 1, 0, 0, 0);

        TestUser user = new TestUser();
        user.setName("自定义时间");
        user.setAge(28);
        user.setCreateTime(customTime);
        user.setUpdateTime(customTime);
        testUserMapper.insert(user);

        TestUser result = testUserMapper.selectById(user.getId());
        assertThat(result.getCreateTime()).isEqualTo(customTime);
        assertThat(result.getUpdateTime()).isEqualTo(customTime);
    }

}
