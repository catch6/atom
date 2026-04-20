/*
 * Copyright 2022-2026 �catch6
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.mindit.atom.mybatisplus.util;

import cn.mindit.atom.api.param.PageDTO;
import cn.mindit.atom.api.param.PageVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jspecify.annotations.NonNull;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Catch
 * @since 2022-09-08
 */
public final class PageUtils {

    private PageUtils() {
    }

    public static <T> Page<T> toPage(@NonNull PageDTO dto) {
        return new Page<>(dto.getPageNo(), dto.getPageSize());
    }

    public static <T> PageVO<T> toPageVO(@NonNull Page<T> page) {
        PageVO<T> pageVO = new PageVO<>();
        pageVO.setPageNo(page.getCurrent());
        pageVO.setPageSize(page.getSize());
        pageVO.setTotalRow(page.getSize() < 0 ? page.getRecords().size() : page.getTotal());
        pageVO.setTotalPage(page.getPages());
        pageVO.setItems(page.getRecords());
        return pageVO;
    }

    public static <T, R> PageVO<R> toPageVO(@NonNull Page<T> page, Function<T, R> mapper) {
        PageVO<R> pageVO = new PageVO<>();
        pageVO.setPageNo(page.getCurrent());
        pageVO.setPageSize(page.getSize());
        pageVO.setTotalRow(page.getSize() < 0 ? page.getRecords().size() : page.getTotal());
        pageVO.setTotalPage(page.getPages());
        pageVO.setItems(page.getRecords().stream().map(mapper).collect(Collectors.toList()));
        return pageVO;
    }

}
