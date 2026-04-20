package cn.mindit.atom.api.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Catch
 * @since 2022-09-08
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PageDTO {

    /**
     * 页码
     */
    @Schema(description = "页码", example = "1", defaultValue = "1")
    private long pageNo = 1;

    /**
     * 每页结果数(-1:查询全部)
     */
    @Schema(description = "每页结果数(-1:查询全部)", example = "15", defaultValue = "15")
    private long pageSize = 15;

    public static <T> PageDTO of() {
        return new PageDTO();
    }

    public static <T> PageDTO of(long pageNo, long pageSize) {
        return new PageDTO(pageNo, pageSize);
    }

}
