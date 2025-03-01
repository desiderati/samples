/*
 * Copyright (c) 2025 - Felipe Desiderati
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package br.tech.desiderati.sample.thymeleaf.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@RestController
@RequestMapping("/v1/thymeleaf")
public class ThymeleafController {

    private static final String MESSAGE = "message";

    private final TemplateEngine templateEngine;

    @Autowired
    public ThymeleafController(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @GetMapping("/templates/{message}")
    public String templatesTemplateAgainst(@PathVariable String message) {
        Context ctx = new Context();
        ctx.setVariable(MESSAGE, message);
        return templateEngine.process("[(#{greeting(${message})})]", ctx);
    }

    @GetMapping("/templates-html/{message}")
    public String templatesHtmlTemplateAgainst(@PathVariable String message) {
        Context ctx = new Context();
        ctx.setVariable(MESSAGE, message);
        return templateEngine.process("html/mail-template", ctx);
    }

    @GetMapping("/templates-text/{message}")
    public String templatesTextTemplateAgainst(@PathVariable String message) {
        Context ctx = new Context();
        ctx.setVariable(MESSAGE, message);
        return templateEngine.process("text/mail-template", ctx);
    }
}
