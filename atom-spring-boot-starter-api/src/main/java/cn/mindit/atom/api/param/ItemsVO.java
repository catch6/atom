package cn.mindit.atom.api.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author Catch
 * @since 2024-01-18
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemsVO<T> {

    @Schema(description = "列表")
    private List<T> items = new ArrayList<>();

    public static <T> ItemsVO<T> of() {
        return new ItemsVO<>();
    }

    public static <T> ItemsVO<T> of(List<T> items) {
        ItemsVO<T> vo = new ItemsVO<>();
        vo.setItems(items);
        return vo;
    }

    public static <T, R> ItemsVO<T> of(List<R> items, Function<R, T> function) {
        ItemsVO<T> vo = new ItemsVO<>();
        vo.setItems(items.stream().map(function).toList());
        return vo;
    }

}
