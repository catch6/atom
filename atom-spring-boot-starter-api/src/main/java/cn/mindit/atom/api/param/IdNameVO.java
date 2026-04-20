package cn.mindit.atom.api.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Catch
 * @since 2023-07-07
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class IdNameVO {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "名称")
    private String name;

    public static IdNameVO of(Long id, String name) {
        return new IdNameVO(id, name);
    }

}
