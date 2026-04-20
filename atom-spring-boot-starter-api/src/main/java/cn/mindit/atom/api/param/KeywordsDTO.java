package cn.mindit.atom.api.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Catch
 * @since 2024-01-18
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class KeywordsDTO {

    @Schema(description = "关键字")
    private String keywords;

    public static KeywordsDTO of(String keywords) {
        return new KeywordsDTO(keywords);
    }

}
