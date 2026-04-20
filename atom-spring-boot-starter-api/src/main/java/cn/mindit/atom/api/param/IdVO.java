package cn.mindit.atom.api.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Catch
 * @since 2024-12-12
 */
@Data
public class IdVO {

    @Schema(description = "ID")
    private Long id;

    public static IdVO of(Long id) {
        IdVO idVO = new IdVO();
        idVO.setId(id);
        return idVO;
    }

}
