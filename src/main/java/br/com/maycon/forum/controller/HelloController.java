package br.com.maycon.forum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

	@RequestMapping("/") //raiz localhost:8080
	@ResponseBody //devolve o retorno normal sem pesquisar um html
	public String hello() {
		return "Hello World";
	}
}
