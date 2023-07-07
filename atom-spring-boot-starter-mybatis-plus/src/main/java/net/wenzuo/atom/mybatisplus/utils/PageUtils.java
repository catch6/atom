package net.wenzuo.atom.mybatisplus.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.wenzuo.atom.core.params.PageRequest;
import net.wenzuo.atom.core.params.PageResponse;
import org.springframework.lang.Nullable;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Catch
 * @since 2023-07-06
 */
public abstract class PageUtils {

	@Nullable
	public static <T> Page<T> convert(@Nullable PageRequest source) {
		if (source == null) {
			return null;
		}
		return new Page<>(source.getPageNumber(), source.getPageSize());
	}

	@Nullable
	public static <T> PageResponse<T> convert(@Nullable Page<T> source) {
		if (source == null) {
			return null;
		}
		PageResponse<T> pageResponse = new PageResponse<>();
		pageResponse.setPageNumber(source.getCurrent());
		pageResponse.setPageSize(source.getSize());
		pageResponse.setTotalRow(source.getTotal());
		pageResponse.setTotalPage(source.getPages());
		pageResponse.setItems(source.getRecords());
		return pageResponse;
	}

	@Nullable
	public static <T, R> PageResponse<R> convert(@Nullable Page<T> source, Function<T, R> function) {
		if (source == null) {
			return null;
		}
		PageResponse<R> pageResponse = new PageResponse<>();
		pageResponse.setPageNumber(source.getCurrent());
		pageResponse.setPageSize(source.getSize());
		pageResponse.setTotalRow(source.getTotal());
		pageResponse.setTotalPage(source.getPages());
		pageResponse.setItems(source.getRecords().stream().map(function).collect(Collectors.toList()));
		return pageResponse;
	}

}
