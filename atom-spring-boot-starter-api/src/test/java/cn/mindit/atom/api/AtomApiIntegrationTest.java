/*
 * Copyright (c) 2022-2025 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.mindit.atom.api;

import cn.mindit.atom.api.util.ValidatorUtils;
import cn.mindit.atom.api.validator.AnyOfString;
import cn.mindit.atom.api.validator.Phone;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Atom API 集成测试
 * 测试整个验证框架在 Spring Boot 环境中的运行情况
 *
 * @author Catch
 * @since 2025-01-01
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=password"
})
class AtomApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testValidationInSpringBootContext() {
        // 测试在 Spring Boot 环境中的验证器是否正常工作
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("testuser");
        dto.setEmail("test@example.com");
        dto.setPhone("13812345678");
        dto.setStatus("ACTIVE");
        
        // 应该不抛出异常
        assertDoesNotThrow(() -> ValidatorUtils.validate(ValidatorUtils.VALIDATOR_ALL, dto));
    }

    @Test
    void testValidatorUtilsInSpringContext() {
        // 测试 ValidatorUtils 在 Spring 上下文中是否正常工作
        assertNotNull(ValidatorUtils.VALIDATOR_FAST);
        assertNotNull(ValidatorUtils.VALIDATOR_ALL);
        
        // 验证两个验证器是不同的实例
        assertNotEquals(ValidatorUtils.VALIDATOR_FAST, ValidatorUtils.VALIDATOR_ALL);
    }

    @Test
    void testConstraintViolationWithMvc() throws Exception {
        // 测试通过 MockMvc 进行验证
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                .contentType("application/json")
                .content("{\"username\":\"\",\"email\":\"invalid-email\",\"phone\":\"123\",\"status\":\"INVALID\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    void testValidRequestWithMvc() throws Exception {
        // 测试有效的请求
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                .contentType("application/json")
                .content("{\"username\":\"testuser\",\"email\":\"test@example.com\",\"phone\":\"13812345678\",\"status\":\"ACTIVE\"}"))
                .andExpect(status().isOk());
    }

    /**
     * 用户注册 DTO
     */
    static class UserRegistrationDto {
        @NotBlank(message = "用户名不能为空")
        @Size(min = 3, max = 20, message = "用户名长度必须在3-20个字符之间")
        private String username;

        @NotBlank(message = "邮箱不能为空")
        @Email(message = "邮箱格式不正确")
        private String email;

        @NotNull(message = "手机号不能为空")
        @Phone(message = "手机号格式不正确")
        private String phone;

        @NotBlank(message = "状态不能为空")
        @AnyOfString(value = {"ACTIVE", "INACTIVE", "PENDING"}, message = "状态必须是 ACTIVE、INACTIVE 或 PENDING")
        private String status;

        // Getters and Setters
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}