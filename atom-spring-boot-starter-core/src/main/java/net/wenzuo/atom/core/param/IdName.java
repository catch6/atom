package net.wenzuo.atom.core.param;

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
public class IdName {

	@Schema(description = "ID")
	private Long id;

	@Schema(description = "名称")
	private String name;

}
