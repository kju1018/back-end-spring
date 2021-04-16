package com.mycompany.webapp.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.webapp.dto.Board;
import com.mycompany.webapp.dto.Pager;
import com.mycompany.webapp.service.BoardsService;

@RestController
@RequestMapping("/boards")
public class BoardsController {
	private final Logger logger = LoggerFactory.getLogger(BoardsController.class);
	
	@Autowired
	private BoardsService boardsService;
	
	@GetMapping("")
	public List<Board> list(@RequestParam(defaultValue = "1") int pageNo) {
		int totalRows = boardsService.getCount();
		Pager pager = new Pager(5, 5, totalRows, pageNo);
		List<Board> list = boardsService.getList(pager);
		
		return list;
	}
	
}

//@Controller는 view와 연계되는 컨트롤러 RESTful에서는 안씀
//@RestController는 return값이 JSON이 되기위한 데이터가 온다. 즉 list가 Json이 된다
