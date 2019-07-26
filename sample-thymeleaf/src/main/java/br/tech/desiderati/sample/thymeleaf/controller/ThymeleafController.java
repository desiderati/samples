/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
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

    private final TemplateEngine templateEngine;

    @Autowired
    public ThymeleafController(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @GetMapping("/templates/{message}")
    public String templatesTemplateAgainst(@PathVariable String message) {
        Context ctx = new Context();
        ctx.setVariable("message", message);
        String content = templateEngine.process("[(#{greeting(${message})})]", ctx);
        return content;
    }

    @GetMapping("/templates-html/{message}")
    public String templatesHtmlTemplateAgainst(@PathVariable String message) {
        Context ctx = new Context();
        ctx.setVariable("message", message);
        String content = templateEngine.process("html/mail-template", ctx);
        return content;
    }

    @GetMapping("/templates-text/{message}")
    public String templatesTextTemplateAgainst(@PathVariable String message) {
        Context ctx = new Context();
        ctx.setVariable("message", message);
        String content = templateEngine.process("text/mail-template", ctx);
        return content;
    }
}
