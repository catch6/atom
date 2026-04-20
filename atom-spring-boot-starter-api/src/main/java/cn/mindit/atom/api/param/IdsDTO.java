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
