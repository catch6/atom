package net.wenzuo.atom.core.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Catch
 * @since 2023-07-07
 */
@Data
public class IdRequest {

	@Schema(description = "ID")
	@NotNull(message = "ID不能为空")
	private Long id;

}
