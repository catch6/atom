package net.wenzuo.atom.mybatisplus.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.wenzuo.atom.core.param.PageRequest;
import net.wenzuo.atom.core.param.PageResponse;
import org.springframework.lang.NonNull;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Catch
 * @since 2023-07-06
 */
public abstract class PageUtils {

	@NonNull
	public static <T> Page<T> toPage(@NonNull PageRequest request) {
		return new Page<>(request.getPageNumber(), request.getPageSize());
	}

	@NonNull
	public static <T> PageResponse<T> toPageResponse(@NonNull Page<T> page) {
		PageResponse<T> pageResponse = new PageResponse<>();
		pageResponse.setPageNumber(page.getCurrent());
		pageResponse.setPageSize(page.getSize());
		pageResponse.setTotalRow(page.getTotal());
		pageResponse.setTotalPage(page.getPages());
		pageResponse.setItems(page.getRecords());
		return pageResponse;
	}

	@NonNull
	public static <T, R> PageResponse<R> toPageResponse(@NonNull Page<T> page, Function<T, R> function) {
		PageResponse<R> pageResponse = new PageResponse<>();
		pageResponse.setPageNumber(page.getCurrent());
		pageResponse.setPageSize(page.getSize());
		pageResponse.setTotalRow(page.getTotal());
		pageResponse.setTotalPage(page.getPages());
		pageResponse.setItems(page.getRecords().stream().map(function).collect(Collectors.toList()));
		return pageResponse;
	}

}
