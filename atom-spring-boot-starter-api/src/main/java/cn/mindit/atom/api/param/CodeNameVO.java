package cn.mindit.atom.api.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Catch
 * @since 2025-02-18
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CodeNameVO {

    @Schema(description = "编码")
    private String code;

    @Schema(description = "名称")
    private String name;

    public static CodeNameVO of(String code, String name) {
        return new CodeNameVO(code, name);
    }

}
