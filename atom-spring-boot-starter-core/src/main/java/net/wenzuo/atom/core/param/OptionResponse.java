package net.wenzuo.atom.core.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author Catch
 * @since 2023-07-07
 */
@Data
public class OptionResponse {

	@Schema(description = "选项列表")
	private List<IdName> items;

}
