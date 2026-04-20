package cn.mindit.atom.api.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Catch
 * @since 2022-09-08
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PageVO<T> {

    /**
     * 页码
     */
    @Schema(description = "页码", example = "1")
    private long pageNo = 1;

    /**
     * 每页结果数(-1:查询全部)
     */
    @Schema(description = "每页结果数", example = "15")
    private long pageSize = 15;

    /**
     * 总页数
     */
    @Schema(description = "总页数", example = "0")
    private long totalPage = 0;

    /**
     * 总结果数
     */
    @Schema(description = "总结果数", example = "0")
    private long totalRow = 0;

    /**
     * 数据
     */
    @Schema(description = "数据", example = "[]")
    private List<T> items = new ArrayList<>();

    public static <T> PageVO<T> of() {
        return new PageVO<>();
    }

}
