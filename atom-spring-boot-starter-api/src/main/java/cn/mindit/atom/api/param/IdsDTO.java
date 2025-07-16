/*
 * Copyright (c) 2022-2025 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.mindit.atom.api.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Catch
 * @since 2023-07-25
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class IdsDTO {

    @Schema(description = "ID列表")
    @NotNull(message = "ID列表不能为空")
    @Size(min = 1, message = "ID列表不能为空")
    private List<Long> ids;

    public static IdsDTO of(List<Long> ids) {
        return new IdsDTO(ids);
    }

}
