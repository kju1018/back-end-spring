package com.mycompany.webapp.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.webapp.dto.Board;
import com.mycompany.webapp.dto.Pager;
import com.mycompany.webapp.dto.Product;
import com.mycompany.webapp.service.ProductsService;

@RestController
@RequestMapping("/products")
public class ProductsController {
	private final Logger logger = LoggerFactory.getLogger(ProductsController.class);
	
	@Autowired
	private ProductsService productsService;
	
	@GetMapping("")
	public Map<String, Object> list(@RequestParam(defaultValue = "1") int pageNo) {
		int totalRows = productsService.getCount();
		Pager pager = new Pager(5, 5, totalRows, pageNo);
		List<Product> list = productsService.getList(pager);
		
		Map<String, Object> map = new HashMap<>();
		map.put("pager", pager);
		map.put("boards", list);
		return map;
	}
	
	@PostMapping("")
	//@RequestBody: 요청 HTTP 본문에 JSON이 포함되어 있을 경우
	public Product create(@RequestBody Product product) {
	   productsService.insert(product);
	   
	   return product;
	}
	
	@GetMapping("/{pid}")
	public Product read(@PathVariable int pid) {
		Product board = productsService.getProduct(pid);
	   return board;
	}

	@PutMapping("")
	public Product update(@RequestBody Product product) {
	   productsService.update(product);
	   
	   return product;
	}

	@DeleteMapping("/{pid}")
	public void delete(@PathVariable int pid) {
	   productsService.delete(pid);
	}
}
