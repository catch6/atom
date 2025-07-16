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

package cn.mindit.atom.test.web.util;

import cn.idev.excel.annotation.ExcelProperty;
import cn.idev.excel.annotation.write.style.ColumnWidth;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import cn.mindit.atom.web.util.ExcelUtils;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Catch
 * @since 2025-04-22
 */
@Slf4j
class ExcelUtilsTest {

    @Test
    void read() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:报表导入导出模板.xlsx");
        log.info("file: {}", file.getAbsoluteFile());
        List<Row> rows = new ArrayList<>();
        ExcelUtils.read(file, Row.class, (head) -> {
            log.info("head: {}", head);
            rows.add(head);
        }, 2);
        log.info("rows: {}", rows);
    }

    @Test
    void write() {
        File file = new File("/Users/zhanghao/Downloads/导出.xlsx");
        List<Row> rows = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            Row row = new Row();
            String no = String.valueOf(i + 1);
            row.setNo(no);
            row.setCode("0000" + no);
            row.setName("电表" + no);
            row.setStart(new BigDecimal("0.00"));
            row.setEnd(new BigDecimal("100.00"));
            row.setUsed(row.getEnd().subtract(row.getStart()));
            row.setAmount(row.getUsed().multiply(new BigDecimal("1")));
            rows.add(row);
        }
        ExcelUtils.write(file, Row.class, rows);
    }

    @Test
    void fillListObject() {
        File file = new File("/Users/zhanghao/Downloads/模板list+object-1.xlsx");
        File template = new File("/Users/zhanghao/Downloads/模板list+object.xlsx");
        List<Row> rows = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            Row row = new Row();
            String no = String.valueOf(i + 1);
            row.setNo(no);
            row.setCode("0000" + no);
            row.setName("电表" + no);
            row.setStart(new BigDecimal("0.00"));
            row.setEnd(new BigDecimal("100.00"));
            row.setUsed(row.getEnd().subtract(row.getStart()));
            row.setAmount(row.getUsed().multiply(new BigDecimal("1")));
            rows.add(row);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("title", "电费统计报表（2025.3.1-2025.3.31）");
        map.put("totalAmount", new BigDecimal("1000.00"));

        ExcelUtils.fill(file, template, rows, map);
    }

    @Test
    void fillListObject2() {
        File file = new File("/Users/zhanghao/Downloads/模板list+object-2.xlsx");
        File template = new File("/Users/zhanghao/Downloads/模板list+object.xlsx");
        List<Row> rows = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            Row row = new Row();
            String no = String.valueOf(i + 1);
            row.setNo(no);
            row.setCode("0000" + no);
            row.setName("电表" + no);
            row.setStart(new BigDecimal("0.00"));
            row.setEnd(new BigDecimal("100.00"));
            row.setUsed(row.getEnd().subtract(row.getStart()));
            row.setAmount(row.getUsed().multiply(new BigDecimal("1")));
            rows.add(row);
        }
        FillDTO dto = new FillDTO("电费统计报表（2025.3.1-2025.3.31）", new BigDecimal("1000.00"));

        ExcelUtils.fill(file, template, rows, dto);
    }

    @Test
    void fillList() {
        File file = new File("/Users/zhanghao/Downloads/模板list1.xlsx");
        File template = new File("/Users/zhanghao/Downloads/模板list.xlsx");
        List<Row> rows = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            Row row = new Row();
            String no = String.valueOf(i + 1);
            row.setNo(no);
            row.setCode("0000" + no);
            row.setName("电表" + no);
            row.setStart(new BigDecimal("0.00"));
            row.setEnd(new BigDecimal("100.00"));
            row.setUsed(row.getEnd().subtract(row.getStart()));
            row.setAmount(row.getUsed().multiply(new BigDecimal("1")));
            rows.add(row);
        }

        ExcelUtils.fill(file, template, rows);
    }

    @Test
    void fillObject() {
        File file = new File("/Users/zhanghao/Downloads/模板object-1.xlsx");
        File template = new File("/Users/zhanghao/Downloads/模板object.xlsx");
        Map<String, Object> map = new HashMap<>();
        map.put("title", "电费统计报表（2025.3.1-2025.3.31）");
        map.put("totalAmount", new BigDecimal("1000.00"));
        ExcelUtils.fill(file, template, map);
    }

    @Test
    void fillObject2() {
        File file = new File("/Users/zhanghao/Downloads/模板object-2.xlsx");
        File template = new File("/Users/zhanghao/Downloads/模板object.xlsx");
        FillDTO dto = new FillDTO("电费统计报表（2025.3.1-2025.3.31）", new BigDecimal("1000.00"));
        ExcelUtils.fill(file, template, dto);
    }

    @AllArgsConstructor
    @Data
    public static class FillDTO {

        private String title;
        private BigDecimal totalAmount;

    }

    @Data
    public static class Row {

        @ColumnWidth(10)
        @ExcelProperty({"电费统计报表（2025.3.1-2025.3.31）", "序号"})
        private String no;
        @ExcelProperty({"电费统计报表（2025.3.1-2025.3.31）", "表号"})
        private String code;
        @ExcelProperty({"电费统计报表（2025.3.1-2025.3.31）", "名称"})
        private String name;
        @ExcelProperty({"电费统计报表（2025.3.1-2025.3.31）", "起始读数"})
        private BigDecimal start;
        @ExcelProperty({"电费统计报表（2025.3.1-2025.3.31）", "终止读数"})
        private BigDecimal end;
        @ExcelProperty({"电费统计报表（2025.3.1-2025.3.31）", "实际用量"})
        private BigDecimal used;
        @ExcelProperty({"电费统计报表（2025.3.1-2025.3.31）", "应缴电费"})
        private BigDecimal amount;

    }

}