package cn.mindit.atom.test.mybatisplus;

import cn.mindit.atom.test.mybatisplus.entity.TestUser;
import cn.mindit.atom.test.mybatisplus.mapper.TestUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LogicDeleteTest {

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
    void insert_deleteTimeIsNull() {
        TestUser user = new TestUser();
        user.setName("张三");
        user.setAge(25);
        testUserMapper.insert(user);

        Map<String, Object> row = jdbcTemplate.queryForMap(
                "SELECT * FROM test_user WHERE id = ?", user.getId());
        assertThat(row.get("delete_time")).isNull();
    }

    @Test
    @Order(2)
    void logicDelete_setsDeleteTimeToNow() {
        TestUser user = new TestUser();
        user.setName("李四");
        user.setAge(30);
        testUserMapper.insert(user);
        Long userId = user.getId();

        LocalDateTime beforeDelete = LocalDateTime.now().minusSeconds(1);
        testUserMapper.deleteById(userId);
        LocalDateTime afterDelete = LocalDateTime.now().plusSeconds(1);

        Map<String, Object> row = jdbcTemplate.queryForMap(
                "SELECT * FROM test_user WHERE id = ?", userId);
        Object deleteTimeObj = row.get("delete_time");
        assertThat(deleteTimeObj).isNotNull();

        LocalDateTime deleteTime;
        if (deleteTimeObj instanceof java.sql.Timestamp ts) {
            deleteTime = ts.toLocalDateTime();
        } else {
            deleteTime = (LocalDateTime) deleteTimeObj;
        }
        assertThat(deleteTime).isAfter(beforeDelete).isBefore(afterDelete);
    }

    @Test
    @Order(3)
    void logicDeletedRecords_excludedFromQuery() {
        TestUser user1 = new TestUser();
        user1.setName("王五");
        user1.setAge(28);
        testUserMapper.insert(user1);

        TestUser user2 = new TestUser();
        user2.setName("赵六");
        user2.setAge(35);
        testUserMapper.insert(user2);

        testUserMapper.deleteById(user1.getId());

        List<TestUser> users = testUserMapper.selectList(null);
        assertThat(users).hasSize(1);
        assertThat(users.get(0).getName()).isEqualTo("赵六");
    }

    @Test
    @Order(4)
    void logicDeletedRecords_excludedFromQueryWrapper() {
        TestUser user = new TestUser();
        user.setName("孙七");
        user.setAge(40);
        testUserMapper.insert(user);
        testUserMapper.deleteById(user.getId());

        LambdaQueryWrapper<TestUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TestUser::getName, "孙七");
        List<TestUser> users = testUserMapper.selectList(wrapper);
        assertThat(users).isEmpty();
    }

    @Test
    @Order(5)
    void selectById_returnsNull_afterLogicDelete() {
        TestUser user = new TestUser();
        user.setName("周八");
        user.setAge(45);
        testUserMapper.insert(user);

        testUserMapper.deleteById(user.getId());

        TestUser result = testUserMapper.selectById(user.getId());
        assertThat(result).isNull();
    }

    @Test
    @Order(6)
    void logicDeletedRecord_stillExistsInDatabase() {
        TestUser user = new TestUser();
        user.setName("吴九");
        user.setAge(50);
        testUserMapper.insert(user);

        testUserMapper.deleteById(user.getId());

        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM test_user WHERE id = ?", Long.class, user.getId());
        assertThat(count).isEqualTo(1);
    }

    @Test
    @Order(7)
    void batchLogicDelete() {
        TestUser user1 = new TestUser();
        user1.setName("测试A");
        user1.setAge(20);
        testUserMapper.insert(user1);

        TestUser user2 = new TestUser();
        user2.setName("测试B");
        user2.setAge(21);
        testUserMapper.insert(user2);

        testUserMapper.deleteByIds(List.of(user1.getId(), user2.getId()));

        List<TestUser> users = testUserMapper.selectList(null);
        assertThat(users).isEmpty();

        Long dbCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM test_user WHERE delete_time IS NOT NULL", Long.class);
        assertThat(dbCount).isEqualTo(2);
    }

}
