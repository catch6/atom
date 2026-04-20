package cn.mindit.atom.api.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Catch
 * @since 2023-07-07
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class IdDTO {

    @Schema(description = "ID")
    @NotNull(message = "ID不能为空")
    private Long id;

    public static IdDTO of(Long id) {
        return new IdDTO(id);
    }

}
