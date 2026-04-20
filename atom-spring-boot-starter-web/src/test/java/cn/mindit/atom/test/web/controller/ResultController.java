package cn.mindit.atom.test.web.controller;

import cn.mindit.atom.core.util.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Catch
 * @since 2023-08-08
 */
@RequestMapping("/result")
@RestController
public class ResultController {

    @PostMapping("/ok")
    public Result<Void> ok() {
        return Result.ok();
    }

}
